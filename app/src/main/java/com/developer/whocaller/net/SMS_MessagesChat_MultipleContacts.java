package com.developer.whocaller.net;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import com.developer.whocaller.net.BroadcastReceivers.Receiver;
import com.developer.whocaller.net.Controller.messages_list_adapter;


public class SMS_MessagesChat_MultipleContacts extends AppCompatActivity {

    RecyclerView chat_RecyclerView;
    LinearLayoutManager lLayout;
    List<sms_messages_model> messageInfos;
    messages_list_adapter adapter;

    TextView userName;
    CircleImageView userImg;

    LinearLayout Cant_Replay_Layout;

    RelativeLayout Message_Container;
    EditText messageSend;
    ImageView dualSims;
    FloatingActionButton send;

    ArrayList<String> name,address;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0;

    private static SMS_MessagesChat_MultipleContacts inst;

    ArrayList<Long> updateMessageInfos;
    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }
    public static SMS_MessagesChat_MultipleContacts instance() {
        return inst;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_messages_chat);
        updateMessageInfos=new ArrayList<>();

        dualSims = findViewById(R.id.Dual_Sim);


        LinearLayout toolbar = findViewById(R.id.toolbar);
        ImageView backButton=findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Cant_Replay_Layout=findViewById(R.id.Cant_Replay_Layout);
        Message_Container=findViewById(R.id.messageContainer);


        userName=findViewById(R.id.UserName);
        userImg=findViewById(R.id.UserImage);

        name=getIntent().getStringArrayListExtra("LogSMSName");
        address=getIntent().getStringArrayListExtra("LogSMSAddress");

        Cant_Replay_Layout.setVisibility(View.GONE);
        Message_Container.setVisibility(View.VISIBLE);

        String allNames="";
        for (int i=0;i<name.size();i++)
        {
            if (allNames.equals(""))
                allNames=allNames+name.get(i);
            else
                allNames=allNames+","+name.get(i);
        }
        userName.setText(allNames);



        lLayout = new LinearLayoutManager(getApplicationContext());
        chat_RecyclerView=findViewById(R.id.chat_SMS_messages);
        lLayout.setStackFromEnd(true);
        chat_RecyclerView.setLayoutManager(lLayout);
        messageInfos=new ArrayList<>();
        adapter=new messages_list_adapter(this,messageInfos);
        chat_RecyclerView.setAdapter(adapter);

        //getmessages();


        final TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(SMS_MessagesChat_MultipleContacts.this);
        final boolean isSIM1Ready = telephonyInfo.isSIM1Ready();
        final boolean isSIM2Ready = telephonyInfo.isSIM2Ready();
        final boolean isDualSIM = telephonyInfo.isDualSIM();

        if (isDualSIM) {
            if (isSIM1Ready && isSIM2Ready) { }
            else { dualSims.setVisibility(View.GONE); }
        }
        else { dualSims.setVisibility(View.GONE); }

        SharedPreferences preferences=getSharedPreferences("DUAL_SIM",MODE_PRIVATE);
        String SIM=preferences.getString("SIM","0");

        if (SIM.equals("0"))
        {
            dualSims.setImageResource(R.drawable.ic_one);
        }
        else if (SIM.equals("1"))
        {
            dualSims.setImageResource(R.drawable.ic_two);
        }

        dualSims.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences=getSharedPreferences("DUAL_SIM",MODE_PRIVATE);
                String SIM=preferences.getString("SIM","0");
                if (SIM.equals("0"))
                {
                    dualSims.setImageResource(R.drawable.ic_two);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("SIM","1");
                    editor.commit();
                }
                else if (SIM.equals("1"))
                {
                    dualSims.setImageResource(R.drawable.ic_one);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("SIM","0");
                    editor.commit();
                }
            }
        });


        messageSend=findViewById(R.id.Message_Send);
        send=findViewById(R.id.fab);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String myPackageName = getPackageName();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (!Telephony.Sms.getDefaultSmsPackage(getApplicationContext()).equals(myPackageName)) {
                        Intent intent =
                                new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                                myPackageName);
                        startActivity(intent);

                    }
                    else {

                        String message = messageSend.getText().toString();
                        if (message.length()>0)
                        {
                            messageInfos.add(new sms_messages_model(0,"",0,message,5,"",0,"",31));
                            adapter.notifyDataSetChanged();
                            for (int i=0;i<address.size();i++)
                            {
                                String phoneNo = address.get(i);
                                messageSend.setText("");
                                sendSMS(phoneNo,message,i);

                            }

                        }

                        else
                            Toast.makeText(getBaseContext(),
                                    "Please write your message.",
                                    Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        check_read_messages_permission();
    }
    private void check_read_messages_permission(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_SMS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_SMS},00);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted

        }
    }

    /*public void getmessages()
    {
        messageInfos=new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        int dateColumn = smsInboxCursor.getColumnIndex("date");
        int typeColumn = smsInboxCursor.getColumnIndex("type");

        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;

        do {
            Calendar calendar=Calendar.getInstance();
            Long timestamp = Long.parseLong(smsInboxCursor.getString(dateColumn));
            calendar.setTimeInMillis(timestamp);
            String stringDate=String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))+"/"+String.valueOf(calendar.get(Calendar.MONTH)+1)+"/"+String.valueOf(calendar.get(Calendar.YEAR));

            MessageInfo info;
            if (smsInboxCursor.getString(typeColumn).equals("1"))
            {
                info=new MessageInfo(null,name,smsInboxCursor.getString(indexBody),stringDate,address);
            }
            else
            {
                info=new MessageInfo(null,"",smsInboxCursor.getString(indexBody),stringDate,address);
            }

            if (!TextUtils.isEmpty(smsInboxCursor.getString(indexAddress)))
            if (smsInboxCursor.getString(indexAddress).equals(address))
            {
                messageInfos.add(info);
            }

        } while (smsInboxCursor.moveToNext());

        Collections.reverse(messageInfos);
        adapter=new MessageAdapter(getApplicationContext(),messageInfos);
        chat_RecyclerView.setAdapter(adapter);
    }*/

    /*----------------------------------------------------------------------------------------------------------------------------------
     * ----------------------------------------------------------------------------------------------------------------------------------
     * ----------------------------------------------------------------------------------------------------------------------------------*/




    public void insert_sms_to_sent(String phone,String sms,long l){
        ContentValues values = new ContentValues();
        values.put("address",phone);
        values.put("body", sms);
        values.put("seen", true);
        values.put("read", true);
        values.put("date",l);
        values.put("type",5);
        values.put("status",31);//in progress
        getContentResolver().insert(Uri.parse("content://sms/sent"), values);
    }

    private void update_sms_sent(long l) {
        ContentValues values = new ContentValues();
        values.put("seen", true);
        values.put("read", true);
        values.put("date",l);
        values.put("type",2);
        values.put("status",0);//sent
        getContentResolver().update(Uri.parse("content://sms/"), values, "date=?", new String[] {String.valueOf(l)});
        messageInfos.clear();
        adapter.notifyDataSetChanged();
    }

    public void update_sms_not_sent(long l) {
        ContentValues values = new ContentValues();
        values.put("seen", true);
        values.put("read", true);
        values.put("date",l);
        values.put("type",5);
        values.put("status",32);//not sent
        getContentResolver().update(Uri.parse("content://sms/"), values, "date=?", new String[] {String.valueOf(l)});
        messageInfos.clear();
        adapter.notifyDataSetChanged();
    }

    public void update_unread(long id)
    {
        Log.d("8qrwrqrfsaq", "seen");
        ContentValues values = new ContentValues();
        values.put("seen", true);
        values.put("read", true);
        getContentResolver().update(Uri.parse("content://sms/inbox"), values, "_id="+id, null);
    }





    /*-------------------------------------------------------------------------------------------------------------------------------
     * -------------------------------------------------------------------------------------------------------------------------------
     * -------------------------------------------------------------------------------------------------------------------------------*/


    private void sendSMS(String phoneNumber, String message,int i)
    {
        final long l=System.currentTimeMillis()+i;
        updateMessageInfos.add(l);
        chat_RecyclerView.setAdapter(adapter);
        String SENT = "SMS_SENT"+l; String DELIVERED = "SMS_DELIVERED"+l;
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

//---when the SMS has been sent---
        getApplication().registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {


                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Log.d("dsadadadadadad", "meshyyyyy");

                        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
                        Intent myIntent=new Intent(getApplicationContext(), Receiver.class);
                        myIntent.putExtra("time",l);
                        PendingIntent amPI = PendingIntent.getBroadcast(getApplicationContext(), (int) l,
                                myIntent, 0);
                        alarmManager.cancel(amPI);

                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        update_sms_sent(l);

                        // getmessages();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Log.d("dsadadadadadad", "meshyyyyy");

                        AlarmManager alarmManager1= (AlarmManager) getSystemService(ALARM_SERVICE);
                        Intent myIntent1=new Intent(getApplicationContext(), Receiver.class);
                        myIntent1.putExtra("time",l);
                        PendingIntent amPI1 = PendingIntent.getBroadcast(getApplicationContext(), (int) l,
                                myIntent1, 0);
                        alarmManager1.cancel(amPI1);

                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        update_sms_not_sent(l);
                        // getmessages();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:

                        AlarmManager alarmManager2= (AlarmManager) getSystemService(ALARM_SERVICE);
                        Intent myIntent2=new Intent(getApplicationContext(), Receiver.class);
                        myIntent2.putExtra("time",l);
                        PendingIntent amPI2 = PendingIntent.getBroadcast(getApplicationContext(), (int) l,
                                myIntent2, 0);
                        alarmManager2.cancel(amPI2);

                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        update_sms_not_sent(l);
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:

                        AlarmManager alarmManager3= (AlarmManager) getSystemService(ALARM_SERVICE);
                        Intent myIntent3=new Intent(getApplicationContext(), Receiver.class);
                        myIntent3.putExtra("time",l);
                        PendingIntent amPI3 = PendingIntent.getBroadcast(getApplicationContext(), (int) l,
                                myIntent3, 0);
                        alarmManager3.cancel(amPI3);

                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        update_sms_not_sent(l);
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:

                        AlarmManager alarmManager4= (AlarmManager) getSystemService(ALARM_SERVICE);
                        Intent myIntent4=new Intent(getApplicationContext(), Receiver.class);
                        myIntent4.putExtra("time",l);
                        PendingIntent amPI4 = PendingIntent.getBroadcast(getApplicationContext(), (int) l,
                                myIntent4, 0);
                        alarmManager4.cancel(amPI4);

                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        update_sms_not_sent(l);
                        break;
                }
            }
        }, new IntentFilter(SENT));

//---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = null;

        final TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(SMS_MessagesChat_MultipleContacts.this);
        final boolean isSIM1Ready = telephonyInfo.isSIM1Ready();
        final boolean isSIM2Ready = telephonyInfo.isSIM2Ready();
        final boolean isDualSIM = telephonyInfo.isDualSIM();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (isDualSIM) {
                if (isSIM1Ready && isSIM2Ready) {
                    final ArrayList<Integer> simCardList = new ArrayList<>();
                    SubscriptionManager subscriptionManager;
                    subscriptionManager = SubscriptionManager.from(SMS_MessagesChat_MultipleContacts.this);

                    if (ActivityCompat.checkSelfPermission
                            (this, Manifest.permission.READ_PHONE_STATE)
                            != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    final List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
                    for (SubscriptionInfo subscriptionInfo : subscriptionInfoList) {
                        int subscriptionId = subscriptionInfo.getSubscriptionId();
                        simCardList.add(subscriptionId);
                    }
                    final SharedPreferences preferences = getSharedPreferences("DUAL_SIM", MODE_PRIVATE);
                    final String SIM = preferences.getString("SIM", "0");
                    if (SIM.equals("0")) {
                        sms = SmsManager.getSmsManagerForSubscriptionId(simCardList.get(0));
                    } else if (SIM.equals("1")) {
                        sms = SmsManager.getSmsManagerForSubscriptionId(simCardList.get(1));
                    }
                }
                else
                {
                    sms=SmsManager.getDefault();
                }
            }
            else
            {
                sms=SmsManager.getDefault();
            }
        }else {
            sms=SmsManager.getDefault();
        }

        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        insert_sms_to_sent(phoneNumber,message,l);
        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent myIntent=new Intent(this, Receiver.class);
        myIntent.putExtra("time",l);
        PendingIntent amPI = PendingIntent.getBroadcast(this, (int) l,
                myIntent, 0);
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.MINUTE,1);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),amPI);
    }
}

