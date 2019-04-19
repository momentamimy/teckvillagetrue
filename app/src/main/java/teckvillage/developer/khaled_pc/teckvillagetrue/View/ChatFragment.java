package teckvillage.developer.khaled_pc.teckvillagetrue.View;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.CustomChatViewAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.CustomRecyclerViewChatAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.SendToChatContactsAdapters;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.ApiAccessToken;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.DataReceivedChatUsers;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.LastMessageModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.RoomModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.WhoCallerApi;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.retrofitHead;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.MessageInfo;

import static teckvillage.developer.khaled_pc.teckvillagetrue.Chat_MessagesChat.ChatStatusChanged;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    TabLayout tabs;
    ViewPager viewPager;
    FloatingActionButton fab;

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
    }



}
