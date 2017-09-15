package com.pjt.xianyu;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    CustomerViewPager viewPager=null;
    ImageView btn_post=null;
    int[]btns=new int[]{R.id.btn_main,R.id.btn_yutang,R.id.btn_message,R.id.btn_person};
    ImageView[]imgs=new ImageView[4];
    int[]img_ids=new int[]{R.id.main_imageview,R.id.yutang_img,R.id.message_img,R.id.person_img};
    int last_id=0;
    int[]res_select=new int[]{R.drawable.comui_tab_home_selected,R.drawable.comui_tab_pond_selected,
    R.drawable.comui_tab_message_selected,R.drawable.comui_tab_person_selected};

    int[]res=new int[]{R.drawable.comui_tab_home,R.drawable.comui_tab_pond,
            R.drawable.comui_tab_message,R.drawable.comui_tab_person};

     Dialog processDialog=null;
    RelativeLayout paizhao=null;
    RelativeLayout zhuanmai=null;
    ImageView posiFinish=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        ContentAdapter  contentAdapter=new ContentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(contentAdapter);

        viewPager.setOffscreenPageLimit(3);
    }

    private void initView() {
        viewPager= (CustomerViewPager) findViewById(R.id.viewpager);
        btn_post= (ImageView) findViewById(R.id.btn_post);
        ButtonListener buttonListener=new ButtonListener();
        int count=0;
        for(int i:btns){
            RelativeLayout btn= (RelativeLayout) findViewById(i);
            btn.setOnClickListener(buttonListener);
            ImageView img= (ImageView) findViewById(img_ids[count]);
            imgs[count]=img;
            count++;
        }
        btn_post.setOnClickListener(buttonListener);
    }

    private class ContentAdapter extends FragmentPagerAdapter{
        ArrayList<Fragment> list=new ArrayList<Fragment>();
        public ContentAdapter(FragmentManager fm) {
            super(fm);
            list.add(new MainFragment());
            list.add(new YutangFragment());
            list.add(new MessageFragment());
            list.add(new PersonFragment());
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    private class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_main:

                    check(0);



                    break;

                case R.id.btn_message:
                    check(2);

                    break;

                case R.id.btn_yutang:

                    check(1);
                    break;
                case R.id.btn_post:
                    View mView = LayoutInflater.from(MainActivity.this).
                            inflate(R.layout.activity_post, null);

                    paizhao = (RelativeLayout) mView.findViewById(R.id.r_paizhao);
                     zhuanmai = (RelativeLayout) mView.findViewById(R.id.r_zhuanmai);
                     posiFinish = (ImageView)  mView.findViewById(R.id.post_finsh);

                    TranslateAnimation translateAnimation= (TranslateAnimation)AnimationUtils.loadAnimation(MainActivity.this,R.anim.paizhao_anim);
                    paizhao.startAnimation(translateAnimation);
                    translateAnimation.setFillAfter(true);

                    TranslateAnimation zhuamaiAnimation= (TranslateAnimation)AnimationUtils.loadAnimation(MainActivity.this,R.anim.zhuanmai_anim);
                    zhuanmai.startAnimation(zhuamaiAnimation);
                    zhuamaiAnimation.setFillAfter(true);

                    RotateAnimation rotateAnimation = (RotateAnimation)AnimationUtils.loadAnimation(MainActivity.this,R.anim.post_anim);
                    posiFinish.startAnimation(rotateAnimation);
                    rotateAnimation.setFillAfter(true);

                    processDialog = new Dialog(MainActivity.this,R.style.processDialog);
                    processDialog.setCancelable(true);
                    processDialog.setContentView(mView);
                    processDialog.show();


                    //一键转卖
                    zhuanmai.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });



                    //拍照
                    paizhao.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_PICK);
                            //设置SD卡中的图片路径
                            Uri data= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                            String type = "image/*";
                            //设置意图和类型（从指定路径下提取一张图片）
                            intent.setDataAndType(data,type);
                            //启动意图并返回结果
                            //参数（意图，请求代码）
                            //请求代码：本App唯一，常量数字
                            startActivityForResult(intent, 2);
                            processDialog.cancel();
                        }
                    });

                    //关闭窗口
                    posiFinish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TranslateAnimation translateAnimation= (TranslateAnimation)AnimationUtils.loadAnimation(MainActivity.this,R.anim.paizhao2_anim);
                            paizhao.startAnimation(translateAnimation);
                            translateAnimation.setFillAfter(true);

                            TranslateAnimation zhuamaiAnimation= (TranslateAnimation)AnimationUtils.loadAnimation(MainActivity.this,R.anim.zhuanmai2_anim);
                            zhuanmai.startAnimation(zhuamaiAnimation);
                            zhuamaiAnimation.setFillAfter(true);

                            RotateAnimation rotateAnimation = (RotateAnimation)AnimationUtils.loadAnimation(MainActivity.this,R.anim.post2_anim);
                            posiFinish.startAnimation(rotateAnimation);
                            rotateAnimation.setFillAfter(true);


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    processDialog.cancel();
                                }
                            }, 200);

                        }
                    });
                    break;


                case R.id.btn_person:

                    check(3);
                    break;
            }
        }

    }

    private void check(int i) {
        viewPager.setCurrentItem(i,false);
        imgs[last_id].setImageResource(res[last_id]);
        last_id=i;
        imgs[i].setImageResource(res_select[i]);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 2:
                if(resultCode==RESULT_OK){
                    Intent intent1=new Intent(MainActivity.this,PostingActivity.class);
                    Uri uri=data.getData();
                    String abspath=getAbsoluteImagePath(uri);
                    Bitmap bitmap= ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(abspath)
                            , 120, 120);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    intent1.putExtra("bit", baos.toByteArray());
                    intent1.putExtra("absfile",abspath);
                    startActivity(intent1);



                }


                break;
        }
    }

    protected String getAbsoluteImagePath(Uri uri) {
        // can post image
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri,
                proj,                 // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null);                 // Order-by clause (ascending by name)

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }


}
