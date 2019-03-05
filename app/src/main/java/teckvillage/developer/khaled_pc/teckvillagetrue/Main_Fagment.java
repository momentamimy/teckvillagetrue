package teckvillage.developer.khaled_pc.teckvillagetrue;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
import teckvillage.developer.khaled_pc.teckvillagetrue.View.Income_Call_fragment;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.Missed_Call_fragment;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.Outgoing_Call_fragment;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.Get_Calls_Log;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.KeyboardlessEditText;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.ContactInfo;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.Get_Calls_Log;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.GridListDataModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.GroupListByDate;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.LogInfo;


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
    boolean scroll;

    AnimationDrawable rocketAnimation;//icon anim
    /*--------------------------------------dial_pad_layout------------------------------------------*/

    RecyclerView contacts,logs,searchLogs;
    LinearLayoutManager lLayout;
    LinearLayoutManager lLayout1;
    LinearLayoutManager lLayout2;
    ArrayList<LogInfo> callLogInfos=new ArrayList<>();
    ArrayList<LogInfo> logInfos;
    ArrayList<LogInfo> loguinfoupdate;
    LogAdapter adapter1;
    Get_Calls_Log get_calls_log;
    boolean shouldExecuteOnResume;

    CoordinatorLayout searchCallLayout;
    ImageView close_search;
    GroupListByDate groupListByDate;

    public Main_Fagment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_main__fagment, container, false);

        groupListByDate=new GroupListByDate();

        close_search=view.findViewById(R.id.close_Search);
        close_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_search_anim_button(200);
            }
        });

        TextView textView=view.findViewById(R.id.dialpad_bottom_rl);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        contacts=view.findViewById(R.id.contact_recycleview);
        logs=view.findViewById(R.id.Logs_recycleview);
        searchLogs=view.findViewById(R.id.Search_Log_recycleview);

        /*--------------------------------------dial_pad_layout------------------------------------------*/
        searchCallLayout=view.findViewById(R.id.search_call_layout);
        padLayout=view.findViewById(R.id.pad_layout);
        fab=view.findViewById(R.id.fab);
        gridView = (GridView) view.findViewById(R.id.gridView1);
        gridShadow=view.findViewById(R.id.shadow_gridView1);
        phone_num_edt = (KeyboardlessEditText) view.findViewById(R.id.dial_pad_num_editText);
        back_space=view.findViewById(R.id.backspace_imageView);
        final ImageView threedots=view.findViewById(R.id.threedots);
        ImageView icon=view.findViewById(R.id.iconnnn);


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
                                Income_Call_fragment nextFrag3= new Income_Call_fragment();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container_main, nextFrag3, "findThisFragment")
                                        .addToBackStack(null)
                                        .commit();
                                return true;
                            case 2:
                                // item one clicked
                                Outgoing_Call_fragment nextFrag2= new Outgoing_Call_fragment();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container_main, nextFrag2, "findThisFragment")
                                        .addToBackStack(null)
                                        .commit();
                                return true;
                            case 3:
                                // item one clicked
                                Missed_Call_fragment nextFrag= new Missed_Call_fragment();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container_main, nextFrag, "findThisFragment")
                                        .addToBackStack(null)
                                        .commit();


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
        textLayout=view.findViewById(R.id.dial_num_edt_rl);
        //get_calls_log=new Get_Calls_Log(getActivity());


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
                else if (scroll==true)
                {
                    scrollOpenAnim(400);
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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (firstclick)
                {
                    writeAnim(400);
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
                //SortSearchCallList(phone_num_edt.getText().toString());
                myTask task=new myTask();
                task.execute(SortSearchCallList(phone_num_edt.getText().toString()));
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
                    deleteAnim(400);
                    firstclick=true;
                }
                //SortSearchCallList(phone_num_edt.getText().toString());
                myTask task=new myTask();
                task.execute(SortSearchCallList(phone_num_edt.getText().toString()));
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
        lLayout2 = new LinearLayoutManager(getActivity());

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

            callLogInfos=logInfos;
            for (int i=0;i<callLogInfos.size();i++)
            {
                for (int j=i+1;j<callLogInfos.size();j++)
                {
                    if (callLogInfos.get(i).getNumber().equals(callLogInfos.get(j).getNumber()))
                    {
                        callLogInfos.remove(j--);
                    }
                }
            }



        }


        /*
        for (String date : groupedHashMap.keySet()) {
            int i=0;
            LogInfo logInfoo=new LogInfo();
            //DateItem dateItem = new DateItem();
           // dateItem.setDate(date);
           // consolidatedList.add(dateItem);
            logInfoo.logDate=date;
            consolidatedList.get(i).setType(1);
            consolidatedList.add(logInfoo);
            i++;

            for (LogInfo pojoOfJsonArray : groupedHashMap.get(date)) {
                GeneralItem generalItem = new GeneralItem();
                LogInfo logInfo2=new LogInfo();
                generalItem.setPojoOfJsonArray(pojoOfJsonArray);//setBookingDataTabs(bookingDataTabs);
                consolidatedList.get(i).setType(2);
                consolidatedList.add(generalItem.);
            }
        }
            */

        Collections.sort(logInfos, LogInfo.BY_DATE);

        logs.setLayoutManager(lLayout1);
        logs.setItemAnimator(new DefaultItemAnimator());
        adapter1=new LogAdapter(getActivity(),groupListByDate.groupListByDate(logInfos));
        logs.setAdapter(adapter1);




        searchLogs.setLayoutManager(lLayout2);
        searchLogs.setItemAnimator(new DefaultItemAnimator());
        CallSearchLogAdapter adapter2=new CallSearchLogAdapter(getActivity(),callLogInfos,phone_num_edt.getText().toString());
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
                if (aBoolean==true) {
                    close_button(400);
                    aBoolean = false;
                }
            }
        });
        searchLogs.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                //scrollCloseAnim();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Do something
                    Log.d("mememem:","111111");
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // Do something
                    Log.d("mememem:","222222");
                } else {
                    // Do something
                    Log.d("mememem:","333333");
                }
                if (!scroll)
                    scrollCloseAnim(400);
            }
        });
        return view;
    }
    private class myTask extends AsyncTask<List<LogInfo>, Void, List<LogInfo>> {

        @Override
        protected List<LogInfo> doInBackground(List<LogInfo>... lists) {
            return lists[0];
        }

        @Override
        protected void onPostExecute(List<LogInfo> infos) {
            super.onPostExecute(infos);
            CallSearchLogAdapter adapter=new CallSearchLogAdapter(getActivity(),infos,phone_num_edt.getText().toString());
            searchLogs.setAdapter(adapter);
        }
    }


    private List<LogInfo> SortSearchCallList(String num) {
        ArrayList<LogInfo> infos=new ArrayList<>();
        for (int i=0;i<callLogInfos.size();i++)
        {
            if (callLogInfos.get(i).getNumber().contains(num))
            {
                infos.add(callLogInfos.get(i));
            }
        }
        int position;
        for (int i=0;i<infos.size();i++)
        {
            position =  infos.get(i).getNumber().indexOf(num);
            if (infos.get(0).getNumber().indexOf(num)>position)
            {
                LogInfo inf=infos.get(i);
                infos.remove(i);
                infos.add(0,inf);
            }
        }

        return infos;
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
    public ObjectAnimator callLayoutAnim(boolean up_down,int duration)
    {

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        if (up_down){
            translationY=ObjectAnimator.ofFloat(padLayout, "y", 0); // metrics.heightPixels or root.getHeight()
            translationY.setDuration(duration);
        }else {
            translationY = ObjectAnimator.ofFloat(padLayout, "y", metrics.widthPixels*2); // metrics.heightPixels or root.getHeight()
            translationY.setDuration(duration);
        }

        return translationY;

    }

    public void callButtonAnim()
    {
        if (!aBoolean)
        {
            open_button(400);
            aBoolean=true;
        }
        else
        {
            close_button(400);
            aBoolean=false;
        }

    }

    public void open_button(int duration)
    {
        ObjectAnimator callLayoutAnim=callLayoutAnim(!aBoolean,400);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(fab, "x", metrics.widthPixels / 2 - fab.getWidth() / 2); // metrics.heightPixels or root.getHeight()
        ObjectAnimator rotate=ObjectAnimator.ofFloat(fab,"rotation",360);
        rotate.setDuration(duration);
        translationX.setDuration(duration);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(callLayoutAnim,rotate,translationX);
        animatorSet.start();
    }

    public void close_button(int duration)
    {
        ObjectAnimator callLayoutAnim=callLayoutAnim(!aBoolean,400);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(fab, "x", metrics.widthPixels-fab.getWidth()-20); // metrics.heightPixels or root.getHeight()
        ObjectAnimator rotate=ObjectAnimator.ofFloat(fab,"rotation",0);
        rotate.setDuration(duration);
        translationX.setDuration(duration);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(callLayoutAnim,rotate,translationX);
        animatorSet.start();
    }

    public void writeAnim(final int duration)
    {
        searchCallLayout.setVisibility(View.VISIBLE);
        textLayout.setVisibility(View.VISIBLE);
        gridShadow.setVisibility(View.GONE);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.greenColor)));


        ObjectAnimator anim = ObjectAnimator.ofFloat(fab,"rotation",720);
        anim.setDuration(duration);
        anim.start();

        rocketAnimation = (AnimationDrawable) fab.getDrawable();
        rocketAnimation.start();

        Thread timer= new Thread(){
            public void run(){
                try { synchronized(this){ wait(duration);
                    getActivity().runOnUiThread(new Runnable() {@Override public void run() {
                        fab.setImageResource(R.drawable.animation_list);
                    }}); }
                } catch (InterruptedException e) { e.printStackTrace(); } }};
        timer.start();
    }

    public void deleteAnim(final int duration)
    {
        searchCallLayout.setVisibility(View.GONE);
        textLayout.setVisibility(View.GONE);
        gridShadow.setVisibility(View.VISIBLE);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

        ObjectAnimator anim = ObjectAnimator.ofFloat(fab,"rotation",360);
        anim.setDuration(duration);
        anim.start();

        rocketAnimation = (AnimationDrawable) fab.getDrawable();
        rocketAnimation.start();

        Thread timer= new Thread(){
            public void run(){
                try { synchronized(this){ wait(duration);
                    getActivity().runOnUiThread(new Runnable() {@Override public void run() {
                        fab.setImageResource(R.drawable.animation_list2);
                    }}); }
                } catch (InterruptedException e) { e.printStackTrace(); } }};
        timer.start();
    }



    public void scrollOpenAnim(final int duration)
    {
        scroll=false;
        translationY=ObjectAnimator.ofFloat(padLayout, "y", 0);
        translationY.setDuration(duration);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(fab, "x", metrics.widthPixels / 2 - fab.getWidth() / 2); // metrics.heightPixels or root.getHeight()
        translationX.setDuration(duration);
        //translationX.start();

        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.greenColor)));


        ObjectAnimator anim = ObjectAnimator.ofFloat(fab,"rotation",720);
        anim.setDuration(duration);
        //anim.start();

        AnimatorSet set=new AnimatorSet();
        set.playTogether(translationY,translationX,anim);
        set.start();

        rocketAnimation = (AnimationDrawable) fab.getDrawable();
        rocketAnimation.start();

        Thread timer= new Thread(){
            public void run(){
                try { synchronized(this){ wait(duration);
                    getActivity().runOnUiThread(new Runnable() {@Override public void run() {
                        fab.setImageResource(R.drawable.animation_list);
                    }}); }
                } catch (InterruptedException e) { e.printStackTrace(); } }};
        timer.start();
    }

    public void scrollCloseAnim(final int duration)
    {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        scroll=true;
        translationY = ObjectAnimator.ofFloat(padLayout, "y", metrics.widthPixels*2); // metrics.heightPixels or root.getHeight()
        translationY.setDuration(400);

        ObjectAnimator translationX = ObjectAnimator.ofFloat(fab, "x", metrics.widthPixels-fab.getWidth()-20); // metrics.heightPixels or root.getHeight()
        translationX.setDuration(duration);
        //translationX.start();
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

        ObjectAnimator anim = ObjectAnimator.ofFloat(fab,"rotation",360);
        anim.setDuration(duration);
        //anim.start();

        AnimatorSet set=new AnimatorSet();
        set.playTogether(translationY,translationX,anim);
        set.start();

        rocketAnimation = (AnimationDrawable) fab.getDrawable();
        rocketAnimation.start();

        Thread timer= new Thread(){
            public void run(){
                try { synchronized(this){ wait(duration);
                    getActivity().runOnUiThread(new Runnable() {@Override public void run() {
                        fab.setImageResource(R.drawable.animation_list2);
                    }}); }
                } catch (InterruptedException e) { e.printStackTrace(); } }};
        timer.start();
    }

    public void close_search_anim_button(final int duration)
    {
        deleteAnim(duration);
        Thread timer= new Thread(){
            public void run(){
                try { synchronized(this){ wait(duration);
                    getActivity().runOnUiThread(new Runnable() {@Override public void run() {
                        close_button(duration);
                        phone_num_edt.setText("");
                        aBoolean=false;
                        scroll=false;
                        CursorVisibility=false;
                        firstclick=true;
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
            adapter1=new LogAdapter(getActivity(),groupListByDate.groupListByDate(loguinfoupdate));
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
