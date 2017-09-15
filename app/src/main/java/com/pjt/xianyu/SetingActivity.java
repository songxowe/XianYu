package com.pjt.xianyu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SetingActivity extends AppCompatActivity {

    Button btn_logout=null;
    SharedPreferences sp=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seting);
        btn_logout= (Button) findViewById(R.id.btn_logout);
        sp=Util.getSp(getApplicationContext());
        ButtonListener buttonListener=new ButtonListener();
        btn_logout.setOnClickListener(buttonListener);
    }

    private class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_logout:
                    SharedPreferences.Editor editor=sp.edit();
                    editor.remove("user_id");
                    editor.commit();
                    for(Context context:Util.ContextArray){
                        ((Activity)context).finish();
                    }
                    Intent intent=new Intent(SetingActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                    break;
            }
        }
    }
}
