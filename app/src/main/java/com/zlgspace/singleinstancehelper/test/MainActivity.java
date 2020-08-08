package com.zlgspace.singleinstancehelper.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zlgspace.singleinstancehelper.test.em.DeviceType;
import com.zlgspace.singleinstancehelper.test.entity.SingleDeviceState;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.mBtn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingleDeviceState.setRunning(true);
                SingleDeviceState.setDevType(DeviceType.PHONE);
                SingleDeviceState.setDevName("ZLG");
            }
        });

        findViewById(R.id.mBtn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity","isRunning:"+SingleDeviceState.getDeviceStateInstance().toString()+"");
            }
        });

    }
}
