package org.wbing.app_location;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import org.wbing.location.WLocation;
import org.wbing.permission.WPermission;
import org.wbing.permission.WPermissionFailed;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * 需要获取权限的界面添加WPermission，参数为需要申请的权限
     */
    @WPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void getPermission() {
        Log.e("TAG", "获取权限成功");
        //初始化
        WLocation.init(this);
        //获取地理位置
        WLocation.get((error, location) -> {
            if (error != null) {
                return;
            }
            Log.e("location", location.toString());
        });
    }


//    /**
//     * 获取权限失败
//     */
//    @WPermissionFailed
//    public void getPermissionFailed() {
//        Log.e("TAG", "获取权限失败");
//    }

    @Override
    public void onClick(View v) {
        getPermission();
    }
}
