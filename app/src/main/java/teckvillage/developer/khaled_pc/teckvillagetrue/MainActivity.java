package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.telecom.TelecomManager;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

import teckvillage.developer.khaled_pc.teckvillagetrue.Camera_Recognition_package.Camera_Recognition;

import teckvillage.developer.khaled_pc.teckvillagetrue.View.BlockList;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.ChatFragment;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG ="fields" ;
    private static final int STATIC_INTEGER_VALUE =221 ;
    public static FragmentManager fragmentManager;
    FrameLayout mainframeLayout,messageframeLayout,contactframeLayout,chatframeLayout;
    private static final String TAG_ANDROID_CONTACTS = "ANDROID_CONTACTS";
    String Email ="work20188888@gmail.com";
    private static final int MY_CAMERA_REQUEST_CODE = 109;
    private static final String PUBLIC_STATIC_STRING_IDENTIFIER = "com.techvillage";

    public BottomNavigationView navigationView2;
    ArrayList<String> PhoneNumberListCameraRecognition = new ArrayList<>();


    boolean SMSNotification=false;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER);
        intent.putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
        startActivity(intent);

        
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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

            startActivity(new Intent(this,FAQ.class));

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

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(row);

        ColorDrawable transparent = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(transparent, 5);//margin for Dilaog
        dialog.getWindow().setBackgroundDrawable(inset);
        dialog.show();

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
                dialog.dismiss();
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

                dialog.dismiss();
                Intent i = new Intent(MainActivity.this,Camera_Recognition.class);
                startActivityForResult(i, STATIC_INTEGER_VALUE);

            }
        });
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

}
