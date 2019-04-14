package teckvillage.developer.khaled_pc.teckvillagetrue.Controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import teckvillage.developer.khaled_pc.teckvillagetrue.Chat_MessagesChat;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.MessageInfo;

public class CustomRecyclerViewChatAdapter extends RecyclerView.Adapter<CustomRecyclerViewChatAdapter.ViewHolder>{

    private ArrayList<MessageInfo> dataSet;
    Context mContext;

    public CustomRecyclerViewChatAdapter(ArrayList<MessageInfo> data, Context context) {
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        MessageInfo info=dataSet.get(position);

        if (info.type.equals("5"))
            return 2;
        else if (info.read.equals("0"))
            return 0;
        else if (info.read.equals("1"))
            return 1;
        else return 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View convertView = inflater.inflate(R.layout.row_message, parent, false);
        ViewHolder viewHolder=new ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MessageInfo dataModel =dataSet.get(position);
        holder.logName.setText(dataModel.logName);
        holder.textMessage.setText(dataModel.logMessage);

        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(dataModel.logDate);
        Calendar calendar1=Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_YEAR) == calendar1.get(Calendar.DAY_OF_YEAR) &&
                calendar.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR))
        {
            holder.messageDate.setText(String.format("%1$tI:%1$tM %1$Tp", calendar));
        }
        else if (calendar.get(Calendar.DAY_OF_YEAR) == calendar1.get(Calendar.DAY_OF_YEAR)-1 &&
                calendar.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR))
        {
            holder.messageDate.setText("yesterday");
        }
        else if (calendar.get(Calendar.DAY_OF_YEAR) > calendar1.get(Calendar.DAY_OF_YEAR)-5 &&
                calendar.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR))
        {
            holder.messageDate.setText(String.format("%1$tA", calendar));
        }
        else
        {
            holder.messageDate.setText(String.format("%1$tb %1$td %1$tY", calendar));
        }

        Log.d("dada5wqeqe", dataModel.read);
        if (holder.getItemViewType()==1) {
            Log.d("dada5wqeqe", "true");
            holder.logName.setTypeface(holder.logName.getTypeface(), Typeface.NORMAL);
            holder.textMessage.setTypeface(holder.textMessage.getTypeface(), Typeface.NORMAL);
            holder.messageDate.setTypeface(holder.messageDate.getTypeface(), Typeface.NORMAL);
            holder.dot.setVisibility(View.GONE);
        } else if (holder.getItemViewType()==0) {
            Log.d("dada5wqeqe", "false");
            holder.logName.setTypeface(holder.logName.getTypeface(), Typeface.BOLD);
            holder.textMessage.setTypeface(holder.textMessage.getTypeface(), Typeface.BOLD);
            holder.messageDate.setTypeface(holder.messageDate.getTypeface(), Typeface.BOLD);
            holder.dot.setVisibility(View.VISIBLE);
        } else if (holder.getItemViewType()==2) {
            holder.notSentIcon.setVisibility(View.VISIBLE);
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, Chat_MessagesChat.class);
                intent.putExtra("LogSMSName",dataSet.get(position).logName);
                intent.putExtra("LogSMSAddress",dataSet.get(position).logAddress);
                intent.putExtra("LogSMSThreadID",dataSet.get(position).thread_id);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView logName;
        TextView textMessage;
        TextView messageDate;
        ImageView notSentIcon;
        ImageView dot;
        ImageView contactPhoto;

        ViewHolder(View itemView) {
            super(itemView);
            logName=itemView.findViewById(R.id.name_of_log);
            textMessage=itemView.findViewById(R.id.text_sms_message);
            messageDate=itemView.findViewById(R.id.date_sms_message);
            dot=itemView.findViewById(R.id.DOT);
            notSentIcon = itemView.findViewById(R.id.NotSent_Icon);
            contactPhoto=itemView.findViewById(R.id.contact_img_log_recycle);
        }
    }
}
