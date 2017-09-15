package com.pjt.xianyu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pjt.xianyu.pojo.User;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText edit_user=null;
    EditText edit_pass=null;
    Button btn_login=null;
    Button remove_user=null;
    Button remove_pass=null;
    Button show_pass=null;
    TextView toregist=null;
    TextView findPass=null;
    boolean isshow=false;
    StringRequest stringRequest=null;
    RequestQueue requestQueue=null;
    SharedPreferences sp=null;
    Gson gson=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestQueue= Volley.newRequestQueue(getApplicationContext());
        gson=new Gson();
        sp=Util.getSp(LoginActivity.this);

        initView();
        edit_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                      if(s.toString().trim().length()>0){
                          remove_pass.setVisibility(View.VISIBLE);
                      }else{
                          remove_pass.setVisibility(View.GONE);
                      }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    remove_user.setVisibility(View.VISIBLE);
                } else {
                    remove_user.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ButtonListener butttoListener=new ButtonListener();
        btn_login.setOnClickListener(butttoListener);
        remove_pass.setOnClickListener(butttoListener);
        remove_user.setOnClickListener(butttoListener);
        findPass.setOnClickListener(butttoListener);
        toregist.setOnClickListener(butttoListener);
        show_pass.setOnClickListener(butttoListener);

    }

    private void initView() {
        edit_user= (EditText) findViewById(R.id.edit_username);
        edit_pass= (EditText) findViewById(R.id.edit_pass);
        btn_login= (Button) findViewById(R.id.btn_login);
        remove_user= (Button) findViewById(R.id.user_remove);
        remove_pass= (Button) findViewById(R.id.pass_remove);
        show_pass= (Button) findViewById(R.id.pw_show);
        toregist= (TextView) findViewById(R.id.btn_toregist);
        findPass= (TextView) findViewById(R.id.btn_findpass);
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_login:
                 stringRequest=new StringRequest(Request.Method.POST,
                         Util.URL + "LoginServlet?type=android&action=login",
                         new Response.Listener<String>() {
                             @Override
                             public void onResponse(String s) {
                                      User user=gson.fromJson(s.trim(),User.class);
                                 if(user.getUsername()!=null) {
                                     SharedPreferences.Editor editor = sp.edit();
                                     editor.putLong("user_id", user.getId());

                                     editor.putString("username", user.getUsername());
                                     editor.commit();

                                     Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                     Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                     startActivity(intent);
                                     Util.clearActivity();
                                     finish();
                                 }else {
                                     Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                 }
                             }
                         },
                         new Response.ErrorListener() {
                             @Override
                             public void onErrorResponse(VolleyError volleyError) {

                             }
                         }){

                     @Override
                     protected Map<String, String> getParams() throws AuthFailureError {
                         HashMap<String,String>map=new HashMap<>();
                         map.put("username",edit_user.getText().toString());
                         map.put("password",edit_pass.getText().toString());


                         return map;
                     }
                 };
                    requestQueue.add(stringRequest);

                    break;

                case R.id.pass_remove:

                    edit_pass.setText("");
                    break;

                case R.id.user_remove:
                    edit_user.setText("");

                    break;

                case R.id.btn_toregist:
                  Intent intent1=new Intent(LoginActivity.this,RegistActivity.class);
                    startActivity(intent1);


                    break;
                case R.id.btn_findpass:


                    break;

                case R.id.pw_show:
                  if(!isshow){
                      edit_pass.setInputType(90);
                      show_pass.setBackgroundResource(R.drawable.pw_show);

                      isshow=true;
                      edit_pass.setSelection(edit_pass.getText().toString().length());

                  }else{
                      show_pass.setBackgroundResource(R.drawable.pw_hide);
                      edit_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                      edit_pass.setSelection(edit_pass.getText().toString().length());
                      isshow=false;
                  }


                    break;
            }
        }
    }
}
