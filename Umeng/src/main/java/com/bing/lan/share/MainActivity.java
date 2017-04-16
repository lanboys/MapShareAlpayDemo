package com.bing.lan.share;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

public class MainActivity extends AppCompatActivity {

    //58cb477c310c937704002709

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

    public void onResume() {
        super.onResume();
         MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void testCrash(View view) {

        throw new NullPointerException("测试空指针异常,被捕获");
    }
}
