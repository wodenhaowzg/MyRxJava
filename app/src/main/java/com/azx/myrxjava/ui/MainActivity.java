package com.azx.myrxjava.ui;

import android.os.Bundle;

import com.azx.myrxjava.test.FlowableTest;
import com.azx.myrxjava.R;

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
        flowableTest.startTest();

//        WatchTest watchTest = new WatchTest();
//        watchTest.startTest();
//        OperatorDoTest operatorDoTest = new OperatorDoTest();
//        operatorDoTest.startTest();

    }
}
