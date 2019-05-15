package com.developer.whocaller.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.whocaller.net.Controller.CustomListViewAdapter;
import com.developer.whocaller.net.Controller.CustomRecyclerViewAdapter;
import com.developer.whocaller.net.Model.Get_User_Contacts;
import com.developer.whocaller.net.Model.MessageInfo;
import com.developer.whocaller.net.Model.UserContactData;

import java.util.ArrayList;

import com.developer.whocaller.net.R;


public class FragMessageContact extends Fragment {

    Get_User_Contacts get_user_contacts;
    ArrayList<UserContactData> userContactData;

    private static FragMessageOthers inst;
    static ArrayList<MessageInfo> contactMessageInfos=new ArrayList<>();
    static ArrayList<MessageInfo> allMessageContactInfos=new ArrayList<>();
    //ListView smsListView;
    // ArrayAdapter arrayAdapter;
    static CustomListViewAdapter customCotactsListViewAdapter;
    static CustomRecyclerViewAdapter customCotactsRecyclerViewAdapter;

    static RecyclerView smsRecyclerView;

    public FragMessageContact() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_message_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //LoadingContactToast(getContext());
        get_user_contacts=new Get_User_Contacts(getActivity());

        //smsListView = (ListView) view.findViewById(R.id.SMSList);
        smsRecyclerView=view.findViewById(R.id.SMSRecycler);
        LinearLayoutManager lLayout=new LinearLayoutManager(getContext());
        customCotactsRecyclerViewAdapter=new CustomRecyclerViewAdapter(allMessageContactInfos,getContext(),"Contact");
        smsRecyclerView.setLayoutManager(lLayout);
        smsRecyclerView.setAdapter(customCotactsRecyclerViewAdapter);

        customCotactsListViewAdapter=new CustomListViewAdapter(contactMessageInfos,getActivity());
        /*smsListView.setAdapter(customCotactsListViewAdapter);
        smsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getContext(),SMS_MessagesChat.class);
                intent.putExtra("LogSMSName",contactMessageInfos.get(position).logName);
                intent.putExtra("LogSMSAddress",contactMessageInfos.get(position).logAddress);
                intent.putExtra("LogSMSThreadID",contactMessageInfos.get(position).thread_id);
                intent.putExtra("LogSMSPosition",position);
                intent.putExtra("TYPE","Contact");
                startActivity(intent);

                /*MessageInfo info=contactMessageInfos.get(position);
                info.setRead("true");
                contactMessageInfos.set(position,info);
                customCotactsListViewAdapter.notifyDataSetChanged();

            }
        });
        smsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.d("2a5er_position", String.valueOf(smsListView.getLastVisiblePosition()));
                if (smsListView.getLastVisiblePosition()>=contactMessageInfos.size()-1&&!endContactsList)
                {
                    // getLoaderManager().initLoader(1,null,FragMessageOthers.this);
                    createContactsloader();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
*/
        // Add SMS Read Permision At Runtime
        // Todo : If Permission Is Not GRANTED
        if(ContextCompat.checkSelfPermission(getActivity().getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {

            // Todo : If Permission Granted Then Show SMS

        } else {
            // Todo : Then Set Permission
            final int REQUEST_CODE_ASK_PERMISSIONS = 123;
            ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    public static void UpdateList()
    {

    }

    static ProgressDialog clt;
    public static void LoadingContactToast(Context context)
    {
        clt = new ProgressDialog(context);
        clt.setTitle("Loading SMS");
        clt.show();
    }
    public static void DismissLoadingContactToast()
    {
        if(clt.isShowing()){
        clt.dismiss();
        }

    }

    public static void createContactsloader()
    {/*
        if (!endContactsList)
        {
            int count=0;
            int i=0;
            for (i=contactMessageInfos.size();i<allMessageContactInfos.size();i++)
            {

                if (count>20)
                {
                    break;
                }
                contactMessageInfos.add(allMessageContactInfos.get(i));
                count++;
            }
            if (i==allMessageContactInfos.size())
            {
                endContactsList=true;
            }
            clt.dismiss();
            customCotactsListViewAdapter.notifyDataSetChanged();
        }
    */
    customCotactsRecyclerViewAdapter.notifyDataSetChanged();
    }


}
