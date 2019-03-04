package teckvillage.developer.khaled_pc.teckvillagetrue;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.ContactAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.CustomGridAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.LogAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.Get_Calls_Log;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.KeyboardlessEditText;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.ContactInfo;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.GridListDataModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.LogInfo;


/**
 * A simple {@link Fragment} subclass.
 */
public class Main_Fagment extends Fragment {

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

    static final String[] GRID_NUM = new String[] {
            "1", "2", "3",
            "4", "5", "6",
            "7" ,"8", "9" ,
            "*", "0", "#"};
    static final String[] GRID_LETTERS = new String[] {
            " ", "abc", "def",
            "ghi", "jkl", "mno",
            "pqrs" ,"tuv", "wxyz" ,
            " ", "+", " "};
    public ArrayList<GridListDataModel> myGridList = new ArrayList<GridListDataModel>();

    boolean aBoolean=false;//for dial phone button open_close
    boolean CursorVisibility=false;
    boolean firstclick=true;

    AnimationDrawable rocketAnimation;//icon anim
    /*--------------------------------------dial_pad_layout------------------------------------------*/

    RecyclerView contacts,logs;
    LinearLayoutManager lLayout;
    LinearLayoutManager lLayout1;
    ArrayList<LogInfo> logInfos;
    ArrayList<LogInfo> loguinfoupdate;
    LogAdapter adapter1;
    Get_Calls_Log get_calls_log;
    boolean shouldExecuteOnResume;

    public Main_Fagment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_main__fagment, container, false);


        contacts=view.findViewById(R.id.contact_recycleview);
        logs=view.findViewById(R.id.Logs_recycleview);

        /*--------------------------------------dial_pad_layout------------------------------------------*/
        padLayout=view.findViewById(R.id.pad_layout);
        fab=view.findViewById(R.id.fab);
        gridView = (GridView) view.findViewById(R.id.gridView1);
        gridShadow=view.findViewById(R.id.shadow_gridView1);
        phone_num_edt = (KeyboardlessEditText) view.findViewById(R.id.dial_pad_num_editText);
        back_space=view.findViewById(R.id.backspace_imageView);
        final ImageView threedots=view.findViewById(R.id.threedots);
        ImageView icon=view.findViewById(R.id.iconnnn);

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
                                return true;
                            case 2:
                                // item one clicked
                                return true;
                            case 3:
                                // item one clicked
                                return true;
                            case 4:
                                // item one clicked
                                return true;

                        }

                        return false;
                    }
                });
                popupMenu.inflate(R.menu.main);
                popupMenu.getMenu().add(0, 1, 1, menuIconWithText(getResources().getDrawable(R.drawable.ic_recieve_call_menu), "Income"));
                popupMenu.getMenu().add(0, 2, 2, menuIconWithText(getResources().getDrawable(R.drawable.ic_arrow_right_menu), "Outgoing"));
                popupMenu.getMenu().add(0, 3, 3, menuIconWithText(getResources().getDrawable(R.drawable.ic_missed_menu), "Missed"));
                popupMenu.getMenu().add(0, 4, 4, menuIconWithText(getResources().getDrawable(R.drawable.ic_block_black_24dp), "Blocked"));

                popupMenu.show();



            }
        });


         get_calls_log=new Get_Calls_Log(getActivity());


        /*--------------------------------------dial_pad_layout------------------------------------------*/
        //make padlayout invisable
        callAnimDefault();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone_num_edt.getText().length()==0)
                {
                    callButtonAnim();
                }
                else
                {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+phone_num_edt.getText().toString()));
                    startActivity(intent);
                }
            }
        });

        //fill numbers
        for (int i=0;i<GRID_NUM.length;i++)
        {
            myGridList.add(new GridListDataModel(GRID_NUM[i],GRID_LETTERS[i]));
        }

        gridView.setAdapter(new CustomGridAdapter(getContext(), myGridList));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (firstclick)
                {
                    writeAnim();
                    firstclick=false;
                }

                grid_num_tv = (TextView) view.findViewById(R.id.grid_num_textView);
                //Toast.makeText(getApplicationContext(), grid_num_tv.getText(), Toast.LENGTH_SHORT).show();
                if (CursorVisibility)
                {
                    insertSelection(phone_num_edt,grid_num_tv.getText().toString());
                }
                else
                {
                    phone_num_edt.setText(phone_num_edt.getText().toString()+grid_num_tv.getText());
                }
            }
        });

        back_space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num=phone_num_edt.getText().toString();

                if (CursorVisibility)
                {
                    removeSelection(phone_num_edt);
                }
                else
                {
                    phone_num_edt.setText(removeChar(num));
                }

                if (phone_num_edt.getText().length()==0)
                {
                    deleteAnim();
                    firstclick=true;
                }
            }
        });


        phone_num_edt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CursorVisibility=true;
                return false;
            }
        });
        /*--------------------------------------dial_pad_layout------------------------------------------*/

        lLayout = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        lLayout1 = new LinearLayoutManager(getActivity());

        List<ContactInfo> contactInfos=new ArrayList<>();

        if(get_calls_log.CheckPermission()){

            contactInfos= get_calls_log.getTopTenContacts();

        }


        contacts.setLayoutManager(lLayout);
        contacts.setItemAnimator(new DefaultItemAnimator());
        ContactAdapter adapter=new ContactAdapter(getActivity(),contactInfos);
        contacts.setAdapter(adapter);



        //**********************Log List******************************
        logInfos=new ArrayList<>();
        if(get_calls_log.CheckPermission()){

            logInfos=get_calls_log.getCallDetails();


        }


        Collections.sort(logInfos, LogInfo.BY_DATE);

        logs.setLayoutManager(lLayout1);
        logs.setItemAnimator(new DefaultItemAnimator());
        adapter1=new LogAdapter(getActivity(),groupListByDate(logInfos));
        logs.setAdapter(adapter1);


        return view;
    }



    /**
     * Receive sorted list and divide it into Letters and contacts
     * @param list
     * @return
     */
    ArrayList<LogInfo> groupListByDate(ArrayList<LogInfo> list) {
        int i = 0;
        ArrayList<LogInfo> customList = new ArrayList<LogInfo>();
        LogInfo logInfo = new LogInfo();
        logInfo.logDate=list.get(0).logDate;
        logInfo.setDateString(getFormattedDate(getActivity(),logInfo.logDate.getTime()));
        logInfo.setType(1);
        customList.add(logInfo);
        for (i = 0; i < list.size() - 1; i++) {
            LogInfo logInfo12 = new LogInfo();
            //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            String dateString1 =getFormattedDate(getActivity(),  list.get(i).logDate.getTime());
            String dateString2 =getFormattedDate(getActivity(),  list.get(i + 1).logDate.getTime());
            //String dateString1 = formatter.format(list.get(i).logDate.getTime());
            //String dateString2 = formatter.format(list.get(i + 1).logDate.getTime());

            if (dateString1.equals(dateString2)) {
                list.get(i).setType(2);
                customList.add(list.get(i));
            } else {
                list.get(i).setType(2);
                customList.add(list.get(i));
                logInfo12.setDateString(dateString2);
                logInfo12.setType(1);
                customList.add(logInfo12);
            }
        }
        list.get(i).setType(2);
        customList.add(list.get(i));
        return customList;
    }

    public String getFormattedDate(Context context, long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String dateTimeFormatString = "EEEE, dd MMM";
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)&&(smsTime.get(Calendar.YEAR) == now.get(Calendar.YEAR)&&(smsTime.get(Calendar.MONTH) == now.get(Calendar.MONTH))) ) {
            return "Today " ;
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1&&(smsTime.get(Calendar.YEAR) == now.get(Calendar.YEAR)&&(smsTime.get(Calendar.MONTH) == now.get(Calendar.MONTH)))){
            return "Yesterday " ;
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("EEEE, dd MMM yyyy", smsTime).toString();
        }
    }



    /*-------------------------------------------dial_pad_layout--------------------------------------------------*/
    public void removeSelection(KeyboardlessEditText inputText)
    {

        int selectionStart=inputText.getSelectionStart();
        int selectionEnd=inputText.getSelectionEnd();


        if(selectionStart!=selectionEnd)
        {
            inputText.getText().delete(selectionStart,selectionEnd);
        }
        else
        {
            if (selectionEnd!=0)
                inputText.getText().delete(selectionStart-1,selectionStart);
        }
    }

    public void insertSelection(KeyboardlessEditText inputText,String numString)
    {
        int selectionStart=inputText.getSelectionStart();
        int selectionEnd=inputText.getSelectionEnd();

        if(selectionStart!=selectionEnd)
        {
            inputText.getText().replace(selectionStart,selectionEnd,numString);
            selectionEnd=inputText.getSelectionEnd();
            inputText.setSelection(selectionEnd);
        }
        else
        {
            inputText.getText().insert(selectionStart,numString);
        }
    }

    public String removeChar(String str) {
        if (str != null && str.length() > 0 ) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public void callAnimDefault()
    {

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        ObjectAnimator translationY = ObjectAnimator.ofFloat(padLayout, "y", metrics.widthPixels*2); // metrics.heightPixels or root.getHeight()
        translationY.setDuration(0);
        translationY.start();

    }
    ObjectAnimator translationY;
    public void callLayoutAnim(boolean up_down)
    {

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        if (up_down){
            translationY=ObjectAnimator.ofFloat(padLayout, "y", 0); // metrics.heightPixels or root.getHeight()
            translationY.setDuration(400);
            translationY.start();
        }else {
            translationY = ObjectAnimator.ofFloat(padLayout, "y", metrics.widthPixels*2); // metrics.heightPixels or root.getHeight()
            translationY.setDuration(400);
            translationY.start();
        }


    }

    public void callButtonAnim()
    {
        if (!aBoolean)
        {
            open_button();
            aBoolean=true;
        }
        else
        {
            close_button();
            aBoolean=false;
        }

    }

    public void open_button()
    {
        callLayoutAnim(!aBoolean);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(fab, "x", metrics.widthPixels / 2 - fab.getWidth() / 2); // metrics.heightPixels or root.getHeight()
        ObjectAnimator rotate=ObjectAnimator.ofFloat(fab,"rotation",360);
        rotate.setDuration(400);
        rotate.start();
        translationX.setDuration(400);
        translationX.start();
    }

    public void close_button()
    {
        callLayoutAnim(!aBoolean);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(fab, "x", metrics.widthPixels-fab.getWidth()-20); // metrics.heightPixels or root.getHeight()
        ObjectAnimator rotate=ObjectAnimator.ofFloat(fab,"rotation",0);
        rotate.setDuration(400);
        rotate.start();
        translationX.setDuration(400);
        translationX.start();
    }

    public void writeAnim()
    {
        textLayout.setVisibility(View.VISIBLE);
        gridShadow.setVisibility(View.GONE);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.greenColor)));


        ObjectAnimator anim = ObjectAnimator.ofFloat(fab,"rotation",720);
        anim.setDuration(200);
        anim.start();

        rocketAnimation = (AnimationDrawable) fab.getDrawable();
        rocketAnimation.start();

        Thread timer= new Thread(){
            public void run(){
                try { synchronized(this){ wait(200);
                    getActivity().runOnUiThread(new Runnable() {@Override public void run() {
                        fab.setImageResource(R.drawable.animation_list);
                    }}); }
                } catch (InterruptedException e) { e.printStackTrace(); } }};
        timer.start();
    }

    public void deleteAnim()
    {
        textLayout.setVisibility(View.GONE);
        gridShadow.setVisibility(View.VISIBLE);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

        ObjectAnimator anim = ObjectAnimator.ofFloat(fab,"rotation",360);
        anim.setDuration(200);
        anim.start();

        rocketAnimation = (AnimationDrawable) fab.getDrawable();
        rocketAnimation.start();

        Thread timer= new Thread(){
            public void run(){
                try { synchronized(this){ wait(200);
                    getActivity().runOnUiThread(new Runnable() {@Override public void run() {
                        fab.setImageResource(R.drawable.animation_list2);
                    }}); }
                } catch (InterruptedException e) { e.printStackTrace(); } }};
        timer.start();
    }
    /*-------------------------------------------dial_pad_layout--------------------------------------------------*/

    @Override
    public void onResume() {
        super.onResume();
        if(shouldExecuteOnResume){
            // Your onResume Code Here
            get_calls_log=new Get_Calls_Log(getActivity());

            logInfos.clear();
            loguinfoupdate=new ArrayList<LogInfo>();

            if(get_calls_log.CheckPermission()){

                loguinfoupdate=get_calls_log.getCallDetails();

            }


            Collections.sort(loguinfoupdate, LogInfo.BY_DATE);

            logs.setLayoutManager(lLayout1);
            logs.setItemAnimator(new DefaultItemAnimator());
            adapter1=new LogAdapter(getActivity(),groupListByDate(loguinfoupdate));
            logs.setAdapter(adapter1);
            adapter1.notifyDataSetChanged();
            //toggleEmptyCases();
        } else{
            shouldExecuteOnResume = true;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }



    private CharSequence menuIconWithText(Drawable r, String title) {

        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }




}
