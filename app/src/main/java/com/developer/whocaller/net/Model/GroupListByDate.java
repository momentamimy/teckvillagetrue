package com.developer.whocaller.net.Model;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateFormat;

import com.developer.whocaller.net.MainApp;
import com.developer.whocaller.net.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by khaled-pc on 3/5/2019.
 */

public class GroupListByDate {


    Context context;
    public GroupListByDate(Context context) {
        this.context=context;
    }

    /**
     * Receive sorted list and divide it into Letters and contacts
     * @param list
     * @return
     */
    public ArrayList<LogInfo> groupListByDate(ArrayList<LogInfo> list) {
        ArrayList<LogInfo> customList = new ArrayList<LogInfo>();

        if(list.size()>0){

        int i = 0;

        LogInfo logInfo = new LogInfo();
        logInfo.logDate=list.get(0).logDate;
        logInfo.setDateString(getFormattedDate(logInfo.logDate.getTime()));
        logInfo.setType(1);
        customList.add(logInfo);
        for (i = 0; i < list.size() - 1; i++) {
            LogInfo logInfo12 = new LogInfo();

            String dateString1 =getFormattedDate(list.get(i).logDate.getTime());
            String dateString2 =getFormattedDate(list.get(i + 1).logDate.getTime());


            if (dateString1.equals(dateString2)) {
                list.get(i).setType(2);
                customList.add(list.get(i));
            } else {
                list.get(i).setType(2);
                customList.add(list.get(i));
                logInfo12.setDateString(dateString2);
                logInfo12.setType(1);
                customList.add(logInfo12);
            }
        }
        list.get(i).setType(2);
        customList.add(list.get(i));
        return customList;
        }
        return customList;
    }

    public String getFormattedDate(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String dateTimeFormatString = "EEEE, dd MMM";
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)&&(smsTime.get(Calendar.YEAR) == now.get(Calendar.YEAR)&&(smsTime.get(Calendar.MONTH) == now.get(Calendar.MONTH))) ) {
            return context.getResources().getString(R.string.today_recycleview) ;
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1&&(smsTime.get(Calendar.YEAR) == now.get(Calendar.YEAR)&&(smsTime.get(Calendar.MONTH) == now.get(Calendar.MONTH)))){
            return context.getResources().getString(R.string.yesterday_recycleview) ;
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("EEEE, dd MMM yyyy", smsTime).toString();
        }
    }

}
