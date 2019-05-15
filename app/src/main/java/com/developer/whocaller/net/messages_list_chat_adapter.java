package com.developer.whocaller.net;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.DataReceived;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.MessageChatModel;
import com.developer.whocaller.net.R;

/**
 * Created by gaber on 12/08/2018.
 */

public class messages_list_chat_adapter extends RecyclerView.Adapter<messages_list_chat_adapter.MyViewHolder> {

    private Context context;
    private List<MessageChatModel> contact_list;
    private int contactID;
    boolean isGroup=false;

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


    public messages_list_chat_adapter(Context context, List<MessageChatModel> contact_list,int contactID) {
        this.context = context;
        this.contact_list = contact_list;
        this.contactID=contactID;
    }


    public messages_list_chat_adapter(Context context, List<MessageChatModel> contact_list,int contactID,boolean isGroup) {
        this.context = context;
        this.contact_list = contact_list;
        this.contactID=contactID;
        this.isGroup=isGroup;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_sms_chat_day, parent, false);
            return new MyViewHolder(itemView);
        }
        else if (viewType == 1) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_sms_chat_send, parent, false);
            return new MyViewHolder(itemView);
        } else if (viewType == 0){
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sms_chat_receive, parent, false);
            return new MyViewHolder(itemView);
        }
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_sms_chat_receive, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public int getItemViewType(int position) {
        MessageChatModel data = contact_list.get(position);
        if (data.isShowDayDate())
        {
            return 2;
        }
        else if (contactID == data.getSender_id()) {
            return 1;
        } else if (contactID == data.getReceiver_id()){
            return 0;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        MessageChatModel data = contact_list.get(position);
        if (holder.getItemViewType()==2)
        {
            Calendar calendar1 =covertTime(data.getCreated_at());
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
        }
        else {
            if (holder.getItemViewType()==1)
            {
                holder.text_sms.setText(data.getText());
            }
            else if (holder.getItemViewType()==0 && isGroup)
            {
                DataReceived sender=data.getSender();
                 Spannable spannable = new SpannableString(sender.getName()+"\n"+data.getText());

                        spannable.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorPrimaryDark)),
                                (sender.getName()+":\n"+data.getText()).indexOf(sender.getName()),
                                (sender.getName()+":\n"+data.getText()).indexOf(sender.getName()) + sender.getName().length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        holder.text_sms.setText(spannable, TextView.BufferType.SPANNABLE);
            }
            else
            {
                holder.text_sms.setText(data.getText());
            }
            Calendar cal=covertTime(data.getCreated_at());
            Date date = new Date(cal.getTimeInMillis());
            SimpleDateFormat df2 = new SimpleDateFormat("hh:mm a");
            String dateText = df2.format(date);
            holder.time_sms.setText(dateText);
            if (holder.getItemViewType()==1)
            {
                if (data.getStatus()) {
                    //progress
                    holder.type_icon.setImageResource(R.drawable.ic_progress_24dp);
                }
                else {
                    //sent
                    holder.type_icon.setImageResource(R.drawable.ic_check_black_24dp);
                }
            }
        }


    }

    @Override
    public int getItemCount() {
        return contact_list.size();
    }



    public Calendar covertTime(String strDate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = sdf.parse(strDate);
            Log.d("OPEN", String.valueOf(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}

