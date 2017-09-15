package com.pjt.xianyu;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


/**
 * A simple {@link Fragment} subclass.
 */
public class Regist_oneFragment extends Fragment {

    EditText edit_phone=null;
    Button btn_next=null;
    Context context=null;
    StringRequest stringRequest=null;
    RequestQueue requestQueue=null;
    SharedPreferences sp=null;

    public Regist_oneFragment() {


    }

    interface OneFragmentOnClick{
        void clickOne();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        requestQueue= Volley.newRequestQueue(context);
        sp=Util.getSp(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_regist_one, container, false);

        edit_phone= (EditText) view.findViewById(R.id.edit_phone);
        btn_next= (Button) view.findViewById(R.id.btn_next);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //下一步
                if(edit_phone.getText().toString().trim().length()>0) {
                    stringRequest = new StringRequest(Util.URL + "UserServlet?type=android&action=ishas&phone=" + edit_phone.getText().toString(),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    //如果帐号不存在
                                    if (s.trim().equals("no")) {
                                        int mobile_code = (int) ((Math.random() * 9 + 1) * 100000);//生成六位数验证码
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putInt("code", mobile_code);
                                        String mobile = edit_phone.getText().toString().trim();
                                        editor.putString("mobile", mobile);
                                        editor.commit();
                                        stringRequest = new StringRequest
                                                ("http://106.ihuyi.com/webservice/sms.php?method=Submit&account=cf_pjtsun&password=924cffd92477b30e1c100c3ae77eff4a&mobile=" + mobile + "&content=" +
                                                        "您的验证码是：" + mobile_code + "。请不要把验证码泄露给其他人。",
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String s) {
                                                                ((RegistActivity) context).clickOne();
                                                            }
                                                        }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError volleyError) {

                                                    }
                                                });
                                        requestQueue.add(stringRequest);
                                    } else {
                                        Toast.makeText(context, "手机号码已经注册", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }
                    );
                    requestQueue.add(stringRequest);

                }else{
                    Toast.makeText(context, "请先输入手机号", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

}
