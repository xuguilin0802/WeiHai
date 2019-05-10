package android.weihaidemo.com.weihaidemo;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PoiActivity extends AppCompatActivity {
    private WebView webView;
    private TextView poiName;
    private TextView poiScore;
    private TextView poiNavi;
    private TextView poiNet;
    private TextView poiTime;
    private TextView poiCall;
    private TextView poiSuggest;
    private ImageView iconNavi;
    private PopupWindow mPopWindow;
    private TextView poiTrafflic;
    private TextView textContent1;
    private TextView textContent2;
    private TextView textContent3;
    private TextView textContent4;
    private TextView textContent5;
    private static double poi_lat ;
    private static double poi_log ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);
        initView();
        webOperate();
        Intent intent =getIntent();
        String poi_id = intent.getStringExtra("poi_id");
        double double_id = Double.parseDouble(poi_id);
        int id  = (int) double_id;
        getData(id);
        iconNavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });
    }
    private void showPopupWindow() { //打开popupwindow
        View contentView = LayoutInflater.from(PoiActivity.this).inflate(R.layout.view_pop,null);
        mPopWindow = new PopupWindow(contentView, ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT,true);
        mPopWindow.setContentView(contentView);
        ImageView gaode_navi = contentView.findViewById(R.id.icon_gaode);
        ImageView baidu_navi = contentView.findViewById(R.id.icon_baidu);
        ImageView browser_navi = contentView.findViewById(R.id.icon_browser);
        gaode_navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGaoDeMap(poi_lat,poi_log,poiNavi.getText().toString());
            }
        });
        baidu_navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBaiduMap(poi_lat,poi_log,poiNavi.getText().toString());
            }
        });
        browser_navi.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse("https://map.baidu.com/mobile/webapp/index/index/foo=bar/tab=line"));
                startActivity(intent);
            }
        });
        View rootview = LayoutInflater.from(PoiActivity.this).inflate(R.layout.activity_poi,null);
        mPopWindow.showAtLocation(rootview,Gravity.BOTTOM,0,0);
        bgAlpha(0.618f);
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                bgAlpha(1.0f);
            }
        });
    }
//    public Map<String,String> simpleMapName(){
//        Map<String,String> Poi
//        String poiname = poiName.getText().toString();
//        return poiname;
//    }

//    public String simpleMapName(String poiname){
//        poiname = poiName.getText().toString();
//        return poiname;
//    }

    private void openGaoDeMap(double dlat, double dlon, String dname) {//打开高德地图
        if (isAvilible(this, "com.autonavi.minimap")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.autonavi.minimap");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse("androidamap://route?sourceApplication=" + R.string.app_name
                    + "&sname=我的位置&dlat=" + dlat
                    + "&dlon=" + dlon
                    + "&dname=" + dname
                    + "&dev=0&m=0&t=1"));
            startActivity(intent);
        } else {
            Toast.makeText(PoiActivity.this, "高德地图未安装", Toast.LENGTH_SHORT).show();
        }
    }

    private void openBaiduMap(double dlat, double dlon, String dname) {//打开百度地图
        if (isAvilible(this, "com.baidu.BaiduMap")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("baidumap://map/direction?origin=我的位置&destination=name:"
                    + dname
                    + "|latlng:" + dlat + "," + dlon
                    + "&mode=transit&sy=3&index=0&target=1"));
            startActivity(intent);
        } else {
            Toast.makeText(PoiActivity.this, "百度地图未安装", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isAvilible(Context context, String packageName ){//检查是否存在此应用

        final PackageManager packageManager = context.getPackageManager();

        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for ( int i = 0; i < pinfo.size(); i++ )
        {

            // 循环判断是否存在指定包名
            if(pinfo.get(i).packageName.equalsIgnoreCase(packageName)){
                return true;
            }

        }
        return false;
    }
    private void bgAlpha(float f){ //改变打开popupwindow
        WindowManager.LayoutParams layoutParams = PoiActivity.this.getWindow().getAttributes();
        layoutParams.alpha = f;
        PoiActivity.this.getWindow().setAttributes(layoutParams);
    }
    private void getData(int id){//接收详细信息
         new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://ml.iwhcity.com:8080/LTMap/countryTour/getCountryToursByPage.do")//请求接口。如果需要传参拼接到接口后面。
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            parseData(responseData,id);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void parseData(String jsonData,int id){ //解析json数据

        Gson gson = new Gson();
        List<TravelGsonData>travelGsonDataList = gson.fromJson(jsonData,new TypeToken<List<TravelGsonData>>(){}.getType());
        TravelGsonData travelGsonData  = travelGsonDataList.get(id);
        poiName.setText(travelGsonData.getV_name());
        poiScore.setText(travelGsonData.getV_score()+".0/5.0");
        poiNavi.setText(travelGsonData.getV_dz());
        ExpandableTextView poiSurvey = (ExpandableTextView) findViewById(R.id.poi_survey);
        poiSurvey.setText(travelGsonData.getV_js());
        //给js传参调用新的html
//        webView.loadUrl("javascript:jsMarker('" + travelGsonData.getV_x() + travelGsonData.getV_y() +"')");
        if (travelGsonData.getV_website()!= null ){
            poiNet.append(travelGsonData.getV_website());
        }
        if (travelGsonData.getV_yysj()!= null) {
            poiTime.append(travelGsonData.getV_yysj());
        }
        if (travelGsonData.getV_lxfs()!= null ){
            poiCall.append(travelGsonData.getV_lxfs());
        }
        if (travelGsonData.getV_jyyw()!= null){
            poiSuggest.append(travelGsonData.getV_jyyw());
        }
        poi_lat = Double.parseDouble(travelGsonData.getV_y());
        poi_log = Double.parseDouble(travelGsonData.getV_y());
        ExpandableTextView poiTraffic = (ExpandableTextView) findViewById(R.id.poi_traffic_detail);
        poiTraffic.setText("公交车："+travelGsonData.getV_gjc()+"\n"+"自驾车："+travelGsonData.getV_zjc()+"\n"+"停车场："
                +travelGsonData.getV_tcc()+"\n"+"加油站："+travelGsonData.getV_jyz());
        ExpandableTextView poiMatch = (ExpandableTextView) findViewById(R.id.poi_match_detail);
        poiMatch.setText(travelGsonData.getV_ptss());
        ExpandableTextView poiSpecial = (ExpandableTextView) findViewById(R.id.poi_special_detail);
        poiSpecial.setText(travelGsonData.getV_tsjg());
    }

    private void requireDetail() {


    }

    private void webOperate(){
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/MapDemo.html");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url == null) return false;

                try{
                    if(!url.startsWith("http://") && !url.startsWith("https://")){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                }catch (Exception e){//防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                }

                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }
    private void initView(){
        webView = findViewById(R.id.poi_web);
        poiName = findViewById(R.id.poi_name);
        poiScore = findViewById(R.id.poi_score);
        poiNavi = findViewById(R.id.poi_navi);
        poiTime = findViewById(R.id.poi_time);
        poiNet = findViewById(R.id.poi_net);
        poiCall = findViewById(R.id.poi_call);
        poiSuggest = findViewById(R.id.poi_suggest);
        iconNavi = findViewById(R.id.icon_navi);
    }

}
