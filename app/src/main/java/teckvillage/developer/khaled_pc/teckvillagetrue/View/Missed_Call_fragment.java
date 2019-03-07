package teckvillage.developer.khaled_pc.teckvillagetrue.View;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import teckvillage.developer.khaled_pc.teckvillagetrue.CallSearchLogAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.LogAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.ContactInfo;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.Get_Calls_Log;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.GroupListByDate;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.LogInfo;

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


        Collections.sort(logInfos, LogInfo.BY_DATE);

        lLayout2 = new LinearLayoutManager(getActivity());
        searchLogs.setLayoutManager(lLayout2);
        searchLogs.setItemAnimator(new DefaultItemAnimator());
        adapter1=new LogAdapter(getActivity(),groupListByDate.groupListByDate(logInfos));
        searchLogs.setAdapter(adapter1);


        return view;
    }



}
