package com.pjt.xianyu;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/05/30.
 */
public class PostingImageAdapter extends BaseAdapter {
    ArrayList<Bitmap> list=null;
    PostingActivity context=null;
    LayoutInflater layoutInflater=null;
    String end = "\r\n";//结束字符
    //两个连字符
    String twoHyphens = "--";

    HttpURLConnection connection=null;
    URL url=null;
    File  upFile=null;


    public PostingImageAdapter(PostingActivity context, ArrayList<Bitmap> bits) {
        this.layoutInflater=LayoutInflater.from(context);
        this.context=context;
        this.list=bits;
    }

    @Override
    public int getCount() {

        return list.size()+1;
    }

    @Override
    public Bitmap getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;

        if(position==list.size()){
            convertView=layoutInflater.inflate(R.layout.posting_add_mode,parent,false);



        }else {

            if(viewHolder==null){
                convertView=layoutInflater.inflate(R.layout.post_img_mode,parent,false);
                viewHolder=new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder) convertView.getTag();
            }
            viewHolder.binder(list.get(position),position);
        }




        return convertView;
    }

    private class ViewHolder {

        ImageView remove_img=null;
        ImageView post_img=null;
        RelativeLayout upload_r=null;
        TextView upload_progress=null;
        MyHandler myHandler=null;

        class MyHandler extends Handler{
            public MyHandler(Looper looper) {
                super(looper);
            }



            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 100:
                        String st= (String) msg.obj;
                        upload_progress.setText(st);
                        break;
                    case 200:
                        String st1= (String) msg.obj;
                        upload_progress.setText(st1);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                             upload_r.setVisibility(View.GONE);
                            }
                        },1000);
                        break;
                }
            }
        }

        public ViewHolder(View convertView) {
            myHandler=new MyHandler(context.getMainLooper());
            remove_img= (ImageView) convertView.findViewById(R.id.posting_remove_img);
            post_img= (ImageView) convertView.findViewById(R.id.posting_img);
            upload_r= (RelativeLayout) convertView.findViewById(R.id.upload_r);
            upload_progress= (TextView) convertView.findViewById(R.id.upload_progress);
        }

        public void binder(Bitmap bitmap, final int position) {
            post_img.setImageBitmap(bitmap);
            remove_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.bits.remove(position);
                    context.filelist.remove(position);
                    context.isUploads.remove(position);
                    context.adapter.notifyDataSetChanged();

                }
            });

            if(!context.isUploads.get(position)){
                //第一次上传
                //上传图片到服务器
                upFile=context.filelist.get(position);
                uploadImage();
                context.isUploads.set(position,true);
            }

        }

        //上传图片的方法
        private void uploadImage() {
            upload_r.setVisibility(View.VISIBLE);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        url=new URL(Util.URL+"uploadServlet");
                        connection= (HttpURLConnection) url.openConnection();
                        Log.d("消息", 1 + "");
                        connection.setRequestMethod("POST");
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.setInstanceFollowRedirects(true);
                        connection.setUseCaches(false);
                        Log.d("消息", 2 + "");
                        connection.setRequestProperty("Connection", "Keep-Alive");
                        connection.setRequestProperty("Charset", "UTF-8");
                        String boundary = "******";
                        connection.setRequestProperty("Content-Type",
                                "multipart/form-data;boundary=" + boundary);
                        Log.d("消息", 3 + "");
                        connection.connect();
                        FileInputStream in=new FileInputStream(upFile.getAbsolutePath());
                        DataOutputStream out=new DataOutputStream(connection.getOutputStream());
                        out.writeBytes(twoHyphens + boundary + end);
                        out
                                .writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
                                        + upFile.getAbsolutePath().substring
                                        (upFile.getAbsolutePath().lastIndexOf("/") + 1)
                                        + "\"" + end);
                        out.writeBytes(end);
                        byte[]buff=new byte[1024];
                        int count=0;
                        long length=upFile.length();
                        double d=0;
                        while((count=in.read(buff))!=-1){

                            out.write(buff,0,count);
                            d+=count*100;
                            double f=((double)d/length);
                            String st=String.format("%.1f",f)+"%";
                            Message msg=new Message();
                            msg.what=100;
                            msg.obj=st;
                            myHandler.sendMessage(msg);
                            Log.d("消息", st);
                        }
                        //新起一行
                        out.writeBytes(end);

                        //设置结束符（在分界符后加两个连字符）
                        out.writeBytes(twoHyphens + boundary + twoHyphens + end);
                        out.flush();
                        out.close();
                        in.close();
                        BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line=br.readLine();
                        br.close();
                        Log.d("消息", "上传完毕");
                        Message msg=new Message();
                        msg.what=200;
                        msg.obj="上传完毕";
                        myHandler.sendMessage(msg);
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.d("消息",e.getMessage());
                    }

                }
            });
            t.start();

        }
    }
}
