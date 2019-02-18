package teckvillage.developer.khaled_pc.teckvillagetrue;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.CustomMessageViewAdapter;


public class Message_Fragment extends Fragment {
    TabLayout tabs;
    ViewPager pager;

    public Message_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabs=view.findViewById(R.id.tabs);
        pager=view.findViewById(R.id.appViewPager);

        CustomMessageViewAdapter adapter=new CustomMessageViewAdapter(getActivity().getSupportFragmentManager());

        //add style for transforming of viewpager
        pager.setPageTransformer(true, adapter);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));

    }
}
