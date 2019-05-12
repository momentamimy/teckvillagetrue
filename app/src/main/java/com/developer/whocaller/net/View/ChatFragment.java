package com.developer.whocaller.net.View;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.developer.whocaller.net.Services.UploadnewContactAdded;
import com.developer.whocaller.net.Controller.CustomChatViewAdapter;
import com.developer.whocaller.net.MainActivity;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import com.developer.whocaller.net.SearchActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    TabLayout tabs;
    ViewPager viewPager;
    FloatingActionButton fab;
    RelativeLayout searchLayout;
    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //service
        UploadNewContact();

        tabs=view.findViewById(R.id.tabs);
        viewPager=view.findViewById(R.id.appViewPager);

        CustomChatViewAdapter adapter=new CustomChatViewAdapter(getChildFragmentManager());

        //add style for transforming of viewpager
        viewPager.setPageTransformer(true, adapter);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        fab=view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),SendToChatActivity.class));
            }
        });

        searchLayout=view.findViewById(R.id.search_layout);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SearchActivity.class));
            }
        });
        ImageView icon=view.findViewById(R.id.iconnnn);

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) getActivity()).openDrawer();
            }
        });
    }



    void UploadNewContact(){
        Intent mIntent = new Intent(getActivity(), UploadnewContactAdded.class);
        UploadnewContactAdded.enqueueWork(getActivity(), mIntent);

    }

}
