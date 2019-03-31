package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.Receiver;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by gaber on 12/08/2018.
 */

public class messages_list_adapter extends RecyclerView.Adapter<messages_list_adapter.MyViewHolder> {

    private Context context;
    private List<sms_messages_model> contact_list;

    public EditText messageSend;
    public RecyclerView chat_RecyclerView;
    public String address;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView text_sms;
        public TextView time_sms;
        public ImageView type_icon;
        public TextView day_date;
        public LinearLayout warnText;

        public MyViewHolder(View view) {
            super(view);
            text_sms = (TextView) view.findViewById(R.id.message);
            time_sms = (TextView) view.findViewById(R.id.time);
            type_icon = view.findViewById(R.id.type_icon);
            day_date = view.findViewById(R.id.Day_Date);
            warnText = view.findViewById(R.id.warn_text);
        }
    }


    public messages_list_adapter(Context context, List<sms_messages_model> contact_list) {
        this.context = context;
        this.contact_list = contact_list;

    }

    public messages_list_adapter(Context context, List<sms_messages_model> contact_list, EditText messageSend, RecyclerView chat_RecyclerView, String address) {
        this.context = context;
        this.contact_list = contact_list;
        this.messageSend = messageSend;
        this.chat_RecyclerView = chat_RecyclerView;
        this.address = address;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 3) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_sms_chat_send, parent, false);
            return new MyViewHolder(itemView);
        }
        if (viewType == 2) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_sms_chat_day, parent, false);
            return new MyViewHolder(itemView);
        } else if (viewType == 1) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_sms_chat_send, parent, false);
            return new MyViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_sms_chat_receive, parent, false);
            return new MyViewHolder(itemView);
        }

    }


    @Override
    public int getItemViewType(int position) {
        sms_messages_model data = contact_list.get(position);
        if (data.int_Type == 5) {
            if (data.status != 31) {
                return 3;
            }
        }
        if (data.isShowDay()) {
            return 2;
        } else if (data.address.equals(context.getSharedPreferences("logged_in", MODE_PRIVATE).getString("phone", ""))) {
            return 1;
        } else {
            return 0;
        }

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final sms_messages_model data = contact_list.get(position);

        if (holder.getItemViewType() == 2) {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTimeInMillis(data.date);
            holder.day_date.setVisibility(View.VISIBLE);
            Calendar calendar2 = Calendar.getInstance();
            if (calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR) &&
                    calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) {
                holder.day_date.setText("Today");
            } else if (calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR) - 1 &&
                    calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) {
                holder.day_date.setText("Yesterday");
            } else if (calendar1.get(Calendar.DAY_OF_YEAR) > calendar2.get(Calendar.DAY_OF_YEAR) - 5 &&
                    calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) {
                holder.day_date.setText(String.format("%1$tA", calendar1));
            } else {
                holder.day_date.setText(String.format("%1$tb %1$td %1$tY", calendar1));
            }


        } else {
            Date date = new Date(data.date);
            SimpleDateFormat df2 = new SimpleDateFormat("hh:mm a");
            String dateText = df2.format(date);
            holder.time_sms.setText(dateText);
            holder.text_sms.setText(data.body);
            if (data.int_Type == 5) {
                if (holder.getItemViewType() == 1) {
                    //progress
                    holder.type_icon.setImageResource(R.drawable.ic_progress_24dp);
                    holder.warnText.setVisibility(View.GONE);
                } else if (holder.getItemViewType() == 3) {
                    holder.type_icon.setImageResource(R.drawable.ic_close_gray_24dp);
                    holder.warnText.setVisibility(View.VISIBLE);
                    holder.text_sms.setTextColor(context.getResources().getColor(R.color.notSent));
                    holder.time_sms.setTextColor(context.getResources().getColor(R.color.notSent));
                    holder.warnText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MyCustomSpeedDialog(data.body,data.id);
                        }
                    });
                    //not_sent
                }
            } else if (data.int_Type == 2) {
                holder.type_icon.setImageResource(R.drawable.ic_check_black_24dp);
            }
            //holder.date_sms.setText(dateText);
         /*if (holder.getItemViewType()==1) {
             Drawable img = context.getResources().getDrawable(R.drawable.seen);
             img.setBounds(0, 0, 33, 33);
             holder.date_sms.setCompoundDrawables(img, null, null, null);
         }*/
            ContentValues values = new ContentValues();
            values.put("read", true);
            values.put("seen", true);
            context.getContentResolver().update(Uri.parse("content://sms/inbox"), values, "_id=" + data.id, null);


        }

    }

    @Override
    public int getItemCount() {
        return contact_list.size();
    }

    Dialog MyDialogNotSent;

    public void MyCustomSpeedDialog(final String SMSBody, final long ID) {
        MyDialogNotSent = new Dialog(context);
        MyDialogNotSent.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialogNotSent.setContentView(R.layout.not_sent_dialog);
        Window window = MyDialogNotSent.getWindow();
        window.setLayout(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        MyDialogNotSent.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView Resend = MyDialogNotSent.findViewById(R.id.Resend);
        TextView Delete = MyDialogNotSent.findViewById(R.id.Delete);
        TextView Edit = MyDialogNotSent.findViewById(R.id.edit);

        Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String myPackageName = context.getPackageName();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (!Telephony.Sms.getDefaultSmsPackage(context).equals(myPackageName)) {
                        Intent intent =
                                new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                                myPackageName);
                        context.startActivity(intent);
                    }
                    else
                    {
                        sendSMS(address,SMSBody);
                        MyDialogNotSent.dismiss();
                    }
                }
                else
                {
                    sendSMS(address,SMSBody);
                    MyDialogNotSent.dismiss();
                }

            }
        });
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String myPackageName = context.getPackageName();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (!Telephony.Sms.getDefaultSmsPackage(context).equals(myPackageName)) {
                        Intent intent =
                                new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                                myPackageName);
                        context.startActivity(intent);
                    }
                    else
                    {
                        delete_sms(ID);
                        MyDialogNotSent.dismiss();
                    }
                }
                else
                {
                    delete_sms(ID);
                    MyDialogNotSent.dismiss();
                }

            }
        });
        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDialogButton(SMSBody);
                MyDialogNotSent.dismiss();
            }
        });

        MyDialogNotSent.show();
    }

    public void EditDialogButton(String SMSBody) {
        messageSend.setText(SMSBody);
    }

    private void sendSMS(String phoneNumber, String message) {
        final long l = System.currentTimeMillis();
        String SENT = "SMS_SENT" + l;
        String DELIVERED = "SMS_DELIVERED" + l;
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                new Intent(DELIVERED), 0);

//---when the SMS has been sent---
        context.getApplicationContext().registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Log.d("dsadadadadadad", "meshyyyyy");

                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                        Intent myIntent = new Intent(context, Receiver.class);
                        myIntent.putExtra("time", l);
                        PendingIntent amPI = PendingIntent.getBroadcast(context, (int) l,
                                myIntent, 0);
                        alarmManager.cancel(amPI);

                        Toast.makeText(context, "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        update_sms_sent(l);

                        // getmessages();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Log.d("dsadadadadadad", "meshyyyyy");

                        AlarmManager alarmManager1 = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                        Intent myIntent1 = new Intent(context, Receiver.class);
                        myIntent1.putExtra("time", l);
                        PendingIntent amPI1 = PendingIntent.getBroadcast(context, (int) l,
                                myIntent1, 0);
                        alarmManager1.cancel(amPI1);

                        Toast.makeText(context, "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        update_sms_not_sent(l);
                        // getmessages();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:

                        AlarmManager alarmManager2 = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                        Intent myIntent2 = new Intent(context, Receiver.class);
                        myIntent2.putExtra("time", l);
                        PendingIntent amPI2 = PendingIntent.getBroadcast(context, (int) l,
                                myIntent2, 0);
                        alarmManager2.cancel(amPI2);

                        Toast.makeText(context, "No service",
                                Toast.LENGTH_SHORT).show();
                        update_sms_not_sent(l);
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:

                        AlarmManager alarmManager3 = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                        Intent myIntent3 = new Intent(context, Receiver.class);
                        myIntent3.putExtra("time", l);
                        PendingIntent amPI3 = PendingIntent.getBroadcast(context, (int) l,
                                myIntent3, 0);
                        alarmManager3.cancel(amPI3);

                        Toast.makeText(context, "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        update_sms_not_sent(l);
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:

                        AlarmManager alarmManager4 = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                        Intent myIntent4 = new Intent(context, Receiver.class);
                        myIntent4.putExtra("time", l);
                        PendingIntent amPI4 = PendingIntent.getBroadcast(context, (int) l,
                                myIntent4, 0);
                        alarmManager4.cancel(amPI4);

                        Toast.makeText(context, "Radio off",
                                Toast.LENGTH_SHORT).show();
                        update_sms_not_sent(l);
                        break;
                }
            }
        }, new IntentFilter(SENT));

//---when the SMS has been delivered---
        context.getApplicationContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        insert_sms_to_sent(phoneNumber, message, l);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(context, Receiver.class);
        myIntent.putExtra("time", l);
        PendingIntent amPI = PendingIntent.getBroadcast(context, (int) l,
                myIntent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 1);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), amPI);
    }


    public void insert_sms_to_sent(String phone, String sms, long l) {
        ContentValues values = new ContentValues();
        values.put("address", phone);
        values.put("body", sms);
        values.put("seen", true);
        values.put("read", true);
        values.put("date", l);
        values.put("type", 5);
        values.put("status", 31);//in progress
        context.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
        refresh();
    }

    private void update_sms_sent(long l) {
        ContentValues values = new ContentValues();
        values.put("seen", true);
        values.put("read", true);
        values.put("date", l);
        values.put("type", 2);
        values.put("status", 0);//sent
        context.getContentResolver().update(Uri.parse("content://sms/"), values, "date=?", new String[]{String.valueOf(l)});
        refresh();
    }

    public void update_sms_not_sent(long l) {
        ContentValues values = new ContentValues();
        values.put("seen", true);
        values.put("read", true);
        values.put("date", l);
        values.put("type", 5);
        values.put("status", 32);//not sent
        context.getContentResolver().update(Uri.parse("content://sms/"), values, "date=?", new String[]{String.valueOf(l)});
        refresh();
    }

    public void update_unread(long id)
    {
        Log.d("8qrwrqrfsaq", "seen");
        ContentValues values = new ContentValues();
        values.put("seen", true);
        values.put("read", true);
        context.getContentResolver().update(Uri.parse("content://sms/inbox"), values, "_id="+id, null);
    }

    private void delete_sms(long ID) {
        context.getContentResolver().delete(Uri.parse("content://sms/"),"_id=?", new String[] {String.valueOf(ID)});
        refresh();
    }



    public void refresh() {
        contact_list.clear();
        get_delivered_messages();
    }

    private void get_delivered_messages() {

        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_ALL = "content://sms/";
        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type", "read", "date_sent", "thread_id", "status"};
            Cursor cur = context.getContentResolver().query(uri, projection, null, null, "date desc");
            if (cur.moveToFirst()) {
                int index_id = cur.getColumnIndex("_id");
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");
                int index_seen = cur.getColumnIndex("read");
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

                    if (strAddress.contains(address)) {
                        contact_list.add(new sms_messages_model(id, strAddress, longDate, strbody, int_Type, strseen, date_sent, strthread_id, int_status));
                        if (!strseen.equals("1")) {
                            update_unread(id);
                        }
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

    private void get_sent_messages() {

        final String SMS_URI_INBOX = "content://sms/";
        final String SMS_URI_ALL = "content://sms/";
        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type", "seen", "date_sent", "thread_id", "status"};
            Cursor cur = context.getContentResolver().query(uri, projection, null, null, "date desc");
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
                        if (int_Type != 1)
                            if (strAddress.contains(address)) {
                                contact_list.add(new sms_messages_model(id, context.getSharedPreferences("logged_in", MODE_PRIVATE).getString("phone", ""), longDate, strbody, int_Type, strseen, date_sent, strthread_id, int_status));

                            }
                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
                sort_messages(contact_list);
            }
        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }
    }

    private void sort_messages(List<sms_messages_model> list) {
        Collections.sort(list, new Comparator<sms_messages_model>() {
            public int compare(sms_messages_model o1, sms_messages_model o2) {
                if (o1.date == o2.date)
                    return 0;
                return o1.date < o2.date ? -1 : 1;
            }
        });
        long myDateDay = 0;
        for (int i = 0; i < contact_list.size(); i++) {
            sms_messages_model model = contact_list.get(i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(model.date);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTimeInMillis(myDateDay);
            Log.d("tmtmtmtmtmtmtmtm2", myDateDay + " : " + model.date);
            if (calendar.get(Calendar.DAY_OF_YEAR) != calendar1.get(Calendar.DAY_OF_YEAR)) {
                Log.d("tmtmtmtmtmtmtmtm1", myDateDay + " : " + model.date);
                myDateDay = model.date;
                sms_messages_model newmodel = new sms_messages_model(0, "", model.date, "", 0, "", model.date, "", 0);
                newmodel.setShowDay(true);
                contact_list.add(i, newmodel);
            }

        }

        this.notifyDataSetChanged();
        if (contact_list.size()>2){
            chat_RecyclerView.smoothScrollToPosition(contact_list.size() - 1);
        }
    }
}

