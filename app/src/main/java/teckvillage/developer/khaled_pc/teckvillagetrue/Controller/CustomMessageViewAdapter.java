package teckvillage.developer.khaled_pc.teckvillagetrue.Controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import teckvillage.developer.khaled_pc.teckvillagetrue.FragMessageContact;
import teckvillage.developer.khaled_pc.teckvillagetrue.FragMessageOthers;
import teckvillage.developer.khaled_pc.teckvillagetrue.FragMessageSpam;


public class CustomMessageViewAdapter extends FragmentPagerAdapter implements ViewPager.PageTransformer {


    public CustomMessageViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        if (position==0)
        {
            fragment=new FragMessageContact();
        }
        if (position==1)
        {
            fragment=new FragMessageOthers();
        }
        if (position==2)
        {
            fragment=new FragMessageSpam();
        }
return fragment;
    }

    @Override
    public int getCount() {
        return 3;
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
