package com.mtr.codetrip.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button clickShowSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView(){
        clickShowSecond = (Button) findViewById(R.id.showsecond);
        clickShowSecond.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.showsecond:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,SideBarActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
