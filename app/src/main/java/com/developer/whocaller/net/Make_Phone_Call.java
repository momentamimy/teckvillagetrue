package com.developer.whocaller.net;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import com.developer.whocaller.net.Model.Get_Calls_Log;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;

/**
 * Created by khaled-pc on 4/12/2019.
 */

public class Make_Phone_Call {

    @SuppressLint("MissingPermission")
    public static void makephonecall(Context context, String phoneNumber){
         TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(context);
        final boolean isSIM1Ready = telephonyInfo.isSIM1Ready();
        final boolean isSIM2Ready = telephonyInfo.isSIM2Ready();
        final boolean isDualSIM = telephonyInfo.isDualSIM();
        if (isDualSIM) {
            if (isSIM1Ready&&isSIM2Ready)
            {
                MyCustomDualSimDialog(context,phoneNumber,telephonyInfo);
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" +phoneNumber));
                context.startActivity(intent);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            context.startActivity(intent);
        }

    }


    public static void MyCustomDualSimDialog(final Context context, final String Number, TelephonyInfo telephonyInfo) {
        Get_Calls_Log get_calls_log = new Get_Calls_Log(context);
        final Dialog MyDialogDualSim = new Dialog(context);
        MyDialogDualSim.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialogDualSim.setContentView(R.layout.dual_sim_dialog);
        Window window = MyDialogDualSim.getWindow();
        window.setLayout(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        MyDialogDualSim.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        String ContactName=get_calls_log.getContactName(Number);

        TextView Title=MyDialogDualSim.findViewById(R.id.Dual_Sim_Titel);
        if (!TextUtils.isEmpty(ContactName))
        {
            Title.setText("Call "+ContactName+" from");
        }
        else
        {
            Title.setText("Call "+Number+" from");
        }

        List<String> SIM_NAMES=telephonyInfo.getNetworkOperator(context);
        TextView sim_name_one=MyDialogDualSim.findViewById(R.id.SIM_Name_One);
        TextView sim_name_two=MyDialogDualSim.findViewById(R.id.SIM_Name_Two);

        if (SIM_NAMES.size()>=2)
        {
            sim_name_one.setText(SIM_NAMES.get(0));
            sim_name_two.setText(SIM_NAMES.get(1));
        }

        RelativeLayout sim_one_layout=MyDialogDualSim.findViewById(R.id.SIM_One_Layout);
        RelativeLayout sim_two_layout=MyDialogDualSim.findViewById(R.id.SIM_Two_Layout);

        sim_one_layout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.putExtra("com.android.phone.extra.slot", 0); //For sim 1
                //intent.putExtra("simSlot", 0); //For sim 1
                intent.setData(Uri.parse("tel:" + Number));
                context.startActivity(intent);
                MyDialogDualSim.dismiss();
            }
        });

        sim_two_layout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.putExtra("simSlot", 1); //For sim 2
                //intent.putExtra("com.android.phone.extra.slot", 1); //For sim 2
                intent.setData(Uri.parse("tel:" + Number));
                context.startActivity(intent);
                MyDialogDualSim.dismiss();
            }
        });
        MyDialogDualSim.show();
    }

}
