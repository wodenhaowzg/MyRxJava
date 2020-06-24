package com.azx.myrxjava;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FlowableTest flowableTest = new FlowableTest();
        // 测试背压策略
        flowableTest.startFlowableTest2();
    }
}
