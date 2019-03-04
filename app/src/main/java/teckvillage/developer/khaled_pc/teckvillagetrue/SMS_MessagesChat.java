package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.MessageAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.MessageInfo;

public class SMS_MessagesChat extends AppCompatActivity {

    RecyclerView chat_RecyclerView;
    LinearLayoutManager lLayout;
    List<MessageInfo> messageInfos;

    TextView userName;
    CircleImageView userImg;

    EditText messageSend;
    FloatingActionButton send;

    String name,address;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_messages_chat);

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


        userName=findViewById(R.id.UserName);
        userImg=findViewById(R.id.UserImage);

        name=getIntent().getStringExtra("LogSMSName");
        address=getIntent().getStringExtra("LogSMSAddress");

        userName.setText(name);

        lLayout = new LinearLayoutManager(getApplicationContext());
        chat_RecyclerView=findViewById(R.id.chat_SMS_messages);
        lLayout.setStackFromEnd(true);
        chat_RecyclerView.setLayoutManager(lLayout);

        messageInfos=new ArrayList<>();

        getmessages();



        messageSend=findViewById(R.id.Message_Send);
        send=findViewById(R.id.fab);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo = address;
                String message = messageSend.getText().toString();
                if (phoneNo.length()>0 && message.length()>0)
                {
                    sendSMS(phoneNo, message);
                    messageSend.setText("");
                }

                else
                    Toast.makeText(getBaseContext(),
                            "Please enter both phone number and message.",
                            Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void getmessages()
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
        MessageAdapter adapter=new MessageAdapter(getApplicationContext(),messageInfos);
        chat_RecyclerView.setAdapter(adapter);
    }

    private void sendSMS(String phoneNumber, String message)
    {

        Calendar calendar=Calendar.getInstance();
        String stringDate=String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))+"/"+String.valueOf(calendar.get(Calendar.MONTH)+1)+"/"+String.valueOf(calendar.get(Calendar.YEAR));
        messageInfos.add(new MessageInfo(null,"",message,stringDate,phoneNumber));
        MessageAdapter adapter=new MessageAdapter(getApplicationContext(),messageInfos);
        chat_RecyclerView.setAdapter(adapter);
        String SENT = "SMS_SENT"; String DELIVERED = "SMS_DELIVERED";
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

//---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {

                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        getmessages();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        getmessages();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
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
