package com.developer.whocaller.net.Model.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by khaled-pc on 4/10/2019.
 */

public class getSharedPreferenceValue {

    public getSharedPreferenceValue() {
    }

    public static String getUserImage(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        return sharedPref.getString("User_img_profile","NoImageHere");
    }

    public static String getUserName(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        return sharedPref.getString("User_name","User_Name");
    }

    public static String getUserEmail(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        return sharedPref.getString("User_email","NoValueStored");
    }

    public static String getUserPhoneNumber(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        return sharedPref.getString("User_phone","NoValueStored");
    }

    public static String getUserCountry(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        return sharedPref.getString("UserCountry","NoValueStored");
    }

    public static String getUseraddress(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        return sharedPref.getString("User_address","Add address");
    }


    public static String getUserGender(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        return sharedPref.getString("User_gender","Prefer not to say");
    }

    public static String getUserTagId(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        return sharedPref.getString("User_TagID","Add Tag");
    }

    public static String getUserShortNote(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        return sharedPref.getString("User_ShortNote","Add a short text about yourself");
    }

    public static String getUserWebsite(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        return sharedPref.getString("User_Website","add website");
    }

    public static int getPageUploadcontacts(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        return sharedPref.getInt("pagenum",1);
    }

    public static String getUploadVCFStatus(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        return sharedPref.getString("Uploadstatus","failed");
    }

    public static String getUploadToptenContactsStatus(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        return sharedPref.getString("UploadstatusTopten","failed");
    }


    public static boolean getBlockListUploadStatus(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        return sharedPref.getBoolean("UploadBlockList",false);
    }


    public static String getTitle(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        return sharedPref.getString("TitleProfile","Notext");
    }


    public static String getCompany(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        return sharedPref.getString("CompanyProfile","Notext");
    }


    public static int getSpamLimit(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        return sharedPref.getInt("SpamLimit",0);
    }


    public static boolean getUploadNewContactStatus(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        return sharedPref.getBoolean("NewContact",false);
    }

}
