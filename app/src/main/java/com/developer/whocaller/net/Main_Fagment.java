package com.developer.whocaller.net;


import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.developer.whocaller.net.Controller.LocaleHelper;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.developer.whocaller.net.Controller.ContactAdapter;
import com.developer.whocaller.net.Controller.CustomGridAdapter;
import com.developer.whocaller.net.Controller.LogAdapter;

import com.developer.whocaller.net.View.Fragments.MainActivityOptionFragments.Blocked_Call_fragment;
import com.developer.whocaller.net.View.Fragments.MainActivityOptionFragments.Income_Call_fragment;
import com.developer.whocaller.net.View.Fragments.MainActivityOptionFragments.Missed_Call_fragment;
import com.developer.whocaller.net.View.Fragments.MainActivityOptionFragments.Outgoing_Call_fragment;
import com.developer.whocaller.net.Model.Get_Calls_Log;
import com.developer.whocaller.net.View.KeyboardlessEditText;
import com.developer.whocaller.net.Model.ContactInfo;
import com.developer.whocaller.net.Model.GridListDataModel;
import com.developer.whocaller.net.Model.GroupListByDate;
import com.developer.whocaller.net.Model.LogInfo;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Main_Fagment extends Fragment implements OnBackPressedListener , LoaderManager.LoaderCallbacks<Cursor>,OpenDialPad,MyInterface {

    String lastnum = "0";
    int numbersofcall = 1;
    List<LogInfo> consolidatedList = new ArrayList<>();
    /*--------------------------------------dial_pad_layout------------------------------------------*/
    public RelativeLayout padLayout;
    public FloatingActionButton fab;
    public GridView gridView;
    public View gridShadow;
    public TextView grid_num_tv;
    public KeyboardlessEditText phone_num_edt;
    public RelativeLayout textLayout;
    public ImageView back_space;
    ArrayList<LogInfo> loglist102 = new ArrayList<>();
    ArrayList<LogInfo> loglist103 = new ArrayList<>();
    ArrayList<Long> loglist_ID = new ArrayList<>();
    public ImageView add_contact;

    static final String[] GRID_NUM = new String[]{
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "*", "0", "#"};
    static final String[] GRID_LETTERS = new String[]{
            " ", "abc", "def",
            "ghi", "jkl", "mno",
            "pqrs", "tuv", "wxyz",
            " ", "+", " "};
    public ArrayList<GridListDataModel> myGridList = new ArrayList<GridListDataModel>();

    boolean aBoolean = false;//for dial phone button open_close
    boolean CursorVisibility = false;
    boolean firstclick = true;
    boolean scroll;

    AnimationDrawable rocketAnimation;//icon anim
    Dialog MyDialogSpeedDial;
    Dialog MyDialogAssignNum;
    SharedPreferences sharedPreferences;
    RelativeLayout searchLayout;
    /*--------------------------------------dial_pad_layout------------------------------------------*/

    RecyclerView contacts, logs, searchLogs;
    LinearLayoutManager lLayout;
    LinearLayoutManager lLayout1;
    LinearLayoutManager lLayout2;
    ArrayList<LogInfo> callLogInfos = new ArrayList<>();
    ArrayList<LogInfo> logInfos = new ArrayList<>();

    ArrayList<LogInfo> loguinfoupdate;
    LogAdapter adapter1;
    Get_Calls_Log get_calls_log;
    boolean shouldExecuteOnResume;

    CoordinatorLayout searchCallLayout;
    ImageView close_search;
    GroupListByDate groupListByDate;
    ContactAdapter adaptertopten;
    List<ContactInfo> contactInfos55;
    ImageView firstimagwaiting, secimagwaiting;
    TextView textwaiting;
    TextView noLogcalltext;
    public static OpenDialPad openDialPad;
    private MyInterface listener ;
    boolean isSinglePhoneNumOnly;
    ImageView emptyrecycle;

    TextView whoCaller_search;
    TextView callText;

    public Main_Fagment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main__fagment, container, false);



        shouldExecuteOnResume = false;

        firstimagwaiting=view.findViewById(R.id.waitingimg_topten);
        secimagwaiting=view.findViewById(R.id.waitingimg_topten2);
        textwaiting=view.findViewById(R.id.textwaiting);
        noLogcalltext=view.findViewById(R.id.nologcalltext);
        emptyrecycle=view.findViewById(R.id.nologcallimage);
        whoCaller_search=view.findViewById(R.id.WhoCaller_search);
        callText=view.findViewById(R.id.call_text);

        getLoaderManager().initLoader(1, null, this);


        logs = view.findViewById(R.id.Logs_recycleview);


        groupListByDate = new GroupListByDate(getActivity());

        close_search = view.findViewById(R.id.close_Search);
        close_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_search_anim_button(200);
            }
        });

        RelativeLayout relativeLayout = view.findViewById(R.id.dialpad_bottom_rl);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //updateViews("en");
            }
        });

        contacts = view.findViewById(R.id.contact_recycleview);

        searchLogs = view.findViewById(R.id.Search_Log_recycleview);
        searchLayout = view.findViewById(R.id.search_layout);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SearchActivity.class));
            }
        });

        /*--------------------------------------dial_pad_layout------------------------------------------*/
        searchCallLayout = view.findViewById(R.id.search_call_layout);
        padLayout = view.findViewById(R.id.pad_layout);
        fab = view.findViewById(R.id.fab);
        gridView = (GridView) view.findViewById(R.id.gridView1);
        gridShadow = view.findViewById(R.id.shadow_gridView1);
        phone_num_edt = (KeyboardlessEditText) view.findViewById(R.id.dial_pad_num_editText);
        back_space = view.findViewById(R.id.backspace_imageView);
        add_contact = view.findViewById(R.id.add_Contact);
        final ImageView threedots = view.findViewById(R.id.threedots);
        ImageView icon = view.findViewById(R.id.iconnnn);
        sharedPreferences = getActivity().getSharedPreferences("number", Context.MODE_PRIVATE);


        //Open Drawer from Fragment
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });


        //********Option menu****************************
        threedots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //
                final PopupMenu popupMenu = new PopupMenu(getContext(), threedots);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case 1:
                                // item one clicked
                                Income_Call_fragment nextFrag3 = new Income_Call_fragment();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container_main, nextFrag3, "findThisFragment")
                                        .addToBackStack(null)
                                        .commit();
                                return true;
                            case 2:
                                // item one clicked
                                Outgoing_Call_fragment nextFrag2 = new Outgoing_Call_fragment();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container_main, nextFrag2, "findThisFragment")
                                        .addToBackStack(null)
                                        .commit();
                                return true;
                            case 3:
                                // item one clicked
                                Missed_Call_fragment nextFrag = new Missed_Call_fragment();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container_main, nextFrag, "findThisFragment")
                                        .addToBackStack(null)
                                        .commit();

                                return true;
                            case 4:
                                // item one clicked
                                Blocked_Call_fragment nextFrag5 = new Blocked_Call_fragment();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container_main, nextFrag5, "findThisFragment")
                                        .addToBackStack(null)
                                        .commit();
                                return true;
                            case 5:
                                // item one clicked
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle(R.string.delete_dialog);
                                builder.setMessage(R.string.dec_delete_dialog);

                                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                                            // TODO: Consider calling
                                            //    ActivityCompat#requestPermissions
                                            // here to request the missing permissions, and then overriding
                                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                            //                                          int[] grantResults)
                                            // to handle the case where the user grants the permission. See the documentation
                                            // for ActivityCompat#requestPermissions for more details.
                                            return;
                                        }
                                        getActivity().getContentResolver().delete(CallLog.Calls.CONTENT_URI, null, null);

                                        //-------------------------------------------------------------restart app-----------------------------------------------------------------------------------------------
                                        getActivity().finish();
                                        startActivity(getActivity().getBaseContext().getPackageManager() .getLaunchIntentForPackage( getActivity().getBaseContext().getPackageName()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    }
                                });
                                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.show();

                                return true;

                        }

                        return false;
                    }
                });

                popupMenu.inflate(R.menu.main);
                popupMenu.getMenu().add(0, 1, 1, menuIconWithText(getResources().getDrawable(R.drawable.ic_recieve_call_menu), getString(R.string.income_popupmeun)));
                popupMenu.getMenu().add(0, 2, 2, menuIconWithText(getResources().getDrawable(R.drawable.ic_arrow_right_menu), getString(R.string.outgoing_popup_menu)));
                popupMenu.getMenu().add(0, 3, 3, menuIconWithText(getResources().getDrawable(R.drawable.ic_missed_menu), getString(R.string.missed_popup_meun)));
                popupMenu.getMenu().add(0, 4, 4, menuIconWithText(getResources().getDrawable(R.drawable.ic_block_black_24dp), getString(R.string.blocked_popup_menu)));
                popupMenu.getMenu().add(5, 5, 5, menuIconWithText(getResources().getDrawable(R.drawable.ic_delete_black_24dp), getString(R.string.delete_popup_menu)));

                popupMenu.show();

            }
        });


        get_calls_log = new Get_Calls_Log(getActivity());
        textLayout = view.findViewById(R.id.dial_num_edt_rl);
        //get_calls_log=new Get_Calls_Log(getActivity());


        /*--------------------------------------dial_pad_layout------------------------------------------*/
        //make padlayout invisable
        callAnimDefault();

        final TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(getActivity());

        String imeiSIM1 = telephonyInfo.getImsiSIM1();
        String imeiSIM2 = telephonyInfo.getImsiSIM2();

        final boolean isSIM1Ready = telephonyInfo.isSIM1Ready();
        final boolean isSIM2Ready = telephonyInfo.isSIM2Ready();

        final boolean isDualSIM = telephonyInfo.isDualSIM();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone_num_edt.getText().length() == 0) {
                    callButtonAnim(400);
                } else if (scroll == true) {
                    scrollOpenAnim(400);
                } else {
                    if (isDualSIM) {
                        if (isSIM1Ready&&isSIM2Ready)
                        {
                            MyCustomDualSimDialog(phone_num_edt.getText().toString(),telephonyInfo);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            String string=phone_num_edt.getText().toString();
                            intent.setData(Uri.parse("tel:" + getStringHashTag(string)));
                            startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        String string=phone_num_edt.getText().toString();
                        intent.setData(Uri.parse("tel:" + getStringHashTag(string)));
                        startActivity(intent);
                    }
                }
            }
        });

        //fill numbers
        for (int i = 0; i < GRID_NUM.length; i++) {
            myGridList.add(new GridListDataModel(GRID_NUM[i], GRID_LETTERS[i]));
        }

        gridView.setAdapter(new CustomGridAdapter(getContext(), myGridList));
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                grid_num_tv = (TextView) view.findViewById(R.id.grid_num_textView);
                if (grid_num_tv.getText().toString().equals("1")) {
                    TelephonyManager tel = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return true;
                    }
                    String num = null;
                    if (tel != null) {
                        num = tel.getVoiceMailNumber();
                    }
                    if (num == null) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(getString(R.string.dialog_voicemail) +
                                getString(R.string.dialogvoicemail2))
                                .setCancelable(false)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    } else {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("voicemail:" + num));
                        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }


                } else if (grid_num_tv.getText().toString().equals("0")) {
                    if (CursorVisibility) {
                        insertSelection(phone_num_edt, "+");
                    } else {
                        phone_num_edt.setText(phone_num_edt.getText().toString() + "+");
                    }

                    if (firstclick) {
                        writeAnim(300);
                        firstclick = false;
                    } else {
                        SortSearchCallList(phone_num_edt.getText().toString());
                    }
                } else if (grid_num_tv.getText().toString().equals("#")) {
                    if (CursorVisibility) {
                        insertSelection(phone_num_edt, ";");
                    } else {
                        phone_num_edt.setText(phone_num_edt.getText().toString() + ";");
                    }

                    if (firstclick) {
                        writeAnim(300);
                        firstclick = false;
                    } else {
                        SortSearchCallList(phone_num_edt.getText().toString());
                    }
                } else if (grid_num_tv.getText().toString().equals("*")) {
                    if (CursorVisibility) {
                        insertSelection(phone_num_edt, ",");
                    } else {
                        phone_num_edt.setText(phone_num_edt.getText().toString() + ",");
                    }

                    if (firstclick) {
                        writeAnim(300);
                        firstclick = false;
                    } else {
                        SortSearchCallList(phone_num_edt.getText().toString());
                    }
                } else {
                    String choosenNum = sharedPreferences.getString("#" + grid_num_tv.getText().toString(), "");
                    if (choosenNum.equals("")) {
                        MyCustomSpeedDialog(grid_num_tv.getText().toString());
                    } else {
                        if (isDualSIM) {
                            if (isSIM1Ready&&isSIM2Ready)
                            {
                                MyCustomDualSimDialog(choosenNum,telephonyInfo);
                            } else {
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + getStringHashTag(choosenNum)));
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + getStringHashTag(choosenNum)));
                            startActivity(intent);
                        }
                    }
                }
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                grid_num_tv = (TextView) view.findViewById(R.id.grid_num_textView);

                //Toast.makeText(getApplicationContext(), grid_num_tv.getText(), Toast.LENGTH_SHORT).show();
                if (CursorVisibility) {
                    insertSelection(phone_num_edt, grid_num_tv.getText().toString());
                } else {
                    phone_num_edt.setText(phone_num_edt.getText().toString() + grid_num_tv.getText());
                    phone_num_edt.setSelection(phone_num_edt.length() - 1);
                }
                if (firstclick) {
                    writeAnim(300);
                    firstclick = false;
                } else {
                    SortSearchCallList(phone_num_edt.getText().toString());
                }
            }
        });

        back_space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = phone_num_edt.getText().toString();

                if (CursorVisibility) {
                    removeSelection(phone_num_edt);
                } else {
                    phone_num_edt.setText(removeChar(num));
                }

                if (phone_num_edt.getText().length() == 0) {
                    deleteAnim(300);
                    firstclick = true;
                }
                SortSearchCallList(phone_num_edt.getText().toString());
            }
        });
        back_space.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                phone_num_edt.setText("");
                deleteAnim(300);
                firstclick = true;
                return true;
            }
        });

        add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_INSERT_OR_EDIT);
                i.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
                i.putExtra(ContactsContract.Intents.Insert.PHONE, phone_num_edt.getText().toString());
                if (Integer.valueOf(Build.VERSION.SDK) > 14)
                    i.putExtra("finishActivityOnSaveCompleted", true); // Fix for 4.0.3 +
                startActivityForResult(i, 50);
            }
        });


        phone_num_edt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CursorVisibility = true;
                return false;
            }
        });
        /*--------------------------------------dial_pad_layout------------------------------------------*/

        lLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        lLayout2 = new LinearLayoutManager(getActivity());

        contactInfos55 = new ArrayList<>();

        if (get_calls_log.CheckPermission()) {

            //contactInfos = get_calls_log.getTopTenContacts();
            LoadTopTenContactsSync();
        }


        contacts.setLayoutManager(lLayout);
        contacts.setItemAnimator(new DefaultItemAnimator());


        /*
        //**********************Log List******************************
        logInfos = new ArrayList<>();
        if (get_calls_log.CheckPermission()) {

            logInfos = get_calls_log.getCallDetails();

            callLogInfos = logInfos;
            for (int i = 0; i < callLogInfos.size(); i++) {
                for (int j = i + 1; j < callLogInfos.size(); j++) {
                    if (callLogInfos.get(i).getNumber().equals(callLogInfos.get(j).getNumber())) {
                        callLogInfos.remove(j--);
                    }
                }
            }


        }


        logs.setLayoutManager(lLayout1);
        logs.setItemAnimator(new DefaultItemAnimator());

        if(logInfos.size()!=0){
            Collections.sort(logInfos, LogInfo.BY_DATE);

            adapter1 = new LogAdapter(getActivity(), groupListByDate.groupListByDate(logInfos));
            logs.setAdapter(adapter1);
        }else {
            adapter1 = new LogAdapter(getActivity(), logInfos);
            logs.setAdapter(adapter1);

        }*/


        lLayout1 = new LinearLayoutManager(getActivity());

        logs.setLayoutManager(lLayout1);
        logs.setItemAnimator(new DefaultItemAnimator());
        logs.hasFixedSize();
        adapter1 = new LogAdapter(getActivity(), loglist103, this);
        logs.setAdapter(adapter1);
        //adapter1.notifyDataSetChanged();


        searchLogs.setLayoutManager(lLayout2);
        searchLogs.setItemAnimator(new DefaultItemAnimator());
        CallSearchLogAdapter adapter2 = new CallSearchLogAdapter(getActivity(), callLogInfos, phone_num_edt.getText().toString());
        searchLogs.setAdapter(adapter2);

        logs.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Scrolling up
                    //Log.d("mememem:","meeeshyyyyyy_UP");
                } else {
                    // Scrolling down
                    //Log.d("mememem:","meeeshyyyyyy_Down");
                }

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Do something
                    //Log.d("mememem:", "111111");
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // Do something
                    //Log.d("mememem:", "222222");
                    if (aBoolean == true) {
                        close_button(400);
                        aBoolean = false;
                    }
                } else {
                    // Do something
                    //Log.d("mememem:", "333333");
                }
            }
        });
        searchLogs.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Do something
                    //Log.d("mememem:", "111111");
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // Do something
                    //Log.d("mememem:", "222222");
                    if (!scroll)
                        scrollCloseAnim(400);
                } else {
                    // Do something
                    //Log.d("mememem:", "333333");
                }
            }
        });


        return view;
    }

    @Override
    public void onBackPressed() {
        if (aBoolean && firstclick) {
            close_button(400);
            aBoolean = false;
        } else if (aBoolean && !firstclick && !scroll) {
            scrollCloseAnim(400);
        } else if (aBoolean && !firstclick && scroll) {
            close_search_anim_button(200);
        } else {

            getActivity().finish();
        }

    }


    private List<LogInfo> SortSearchCallList(String num) {
        ArrayList<LogInfo> infos = new ArrayList<>();
        for (int i = 0; i < callLogInfos.size(); i++) {
            if (callLogInfos.get(i).getNumber().contains(num)) {
                infos.add(callLogInfos.get(i));
            }
        }
        int position;
        for (int i = 0; i < infos.size(); i++) {
            position = infos.get(i).getNumber().indexOf(num);
            if (infos.get(0).getNumber().indexOf(num) > position) {
                LogInfo inf = infos.get(i);
                infos.remove(i);
                infos.add(0, inf);
            }
        }

        CallSearchLogAdapter adapter = new CallSearchLogAdapter(getActivity(), infos, phone_num_edt.getText().toString());
        searchLogs.setAdapter(adapter);

        return infos;
    }


    /*-------------------------------------------dial_pad_layout--------------------------------------------------*/
    public void removeSelection(KeyboardlessEditText inputText) {

        int selectionStart = inputText.getSelectionStart();
        int selectionEnd = inputText.getSelectionEnd();


        if (selectionStart != selectionEnd) {
            inputText.getText().delete(selectionStart, selectionEnd);
        } else {
            if (selectionEnd != 0)
                inputText.getText().delete(selectionStart - 1, selectionStart);
        }
    }

    public void insertSelection(KeyboardlessEditText inputText, String numString) {
        int selectionStart = inputText.getSelectionStart();
        int selectionEnd = inputText.getSelectionEnd();

        if (selectionStart != selectionEnd) {
            inputText.getText().replace(selectionStart, selectionEnd, numString);
            selectionEnd = inputText.getSelectionEnd();
            inputText.setSelection(selectionEnd);
        } else {
            inputText.getText().insert(selectionStart, numString);
        }
    }

    public String removeChar(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public void callAnimDefault() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1){
            // Do something for lollipop and above versions
        } else{
            // do something for phones running an SDK before lollipop
            fab.setImageResource(R.drawable.ic_telephone);
        }

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        ObjectAnimator translationY = ObjectAnimator.ofFloat(padLayout, "y", metrics.widthPixels * 2); // metrics.heightPixels or root.getHeight()
        translationY.setDuration(0);
        translationY.start();

    }

    ObjectAnimator translationY;

    public ObjectAnimator callLayoutAnim(boolean up_down, int duration) {

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        if (up_down) {
            translationY = ObjectAnimator.ofFloat(padLayout, "y", 0); // metrics.heightPixels or root.getHeight()
            translationY.setDuration(duration);
        } else {
            translationY = ObjectAnimator.ofFloat(padLayout, "y", metrics.widthPixels * 2); // metrics.heightPixels or root.getHeight()
            translationY.setDuration(duration);
        }

        return translationY;

    }

    public void callButtonAnim(int dur) {
        if (!aBoolean) {
            open_button(dur);
            aBoolean = true;
        } else {
            close_button(dur);
            aBoolean = false;
        }

    }

    public void open_button(int duration, boolean myBoolean) {
        ObjectAnimator callLayoutAnim = callLayoutAnim(!myBoolean, 400);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(fab, "x", metrics.widthPixels / 2 - fab.getWidth() / 2); // metrics.heightPixels or root.getHeight()
        ObjectAnimator rotate = ObjectAnimator.ofFloat(fab, "rotation", 360);
        rotate.setDuration(duration);
        translationX.setDuration(duration);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(callLayoutAnim, rotate, translationX);
        animatorSet.start();
    }
    public int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
    public void open_button(int duration) {
        ObjectAnimator callLayoutAnim = callLayoutAnim(!aBoolean, 400);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(fab, "x", metrics.widthPixels / 2 - dpToPx(30,getContext())); // metrics.heightPixels or root.getHeight()
        ObjectAnimator rotate = ObjectAnimator.ofFloat(fab, "rotation", 360);
        rotate.setDuration(duration);
        translationX.setDuration(duration);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(callLayoutAnim, rotate, translationX);
        animatorSet.start();
    }

    public void close_button(int duration) {
        ObjectAnimator callLayoutAnim = callLayoutAnim(!aBoolean, 400);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(fab, "x", metrics.widthPixels - fab.getWidth() - 20); // metrics.heightPixels or root.getHeight()
        ObjectAnimator rotate = ObjectAnimator.ofFloat(fab, "rotation", 0);
        rotate.setDuration(duration);
        translationX.setDuration(duration);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(callLayoutAnim, rotate, translationX);
        animatorSet.start();
    }

    public void writeAnim(final int duration) {
        searchCallLayout.setVisibility(View.VISIBLE);
        textLayout.setVisibility(View.VISIBLE);
        gridShadow.setVisibility(View.GONE);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.greenColor)));


        ObjectAnimator anim = ObjectAnimator.ofFloat(fab, "rotation", 720);
        anim.setDuration(duration);
        anim.start();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1){
            // Do something for lollipop and above versions
            rocketAnimation = (AnimationDrawable) fab.getDrawable();
            rocketAnimation.start();

            Thread timer = new Thread() {
                public void run() {
                    try {
                        synchronized (this) {
                            wait(duration + 100);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    fab.setImageResource(R.drawable.animation_list);
                                    SortSearchCallList(phone_num_edt.getText().toString());
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            timer.start();
        } else{
            // do something for phones running an SDK before lollipop
            fab.setImageResource(R.drawable.ic_phone_white_24dp);
            Thread timer = new Thread() {
                public void run() {
                    try {
                        synchronized (this) {
                            wait(duration + 100);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SortSearchCallList(phone_num_edt.getText().toString());
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            timer.start();
        }



    }

    public void deleteAnim(final int duration) {
        searchCallLayout.setVisibility(View.GONE);
        textLayout.setVisibility(View.GONE);
        gridShadow.setVisibility(View.VISIBLE);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

        ObjectAnimator anim = ObjectAnimator.ofFloat(fab, "rotation", 360);
        anim.setDuration(duration);
        anim.start();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1){
            // Do something for lollipop and above versions
            rocketAnimation = (AnimationDrawable) fab.getDrawable();
            rocketAnimation.start();

            Thread timer = new Thread() {
                public void run() {
                    try {
                        synchronized (this) {
                            wait(duration);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    fab.setImageResource(R.drawable.animation_list2);
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            timer.start();
        } else{
            // do something for phones running an SDK before lollipop
            fab.setImageResource(R.drawable.ic_telephone);
        }


    }


    public void scrollOpenAnim(final int duration) {
        scroll = false;
        translationY = ObjectAnimator.ofFloat(padLayout, "y", 0);
        translationY.setDuration(duration);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(fab, "x", metrics.widthPixels / 2 - fab.getWidth() / 2); // metrics.heightPixels or root.getHeight()
        translationX.setDuration(duration);
        //translationX.start();

        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.greenColor)));


        ObjectAnimator anim = ObjectAnimator.ofFloat(fab, "rotation", 720);
        anim.setDuration(duration);
        //anim.start();

        AnimatorSet set = new AnimatorSet();
        set.playTogether(translationY, translationX, anim);
        set.start();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1){
            // Do something for lollipop and above versions
            rocketAnimation = (AnimationDrawable) fab.getDrawable();
            rocketAnimation.start();

            Thread timer = new Thread() {
                public void run() {
                    try {
                        synchronized (this) {
                            wait(duration);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    fab.setImageResource(R.drawable.animation_list);
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            timer.start();
        } else{
            // do something for phones running an SDK before lollipop
            fab.setImageResource(R.drawable.ic_phone_white_24dp);
        }


    }

    public void scrollCloseAnim(final int duration) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        scroll = true;
        translationY = ObjectAnimator.ofFloat(padLayout, "y", metrics.widthPixels * 2); // metrics.heightPixels or root.getHeight()
        translationY.setDuration(400);

        ObjectAnimator translationX = ObjectAnimator.ofFloat(fab, "x", metrics.widthPixels - fab.getWidth() - 20); // metrics.heightPixels or root.getHeight()
        translationX.setDuration(duration);
        //translationX.start();
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

        ObjectAnimator anim = ObjectAnimator.ofFloat(fab, "rotation", 360);
        anim.setDuration(duration);
        //anim.start();

        AnimatorSet set = new AnimatorSet();
        set.playTogether(translationY, translationX, anim);
        set.start();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1){
            // Do something for lollipop and above versions
            rocketAnimation = (AnimationDrawable) fab.getDrawable();
            rocketAnimation.start();

            Thread timer = new Thread() {
                public void run() {
                    try {
                        synchronized (this) {
                            wait(duration);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    fab.setImageResource(R.drawable.animation_list2);
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            timer.start();
        } else{
            // do something for phones running an SDK before lollipop
            fab.setImageResource(R.drawable.ic_telephone);
        }

    }

    public void close_search_anim_button(final int duration) {
        deleteAnim(duration);
        phone_num_edt.setText("");
        aBoolean = false;
        scroll = false;
        CursorVisibility = false;
        firstclick = true;

    }
    /*-------------------------------------------dial_pad_layout--------------------------------------------------*/

    @Override
    public void onResume() {
        super.onResume();
        if (shouldExecuteOnResume) {


//            loglist103.clear();
            //Uri CONTACT_URI =CallLog.Calls.CONTENT_URI;
            //getLoaderManager().restartLoader(0, null, this);
            //getContext().getContentResolver().notifyChange(CONTACT_URI, null);
            adapter1.notifyDataSetChanged();
        } else{
           shouldExecuteOnResume = true;
        }
        /*
        if (shouldExecuteOnResume) {
            // Your onResume Code Here
            get_calls_log = new Get_Calls_Log(getActivity());

            logInfos.clear();
            loguinfoupdate = new ArrayList<LogInfo>();

            if (get_calls_log.CheckPermission()) {

                loguinfoupdate = get_calls_log.getCallDetails();

            }


            Collections.sort(loguinfoupdate, LogInfo.BY_DATE);

            logs.setLayoutManager(lLayout1);
            logs.setItemAnimator(new DefaultItemAnimator());
            adapter1 = new LogAdapter(getActivity(), groupListByDate.groupListByDate(loguinfoupdate));
            logs.setAdapter(adapter1);
            adapter1.notifyDataSetChanged();
            //Dah elcall search mlksh da3wa beeh ya3m**********************************************
            callLogInfos = loguinfoupdate;
            for (int i = 0; i < callLogInfos.size(); i++) {
                for (int j = i + 1; j < callLogInfos.size(); j++) {
                    if (callLogInfos.get(i).getNumber().equals(callLogInfos.get(j).getNumber())) {
                        callLogInfos.remove(j--);
                    }
                }
            }

            //*************************************************************************************
            //toggleEmptyCases();
        } else {
            shouldExecuteOnResume = true;
        }*/

    }


    private CharSequence menuIconWithText(Drawable r, String title) {

        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }

    public void MyCustomAssignNumDialog(final String number) {
        MyDialogAssignNum = new Dialog(getContext());
        MyDialogAssignNum.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialogAssignNum.setContentView(R.layout.assign_number_speed_dial_dialog);
        MyDialogAssignNum.show();
        Window window = MyDialogAssignNum.getWindow();
        window.setLayout(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        MyDialogAssignNum.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView speedDialTitle = MyDialogAssignNum.findViewById(R.id.speed_dial_title);
        speedDialTitle.setText("Speed dial #" + number);
        final EditText phoneNum = MyDialogAssignNum.findViewById(R.id.edit_phone_number);
        final TextInputLayout inputLayout = MyDialogAssignNum.findViewById(R.id.input_layout_phone_number);
        TextView OK, Cancel;
        OK = MyDialogAssignNum.findViewById(R.id.btn_submit);
        Cancel = MyDialogAssignNum.findViewById(R.id.btn_close);
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(phoneNum.getText().toString().trim())) {
                    inputLayout.setError("Empty Field");
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("#" + number, phoneNum.getText().toString().trim());
                    editor.commit();
                    MyDialogAssignNum.dismiss();
                    MyDialogSpeedDial.dismiss();
                }
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogAssignNum.dismiss();
            }
        });
    }

    public static final int PICK_CONTACT = 1;

    public void MyCustomSpeedDialog(final String number) {
        MyDialogSpeedDial = new Dialog(getContext());
        MyDialogSpeedDial.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialogSpeedDial.setContentView(R.layout.long_press_dialog);
        Window window = MyDialogSpeedDial.getWindow();
        window.setLayout(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        MyDialogSpeedDial.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView speedDialTitle = MyDialogSpeedDial.findViewById(R.id.speed_dial_title);
        speedDialTitle.setText("Speed dial #" + number);
        TextView assignContact = MyDialogSpeedDial.findViewById(R.id.assign_contact);
        assignContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });
        TextView assignPhoneNumber = MyDialogSpeedDial.findViewById(R.id.assign_phone_number);
        assignPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomAssignNumDialog(number);
            }
        });
        TextView editSpeedDial = MyDialogSpeedDial.findViewById(R.id.edit_speed_dial);
        editSpeedDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditSpeedDialActivity.class));
                MyDialogSpeedDial.dismiss();
            }
        });
        MyDialogSpeedDial.show();
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        if ((reqCode == PICK_CONTACT) && (resultCode == RESULT_OK)) {
            Cursor MyCursor = null;
            try {
                Uri uri = data.getData();
                MyCursor = getActivity().getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
                if (MyCursor != null && MyCursor.moveToNext()) {
                    String phone = MyCursor.getString(0);
                    Log.d("Phone", phone);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("#" + grid_num_tv.getText().toString(), phone);
                    editor.apply();
                    MyDialogSpeedDial.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ((reqCode == PICK_CONTACT) && (resultCode == RESULT_CANCELED)) {

            //MyDialogSpeedDial.dismiss();
        }
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Uri CONTACT_URI = CallLog.Calls.CONTENT_URI;
        String[] projection = {
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.CACHED_NUMBER_TYPE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE,
                CallLog.Calls._ID,
        };
        CursorLoader cursorLoader = new CursorLoader(getActivity(), CONTACT_URI, projection, null, null, CallLog.Calls.DATE + " DESC");
        return cursorLoader;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (phone_num_edt.getText().length() != 0) {
            writeAnim(0);
            open_button(0, false);
            SortSearchCallList(phone_num_edt.getText().toString());
            firstclick = false;
        }
        Log.d("mesheyyyy", "onLoadFinished");
        //logInfos = new ArrayList<>();
        //ArrayList<LogInfo> loglist103 = new ArrayList<>();
        groupListByDate = new GroupListByDate(getActivity());
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        loglist103.clear();
        logInfos.clear();

        String phName, phNumber, callDate, dateStringhour;
        String dir = null;
        String typephone = null;
        int phmobileType, callType;
        Date callDayTime;
        long log_call_id;

        //Date Format  "dd-MM-yyyy h:mm a"
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a", Locale.ENGLISH);

        if (!cursor.moveToNext())
        {
            Log.e("GOPAL", "Empty Cursor");
            callLogInfos=loglist103;

            //set  new list
            adapter1 = new LogAdapter(getActivity(), loglist103);
            logs.setAdapter(adapter1);
            adapter1.notifyDataSetChanged();
            toggleEmptyLogRecycle(loglist103);
        }
        else {
            do {
                log_call_id = cursor.getLong(cursor.getColumnIndex(CallLog.Calls._ID));
                phName = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                phmobileType = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.CACHED_NUMBER_TYPE));
                phNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                callType = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
                callDate = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
                callDayTime = new Date(Long.valueOf(callDate));

                // do what ever you want here

                //callDuration = managedCursor.getString(duration);
                dateStringhour = formatter.format(new Date(Long.parseLong(callDate)));

                switch (callType) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "OUTGOING";
                        break;

                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "INCOMING";
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        dir = "MISSED";
                        break;

                    case CallLog.Calls.REJECTED_TYPE:
                        dir = "REJECTED";
                        break;

                    case CallLog.Calls.VOICEMAIL_TYPE:
                        dir = "VOICEMAIL";
                        break;

                    case CallLog.Calls.BLOCKED_TYPE:
                        dir = "BLOCKED";
                        break;

                    case CallLog.Calls.ANSWERED_EXTERNALLY_TYPE:
                        dir = "ANSWERED";
                        break;
                }

                switch (phmobileType) {
                    case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                        typephone = "Mobile";
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                        typephone = "Home";
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                        typephone = "Work";
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                        typephone = "Work Fax";
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                        typephone = "Home Fax";
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN:
                        typephone = "Main";
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
                        typephone = "Pager";
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM:
                        typephone = "Other";//Custom
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                        typephone = "Other";
                        break;
                    default:
                        break;
                }
                if (phName == null || phName.equals("")) {
                    if (phNumber != null) {

                        if (phNumber.equals("")) {

                            phName = "Unknown";

                        } else {

                            phName = phNumber;
                           /*
                        if (contactExists(phNumber)) {
                            //phName = getContactName(phNumber);
                            phName = contactsName;
                            switch (TypeOfNumph) {
                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                    typephone = "Mobile";
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                    typephone = "Home";
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                    typephone = "Work";
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                                    typephone = "Work Fax";
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                                    typephone = "Home Fax";
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN:
                                    typephone = "Main";
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
                                    typephone = "Pager";
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM:
                                    typephone = "Custom";
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                                    typephone = "Other";
                                    break;
                                default:
                                    typephone = "Other";
                                    break;

                            }

                        } else {
                            if (phNumber.equals("")) {
                                phName = "Unknown";

                            } else {
                                phName = phNumber;
                            }
                        }*/
                        }


                    } else {
                        phName = "Unknown Number";
                    }
                }


                logInfos.add(new LogInfo(null, phName, dir, callDayTime, typephone, dateStringhour, phNumber, numbersofcall,log_call_id));


            } while (cursor.moveToNext());

            /*
                    ___\ \________
                   |___\_\______  \   | |        ________
                    ___\_\____|   |  | |       / _______ \
                   / ____________/  | |  _   / /        \ \
                 __| \__\_\________| |_| |_/ /__________\ \
               /___\ \____________________________________\
                    \ \
                     \ \____
                      \_____|
                                     Mo Salah Mo Salah
                    */
            Log.d("sizebefore", String.valueOf(logInfos.size()));
           //If List Size One Item
           if(logInfos.size()==1){
               //Add first Item
               loglist_ID.add(logInfos.get(0).getCall_id());//list of call log ID
               loglist103.add(new LogInfo(null, logInfos.get(0).logName, logInfos.get(0).logIcon, logInfos.get(0).logDate, logInfos.get(0).getNumberType(), logInfos.get(0).hour, logInfos.get(0).getNumber(), numbersofcall, new ArrayList(loglist_ID)));

           }else {
               //More Than one item in list
               for (int i = 0; i < logInfos.size() - 1; i++) {

                   String numString1 = logInfos.get(i).getNumber();
                   String num = logInfos.get(i + 1).getNumber();

                   //remove Country Code from first num
                   if (numString1.startsWith("+2")) {
                       numString1 = numString1.substring(2);

                   } else if (numString1.startsWith("+")) {

                       try {
                           // phone must begin with '+'
                           Phonenumber.PhoneNumber numberProto = phoneUtil.parse(numString1, "");
                           numString1 = String.valueOf(numberProto.getNationalNumber());

                       } catch (NumberParseException e) {
                           System.err.println("NumberParseException was thrown: " + e.toString());
                       }
                   }

                   //remove Country Code from second num
                   if (num.startsWith("+2")) {
                       num = num.substring(2);

                   } else if (num.startsWith("+")) {

                       try {
                           // phone must begin with '+'
                           Phonenumber.PhoneNumber numberProto = phoneUtil.parse(num, "");
                           num = String.valueOf(numberProto.getNationalNumber());

                       } catch (NumberParseException e) {
                           System.err.println("NumberParseException was thrown: " + e.toString());
                       }
                   }

                   if (numString1.equals(num) && logInfos.get(i).logIcon.equals(logInfos.get(i + 1).logIcon) && groupListByDate.getFormattedDate(logInfos.get(i).logDate.getTime()).equals(groupListByDate.getFormattedDate(logInfos.get(i + 1).logDate.getTime()))) {
                       numbersofcall++;
                       loglist_ID.add(logInfos.get(i).getCall_id());//list of call log ID
                       isSinglePhoneNumOnly = false;
                   } else {
                       isSinglePhoneNumOnly = true;
                       loglist_ID.add(logInfos.get(i).getCall_id());//list of call log ID
                       loglist103.add(new LogInfo(null, logInfos.get(i).logName, logInfos.get(i).logIcon, logInfos.get(i).logDate, logInfos.get(i).getNumberType(), logInfos.get(i).hour, logInfos.get(i).getNumber(), numbersofcall, new ArrayList(loglist_ID)));
                       numbersofcall = 1;
                       loglist_ID.clear();
                   }

                   //For Last item in list
                   if (i == logInfos.size() - 2) {

                       Log.w("forloop", "howmach");

                       if (numString1.equals(num) && logInfos.get(i).logIcon.equals(logInfos.get(i + 1).logIcon) && groupListByDate.getFormattedDate(logInfos.get(i).logDate.getTime()).equals(groupListByDate.getFormattedDate(logInfos.get(i + 1).logDate.getTime()))) {

                           //Check If There Single Phone Number In Call Log database
                           if (!isSinglePhoneNumOnly) {
                               loglist_ID.add(logInfos.get(i+1).getCall_id());//list of call log ID
                               loglist103.add(new LogInfo(null, logInfos.get(i).logName, logInfos.get(i).logIcon, logInfos.get(i).logDate, logInfos.get(i).getNumberType(), logInfos.get(i).hour, logInfos.get(i).getNumber(), numbersofcall, new ArrayList(loglist_ID)));
                               numbersofcall = 1;
                               loglist_ID.clear();
                           }

                       } else {
                           loglist_ID.add(logInfos.get(logInfos.size() - 1).getCall_id());//list of call log ID
                           loglist103.add(new LogInfo(null, logInfos.get(logInfos.size() - 1).logName, logInfos.get(logInfos.size() - 1).logIcon, logInfos.get(logInfos.size() - 1).logDate, logInfos.get(logInfos.size() - 1).getNumberType(), logInfos.get(logInfos.size() - 1).hour, logInfos.get(logInfos.size() - 1).getNumber(), numbersofcall, new ArrayList(loglist_ID)));
                       }

                   }

               }
           }
            //For Last item in list

             /*
            //Check If There Single Phone Number In Call Log database
            if(!isSinglePhoneNumOnly){
                loglist103.add(new LogInfo(null, logInfos.get(0).logName, logInfos.get(0).logIcon, logInfos.get(0).logDate, logInfos.get(0).getNumberType(), logInfos.get(0).hour, logInfos.get(0).getNumber(), numbersofcall, new ArrayList(loglist_ID)));
            }*/

            callLogInfos = loglist103;
            Log.d("size", String.valueOf(loglist103.size()));
            adapter1 = new LogAdapter(getActivity(), groupListByDate.groupListByDate(loglist103),this);
            logs.setAdapter(adapter1);
            adapter1.notifyDataSetChanged();
            toggleEmptyLogRecycle(loglist103);
        }
    }


    @Override
    public void onLoaderReset(Loader loader) {

    }

    void LoadTopTenContactsSync() {
        ProgressDialog pd;
        new AsyncTask<Void, Void, List<ContactInfo>>() {
            @Override
            protected void onPreExecute() {
                //pd = ProgressDialog.show(getActivity(), "Loading..", "Please Wait", true, false);
            }// End of onPreExecute method

            @Override
            protected List<ContactInfo> doInBackground(Void... params) {

                contactInfos55 = get_calls_log.getTopTenContacts();

                return contactInfos55;
            }// End of doInBackground method

            @Override
            protected void onPostExecute(List<ContactInfo> result) {
                // pd.dismiss();
                adaptertopten = new ContactAdapter(getActivity(), result);
                contacts.setAdapter(adaptertopten);
                toggleEmptyCases();

            }//End of onPostExecute method
        }.execute();

    }


    private void toggleEmptyCases() {

        firstimagwaiting.setVisibility(View.GONE);
        secimagwaiting.setVisibility(View.GONE);
        textwaiting.setVisibility(View.GONE);

    }

    private void toggleEmptyLogRecycle(List<LogInfo> result) {

       if(result.size()>0){
           noLogcalltext.setVisibility(View.GONE);
           emptyrecycle.setVisibility(View.GONE);
       }else {
           noLogcalltext.setVisibility(View.VISIBLE);
           emptyrecycle.setVisibility(View.VISIBLE);
       }

    }


    Dialog MyDialogDualSim;

    public void MyCustomDualSimDialog(final String Number,TelephonyInfo telephonyInfo) {
        MyDialogDualSim = new Dialog(getContext());
        MyDialogDualSim.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialogDualSim.setContentView(R.layout.dual_sim_dialog);
        Window window = MyDialogDualSim.getWindow();
        window.setLayout(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        MyDialogDualSim.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        String ContactName=get_calls_log.getContactName(Number);

        TextView Title=MyDialogDualSim.findViewById(R.id.Dual_Sim_Titel);
        if (!TextUtils.isEmpty(ContactName))
        {
            Title.setText("Call "+ContactName+" from");
        }
        else
        {
            Title.setText("Call "+Number+" from");
        }

        List<String>SIM_NAMES=telephonyInfo.getNetworkOperator(getActivity());
        TextView sim_name_one=MyDialogDualSim.findViewById(R.id.SIM_Name_One);
        TextView sim_name_two=MyDialogDualSim.findViewById(R.id.SIM_Name_Two);

        if (SIM_NAMES.size()>=2)
        {
            sim_name_one.setText(SIM_NAMES.get(0));
            sim_name_two.setText(SIM_NAMES.get(1));
        }

        RelativeLayout sim_one_layout=MyDialogDualSim.findViewById(R.id.SIM_One_Layout);
        RelativeLayout sim_two_layout=MyDialogDualSim.findViewById(R.id.SIM_Two_Layout);

        sim_one_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.putExtra("com.android.phone.extra.slot", 0); //For sim 1
                //intent.putExtra("simSlot", 0); //For sim 1
                intent.setData(Uri.parse("tel:" + getStringHashTag(Number)));
                startActivity(intent);
                MyDialogDualSim.dismiss();
            }
        });

        sim_two_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.putExtra("simSlot", 1); //For sim 2
                //intent.putExtra("com.android.phone.extra.slot", 1); //For sim 2
                intent.setData(Uri.parse("tel:" + getStringHashTag(Number)));
                startActivity(intent);
                MyDialogDualSim.dismiss();
            }
        });
    MyDialogDualSim.show();
    }

    public void openDialPadWithNumber(String num) {
        if (!aBoolean) {
            callButtonAnim(0);
            phone_num_edt.setText(num);
            writeAnim(0);
            SortSearchCallList(num);
            firstclick = false;
        } else if (firstclick) {
            phone_num_edt.setText(num);
            writeAnim(0);
            SortSearchCallList(num);
            firstclick = false;
        } else if (scroll) {
            scrollOpenAnim(0);
            phone_num_edt.setText(num);
            SortSearchCallList(num);
        } else {
            phone_num_edt.setText(num);
            SortSearchCallList(num);
        }
    }


    @Override
    public void onClickEditNumber(String value) {
        openDialPadWithNumber(value);
    }



    @Override
    public void myAction(String value) {
        openDialPadWithNumber(value);
    }

    public String getStringHashTag(String str)
    {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == '#') {
            str = str.substring(0, str.length() - 1)+Uri.encode("#");
        }
        return str;
    }

}
