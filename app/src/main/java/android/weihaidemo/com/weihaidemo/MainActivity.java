package android.weihaidemo.com.weihaidemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private WebView webView;
    private ImageView menu;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private EditText search_text;
    private ImageView search;
    private ImageView subject;
    private ImageView three_dt;
    private ImageView center;
    private TextView travel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        webOperate();
        webView.setWebContentsDebuggingEnabled(true);

        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //在这里处理item的点击事件
                switch (item.getItemId()){
                    case R.id.travel:
                        Toast.makeText(MainActivity.this,"hello",Toast.LENGTH_SHORT).show();
                        webView.loadUrl("javascript:jsTravel()");
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                    case R.id.plan:
                        break;
                    case R.id.tradition:
                        break;
                    case R.id.overview:
                        webView.loadUrl("avascript:jsPano()");
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                    case R.id.measure:
                        break;
                    case R.id.street:
                        break;
                    case R.id.horse:
                        break;
                }
                return true;
            }
        });
//        FloatingActionButton navi = findViewById(R.id.navi);
//        FloatingActionButton layer = findViewById(R.id.layer);
//        navi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                webView.loadUrl("javascript:jsLocate()");
//            }
//        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        subject.setOnClickListener(new View.OnClickListener() {
            String data = "hello";
            public void onClick(View v) {
                webView.evaluateJavascript("javascript:callJs()", new ValueCallback<String>() {
                    public void onReceiveValue(String value) {
                        Toast.makeText(MainActivity.this,value,Toast.LENGTH_SHORT).show();
                        //此处为 js 返回的结果
                    }
                });
            }
        });
        three_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ThreeDActivity.class);
                startActivity(intent);
            }
        });
    }
    @JavascriptInterface
    public void teavelPOIDetail(String v_id){

        Intent intent = new Intent(MainActivity.this,PoiActivity.class);
        intent.putExtra("poi_id",v_id);
        startActivity(intent);
    }
    public void setItemIconTintList(@Nullable ColorStateList tint) {
        navigationView.setItemIconTintList(tint);
    }
    private void webOperate(){
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl("http://115.29.65.225:8080");
        webView.addJavascriptInterface(this, "android");
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
        webView = findViewById(R.id.webView);
        menu = findViewById(R.id.menu);
        drawerLayout = findViewById(R.id.drawer_layout);
        search_text = findViewById(R.id.search_text);
        search = findViewById(R.id.search);
        subject = findViewById(R.id.subject);
        center = findViewById(R.id.center);
        three_dt = findViewById(R.id.three_dt);
        navigationView = findViewById(R.id.nav_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.subject:
                break;
            case R.id.center:
                break;
            case R.id.three_dt:
                break;
                
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.twod_menu,menu);
        return  true;
    }
//    public boolean onOptionsItemSelected(MenuItem  item){
//        switch (item.getItemId()){
//            case R.id.travel:
//                Toast.makeText(MainActivity.this,"hello",Toast.LENGTH_SHORT).show();
//                webView.loadUrl("javascript:jsTravel()");
//                break;
//            case R.id.plan:
//                break;
//            case R.id.tradition:
//                break;
//            case R.id.overview:
//                break;
//            case R.id.measure:
//                break;
//            case R.id.street:
//                break;
//            case R.id.horse:
//                break;
//        }
//        return false;
//    }
}
