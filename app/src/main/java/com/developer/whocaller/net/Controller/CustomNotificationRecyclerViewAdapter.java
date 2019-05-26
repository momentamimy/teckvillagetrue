package com.developer.whocaller.net.Controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.NotificattionDataReceived;
import com.developer.whocaller.net.R;

import java.util.List;



public class CustomNotificationRecyclerViewAdapter extends RecyclerView.Adapter<CustomNotificationRecyclerViewAdapter.ViewHolder>{

    private List<NotificattionDataReceived> dataSet;
    Context mContext;

    public CustomNotificationRecyclerViewAdapter(List<NotificattionDataReceived> data, Context context) {
        this.dataSet = data;
        this.mContext = context;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View convertView = inflater.inflate(R.layout.row_notification, parent, false);
        ViewHolder viewHolder=new ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NotificattionDataReceived dataModel =dataSet.get(position);
        holder.logName.setText(dataModel.getTitle());
        holder.textMessage.setText(dataModel.getBody());

        /*Calendar calendar=Calendar.getInstance();
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
        }*/
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView logName;
        TextView textMessage;
        TextView messageDate;
        ImageView contact_img_log_recycle;

        ViewHolder(View itemView) {
            super(itemView);
            logName=itemView.findViewById(R.id.name_of_log);
            textMessage=itemView.findViewById(R.id.text_sms_message);
            messageDate=itemView.findViewById(R.id.date_notifcation_message);
            contact_img_log_recycle=itemView.findViewById(R.id.contact_img_log_recycle);
            messageDate.setVisibility(View.GONE);
        }
    }
}
