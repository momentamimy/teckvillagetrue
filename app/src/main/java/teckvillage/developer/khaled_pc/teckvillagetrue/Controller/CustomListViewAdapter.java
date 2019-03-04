package teckvillage.developer.khaled_pc.teckvillagetrue.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.MessageInfo;

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

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_message, parent, false);
        }
            TextView logName = (TextView) convertView.findViewById(R.id.name_of_log);
            TextView textMessage = convertView.findViewById(R.id.text_sms_message);
            TextView messageDate = convertView.findViewById(R.id.date_sms_message);

            result = convertView;


            Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            result.startAnimation(animation);
            lastPosition = position;

            logName.setText(dataModel.logName);
            textMessage.setText(dataModel.logMessage);
            messageDate.setText(dataModel.logDate);
            // Return the completed view to render on screen
            return convertView;


    }
}
