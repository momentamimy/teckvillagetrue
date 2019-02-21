package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by khaled-pc on 2/20/2019.
 */

public class Permission {
    Context context;

    public Permission(Context context) {
        this.context=context;
    }

    // Request a runtime permission to app user.
    public void requestPermission(String permission,int requestCode)
    {
        String requestPermissionArray[] = {permission};
        ActivityCompat.requestPermissions((Activity) context, requestPermissionArray, requestCode);
    }


    // Check whether user has phone contacts manipulation permission or not.
    public boolean hasPhoneContactsPermission(String permission)
    {
        boolean ret = false;

        // If android sdk version is bigger than 23 the need to check run time permission.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // return phone read contacts permission grant status.
            int hasPermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
            // If permission is granted then return true.
            if (hasPermission == PackageManager.PERMISSION_GRANTED) {
                ret = true;
            }
        }else
        {
            ret = true;
        }
        return ret;
    }
}
