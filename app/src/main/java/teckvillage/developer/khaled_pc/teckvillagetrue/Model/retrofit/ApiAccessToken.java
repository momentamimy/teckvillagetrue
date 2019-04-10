package teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by khaled-pc on 4/8/2019.
 */

public class ApiAccessToken {

  public static String  getAPIaccessToken(Context context){
      SharedPreferences sharedPref = context.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
      return sharedPref.getString("User_API_token","NoAPIACCESS");
    }

}
