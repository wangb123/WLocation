package org.wbing.location;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.lang.ref.WeakReference;

/**
 * @author wangbing
 * @date 2018/11/16
 */
public class WLocation {

    /**
     * name
     */
    public String name;
    /**
     * 城市
     */
    public String city;
    /**
     * 详细地址
     */
    public String address;

    /**
     * 纬度，浮点数，范围为-90~90，负数表示南纬。百度坐标系
     */
    public double latitude;

    /**
     * 经度，浮点数，范围为-180~180，负数表示西经。百度坐标系
     */
    public double longitude;

    /**
     * 定位时间
     */
    public long timestamp;


    public WLocation(String name, String city, String address, double latitude, double longitude) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "WLocation{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", timestamp=" + timestamp +
                '}';
    }

    /**
     * 设置一个默认的位置
     */
    private static WLocation DEFAULT;
    private static Context mContext;
    private static ChooseDelegate chooseDelegate;
    private static OpenDelegate openDelegate;
    private static WLocation lastPosition;

    public static void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public static WLocation getDefault() {
        return DEFAULT;
    }

    public static void setDefault(WLocation DEFAULT) {
        WLocation.DEFAULT = DEFAULT;
    }

    /**
     * 获取用户的地理位置
     *
     * @param callback
     */
    public static void get(Callback callback) {
        LocationClient client = new LocationClient(mContext);
        client.registerLocationListener(new LocationListener(client, callback));

        LocationClientOption option = new LocationClientOption();
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
        option.setCoorType("bd09ll");
        option.setOpenGps(true);
        option.setIgnoreKillProcess(true);
        option.SetIgnoreCacheException(true);
        option.setWifiCacheTimeOut(5 * 60 * 1000);
        option.setEnableSimulateGps(false);
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);

        client.setLocOption(option);

        client.start();
    }

    /**
     * 打开地图选择位置。
     *
     * @param callback
     */
    public static void choose(Callback callback) {
        if (chooseDelegate == null) {
            //todo 默认实现
        } else {
            chooseDelegate.choose(callback);
        }
    }

    /**
     * @param context
     * @param latitude
     * @param longitude
     */
    public static void open(Context context, float latitude, float longitude) {
        if (openDelegate == null) {
            //todo 默认实现
        } else {
            openDelegate.open(context, latitude, longitude);
        }
    }

    public interface Callback {
        void call(LocationError error, WLocation location);
    }

    public interface ChooseDelegate {
        void choose(Callback callback);
    }

    public interface OpenDelegate {
        void open(Context context, float latitude, float longitude);
    }

    private static class LocationListener extends BDAbstractLocationListener {
        private WeakReference<Callback> callbackWeakReference;
        private LocationClient client;

        public LocationListener(LocationClient client, Callback callback) {
            this.client = client;
            this.callbackWeakReference = new WeakReference<>(callback);
        }


        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (client != null) {
                client.stop();
            }
            if (callbackWeakReference == null) {
                return;
            }
            Callback callback = callbackWeakReference.get();
            if (callback == null) {
                return;
            }
            if (bdLocation == null) {
                if (DEFAULT == null) {
                    callback.call(new LocationError(-1, "未获取到地理位置信息"), null);
                } else {
                    callback.call(null, DEFAULT);
                }
                return;
            }

            callback.call(null, new WLocation(bdLocation.getLocationDescribe(),
                    bdLocation.getCity(),
                    bdLocation.getAddrStr(),
                    bdLocation.getLatitude(),
                    bdLocation.getLongitude()));
        }
    }

    public static class LocationError extends Throwable {
        private int code;
        private String msg;

        public LocationError(int code, String msg) {
            super(msg);
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
