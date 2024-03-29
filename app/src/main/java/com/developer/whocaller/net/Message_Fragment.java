package com.developer.whocaller.net;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.developer.whocaller.net.Controller.CustomMessageViewAdapter;

import java.util.ArrayList;

import com.developer.whocaller.net.Model.Get_User_Contacts;
import com.developer.whocaller.net.Model.MessageInfo;


import static com.developer.whocaller.net.FragMessageContact.DismissLoadingContactToast;
import static com.developer.whocaller.net.FragMessageContact.LoadingContactToast;
import static com.developer.whocaller.net.FragMessageContact.allMessageContactInfos;
import static com.developer.whocaller.net.FragMessageContact.createContactsloader;
import static com.developer.whocaller.net.FragMessageOthers.allMessageOtherInfos;
import static com.developer.whocaller.net.FragMessageOthers.createOthersloader;


public class Message_Fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> , OnBackPressedListener{

    public static boolean SMSCHANGE=true;

    boolean DialogProgress=true;
    Get_User_Contacts get_user_contacts;
    FloatingActionButton fab;
    TabLayout tabs;
    ViewPager pager;
    static ArrayList<MessageInfo> messageInfos=new ArrayList<>();

    RelativeLayout searchLayout;
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

        get_user_contacts=new Get_User_Contacts(getActivity());
        tabs=view.findViewById(R.id.tabs);
        pager=view.findViewById(R.id.appViewPager);
        fab=view.findViewById(R.id.Send_message_fab_button);
        searchLayout=view.findViewById(R.id.search_layout);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),SearchActivity.class));
            }
        });

        ImageView icon=view.findViewById(R.id.iconnnn);
        final ImageView threedots=view.findViewById(R.id.threedots);
        threedots.setVisibility(View.GONE);


        if(ContextCompat.checkSelfPermission(getActivity().getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {

            // Todo : If Permission Granted Then Show SMS
            getLoaderManager().initLoader(1,null,this);

        } else {
            // Todo : Then Set Permission
            final int REQUEST_CODE_ASK_PERMISSIONS = 123;
            ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
        }


        //********Option menu****************************
        threedots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //
                final PopupMenu popupMenu = new PopupMenu(getContext(), threedots);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.readall:
                                // item one clicked
                                return true;


                        }

                        return false;
                    }
                });
                popupMenu.inflate(R.menu.message_menu);
                popupMenu.show();



            }
        });


        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) getActivity()).openDrawer();
            }
        });



        CustomMessageViewAdapter adapter=new CustomMessageViewAdapter(getChildFragmentManager());

        //add style for transforming of viewpager
        pager.setPageTransformer(true, adapter);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),SendToActivity.class));
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (DialogProgress)
            {
                LoadingContactToast(getActivity());
                DialogProgress=false;
            }
        Uri uri= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            uri = Telephony.Sms.CONTENT_URI;
        }
        else
        {
            uri=Uri.parse("content://sms/");
        }
        getContext().getContentResolver().notifyChange(uri, null);

            String[] projection = new String[] { "address", "body", "date", "type","read,thread_id"};
            CursorLoader cursorLoader=new CursorLoader(getActivity(),uri ,projection,null,null,"date desc");
            return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (SMSCHANGE) {
            Log.d("paleez_LA_Paleez_LA", "onLoadFinished");
            messageInfos.clear();
            allMessageOtherInfos.clear();
            allMessageContactInfos.clear();
            FragMessageSpam.allMessageSpamInfos.clear();
            int indextype = data.getColumnIndex("type");
            int indexBody = data.getColumnIndex("body");
            int indexAddress = data.getColumnIndex("address");
            int dateColumn = data.getColumnIndex("date");
            int index_seen = data.getColumnIndex("read");
            int threadId_index = data.getColumnIndex("thread_id");

            if (indexBody < 0 || !data.moveToFirst()) return;
            do {
                if (!TextUtils.isEmpty(data.getString(indexAddress))) {

                    Long timestamp = Long.parseLong(data.getString(dateColumn));

                    MessageInfo info = new MessageInfo(null, data.getString(indexAddress), data.getString(indexBody), timestamp, data.getString(indexAddress), data.getString(index_seen), data.getString(threadId_index));
                    info.setType(data.getString(indextype));
                    boolean Add = true;
                    for (int i = 0; i < messageInfos.size(); i++) {
                        if (!TextUtils.isEmpty(messageInfos.get(i).logName))
                            if (messageInfos.get(i).thread_id.equals(data.getString(threadId_index))) {
                                Add = false;
                            }
                    }
                    if (!TextUtils.isEmpty(info.getLogAddress()))
                        if (Add) {
                            String s = info.logAddress.replace("+", "");
                            String regex = "\\d+";
                            String name = null;
                            if (s.matches(regex)) {
                                name = get_user_contacts.getContactName(info.logAddress, getActivity());
                            }
                            if (!TextUtils.isEmpty(name)) {
                                info.setLogName(name);//contact
                                info.setTypeMessage("contact");
                            } else {
                                if (info.logAddress.matches(regex) && info.logAddress.length() < 6) {
                                    info.setTypeMessage("spam");//ohters or spanm :to be continue
                                } else {
                                    info.setTypeMessage("others");//ohters or spanm :to be continue
                                }
                            }
                            messageInfos.add(info);
                        }

                    //  arrayAdapter.add(str);
                }
            } while (data.moveToNext());
            for (int i = 0; i < messageInfos.size(); i++) {
                if (messageInfos.get(i).getTypeMessage().equals("contact")) {
                    allMessageContactInfos.add(messageInfos.get(i));
                } else if (messageInfos.get(i).getTypeMessage().equals("others")) {
                    allMessageOtherInfos.add(messageInfos.get(i));
                } else if (messageInfos.get(i).getTypeMessage().equals("spam")) {
                    FragMessageSpam.allMessageSpamInfos.add(messageInfos.get(i));
                }
            }
            DismissLoadingContactToast();
            createOthersloader();
            createContactsloader();
            FragMessageSpam.createSpamloader();
            SMSCHANGE=false;
        }
        Log.d("la_paleeez",allMessageOtherInfos.get(0).logMessage);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("laPaleezLaPaleezLa", "onResume: ");
        //getLoaderManager().restartLoader(1,null,this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("laPaleezLaPaleezLa", "onStart: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("laPaleezLaPaleezLa", "onStop: ");
    }

    @Override
    public void onBackPressed() {

        getActivity().finish();

    }
}
