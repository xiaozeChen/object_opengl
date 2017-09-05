package com.custom.chenxz.object_opengl.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.custom.chenxz.object_opengl.R;
import com.custom.chenxz.object_opengl.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_test1)
    Button btnTest1;
    @BindView(R.id.btn_test2)
    Button btnTest2;
    @BindView(R.id.btn_test3)
    Button btnTest3;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.btn_test1, R.id.btn_test2, R.id.btn_test3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_test1:
                startActivity(new Intent(this, OpenGLES20Activity.class));
                break;
            case R.id.btn_test2:
                break;
            case R.id.btn_test3:
                break;
        }
    }
}
