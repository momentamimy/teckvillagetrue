package com.developer.whocaller.net.View;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import com.developer.whocaller.net.R;

public class LogHolder extends RecyclerView.ViewHolder {

    public CircleImageView logCircleImageView;
    public TextView logName;
    public ImageView logIcon;
    public TextView logDate,numbersofcallinminte;
    public TextView callType;
    public ProgressBar progressBar;
    public CircleImageView progressCircleImageView;
    public TextView datesection;
    public LinearLayout item;
    public RelativeLayout calllayout;
    public RelativeLayout infoicon,chaticon;

    public LogHolder(View itemView) {
        super(itemView);
        logCircleImageView=itemView.findViewById(R.id.contact_img_log_recycle);
        logName=itemView.findViewById(R.id.name_of_log);
        logIcon=itemView.findViewById(R.id.type_of_call);
        logDate=itemView.findViewById(R.id.date_of_call_log);
        callType=itemView.findViewById(R.id.type_of_num_log);
        progressBar=itemView.findViewById(R.id.progress);
        progressCircleImageView=itemView.findViewById(R.id.progress_contact_img);
        datesection=itemView.findViewById(R.id.datesection);
        item=itemView.findViewById(R.id.layoutlist);
        infoicon=itemView.findViewById(R.id.info);
        chaticon=itemView.findViewById(R.id.chaticon);
        calllayout=itemView.findViewById(R.id.firstl);
        numbersofcallinminte=itemView.findViewById(R.id.numbersofcall);
    }
}
