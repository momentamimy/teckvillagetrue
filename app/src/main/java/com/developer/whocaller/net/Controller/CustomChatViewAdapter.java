package com.developer.whocaller.net.Controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.developer.whocaller.net.AllChatRoomsFragment;
import com.developer.whocaller.net.View.LastMessasgesFragment;


public class CustomChatViewAdapter extends FragmentPagerAdapter implements ViewPager.PageTransformer {


    public CustomChatViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        if (position==0)
        {
            fragment=new LastMessasgesFragment();
        }
        if (position==1)
        {
            fragment=new AllChatRoomsFragment();
        }
return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
    private static final float MIN_SCALE = 0.65f;
    private static final float MIN_ALPHA = 0.3f;

    @Override
    public void transformPage(View page, float position) {

        if (position <-1){  // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setAlpha(0);

        }
        else if (position <=1){ // [-1,1]

            page.setScaleX(Math.max(MIN_SCALE,1- Math.abs(position)));
            page.setScaleY(Math.max(MIN_SCALE,1- Math.abs(position)));
            page.setAlpha(Math.max(MIN_ALPHA,1- Math.abs(position)));

        }
        else {  // (1,+Infinity]
            // This page is way off-screen to the right.
            page.setAlpha(0);

        }


    }
}
