package android.weihaidemo.com.weihaidemo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class MapUtils {
    private static final String BAIDU_PACKAGE_NAME = "com.baidu.BaiduMap";
    private static final String GAODE_PACKAGE_NAME = "com.autonavi.minimap";
    private static final String TENCENT_PACKAGE_NAME = "com.tencent.map";

    /**
     * 是否安装百度地图
     */
    public static boolean haveBaiduMap(Context context) {
        return exist(context, BAIDU_PACKAGE_NAME);
    }

    public static boolean haveGaodeMap(Context context) {
        return exist(context, GAODE_PACKAGE_NAME);
    }

    public static boolean haveTencentMap(Context context) {
        return exist(context, TENCENT_PACKAGE_NAME);
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return true 存在
     */
    public static boolean exist(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有true，没有false
        return packageNames.contains(packageName);
    }

    /**
     * 调用高德地图app,导航
     *
     * @param context            上下文
     * @param  latitude          目标经度
     * @param longitude          目标纬度
     * @param destinationAddress 目标地址
     *                           高德地图：http://lbs.amap.com/api/amap-mobile/guide/android/route
     */
    public static void openGaodeMap(Context context, String latitude,String longitude,
                                    String destinationAddress) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("amapuri://route/plan/?" +
                "dlat=" + latitude +
                "&dlon=" + longitude +
                "&dname=" + destinationAddress +
                "&dev=0" +
                "&t=0"));
        context.startActivity(intent);
    }

    /**
     * 调用百度地图
     *
     * @param latitude        目的地经度
     * @param longitude       目的地纬度
     * @param destinationAddress 目的地地址
     *                           百度参考网址：http://lbsyun.baidu.com/index.php?title=uri/api/android
     */
    public static void openBaiduMap(Context context, String latitude,String longitude,
                                    String destinationAddress) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("baidumap://map/direction?" +
                "destination=latlng:" + latitude + "," + longitude +
                "|name:" + destinationAddress +
                "&mode=driving"));
        context.startActivity(intent);
    }

    /**
     * 调用腾讯地图
     *
     * @param context
     * @param latitude        目的地经度
     * @param longitude       目的地纬度
     * @param destinationAddress 目的地地址
     *                           腾讯地图参考网址：http://lbs.qq.com/uri_v1/guide-route.html
     */
    public static void openTentcentMap(Context context,String latitude,String longitude, String destinationAddress) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse("qqmap://map/routeplan?" +
                "type=drive" +
                "&from=" +
                "&fromcoord=" +
                "&to=" + destinationAddress +
                "&tocoord=" + latitude + "," + longitude +
                "&policy=0" +
                "&referer=appName"));
        context.startActivity(intent);
    }
    /**
     * 打开网页版 导航
     * 不填我的位置，则通过浏览器定未当前位置
     *
     * @param context
     * @param fromlatitude 起点经度
     * @param fromlongitude 起点纬度
     * @param myAddress 起点地址名展示
     * @param tolatitude 终点经度
     * @param tolongitude 重点纬度
     * @param destinationAddress 终点地址名展示
     */
    public static void openBrowserMap(Context context, String fromlatitude,String fromlongitude, String myAddress, String tolatitude,String tolongitude,
                                      String destinationAddress) {

        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse("http://uri.amap.com/navigation?" +
                "from=" + fromlatitude + "," + fromlongitude + "," + myAddress +
                "to=" + tolatitude + "," + tolongitude + "," + destinationAddress +
                "&mode=car&policy=1&src=mypage&coordinate=gaode&callnative=0"));
        context.startActivity(intent);
    }
}
