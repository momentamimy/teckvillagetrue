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
    static ArrayList<MessageInfo> allMessageOtherInfos=new ArrayList<>();

    static CustomRecyclerViewAdapter customOtherRecyclerViewAdapter;

    static RecyclerView smsRecyclerView;

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

        smsRecyclerView=view.findViewById(R.id.SMSRecycler);
        LinearLayoutManager lLayout=new LinearLayoutManager(getContext());
        customOtherRecyclerViewAdapter=new CustomRecyclerViewAdapter(allMessageOtherInfos,getContext(),"Other");
        smsRecyclerView.setLayoutManager(lLayout);
        smsRecyclerView.setAdapter(customOtherRecyclerViewAdapter);

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
        customOtherRecyclerViewAdapter.notifyDataSetChanged();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        customOtherRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
