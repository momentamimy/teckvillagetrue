package com.developer.whocaller.net.View;

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
import android.telecom.TelecomManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import com.developer.whocaller.net.AutoStartHintActivity;
import com.developer.whocaller.net.Controller.LocaleHelper;
import com.developer.whocaller.net.MainActivity;
import com.developer.whocaller.net.R;


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
                Manifest.permission.WRITE_CALL_LOG,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                //Manifest.permission.CAMERA,
                Manifest.permission.PROCESS_OUTGOING_CALLS
        };

        sharedPreferences = getSharedPreferences("WhoCaller?", Context.MODE_PRIVATE);
        IsLogin = sharedPreferences.getBoolean("UserLogin", false);


        TextView t2 = (TextView) findViewById(R.id.privacy);
        t2.setMovementMethod(LinkMovementMethod.getInstance());
        t2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://whocaller.net/privacy-policy/"));
                startActivity(intent);
            }

        });
        TextView t3 = (TextView) findViewById(R.id.termsOfService);
        t3.setMovementMethod(LinkMovementMethod.getInstance());
        t3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://whocaller.net/terms-of-service/"));
                startActivity(intent);
            }

        });

        /*
         //when Login Success
         SharedPreferences sharedPref = mContext.getSharedPreferences("WhoCaller?", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("UserLogin", IsLogin);
                        editor.putString("User_API_token", userAPItoken);
                        editor.commit();*/
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M){

            if(IsLogin){
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
            }

        } else{
            if (EasyPermissions.hasPermissions(getApplicationContext(),perms))
            {
                //Toast.makeText(getApplicationContext(),"Granted",Toast.LENGTH_LONG).show();
                if(IsLogin){
                    Intent intent=new Intent(this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    String manufacturer = android.os.Build.MANUFACTURER;
                    if ("xiaomi".equalsIgnoreCase(manufacturer)|| "oppo".equalsIgnoreCase(manufacturer)||
                            "vivo".equalsIgnoreCase(manufacturer)||
                            "Letv".equalsIgnoreCase(manufacturer)||
                            "Honor".equalsIgnoreCase(manufacturer))
                    {
                        Intent intent=new Intent(this, AutoStartHintActivity.class);//LoginActivity
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Intent intent=new Intent(this,LoginActivity.class);//LoginActivity
                /* Intent intent=new Intent(this,Signup.class);//LoginActivity
                intent.putExtra("countrycode","+20");//remove
                intent.putExtra("phonenumber","01021155607");//remove
                intent.putExtra("contryname","Egypt");//remove*/
                        startActivity(intent);
                        finish();
                    }
                }

            }
        }


        proceed=findViewById(R.id.proceed);
        proceed.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //checkDrawOverlayPermission();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.v("App", "Build Version Greater than or equal to M: " + Build.VERSION_CODES.M);
                    Intent intent1 = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                            .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
                    if (intent1.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent1,30);
                        Log.w("apimashy", "Intent available to handle action");
                        checkDrawOverlayPermission();
                    }
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
                Manifest.permission.WRITE_CALL_LOG,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                //Manifest.permission.CAMERA,
                Manifest.permission.PROCESS_OUTGOING_CALLS
        };

        String[] perms1={
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.WRITE_CALL_LOG,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                //Manifest.permission.CAMERA,
                Manifest.permission.PROCESS_OUTGOING_CALLS
        };




        if (EasyPermissions.hasPermissions(getApplicationContext(),perms))
        {
            //Toast.makeText(getApplicationContext(),"Granted",Toast.LENGTH_LONG).show();
            if(IsLogin){
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                String manufacturer = android.os.Build.MANUFACTURER;
                if ("xiaomi".equalsIgnoreCase(manufacturer)|| "oppo".equalsIgnoreCase(manufacturer)||
                        "vivo".equalsIgnoreCase(manufacturer)||
                        "Letv".equalsIgnoreCase(manufacturer)||
                        "Honor".equalsIgnoreCase(manufacturer))
                {
                    Intent intent=new Intent(this, AutoStartHintActivity.class);//LoginActivity
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent=new Intent(this,LoginActivity.class);//LoginActivity
                /* Intent intent=new Intent(this,Signup.class);//LoginActivity
                intent.putExtra("countrycode","+20");//remove
                intent.putExtra("phonenumber","01021155607");//remove
                intent.putExtra("contryname","Egypt");//remove*/
                    startActivity(intent);
                    finish();
                }

            }
        }
        else
        {
            EasyPermissions.requestPermissions(this,"we need these permissions",Request_Code,perms1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("daldjaljdajdwqjpf", String.valueOf(requestCode));
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

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
}
