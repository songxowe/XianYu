package com.pjt.xianyu;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private List<View> views; //放置需要切换的页面
    private ViewPagerAdapter vpAdater; //适配器
    private int[] layoutIds; //切换页面的布局id
    private int[] dotsIds; //导航点的id
    private ImageView[] dots; //导航点集合
    private Button enterButton;




    class ViewPagerAdapter extends PagerAdapter {

        List<View> views; //用于放置导航页
        private Context context;

        public ViewPagerAdapter(List<View> views, Context context) {
            super();
            this. views = views;
            this. context = context;
        }

        //   获取需要滑动的控件数量
        public int getCount() {
            // TODO Auto-generated method stub
            return views.size();
        }


        //   判断是否是同一个元素
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        //   超出了缓存数量，销毁
        @Override
        public void destroyItem(View container, int position, Object object) {
            // TODO Auto-generated method stub
            ((ViewPager)container).removeView( views.get(position));
        }

        //   初始化显示加载图片
        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager)container).addView( views.get(position));
            return views.get(position);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initViews(); //初始化组件
    }

    void initViews(){
        LayoutInflater inflater = LayoutInflater. from(this);
        layoutIds = new int[]{R.layout. screen1, R.layout.screen2, R.layout.screen3 ,
                R.layout. screen4};
        dotsIds = new int[]{R.id. dot_01, R.id. dot_02, R.id.dot_03,
                R.id. dot_04};


//         初始化views集合
        views = new ArrayList<View>();
        for( int i=0; i< layoutIds. length; i++){
            views.add(inflater.inflate( layoutIds[i], null));
        }

//         初始化dots
        dots = new ImageView[ dotsIds. length];
        for( int i=0; i< dotsIds. length; i++){
            dots[i] = (ImageView)findViewById( dotsIds[i]);
        }

        viewPager = (ViewPager)findViewById(R.id.splash_viewpager );
        vpAdater = new ViewPagerAdapter( views, this);
        viewPager.setAdapter(vpAdater); //为viewPager设置适配器

        //同时需要监听ViewPager滑动的情况，根据滑动的状态设置导航点
        viewPager.setOnPageChangeListener(this);

        enterButton = (Button) (views.get(views.size()-1)).findViewById(R.id.enter_button);
        enterButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(SplashActivity.this , MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }

    @Override
    public void onPageScrollStateChanged( int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled( int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }


    //根据选定的页面状态设置导航点
    public void onPageSelected( int arg0) {
        //选定arg0位置的页面
        for( int i=0; i< views.size(); i++){
            if(i == arg0){
                dots[i].setImageResource(R.drawable.wihte_circle);
            }
            else{
                dots[i].setImageResource(R.drawable.gray_circle);
            }
        }
    }
}
