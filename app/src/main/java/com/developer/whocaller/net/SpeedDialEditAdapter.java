package com.developer.whocaller.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import teckvillage.developer.khaled_pc.teckvillagetrue.R;

public class SpeedDialEditAdapter extends ArrayAdapter<EditSpeedModel> {

    List<EditSpeedModel> editSpeedModels;
    Context mContext;
    SharedPreferences sharedPreferences;

    public SpeedDialEditAdapter(List<EditSpeedModel> data, Context context) {

        super(context, R.layout.row_speed_dial_edit, data);
        this.editSpeedModels = data;
        this.mContext = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_speed_dial_edit, parent, false);
        }

        TextView numberCircle=convertView.findViewById(R.id.Dial_Number);
        TextView phoneNumber=convertView.findViewById(R.id.add_contact_or_number);
        numberCircle.setText(editSpeedModels.get(position).num);
        sharedPreferences = mContext.getSharedPreferences("number", Context.MODE_PRIVATE);
        String num=sharedPreferences.getString("#"+editSpeedModels.get(position).num,"");
        if (!num.equals(""))
        {
            phoneNumber.setText(num);
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return editSpeedModels.size();
    }
}
