package com.developer.whocaller.net.Model.retrofit;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by khaled-pc on 4/8/2019.
 */

public class ApiAccessToken {

  public static String  getAPIaccessToken(Context context){
      SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
      return sharedPref.getString("User_API_token","NoAPIACCESS");//NoAPIACCESS
    }

    public static int  getID(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        return sharedPref.getInt("User_ID",-1);
    }
}
