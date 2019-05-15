package com.developer.whocaller.net.View;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.developer.whocaller.net.R;


public class SMSMessageHolder extends RecyclerView.ViewHolder{

    public TextView message;

    public SMSMessageHolder(View itemView) {
        super(itemView);
        message=itemView.findViewById(R.id.message);
    }
}