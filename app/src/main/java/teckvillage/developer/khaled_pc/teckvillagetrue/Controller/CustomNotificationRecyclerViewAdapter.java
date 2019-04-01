package teckvillage.developer.khaled_pc.teckvillagetrue.Controller;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.MessageInfo;

public class CustomNotificationRecyclerViewAdapter extends RecyclerView.Adapter<CustomNotificationRecyclerViewAdapter.ViewHolder>{

    private ArrayList<MessageInfo> dataSet;
    Context mContext;

    public CustomNotificationRecyclerViewAdapter(ArrayList<MessageInfo> data, Context context) {
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
        MessageInfo dataModel =dataSet.get(position);
        holder.logName.setText(dataModel.logName);
        holder.textMessage.setText(dataModel.logMessage);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView logName;
        TextView textMessage;
        ImageView contact_img_log_recycle;

        ViewHolder(View itemView) {
            super(itemView);
            logName=itemView.findViewById(R.id.name_of_log);
            textMessage=itemView.findViewById(R.id.text_sms_message);
            contact_img_log_recycle=itemView.findViewById(R.id.contact_img_log_recycle);
        }
    }
}
