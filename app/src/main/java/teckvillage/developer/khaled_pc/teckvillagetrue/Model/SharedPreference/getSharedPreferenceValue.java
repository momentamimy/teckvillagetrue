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
}
