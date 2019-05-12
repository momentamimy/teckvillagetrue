package com.developer.whocaller.net.Controller;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import com.developer.whocaller.net.Model.MessageInfo;

public class CustomListViewAdapter extends ArrayAdapter<MessageInfo> {

    private ArrayList<MessageInfo> dataSet;
    Context mContext;

    public CustomListViewAdapter(ArrayList<MessageInfo> data, Context context) {
        super(context, R.layout.row_message, data);
        this.dataSet = data;
        this.mContext = context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MessageInfo dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        final View result;



            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_message, parent, false);

            ImageView contactPhoto=convertView.findViewById(R.id.contact_img_log_recycle);
            ImageView dot=convertView.findViewById(R.id.DOT);
            TextView logName = (TextView) convertView.findViewById(R.id.name_of_log);
            TextView textMessage = convertView.findViewById(R.id.text_sms_message);
            TextView messageDate = convertView.findViewById(R.id.date_sms_message);
            ImageView notSentIcon = convertView.findViewById(R.id.NotSent_Icon);

            result = convertView;


            Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            //result.startAnimation(animation);
            lastPosition = position;

            Calendar calendar=Calendar.getInstance();
            calendar.setTimeInMillis(dataModel.logDate);
            Calendar calendar1=Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_YEAR) == calendar1.get(Calendar.DAY_OF_YEAR) &&
                calendar.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR))
        {
            messageDate.setText(String.format("%1$tI:%1$tM %1$Tp", calendar));
        }
        else if (calendar.get(Calendar.DAY_OF_YEAR) == calendar1.get(Calendar.DAY_OF_YEAR)-1 &&
                calendar.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR))
        {
            messageDate.setText("yesterday");
        }
        else if (calendar.get(Calendar.DAY_OF_YEAR) > calendar1.get(Calendar.DAY_OF_YEAR)-5 &&
                calendar.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR))
        {
            messageDate.setText(String.format("%1$tA", calendar));
        }
        else
        {
            messageDate.setText(String.format("%1$tb %1$td %1$tY", calendar));
        }

            logName.setText(dataModel.logName);
            textMessage.setText(dataModel.logMessage);
        Log.d("paaaaalleeeeeeez",dataModel.typeMessage);
            if (dataModel.type.equals("5"))
            {
                notSentIcon.setVisibility(View.VISIBLE);
            }

        Log.d("dada5wqeqe",dataModel.read);
            if (dataModel.read.equals("1"))
            {
                Log.d("dada5wqeqe","true");
                logName.setTypeface(logName.getTypeface(), Typeface.NORMAL);
                textMessage.setTypeface(textMessage.getTypeface(), Typeface.NORMAL);
                messageDate.setTypeface(messageDate.getTypeface(), Typeface.NORMAL);
                dot.setVisibility(View.GONE);
            }
            else if (dataModel.read.equals("0"))
            {
                Log.d("dada5wqeqe","false");
                logName.setTypeface(logName.getTypeface(), Typeface.BOLD);
                textMessage.setTypeface(textMessage.getTypeface(), Typeface.BOLD);
                messageDate.setTypeface(messageDate.getTypeface(), Typeface.BOLD);
                dot.setVisibility(View.VISIBLE);
            }


            if (dataModel.getTypeMessage().equals("spam"))
            {
                int num=0;
                TextView report=convertView.findViewById(R.id.report);
                //report.setVisibility(View.VISIBLE);
                report.setText(num+" report");

                contactPhoto.setImageResource(R.drawable.ic_nurse_red);
            }
            // Return the completed view to render on screen
            return convertView;


    }

    @Override
    public int getCount() {
        return dataSet.size();
    }
}
