package com.developer.whocaller.net.Controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import java.util.List;


import com.developer.whocaller.net.R;
import com.developer.whocaller.net.View.SMSMessageHolder;
import com.developer.whocaller.net.Model.MessageInfo;

public class MessageAdapter extends RecyclerView.Adapter<SMSMessageHolder> {
    List<MessageInfo> itemList;
    Context mContext;


    public static final int MessageSend=0;
    public static final int MessageReceive=1;

    public MessageAdapter(Context context, List<MessageInfo> infos)
    {
        mContext=context;
        itemList=infos;
    }

    @NonNull
    @Override
    public SMSMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType==MessageSend)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sms_chat_send, null);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sms_chat_receive, null);
        }
        SMSMessageHolder messageHolder=new SMSMessageHolder(view);
        return messageHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SMSMessageHolder holder, final int position) {

        MessageInfo messageInfo= itemList.get(position);
        holder.message.setText(messageInfo.logMessage);
        //holder.message.setText(messageInfo.logDate);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (itemList.get(position).logName.equals(""))
        {
            return MessageSend;
        }
        else
        {
            return MessageReceive;
        }
    }
}