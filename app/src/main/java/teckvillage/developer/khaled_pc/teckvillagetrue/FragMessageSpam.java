package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.CustomListViewAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.Get_User_Contacts;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.MessageInfo;


public class FragMessageSpam extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {

    Get_User_Contacts get_user_contacts;

    private static FragMessageSpam inst;
    static ArrayList<MessageInfo> spamMessageInfos=new ArrayList<>();
    static ArrayList<MessageInfo> allMessageSpamInfos=new ArrayList<>();
    ListView smsListView;
    static CustomListViewAdapter customSpamListViewAdapter;
    static boolean endSpamList=false;

    public static FragMessageSpam instance() {
        return inst;
    }
    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }
    public FragMessageSpam() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_message_spam, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        get_user_contacts=new Get_User_Contacts(getActivity());

        smsListView = (ListView) view.findViewById(R.id.SMSList);

        customSpamListViewAdapter=new CustomListViewAdapter(spamMessageInfos,getActivity());
        smsListView.setAdapter(customSpamListViewAdapter);
        smsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(getContext(),SMS_MessagesChat.class);
                intent.putExtra("LogSMSName",spamMessageInfos.get(position).logName);
                intent.putExtra("LogSMSAddress",spamMessageInfos.get(position).logAddress);
                intent.putExtra("LogSMSThreadID",spamMessageInfos.get(position).thread_id);
                intent.putExtra("LogSMSPosition",position);
                intent.putExtra("TYPE","Spam");
                startActivity(intent);

                /*MessageInfo info=spamMessageInfos.get(position);
                info.setRead("true");
                spamMessageInfos.set(position,info);
                customSpamListViewAdapter.notifyDataSetChanged();
            */}
        });
        smsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.d("2a5er_position", String.valueOf(smsListView.getLastVisiblePosition()));
                if (smsListView.getLastVisiblePosition()>=spamMessageInfos.size()-1&&!endSpamList)
                {
                    // getLoaderManager().initLoader(1,null,FragMessageOthers.this);
                    createSpamloader();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        // Add SMS Read Permision At Runtime
        // Todo : If Permission Is Not GRANTED
        if(ContextCompat.checkSelfPermission(getActivity().getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {

            // Todo : If Permission Granted Then Show SMS
            //getLoaderManager().initLoader(2,null,FragMessageOthers.this);

        } else {
            // Todo : Then Set Permission
            final int REQUEST_CODE_ASK_PERMISSIONS = 123;
            ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
        }

        createSpamloader();
    }


    public static void createSpamloader()
    {
        if (!endSpamList)
        {
            int count=0;
            int i=0;
            for (i=spamMessageInfos.size();i<allMessageSpamInfos.size();i++)
            {

                if (count>20)
                {
                    break;
                }
                spamMessageInfos.add(allMessageSpamInfos.get(i));
                count++;
            }
            if (i==allMessageSpamInfos.size())
            {
                endSpamList=true;
            }

            customSpamListViewAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        customSpamListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
