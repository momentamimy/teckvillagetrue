package com.developer.whocaller.net.View.Fragments.MainActivityOptionFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import com.developer.whocaller.net.Controller.LogAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import com.developer.whocaller.net.Model.Get_Calls_Log;
import com.developer.whocaller.net.Model.GroupListByDate;
import com.developer.whocaller.net.Model.LogInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class Missed_Call_fragment extends Fragment {

    Get_Calls_Log get_calls_log;
    GroupListByDate groupListByDate;
    ImageView close_search;
    RecyclerView searchLogs;
    LinearLayoutManager lLayout2;
    ArrayList<LogInfo> logInfos;
    LogAdapter adapter1;
    TextView emptyrecycle;


    public Missed_Call_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_missed__call_fragment, container, false);


         //Title
        TextView Activitytitle=view.findViewById(R.id.titleoffragment);
        Activitytitle.setText("Missed");

        emptyrecycle=view.findViewById(R.id.textifempty);
        emptyrecycle.setText("No missed calls");

        get_calls_log=new Get_Calls_Log(getActivity());
        groupListByDate=new GroupListByDate();

        //Close Fragment
        close_search=view.findViewById(R.id.close_Search);
        searchLogs=view.findViewById(R.id.Search_Log_recycleview);

        close_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() != 0) {
                    getFragmentManager().popBackStackImmediate();
                }
            }
        });

        //**********************Log List******************************
        logInfos=new ArrayList<>();
        if(get_calls_log.CheckPermission()){

            logInfos=get_calls_log.getMissed();


        }




        if(logInfos.size()>0) {

            Collections.sort(logInfos, LogInfo.BY_DATE);
            lLayout2 = new LinearLayoutManager(getActivity());
            searchLogs.setLayoutManager(lLayout2);
            searchLogs.setItemAnimator(new DefaultItemAnimator());
            adapter1 = new LogAdapter(getActivity(), groupListByDate.groupListByDate(logInfos));
            searchLogs.setAdapter(adapter1);
            toggleEmptyCases(logInfos);
        }


        return view;
    }



    private void toggleEmptyCases(ArrayList<LogInfo> logInfosemp) {
        // you can check casessList.size() > 0

        if (logInfosemp.size() > 0) {

            emptyrecycle.setVisibility(View.GONE);

        } else {
            emptyrecycle.setVisibility(View.VISIBLE);

        }
    }

}
