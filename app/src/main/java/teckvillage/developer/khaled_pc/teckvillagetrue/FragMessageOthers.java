package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.content.ContentResolver;
import android.content.Context;
import android.support.v4.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;
import java.util.Calendar;

import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.CustomListViewAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.CustomRecyclerViewAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.Get_User_Contacts;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.MessageInfo;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.UserContactData;


public class FragMessageOthers extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    Get_User_Contacts get_user_contacts;

    private static FragMessageOthers inst;
    ArrayList<String> smsMessagesList = new ArrayList<String>();
    static ArrayList<MessageInfo> ohterMessageInfos=new ArrayList<>();
    static ArrayList<MessageInfo> allMessageOtherInfos=new ArrayList<>();
    ListView smsListView;
    static CustomListViewAdapter customOhtersListViewAdapter;
    static boolean endOhtersList=false;

    public static FragMessageOthers instance() {
        return inst;
    }
    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    public FragMessageOthers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_message_others, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        get_user_contacts=new Get_User_Contacts(getActivity());

        smsListView = (ListView) view.findViewById(R.id.SMSList);

        customOhtersListViewAdapter=new CustomListViewAdapter(ohterMessageInfos,getActivity());
        smsListView.setAdapter(customOhtersListViewAdapter);
        smsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(getContext(),SMS_MessagesChat.class);
                intent.putExtra("LogSMSName",ohterMessageInfos.get(position).logName);
                intent.putExtra("LogSMSAddress",ohterMessageInfos.get(position).logAddress);
                startActivity(intent);

                MessageInfo info=ohterMessageInfos.get(position);
                info.setRead("true");
                ohterMessageInfos.set(position,info);
                customOhtersListViewAdapter.notifyDataSetChanged();
            }
        });
        smsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.d("2a5er_position", String.valueOf(smsListView.getLastVisiblePosition()));
                if (smsListView.getLastVisiblePosition()>=ohterMessageInfos.size()-1&&!endOhtersList)
                {
                   // getLoaderManager().initLoader(1,null,FragMessageOthers.this);
                    createOthersloader();
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

    }


    public static void createOthersloader()
    {
        if (!endOhtersList)
        {
            int count=0;
            int i=0;
            for (i=ohterMessageInfos.size();i<allMessageOtherInfos.size();i++)
            {

                if (count>20)
                {
                    break;
                }
                ohterMessageInfos.add(allMessageOtherInfos.get(i));
                count++;
            }
            if (i==allMessageOtherInfos.size())
            {
                endOhtersList=true;
            }

            customOhtersListViewAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        customOhtersListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }



    /*public void updateList(final MessageInfo messageInfo) {
        customListViewAdapter.insert(messageInfo, 0);
        customListViewAdapter.notifyDataSetChanged();
    }*/

}
