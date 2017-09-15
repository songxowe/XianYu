package com.pjt.xianyu;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

public class RegistActivity extends AppCompatActivity implements Regist_oneFragment.OneFragmentOnClick {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        if(savedInstanceState==null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.content, new Regist_oneFragment(), "one");
            ft.commit();
        }

    }

    @Override
    public void clickOne() {
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.content,new Regist_Two_Fragment(),"two");
        ft.commit();
    }
}
