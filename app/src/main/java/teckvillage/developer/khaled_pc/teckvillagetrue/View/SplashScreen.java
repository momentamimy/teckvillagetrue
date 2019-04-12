package teckvillage.developer.khaled_pc.teckvillagetrue.View;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import teckvillage.developer.khaled_pc.teckvillagetrue.MainActivity;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;

public class SplashScreen extends AppCompatActivity implements EasyPermissions.PermissionCallbacks  {

    public final static int Request_Code=999;
    SharedPreferences sharedPreferences;
    boolean IsLogin;
    TextView proceed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        String[] perms={
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };

        sharedPreferences = getSharedPreferences("WhoCaller?", Context.MODE_PRIVATE);
        IsLogin = sharedPreferences.getBoolean("UserLogin", false);


        /*
         //when Login Success
         SharedPreferences sharedPref = mContext.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("UserLogin", IsLogin);
                        editor.putString("User_API_token", userAPItoken);
                        editor.commit();*/

        if (EasyPermissions.hasPermissions(getApplicationContext(),perms))
        {
            //Toast.makeText(getApplicationContext(),"Granted",Toast.LENGTH_LONG).show();
            if(IsLogin){
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent=new Intent(this,Signup.class);//LoginActivity
                intent.putExtra("countrycode","+20");//remove
                intent.putExtra("phonenumber","01021155555");//remove
                startActivity(intent);
                finish();
            }

        }

        proceed=findViewById(R.id.proceed);
        proceed.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //checkDrawOverlayPermission();
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                    Log.v("App", "Build Version Greater than or equal to M: " + Build.VERSION_CODES.M);
                    checkDrawOverlayPermission();
                } else {
                    Log.v("App", "OS Version Less than M");
                    //No need for Permission as less then M OS.
                    callPermissions();
                }
            }
        });


    }

    @AfterPermissionGranted(Request_Code)
    public void callPermissions()
    {

        String[] perms={
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };
        if (EasyPermissions.hasPermissions(getApplicationContext(),perms))
        {
            //Toast.makeText(getApplicationContext(),"Granted",Toast.LENGTH_LONG).show();
            if(IsLogin){
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent=new Intent(this,Signup.class);//LoginActivity
                intent.putExtra("countrycode","+20");//remove
                intent.putExtra("phonenumber","01280945456");//remove
                startActivity(intent);
                finish();
            }
        }
        else
        {
            EasyPermissions.requestPermissions(this,"we need these permissions",Request_Code,perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms))
        {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE)
        {
           // Toast.makeText(this,"granted",Toast.LENGTH_LONG).show();
        }

        //check if received result code
        //  is equal our requested code for draw permission
        if (requestCode == REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    callPermissions();
                }
            }
        }
    }

    // code to post/handler request for permission
    public final static int REQUEST_CODE = 1018;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkDrawOverlayPermission() {
        Log.v("App", "Package Name: " + getApplicationContext().getPackageName());

        // check if we already  have permission to draw over other apps
        if (!Settings.canDrawOverlays(this)) {
            Log.v("App", "Requesting Permission" + Settings.canDrawOverlays(this));
            // if not construct intent to request permission
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getApplicationContext().getPackageName()));
             // request permission via start activity for result
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            Log.v("App", "We already have permission for it.");
            callPermissions();
        }
    }
}
