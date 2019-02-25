package teckvillage.developer.khaled_pc.teckvillagetrue;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.ContactAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.CustomGridAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.LogAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.KeyboardlessEditText;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.ContactInfo;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.Get_Calls_Log;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.GridListDataModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.LogInfo;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.UserContactData;


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
        textLayout=view.findViewById(R.id.dial_num_edt_rl);
        Get_Calls_Log get_calls_log=new Get_Calls_Log(getActivity());


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

        contactInfos.add(new ContactInfo("http://i.imgur.com/DvpvklR.png","khaled","Mobile"));
        contactInfos.add(new ContactInfo("https://firebasestorage.googleapis.com/v0/b/chatapp-f5386.appspot.com/o/imgs%2F3C8oCUSQjlZqV0kivIBhpwT5fca2.jpg?alt=media&token=a9e39edd-6d27-4995-969d-a76ddbfb1aab","Mo'men El-Tamimy","Mobile"));
        contactInfos.add(new ContactInfo(null,"khaled","Mobile"));
        contactInfos.add(new ContactInfo(null,"khaled eltarabily","Mobile"));
        contactInfos.add(new ContactInfo(null,"khaled","Mobile"));
        contactInfos.add(new ContactInfo(null,"khaled","Mobile"));
        contactInfos.add(new ContactInfo(null,"khaled","Mobile"));

        contacts.setLayoutManager(lLayout);
        contacts.setItemAnimator(new DefaultItemAnimator());
        ContactAdapter adapter=new ContactAdapter(getActivity(),contactInfos);
        contacts.setAdapter(adapter);



        //**********************Log List******************************
        ArrayList<LogInfo> logInfos=new ArrayList<>();
        if(get_calls_log.CheckPermission()){

            logInfos=get_calls_log.getCallDetails();

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
        LogAdapter adapter1=new LogAdapter(getActivity(),groupListByDate(logInfos));
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
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1  ){
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
}
