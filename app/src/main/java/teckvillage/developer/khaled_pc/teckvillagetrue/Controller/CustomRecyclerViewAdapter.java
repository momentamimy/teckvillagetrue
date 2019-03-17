package teckvillage.developer.khaled_pc.teckvillagetrue.Controller;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.MessageInfo;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder>{

    private ArrayList<MessageInfo> dataSet;
    Context mContext;

    public CustomRecyclerViewAdapter(ArrayList<MessageInfo> data, Context context) {
        this.dataSet = data;
        this.mContext = context;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View convertView = inflater.inflate(R.layout.row_message, parent, false);
        ViewHolder viewHolder=new ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MessageInfo dataModel =dataSet.get(position);
        holder.logName.setText(dataModel.logName);
        holder.textMessage.setText(dataModel.logMessage);
        holder.messageDate.setText(dataModel.logDate);
        Log.d("dada5wqeqe", dataModel.read);
        if (dataModel.read.equals("1")) {
            Log.d("dada5wqeqe", "true");
            holder.logName.setTypeface(holder.logName.getTypeface(), Typeface.NORMAL);
            holder.textMessage.setTypeface(holder.textMessage.getTypeface(), Typeface.NORMAL);
            holder.messageDate.setTypeface(holder.messageDate.getTypeface(), Typeface.NORMAL);
        } else if (dataModel.read.equals("0")) {
            Log.d("dada5wqeqe", "false");
            holder.logName.setTypeface(holder.logName.getTypeface(), Typeface.BOLD);
            holder.textMessage.setTypeface(holder.textMessage.getTypeface(), Typeface.BOLD);
            holder.messageDate.setTypeface(holder.messageDate.getTypeface(), Typeface.BOLD);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView logName;
        TextView textMessage;
        TextView messageDate;

        ViewHolder(View itemView) {
            super(itemView);
            logName=itemView.findViewById(R.id.name_of_log);
            textMessage=itemView.findViewById(R.id.text_sms_message);
            messageDate=itemView.findViewById(R.id.date_sms_message);

        }
    }
}
