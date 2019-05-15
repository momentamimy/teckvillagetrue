package com.developer.whocaller.net;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.developer.whocaller.net.Controller.CustomSearchViewAdapter;

import com.developer.whocaller.net.R;

public class SearchActivity extends AppCompatActivity {


    TabLayout tabs;
    ViewPager pager;
    ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        backIcon=findViewById(R.id.back_Search);
        tabs=findViewById(R.id.tabs);
        pager=findViewById(R.id.appViewPager);
        CustomSearchViewAdapter adapter=new CustomSearchViewAdapter(getSupportFragmentManager());
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //add style for transforming of viewpager
        pager.setPageTransformer(true, adapter);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));
    }
}
