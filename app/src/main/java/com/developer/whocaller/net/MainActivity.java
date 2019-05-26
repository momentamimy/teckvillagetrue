package com.developer.whocaller.net;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.whocaller.net.Camera_Recognition_package.Camera_Recognition;
import com.developer.whocaller.net.Controller.LocaleHelper;
import com.developer.whocaller.net.Services.FileUploadService;
import com.developer.whocaller.net.Services.GetInitialService;
import com.developer.whocaller.net.Services.UploadBlockListService;
import com.developer.whocaller.net.Services.UploadTopTenContactsService;
import com.developer.whocaller.net.View.ChatFragment;
import com.developer.whocaller.net.View.CheckNetworkConnection;
import com.developer.whocaller.net.View.ConnectionDetector;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import de.hdodenhof.circleimageview.CircleImageView;


import com.developer.whocaller.net.View.BlockList;
import com.developer.whocaller.net.Model.SharedPreference.getSharedPreferenceValue;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG ="fields" ;
    private static final int STATIC_INTEGER_VALUE =221 ;
    public static FragmentManager fragmentManager;
    FrameLayout mainframeLayout,messageframeLayout,contactframeLayout,chatframeLayout;
    private static final String TAG_ANDROID_CONTACTS = "ANDROID_CONTACTS";
    String Email ="developer@whocaller.net";
    private static final int MY_CAMERA_REQUEST_CODE = 109;
    private static final String PUBLIC_STATIC_STRING_IDENTIFIER = "com.techvillage";

    public BottomNavigationView navigationView2;
    ArrayList<String> PhoneNumberListCameraRecognition = new ArrayList<>();
    private static final String STRING_ARRAY_SAMPLE = "./contacts-who-caller.csv";
    SharedPreferences sharedPreferences;

    private boolean csv_status = false;
    CSVWriter writer = null;
    String  vfile = "Contact_WhoCaller"+".vcf";
    ArrayList<String> vCard ;
    private Cursor cursor;
    boolean stateVcf;

    boolean SMSNotification=false;
    Dialog dialogcameraresult;
    public OpenDialPad openDialPad;
    private MyInterface listener ;
    String isContactsUpload,isTopTencontactsUpload;
    boolean UploadBlockList;
    View navView;
    NavigationView navigationView;
    boolean shouldExecuteOnResume;
    String urlFAQ = "http://whocaller.net/faq/";

    // Constants
    // Content provider scheme
    public static final String SCHEME = "content://";
    // Content provider authority
    public static final String AUTHORITY = "com.example.android.datasync.provider";
    // Path for the content provider table
    public static final String TABLE_PATH = "data_table";
    // Account
    public static final String ACCOUNT = "default_account";
    // Global variables
    // A content URI for the content provider's data table
    Uri uri;
    // A content resolver for accessing the provider
    ContentResolver mResolver;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        shouldExecuteOnResume = false;

        FirebaseMessaging.getInstance().subscribeToTopic("all").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "lalalala");
                        }
                        else
                        {
                            Log.d(TAG, "lapaleez");
                        }

                    }
                });

        /*Intent intent = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER);
        intent.putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
        startActivity(intent);*/


        
        SMSNotification=getIntent().getBooleanExtra("NOTIFICATION",false);

        navigationView2=findViewById(R.id.botnav);


        messageframeLayout=(FrameLayout) findViewById(R.id.fragment_container_message);//connect Framelayout
        mainframeLayout=(FrameLayout) findViewById(R.id.fragment_container_main);//connect Framelayout
        contactframeLayout=(FrameLayout) findViewById(R.id.fragment_container_contact);//connect Framelayout
        chatframeLayout=(FrameLayout) findViewById(R.id.fragment_container_chat);//connect Framelayout




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //****************************************************Set HomeFragment as default*********************************************************
        navigationView2.setSelectedItemId(R.id.nav_phone);
        fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        if(mainframeLayout !=null){
            if(savedInstanceState !=null){
                return;
            }
            Main_Fagment fragment_1=new Main_Fagment();
            setListener(fragment_1);//Open Dialpad From Activity
            fragmentTransaction.add(R.id.fragment_container_main,fragment_1,null);
            fragmentTransaction.commit();
        }

        navigationView2.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Fragment selectedFragment = null;
                switch (item.getItemId())
                {
                    case R.id.nav_message:
                        /*selectedFragment = new Message_Fragment();
                            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container_main);
                            if (f instanceof Message_Fragment) {
                                // Do something
                                Log.w("slahhmessage","slalalal");
                                //fragmentTransaction.hide(f);
                            }
                            else
                            {
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_main,
                                        selectedFragment).commit();

                            }*/
                        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container_message);
                        if (f instanceof Message_Fragment) {
                            // Do something
                            Log.w("slahh","slalalal");
                            //fragmentTransaction.hide(f);
                        }
                        else
                        {
                        FragmentTransaction fragmentTransaction1=fragmentManager.beginTransaction();
                        if(messageframeLayout !=null){
                            if(savedInstanceState !=null){
                                break;
                            }
                            Message_Fragment fragment_1=new Message_Fragment();
                            fragmentTransaction1.add(R.id.fragment_container_message,fragment_1,null);
                            fragmentTransaction1.commit();
                        }
                        }
                        messageframeLayout.setVisibility(View.VISIBLE);
                        mainframeLayout.setVisibility(View.GONE);
                        contactframeLayout.setVisibility(View.GONE);
                        chatframeLayout.setVisibility(View.GONE);
                        break;
                    case R.id.nav_phone:
                            /*selectedFragment=new Main_Fagment();
                            Fragment f1 = getSupportFragmentManager().findFragmentById(R.id.fragment_container_main);
                            if (f1 instanceof Main_Fagment) {
                                // Do something
                                Log.w("slahhphone","slalalal");
                               // fragmentTransaction.hide(f1);
                            }
                            else
                            {
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_main,
                                        selectedFragment).commit();
                            }*/
                        Fragment f1 = getSupportFragmentManager().findFragmentById(R.id.fragment_container_main);
                        if (f1 instanceof Main_Fagment) {
                            // Do something
                            Log.w("slahh","slalalal");
                            //fragmentTransaction.hide(f);
                        }
                        else
                        {
                        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                        if(mainframeLayout !=null){
                            if(savedInstanceState !=null){
                                break;
                            }
                            Main_Fagment fragment_1=new Main_Fagment();
                            fragmentTransaction.add(R.id.fragment_container_main,fragment_1,null);
                            fragmentTransaction.commit();
                        }
                        }
                        messageframeLayout.setVisibility(View.GONE);
                        mainframeLayout.setVisibility(View.VISIBLE);
                        contactframeLayout.setVisibility(View.GONE);
                        chatframeLayout.setVisibility(View.GONE);
                        break;
                    case R.id.nav_contacts:
                        /*selectedFragment=new Contacts();
                            Fragment f2 = getSupportFragmentManager().findFragmentById(R.id.fragment_container_main);
                            if (f2 instanceof Contacts) {
                                // Do something
                                Log.w("slahhcontacts","slalalal");
                               // fragmentTransaction.hide(f2);
                               }
                            else
                            {
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_main,
                                        selectedFragment).commit();
                            }*/
                        Fragment f2 = getSupportFragmentManager().findFragmentById(R.id.fragment_container_contact);
                        if (f2 instanceof Contacts) {
                            // Do something
                            Log.w("slahh","slalalal");
                            //fragmentTransaction.hide(f);
                        }
                        else {
                            FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                            if (contactframeLayout != null) {
                                if (savedInstanceState != null) {
                                    break;
                                }
                                Contacts fragment_1 = new Contacts();
                                fragmentTransaction2.add(R.id.fragment_container_contact, fragment_1, null);
                                fragmentTransaction2.commit();
                            }
                        }
                        messageframeLayout.setVisibility(View.GONE);
                        mainframeLayout.setVisibility(View.GONE);
                        contactframeLayout.setVisibility(View.VISIBLE);
                        chatframeLayout.setVisibility(View.GONE);
                        break;
                    case R.id.nav_chat:
                        /*selectedFragment=new Contacts();
                        Fragment f3 = getSupportFragmentManager().findFragmentById(R.id.fragment_container_main);
                        if (f3 instanceof Contacts) {
                            // Do something
                            // fragmentTransaction.hide(f2);
                        }
                        else
                        {
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_main,
                                    selectedFragment).commit();
                        }*/
                        Fragment f3 = getSupportFragmentManager().findFragmentById(R.id.fragment_container_chat);
                        if (f3 instanceof ChatFragment) {
                            // Do something
                            Log.w("slahh","slalalal");
                            //fragmentTransaction.hide(f);
                        }
                        else {
                            FragmentTransaction fragmentTransaction3 = fragmentManager.beginTransaction();
                            if (chatframeLayout != null) {
                                if (savedInstanceState != null) {
                                    break;
                                }
                                ChatFragment fragment_1 = new ChatFragment();
                                fragmentTransaction3.add(R.id.fragment_container_chat, fragment_1, null);
                                fragmentTransaction3.commit();
                            }
                        }
                        messageframeLayout.setVisibility(View.GONE);
                        mainframeLayout.setVisibility(View.GONE);
                        contactframeLayout.setVisibility(View.GONE);
                        chatframeLayout.setVisibility(View.VISIBLE);
                        break;
                }

                return true;
            }
        });

        if (SMSNotification)
        {
            navigationView2.setSelectedItemId(R.id.nav_message);
        }



        //Change Header NAV Drawer
        changeHeaderNavDrawer();


        //Check wifi or data available
        if (CheckNetworkConnection.hasInternetConnection(MainActivity.this)) {

            //Check internet Access
            if (ConnectionDetector.hasInternetConnection(MainActivity.this)) {



                //Upload VCF File
                isContactsUpload=getSharedPreferenceValue.getUploadVCFStatus(MainActivity.this);
                if(isContactsUpload.equals("failed")){
                    try {
                        Log.w("UploadVcfTime","UploadVcfOneTime");
                        Upload_VCF_File_Background();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }




                //Upload Top Ten Contacts
                isTopTencontactsUpload=getSharedPreferenceValue.getUploadToptenContactsStatus(MainActivity.this);
                if (isTopTencontactsUpload.equals("failed")) {
                    try {
                        Log.w("UploadTopTenOneTime","UploadTopTenOneTime");
                        UploadTopTenContacts();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                UploadBlockList=getSharedPreferenceValue.getBlockListUploadStatus(MainActivity.this);
                if(UploadBlockList){
                    UploadBlocklistNumbers();
                }


                //Get Tags Data
                GetItinitail();

            }
        }




        //****************************************************Change in Contacts*****************************************
        // Get the content resolver object for your app
        // mResolver = getContentResolver();

        /*
         * Create a content observer object.
         * Its code does not mutate the provider, so set
         * selfChange to "false"
         */
        //TableObserver observer = new TableObserver();
        /*
         * Register the observer for the data table. The table's path
         * and any of its subpaths trigger the observer.
         */
        //mResolver.registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, observer);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();

            } else {

                if (fragmentList != null) {
                    //TODO: Perform your logic to pass back press here
                    for(Fragment fragment : fragmentList){
                        if(fragment instanceof OnBackPressedListener){
                            ((OnBackPressedListener)fragment).onBackPressed();
                        }
                    }
                }

            }

        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera_recognition) {
            // Handle the camera action
            CameraRecog();

        } else if (id == R.id.nav_Notifcation) {

            startActivity(new Intent(this,Notifications.class));

        } else if (id == R.id.nav_FAQ) {

            openFAQPageWebsite();

        } else if (id == R.id.nav_sendfeedback) {
            //****************************send email************************************************
            SendEmail();

        } else if (id == R.id.nav_share) {
           //Share App
            ShareApp();

        } else if (id == R.id.nav_setting) {

            startActivity(new Intent(this,setting.class));

        }else if (id == R.id.nav_edit_profile) {

            startActivity(new Intent(getApplicationContext(),UserProfileActivity.class));

        }else if (id == R.id.nav_blocklist) {

            startActivity(new Intent(getApplicationContext(),BlockList.class));
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void doSomethingForEachUniquePhoneNumber(Context context) {
        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE,
                //plus any other properties you wish to query
        };

        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
        } catch (SecurityException e) {
            //SecurityException can be thrown if we don't have the right permissions
        }

        if (cursor != null) {
            try {
                HashSet<String> normalizedNumbersAlreadyFound = new HashSet<>();
                int indexOfNormalizedNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
                int indexOfDisplayName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int indexOfDisplayNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int indexOfphoneType = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);

                while (cursor.moveToNext()) {
                    String normalizedNumber = cursor.getString(indexOfNormalizedNumber);
                    if (normalizedNumbersAlreadyFound.add(normalizedNumber)) {
                        String displayName = cursor.getString(indexOfDisplayName);
                        String displayNumber = cursor.getString(indexOfDisplayNumber);
                        int  phoneType = cursor.getInt(indexOfphoneType);

                        //haven't seen this number yet: do something with this contact!
                        Log.i(TAG, "Name: " + displayName);
                        //Log.i(TAG, "Number: " + displayNumber);

                        switch (phoneType) {
                            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                Log.i(TAG, "Mobile Number: " + displayNumber);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                Log.i(TAG, "Home Number: " + displayNumber);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                Log.i(TAG, "Work Number: " + displayNumber);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                                Log.i(TAG, "Other Number: " + displayNumber);
                                break;
                            default:
                                break;
                        }
                        Log.i(TAG, "------------------------------------------------------------------------------------------");


                    } else {
                        //don't do anything with this contact because we've already found this number
                    }
                }
            } finally {
                cursor.close();
            }
        }
    }


    void openFAQPageWebsite(){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(urlFAQ));
        startActivity(i);
    }

    // After user select Allow or Deny button in request runtime permission dialog
    // , this method will be invoked.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted

                    Toast.makeText(getApplicationContext(), "You allowed permission, please click the button again.", Toast.LENGTH_LONG).show();

                }
                else {

                    Toast.makeText(getApplicationContext(), "You must approve the permission", Toast.LENGTH_SHORT).show();
                }
                break;

            case 3:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted

                    Toast.makeText(getApplicationContext(), "You allowed permission, please click the button again.", Toast.LENGTH_LONG).show();

                }
                else {

                    Toast.makeText(getApplicationContext(), "You must approve the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            case MY_CAMERA_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted

                    //startActivity(new Intent(this,Camera_Recognition.class));
                    Intent i = new Intent(this,Camera_Recognition.class);
                    startActivityForResult(i, STATIC_INTEGER_VALUE);

                }
                else {

                    Toast.makeText(getApplicationContext(), "You must approve the permission", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (STATIC_INTEGER_VALUE) : {

                if (resultCode == Activity.RESULT_OK) {

                    // TODO Recieve Phone Numbers ArrayList From Camera Recogition Activity
                    //String newText = data.getStringExtra(PUBLIC_STATIC_STRING_IDENTIFIER);
                    PhoneNumberListCameraRecognition= data.getStringArrayListExtra(PUBLIC_STATIC_STRING_IDENTIFIER);

                    if(PhoneNumberListCameraRecognition.size()==1){
                        //Open Dialog With Phone Number That is Retrieved From Activity Recognition
                        DisplayResultCameraRecognitionDialog(PhoneNumberListCameraRecognition.get(0));
                    }else {

                        //Display Dialog OnMultiChoice To Enable User Select One Of Result Phone numbers from Camera recogition
                        DisplayArraylistResultCameraRecognitionDialog(PhoneNumberListCameraRecognition);
                    }

                }
                break;


            }
        }
    }


    void DisplayResultCameraRecognitionDialog(final String newText){

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.result_dialog_text_recognition, null);

        dialogcameraresult = new Dialog(MainActivity.this);
        dialogcameraresult.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogcameraresult.setContentView(row);

        ColorDrawable transparent = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(transparent, 5);//margin for Dilaog
        dialogcameraresult.getWindow().setBackgroundDrawable(inset);
        dialogcameraresult.show();

        final TextView number=row.findViewById(R.id.name_call_num);
        number.setText(newText);

        RelativeLayout call = row.findViewById(R.id.callphoneicon2);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + newText));
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });

        //On Click Send Message
        RelativeLayout message = row.findViewById(R.id.messageiconsw2);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //newText
                dialogcameraresult.dismiss();
                Intent intent = new Intent(MainActivity.this,SMS_MessagesChat.class);
                intent.putExtra("LogSMSName",newText);
                intent.putExtra("LogSMSAddress",newText);
                startActivity(intent);

            }
        });

        RelativeLayout scanagain = row.findViewById(R.id.scanagain);
        scanagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogcameraresult.dismiss();
                Intent i = new Intent(MainActivity.this,Camera_Recognition.class);
                startActivityForResult(i, STATIC_INTEGER_VALUE);

            }
        });


        //On Click Charge Card
        RelativeLayout chargecard = row.findViewById(R.id.chargecard);
        chargecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //newText
                //dialog.dismiss();
                showDialogChooseTYPESIM(newText);

            }
        });

    }

    public  void showDialogChooseTYPESIM(final String number){

        try {

            AlertDialog.Builder SIMDialog = new AlertDialog.Builder(this);
            SIMDialog.setTitle("Select Your SIM");
            SIMDialog.setIcon(R.drawable.ic_sim);
            String[] SIMDialogItems = {
                    "Orange",
                    "Vodafone",
                    "Etisala",
                    "WE"};

            SIMDialog.setItems(SIMDialogItems, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            dialogcameraresult.dismiss();
                            String chargingNumberorage="#"+number+"*102*";
                            listener.myAction(chargingNumberorage);
                            break;
                        case 1:
                            dialogcameraresult.dismiss();
                            String chargingNumbervoda="#"+number+"*858*";
                            listener.myAction(chargingNumbervoda);
                            break;
                        case 2:
                            dialogcameraresult.dismiss();
                            String chargingNumberetisilat="*555*"+number+"#";
                            listener.myAction(chargingNumberetisilat);
                            break;
                        case 3:
                            dialogcameraresult.dismiss();
                            String chargingNumberWe="#"+number+"*555*";
                            listener.myAction(chargingNumberWe);
                            break;
                    }
                }
            });
            SIMDialog.show();

            //Toast.makeText(this, "Camera Permission Error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    void DisplayArraylistResultCameraRecognitionDialog(ArrayList<String> phonenumbers){

        //Create sequence of items
        final CharSequence[] phonenums = phonenumbers.toArray(new String[phonenumbers.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Select Phone Number");
        dialogBuilder.setItems(phonenums, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //String selectedText = phonenums[item].toString();
                //Selected item in listview
                //Open Dialog With Phone Number That is Selected From This Dialog
                DisplayResultCameraRecognitionDialog(phonenums[item].toString());
            }
        });
        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();
        //Show the dialog
        alertDialogObject.show();

    }


    // Return all raw_contacts _id in a list.
    private List<Integer> getRawContactsIdList()
    {
        List<Integer> ret = new ArrayList<Integer>();

        ContentResolver contentResolver = getContentResolver();

        // Row contacts content uri( access raw_contacts table. ).
        Uri rawContactUri = ContactsContract.RawContacts.CONTENT_URI;
        // Return _id column in contacts raw_contacts table.
        String queryColumnArr[] = {ContactsContract.RawContacts._ID};
        // Query raw_contacts table and return raw_contacts table _id.
        Cursor cursor = contentResolver.query(rawContactUri,queryColumnArr, null, null, null);
        if(cursor!=null)
        {
            cursor.moveToFirst();
            do{
                int idColumnIndex = cursor.getColumnIndex(ContactsContract.RawContacts._ID);
                int rawContactsId = cursor.getInt(idColumnIndex);
                ret.add(new Integer(rawContactsId));
            }while(cursor.moveToNext());
        }

        cursor.close();

        return ret;
    }

    public void openDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(Gravity.START);
    }


    void ShareApp(){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Who Caller?");
            String sAux ="https://play.google.com/store/apps/details?id="+ getPackageName() +"\n\n";//link el prnamg 3ala playstore
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "Choose App"));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    void SendEmail(){
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO );
            intent.setType("plain/text");
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] { Email });
            intent.putExtra(Intent.EXTRA_SUBJECT, "Suggestion from application Who caller?");
            intent.putExtra(Intent.EXTRA_TEXT, "Type your mail text here");
            startActivity(Intent.createChooser(intent, "submit a suggestion or complaint"));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    void CameraRecog(){
        //Check Permission Before Open Activity
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);

        }else {
            // startActivity(new Intent(this,Camera_Recognition.class));
            Intent i = new Intent(this,Camera_Recognition.class);
            startActivityForResult(i, STATIC_INTEGER_VALUE);
        }

    }

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }



    private void createCSV() {

        try {
            writer = new CSVWriter(new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/who_caller_contact.csv"));
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String displayName;
        String number;
        long _id;
        String columns[] = new String[]{ ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER};
        if (writer != null) {
            writer.writeColumnNames(); // Write column header
        }

        ContentResolver cr = getContentResolver();
        // Next query for all contacts, and use the phones mapping
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, columns, null, null, ContactsContract.Data.DISPLAY_NAME + " COLLATE LOCALIZED ASC");

        startManagingCursor(cursor);
        if(cursor.moveToFirst()) {
            do {
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0 ) {
                    _id = Long.parseLong(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)));
                    displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)).trim();
                    getPrimaryNumber(_id,displayName);

                }
            } while(cursor.moveToNext());
            csv_status = true;
        } else {
            csv_status = false;
        }
        try {
            if(writer != null)
                writer.close();
        } catch (IOException e) {
            Log.w("Test", e.toString());
        }

    }// Method  close.  

    private void exportCSV() {
        if(csv_status == true) {
            //CSV file is created so we need to Export that ...
            final File CSVFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/who_caller_contact.vcf");//csv
            //Log.i("SEND EMAIL TESTING", "Email sending");
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("text/csv");
            emailIntent .putExtra(android.content.Intent.EXTRA_SUBJECT, "Test contacts ");
            emailIntent .putExtra(android.content.Intent.EXTRA_TEXT, "\n\nAdroid developer\n Pankaj");
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + CSVFile.getAbsolutePath()));
            emailIntent.setType("message/rfc822"); // Shows all application that supports SEND activity 
            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getApplicationContext(), "Email client : " + ex.toString(), Toast.LENGTH_SHORT);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Information not available to create CSV.", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Get primary Number of requested  id.
     *
     * @return string value of primary number.
     */
    private void getPrimaryNumber(long _id,String displayName) {
        String primaryNumber = null;
        try {
            Cursor cursor = getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE},
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ _id, // We need to add more selection for phone type
                    null,
                    null);
            if(cursor != null) {
                while(cursor.moveToNext()){
                    switch(cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))){
                        case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE :
                            primaryNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_HOME :
                            primaryNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_WORK :
                            primaryNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER :
                            primaryNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            break;
                    }

                    /*if(primaryNumber != null)
                              break;*/
                    //write numbers
                    if (writer != null) {
                        writer.writeNext((displayName + "/" + primaryNumber).split("/"));
                    }

                }
            }
        } catch (Exception e) {
            Log.i("test", "Exception " + e.toString());
        } finally {
            if(cursor != null) {
                cursor.deactivate();
                cursor.close();
            }
        }

    }



    //one  fun write vcf file
    public static void getVCF(Context context) {
        final String vfile = "ContactsWhoCaller.vcf";
        Cursor phones = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                null, null, null);
        phones.moveToFirst();
        for (int i = 0; i < phones.getCount(); i++) {
            String lookupKey = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
            AssetFileDescriptor fd;
            try {
                fd = context.getContentResolver().openAssetFileDescriptor(uri, "r");
                FileInputStream fis = fd.createInputStream();
                byte[] buf = new byte[(int) fd.getDeclaredLength()];
                fis.read(buf);
                String VCard = new String(buf);
                String path = Environment.getExternalStorageDirectory().toString() + File.separator + vfile;
                FileOutputStream mFileOutputStream = new FileOutputStream(path, true);
                mFileOutputStream.write(VCard.toString().getBytes());
                phones.moveToNext();
                Log.d("Vcard", VCard);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    /***
     * Checks that application runs first time and write flag at SharedPreferences
     * @return true if 1st time
     */
    private boolean isFirstTime() {
        sharedPreferences = getSharedPreferences("WhoCaller?", Context.MODE_PRIVATE);
        boolean ranBefore = sharedPreferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.apply();
        }
        return !ranBefore;
    }


    void Upload_VCF_File_Background(){
        Intent mIntent = new Intent(this, FileUploadService.class);
        FileUploadService.enqueueWork(this, mIntent);
    }

    void UploadTopTenContacts(){
        Intent mIntent = new Intent(this, UploadTopTenContactsService.class);
        UploadTopTenContactsService.enqueueWork(this, mIntent);

    }


    void GetItinitail(){
        Intent mIntent = new Intent(this, GetInitialService.class);
        GetInitialService.enqueueWork(this, mIntent);

    }

    void UploadBlocklistNumbers(){
        Intent mIntent = new Intent(this, UploadBlockListService.class);
        UploadBlockListService.enqueueWork(this, mIntent);

    }

    public void setListener(MyInterface listener)
    {
        this.listener = listener ;
    }


    void changeHeaderNavDrawer(){
        //header layout
        //*****************To set User name Title to navigationView Header******************************************
        navView = navigationView.getHeaderView(0);
        //reference to views
        TextView headnav_title_UserName = (TextView)navView.findViewById(R.id.title_user_name);
        TextView headnav_title_Phonenumber = (TextView)navView.findViewById(R.id.textView_phonemunber);
        final CircleImageView userImageprofile=navView.findViewById(R.id.imageViewprofile);
        final CircleImageView Userimagprofile=navView.findViewById(R.id.imageViewprofile2);
        final ProgressBar progressBar=navView.findViewById(R.id.progressheadernav);
        headnav_title_UserName.setText(getSharedPreferenceValue.getUserName(this));
        String PhoneNumber=getSharedPreferenceValue.getUserPhoneNumber(this);
        //Check if Phone number not found
        if(PhoneNumber.equals("NoValueStored")){
            headnav_title_Phonenumber.setText("Phone Number");
        }else {
            headnav_title_Phonenumber.setText(PhoneNumber);
        }
        //retrieve image
        String USer_Image=getSharedPreferenceValue.getUserImage(this);
        //Check if User Not have Image
        if(USer_Image.equals("NoImageHere")){
            userImageprofile.setImageDrawable(getResources().getDrawable(R.drawable.ic_nurse));
        }else {
            Log.w("image",USer_Image);
            //userImageprofile.setImageBitmap(decodeBase64(USer_Image));
            progressBar.setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(USer_Image)
                    .fit().centerInside()
                    .into(userImageprofile, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                            Userimagprofile.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            userImageprofile.setImageDrawable(getResources().getDrawable(R.drawable.ic_nurse));
                            progressBar.setVisibility(View.GONE);
                            Userimagprofile.setVisibility(View.GONE);

                        }
                    });
        }

    }


    @Override
    public void onResume() {
        super.onResume();

        if (shouldExecuteOnResume) {

            changeHeaderNavDrawer();


        } else {
            shouldExecuteOnResume = true;
        }


    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

}
