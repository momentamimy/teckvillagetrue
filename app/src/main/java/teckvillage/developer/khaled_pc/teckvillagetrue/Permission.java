package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by khaled-pc on 2/20/2019.
 */

public class Permission {
    Context context;
    private static final Map<String, Boolean> permissionsResults = new ConcurrentHashMap<>();

    // Permissions names
    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    public static final String RECEIVE_SMS = "android.permission.RECEIVE_SMS";
    public static final String WRITE_SMS = "android.permission.WRITE_SMS";
    public static final String READ_SMS = "android.permission.READ_SMS";
    public static final String SEND_SMS = "android.permission.SEND_SMS";
    public static final String CALL_PHONE = "android.permission.CALL_PHONE";
    public static final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    public static final String READ_CONTACTS = "android.permission.READ_CONTACTS";
    public static final String WRITE_CONTACTS = "android.permission.WRITE_CONTACTS";
    public static final String READ_CALL_LOG = "android.permission.READ_CALL_LOG";
    public static final String WRITE_CALL_LOG = "android.permission.WRITE_CALL_LOG";

    public Permission(Context context) {
        this.context=context;
    }

    // Request a runtime permission to app user.
    public void requestPermission(String permission,int requestCode)
    {
        String requestPermissionArray[] = {permission};
        ActivityCompat.requestPermissions((Activity) context, requestPermissionArray, requestCode);
    }

    /**
     * Checks for permission
     **/
    public static boolean isGranted(@NonNull Context context, @NonNull String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        Boolean result = permissionsResults.get(permission);
        if (result == null) {
            int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
            result = (permissionCheck == PackageManager.PERMISSION_GRANTED);
            permissionsResults.put(permission, result);
        }
        return result;
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
