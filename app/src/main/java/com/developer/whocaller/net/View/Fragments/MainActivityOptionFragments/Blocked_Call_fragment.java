package com.developer.whocaller.net.View.Fragments.MainActivityOptionFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.whocaller.net.Model.Get_list_from_logCall_depend_selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.developer.whocaller.net.Controller.LogAdapter;
import com.developer.whocaller.net.R;
import com.developer.whocaller.net.Model.Get_Calls_Log;
import com.developer.whocaller.net.Model.GroupListByDate;
import com.developer.whocaller.net.Model.LogInfo;
import com.developer.whocaller.net.Model.database.Database_Helper;

/**
 * A simple {@link Fragment} subclass.
 */
public class Blocked_Call_fragment extends Fragment {

    Get_Calls_Log get_calls_log;
    Get_list_from_logCall_depend_selection get_outgoing_list;
    GroupListByDate groupListByDate;
    ImageView close_search;
    RecyclerView searchLogs;
    LinearLayoutManager lLayout2;
    ArrayList<LogInfo> logInfos;
    LogAdapter adapter1;
    TextView emptyrecycle;
    Database_Helper db;
    List<Long> BlockInfoHistory=new ArrayList<Long>();
    LogInfo logInfo;

    public Blocked_Call_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_missed__call_fragment, container, false);


         //Title
        TextView Activitytitle=view.findViewById(R.id.titleoffragment);
        Activitytitle.setText("Blocked");

        emptyrecycle=view.findViewById(R.id.textifempty);
        emptyrecycle.setText("No blocked calls");

        //get_outgoing_list=new Get_list_from_logCall_depend_selection(getActivity());
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


        db=new Database_Helper(getActivity());
        BlockInfoHistory=db.getAllBlocklistHistoryCallLogIDCOLUMN();//retreive all ID of call Log

        //IF NO Log Call history
        if(BlockInfoHistory.size()>0){

            if(get_calls_log.CheckPermission()) {

                for (int i = 0; i < BlockInfoHistory.size(); i++) {
                    Log.w("showIdOfLogCall", String.valueOf(BlockInfoHistory.get(i)));

                    logInfo=new LogInfo();
                    logInfo=Get_Calls_Log.getCalllogByID(getActivity(), BlockInfoHistory.get(i));//Get Log call Details from Call Log Table
                    if(logInfo != null){
                        logInfos.add(logInfo);
                    }

                }

            }


        }

        /*
        if(get_calls_log.CheckPermission()){
            logInfos=get_outgoing_list.get_Outgoing_list(CallLog.Calls.TYPE + "=" + CallLog.Calls.BLOCKED_TYPE);
        }*/


        if(logInfos.size()>0){

            Collections.sort(logInfos, LogInfo.BY_DATE);
            lLayout2 = new LinearLayoutManager(getActivity());
            searchLogs.setLayoutManager(lLayout2);
            searchLogs.setItemAnimator(new DefaultItemAnimator());
            adapter1=new LogAdapter(getActivity(),groupListByDate.groupListByDate(logInfos));
            searchLogs.setAdapter(adapter1);
            toggleEmptyCases(logInfos);

        }else {
            //empty recycleview
            lLayout2 = new LinearLayoutManager(getActivity());
            searchLogs.setLayoutManager(lLayout2);
            searchLogs.setItemAnimator(new DefaultItemAnimator());
            adapter1=new LogAdapter(getActivity(),logInfos);
            searchLogs.setAdapter(adapter1);
            toggleEmptyCases(logInfos);

        }







        return view;
    }


    private void toggleEmptyCases(ArrayList<LogInfo> logInfosemp) {

         if (logInfosemp.size() > 0) {

            emptyrecycle.setVisibility(View.GONE);

        } else {
            emptyrecycle.setVisibility(View.VISIBLE);

        }
    }

}
