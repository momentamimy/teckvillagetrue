package teckvillage.developer.khaled_pc.teckvillagetrue;


import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.ContactAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.CustomGridAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.LogAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.KeyboardlessEditText;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.ContactInfo;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.GridListDataModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.LogInfo;


/**
 * A simple {@link Fragment} subclass.
 */
public class Main_Fagment extends Fragment {

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
        /*--------------------------------------dial_pad_layout------------------------------------------*/

        /*--------------------------------------dial_pad_layout------------------------------------------*/
        //make padlayout invisable
        callAnimDefault();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callButtonAnim();
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
                if (phone_num_edt.getText().length()==1)
                {
                    deleteAnim();
                    firstclick=true;
                }
                if (CursorVisibility)
                {
                    removeSelection(phone_num_edt);
                }
                else
                {
                    phone_num_edt.setText(removeChar(num));
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



        List<LogInfo> logInfos=new ArrayList<>();
        logInfos.add(new LogInfo("http://i.imgur.com/DvpvklR.png","khaled","income","06:43","Mobile"));
        logInfos.add(new LogInfo("https://firebasestorage.googleapis.com/v0/b/chatapp-f5386.appspot.com/o/imgs%2F3C8oCUSQjlZqV0kivIBhpwT5fca2.jpg?alt=media&token=a9e39edd-6d27-4995-969d-a76ddbfb1aab","khaled","income","07:03 PM","Mobile"));
        logInfos.add(new LogInfo(null,"khaled","income","01:30 PM","Mobile"));
        logInfos.add(new LogInfo(null,"momen","outcome","10:40 AM","Mobile"));
        logInfos.add(new LogInfo(null,"khaled","income","02:20 pm","Mobile"));
        logInfos.add(new LogInfo(null,"momen","outcome","05:55 pm","Mobile"));

        logs.setLayoutManager(lLayout1);
        logs.setItemAnimator(new DefaultItemAnimator());
        LogAdapter adapter1=new LogAdapter(getActivity(),logInfos);
        logs.setAdapter(adapter1);


        return view;
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


        ObjectAnimator anim = ObjectAnimator.ofFloat(fab,"rotation",360);
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

        ObjectAnimator anim = ObjectAnimator.ofFloat(fab,"rotation",0);
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
