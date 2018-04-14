package hnwebproject.com.mlmp.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import hnwebproject.com.mlmp.R;
import hnwebproject.com.mlmp.Utility.PermissionUtility;


public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_get_started;
    private PermissionUtility putility;
    private ArrayList<String> permission_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {

        btn_get_started= (Button) findViewById(R.id.btn_get_started);
        btn_get_started.setOnClickListener(this);

        getPermissions();
    }

    private void getPermissions() {

        putility = new PermissionUtility(this);
        permission_list = new ArrayList<String>();
        permission_list.add(Manifest.permission.INTERNET);

        permission_list.add(Manifest.permission.ACCESS_NETWORK_STATE);
        permission_list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permission_list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permission_list.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permission_list.add(Manifest.permission.CALL_PHONE);
        permission_list.add(Manifest.permission.CAMERA);


// permission_list.add(android.Manifest.permission.ACCESS_FINE_LOCATION);

        putility.setListner(new PermissionUtility.OnPermissionCallback() {
            @Override
            public void OnComplete(boolean is_granted) {
                Log.i("OnPermissionCallback", "is_granted = " + is_granted);
                if (is_granted) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {


                        }
                    }, 5000);
                } else {

                }
            }
        });

        putility.checkPermission(permission_list);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        putility.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_get_started:
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);

                final SharedPreferences settings = getSharedPreferences("AOP_PREFS", Context.MODE_PRIVATE); //1
               Boolean islogin = settings.getBoolean("login", false);
               String status = settings.getString("status", null);

/*.equals(true)*/
                if (islogin)
                {
                    if(status.equals("0"))
                    {
                        Intent intent_main = new Intent(SplashActivity.this, ViewPRofileActivity.class);
                        startActivity(intent_main);
                        finish();
                    }else
                    {
                        Intent intent_main = new Intent(SplashActivity.this, DashBoardActivity.class);
                        startActivity(intent_main);
                        finish();
                    }


                } else {
                    Intent intentLogin = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intentLogin);
                    finish();
                }
                break;
        }
    }
}
