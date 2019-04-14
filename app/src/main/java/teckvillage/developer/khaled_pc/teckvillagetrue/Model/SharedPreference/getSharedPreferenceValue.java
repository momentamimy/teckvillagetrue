package teckvillage.developer.khaled_pc.teckvillagetrue.model.SharedPreference;

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

    public static String getUserCompany(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        return sharedPref.getString("User_company","NoValueStored");
    }

    public static String getUserGender(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        return sharedPref.getString("User_gender","Prefer_not_to_say");
    }

    public static String getUserTagId(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        return sharedPref.getString("User_TagID","Email");
    }

    public static String getUserShortNote(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        return sharedPref.getString("User_ShortNote","Add tag");
    }

    public static String getUserWebsite(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        return sharedPref.getString("User_Website","add website");
    }

}
