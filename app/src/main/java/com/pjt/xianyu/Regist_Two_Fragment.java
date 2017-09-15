package com.pjt.xianyu;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class Regist_Two_Fragment extends Fragment {

    Context context = null;
    Button btn_chongfa = null;
    Button btn_regist = null;
    EditText pass = null;
    EditText repass = null;
    EditText code = null;
    String phone = null;
    SharedPreferences sp = null;
    MyHandler myHandler = null;
    int count = 60;
    StringRequest registRequest = null;
    RequestQueue requestQueue = null;
    int text_code = 0;
    int mobile_code=0;
    StringRequest stringRequest = null;

    class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:

                    btn_chongfa.setText(count + "秒后重发");
                    break;

                case 200:
                    btn_chongfa.setEnabled(true);
                    btn_chongfa.setText("重新发送");

                    break;
            }
        }
    }

    public Regist_Two_Fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        sp = Util.getSp(context);
        myHandler = new MyHandler(context.getMainLooper());
        requestQueue = Volley.newRequestQueue(context);
        phone = sp.getString("mobile", null);
        text_code = sp.getInt("code", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_regist__two_, container, false);
        btn_chongfa = (Button) view.findViewById(R.id.btn_chongfa);
        btn_regist = (Button) view.findViewById(R.id.btn_regist);


        ButtonListener buttonListener = new ButtonListener();
        pass = (EditText) view.findViewById(R.id.edit_pass);
        repass = (EditText) view.findViewById(R.id.edit_repass);
        code = (EditText) view.findViewById(R.id.edit_code);


        btn_chongfa.setOnClickListener(buttonListener);
        btn_regist.setOnClickListener(buttonListener);
        timer();


        return view;
    }

    private void timer() {
        count = 60;
        btn_chongfa.setEnabled(false);
        btn_chongfa.setText(count + "秒后重发");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (count > 1) {
                        Thread.sleep(1000);
                        count--;
                        Message msg = new Message();
                        msg.what = 100;
                        msg.obj = count;
                        myHandler.sendMessage(msg);
                    }

                    Message msg = new Message();
                    msg.what = 200;
                    myHandler.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_regist:
                    if (pass.getText().toString().equals(repass.getText().toString())) {
                        if (code.getText().toString().equals(String.valueOf(text_code))) {
                            registRequest = new StringRequest(Request.Method.POST,Util.URL + "UserServlet?action=regist&type=android",
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String s) {
                                            if (s.trim().equals("yes")) {
                                                //注册成功
                                                Intent intent = new Intent(context, MainActivity.class);
                                                startActivity(intent);
                                                ((Activity) context).finish();
                                                Util.clearActivity();

                                            } else {
                                                //注册失败
                                            }

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {

                                        }
                                    }
                            ) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("phone", phone);
                                    map.put("pass", pass.getText().toString());

                                    return map;
                                }
                            };

                            requestQueue.add(registRequest);

                        }


                    }


                    break;

                case R.id.btn_chongfa:

                   mobile_code = (int) ((Math.random() * 9 + 1) * 100000);//生成六位数验证码
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("code", mobile_code);
                    String mobile = phone;
                    editor.putString("mobile", mobile);
                    editor.commit();
                    stringRequest = new StringRequest
                            ("http://106.ihuyi.com/webservice/sms.php?method=Submit&account=cf_pjtsun&password=924cffd92477b30e1c100c3ae77eff4a&mobile=" + mobile + "&content=" +
                                    "您的验证码是：" + mobile_code + "。请不要把验证码泄露给其他人。",
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String s) {
                                          text_code=mobile_code;
                                            timer();
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                }
                            });
                    requestQueue.add(stringRequest);

                    break;
            }
        }
    }
}
