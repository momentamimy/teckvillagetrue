package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.Receiver;

public class SMS_MessagesChat extends AppCompatActivity {

    RecyclerView chat_RecyclerView;
    LinearLayoutManager lLayout;
    List<sms_messages_model> messageInfos;
    messages_list_adapter adapter;

    TextView userName;
    CircleImageView userImg;

    LinearLayout Cant_Replay_Layout;

    RelativeLayout Message_Container;
    EditText messageSend;
    FloatingActionButton send;

    String name,address;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0;

    private static SMS_MessagesChat inst;

    ArrayList<Long> updateMessageInfos;
    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }
    public static SMS_MessagesChat instance() {
        return inst;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_messages_chat);
        updateMessageInfos=new ArrayList<>();
        final String myPackageName = getPackageName();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName)) {
                Intent intent =
                        new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                        myPackageName);
                startActivity(intent);

            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Cant_Replay_Layout=findViewById(R.id.Cant_Replay_Layout);
        Message_Container=findViewById(R.id.messageContainer);


        userName=findViewById(R.id.UserName);
        userImg=findViewById(R.id.UserImage);

        name=getIntent().getStringExtra("LogSMSName");
        address=getIntent().getStringExtra("LogSMSAddress");
        String regex = "\\d+";
        String s=address.replace("+","");
        name=address;
        if (!s.matches(regex))
        {
            Cant_Replay_Layout.setVisibility(View.VISIBLE);
            Message_Container.setVisibility(View.GONE);
        }
        else
        {
            Cant_Replay_Layout.setVisibility(View.GONE);
            Message_Container.setVisibility(View.VISIBLE);
        }

        userName.setText(name);

        lLayout = new LinearLayoutManager(getApplicationContext());
        chat_RecyclerView=findViewById(R.id.chat_SMS_messages);
        lLayout.setStackFromEnd(true);
        chat_RecyclerView.setLayoutManager(lLayout);
        messageInfos=new ArrayList<>();
        adapter=new messages_list_adapter(this,messageInfos);
        chat_RecyclerView.setAdapter(adapter);

        //getmessages();



        messageSend=findViewById(R.id.Message_Send);
        send=findViewById(R.id.fab);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo = address;
                String message = messageSend.getText().toString();
                if (phoneNo.length()>0 && message.length()>0)
                {
                    messageSend.setText("");
                    sendSMS(phoneNo,message);

                }

                else
                    Toast.makeText(getBaseContext(),
                            "Please enter both phone number and message.",
                            Toast.LENGTH_SHORT).show();
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
            get_delivered_messages();
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
        refresh();
    }

    private void update_sms_sent(long l) {
        ContentValues values = new ContentValues();
        values.put("seen", true);
        values.put("read", true);
        values.put("date",l);
        values.put("type",2);
        values.put("status",0);//sent
        getContentResolver().update(Uri.parse("content://sms/"), values, "date=?", new String[] {String.valueOf(l)});
        refresh();
    }

    public void update_sms_not_sent(long l) {
        ContentValues values = new ContentValues();
        values.put("seen", true);
        values.put("read", true);
        values.put("date",l);
        values.put("type",5);
        values.put("status",32);//not sent
        getContentResolver().update(Uri.parse("content://sms/"), values, "date=?", new String[] {String.valueOf(l)});
        refresh();
    }


    public void refresh(){
        messageInfos.clear();
        get_delivered_messages();
    }
    private void get_delivered_messages(){

        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_ALL = "content://sms/";
        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] projection = new String[] { "_id", "address", "person", "body", "date", "type","seen","date_sent","thread_id","status"};
            Cursor cur = getContentResolver().query(uri, projection, null, null, "date desc");
            if (cur.moveToFirst()) {
                int index_id = cur.getColumnIndex("_id");
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");
                int index_seen = cur.getColumnIndex("seen");
                int index_date_sent = cur.getColumnIndex("date_sent");
                int index_thread_id = cur.getColumnIndex("thread_id");
                int index_status = cur.getColumnIndex("status");

                do {
                    long id = cur.getLong(index_id);
                    String strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int int_Type = cur.getInt(index_Type);
                    String strseen = cur.getString(index_seen);
                    long date_sent = cur.getInt(index_date_sent);
                    String strthread_id = cur.getString(index_thread_id);
                    int int_status = cur.getInt(index_status);

                    if (strAddress.contains(address.replaceAll("\\s+",""))) {
                        messageInfos.add(new sms_messages_model(id,strAddress, longDate, strbody+int_status,int_Type,strseen,date_sent,strthread_id,int_status));

                    }
                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
                get_sent_messages();
            }

        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }
    }
    private void get_sent_messages(){

        final String SMS_URI_INBOX = "content://sms/";
        final String SMS_URI_ALL = "content://sms/";
        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] projection = new String[] { "_id", "address", "person", "body", "date", "type","seen","date_sent","thread_id","status"};
            Cursor cur = getContentResolver().query(uri, projection, null, null, "date desc");
            if (cur.moveToFirst()) {
                int index_id = cur.getColumnIndex("_id");
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");
                int index_seen = cur.getColumnIndex("seen");
                int index_date_sent = cur.getColumnIndex("date_sent");
                int index_thread_id = cur.getColumnIndex("thread_id");
                int index_status = cur.getColumnIndex("status");

                do {
                    long id = cur.getLong(index_id);
                    String strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int int_Type = cur.getInt(index_Type);
                    String strseen = cur.getString(index_seen);
                    long date_sent = cur.getInt(index_date_sent);
                    String strthread_id = cur.getString(index_thread_id);
                    int int_status = cur.getInt(index_status);

                    if (!TextUtils.isEmpty(strAddress))
                        if (int_Type!=1)
                        if (strAddress.contains(address.replaceAll("\\s+",""))) {
                            messageInfos.add(new sms_messages_model(id,getSharedPreferences("logged_in",MODE_PRIVATE).getString("phone",""), longDate, strbody,int_Type,strseen,date_sent,strthread_id,int_status));

                        }
                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
                sort_messages(messageInfos);
            }
        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }
    }

    private void sort_messages(List<sms_messages_model>list){
        Collections.sort(list, new Comparator<sms_messages_model>(){
            public int compare(sms_messages_model o1, sms_messages_model o2){
                if(o1.date == o2.date)
                    return 0;
                return o1.date < o2.date ? -1 : 1;
            }
        });
        adapter.notifyDataSetChanged();
        if (messageInfos.size()>2){
            chat_RecyclerView.smoothScrollToPosition(messageInfos.size() - 1);
        }

    }
    /*-------------------------------------------------------------------------------------------------------------------------------
     * -------------------------------------------------------------------------------------------------------------------------------
     * -------------------------------------------------------------------------------------------------------------------------------*/


    private void sendSMS(String phoneNumber, String message)
    {
        final long l=System.currentTimeMillis();
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

        SmsManager sms = SmsManager.getDefault();
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
        // addMessageToSent(phoneNumber,message);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final String myPackageName = getPackageName();
            if (!Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName)) {

                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, myPackageName);
                startActivityForResult(intent, 1);
            }else {
                saveSms(phoneNumber, message);
            }
        }else {
            saveSms(phoneNumber, message);
        }*/
    }

    public boolean saveSms(String phoneNumber, String message) {
        boolean ret = false;
        try {
            ContentValues values = new ContentValues();
            values.put("address", phoneNumber);
            values.put("body", message);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Uri uri = Telephony.Sms.Sent.CONTENT_URI;

                getContentResolver().insert(uri, values);
            }
            else {
                getContentResolver().insert(Uri.parse("content://sms/sent"), values);
            }

            ret = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            ret = false;
        }
        return ret;
    }



}

