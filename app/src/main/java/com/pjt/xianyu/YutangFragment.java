package com.pjt.xianyu;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.rollviewpager.RollPagerView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class YutangFragment extends Fragment {

    private TabLayout tabLayout=null;
    private Context context=null;
    private ViewPager viewPager=null;
    RollPagerView rollPagerView=null;

    public YutangFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_yutang, container, false);
        tabLayout= (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setTabTextColors(Color.BLACK, Color.BLACK);
        viewPager= (ViewPager) view.findViewById(R.id.yutang_viewpager);
        ContentAdapter contentAdapter=new ContentAdapter(getChildFragmentManager());
        viewPager.setAdapter(contentAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private class ContentAdapter  extends FragmentPagerAdapter{
        ArrayList<Fragment> list=null;
        String[]titles=new String[]{"发现","我的"};

        public ContentAdapter(FragmentManager fm) {
            super(fm);
            list=new ArrayList<>();
            list.add(new YT_dis_Fragment());
            list.add(new YT_myFragment());
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
