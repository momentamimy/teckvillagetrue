package teckvillage.developer.khaled_pc.teckvillagetrue.model;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import java.util.ArrayList;

import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.AlphabetItem;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by khaled-pc on 2/18/2019.
 */

public class Get_User_Contacts implements  ActivityCompat.OnRequestPermissionsResultCallback{
    Context context;
    ArrayList<UserContactData> contactInfos;

    private static final String TAG ="fields" ;

    public Get_User_Contacts(Context context) {
        this.context=context;

    }

    public  void Checkandgetcontactlist(){
        //read contacts
        if(!hasPhoneContactsPermission(Manifest.permission.READ_CONTACTS))
        {
            requestPermission(Manifest.permission.READ_CONTACTS);
        }else {
            getContactList();
            //doSomethingForEachUniquePhoneNumber(this);
            Toast.makeText(context, "Contact data has been printed in the android monitor log..", Toast.LENGTH_SHORT).show();
        }
    }


    // Request a runtime permission to app user.
    private void requestPermission(String permission)
    {
        String requestPermissionArray[] = {permission};
        ActivityCompat.requestPermissions((Activity) context, requestPermissionArray, 1);
    }


    // Check whether user has phone contacts manipulation permission or not.
    private boolean hasPhoneContactsPermission(String permission)
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

    /*
    // After user select Allow or Deny button in request runtime permission dialog
    // , this method will be invoked.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int length = grantResults.length;
        if(length > 0)
        {
            int grantResult = grantResults[0];

            if(grantResult == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getApplicationContext(), "You allowed permission, please click the button again.", Toast.LENGTH_LONG).show();
            }else
            {
                Toast.makeText(getApplicationContext(), "You denied permission.", Toast.LENGTH_LONG).show();
            }
        }
    }
    */

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        int length = grantResults.length;
        if(length > 0)
        {
            int grantResult = grantResults[0];

            if(grantResult == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getApplicationContext(), "You allowed permission, please click the button again.", Toast.LENGTH_LONG).show();
            }else
            {
                Toast.makeText(getApplicationContext(), "You denied permission.", Toast.LENGTH_LONG).show();
            }
        }
    }


     public ArrayList<UserContactData> getContactList() {
        contactInfos = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC ");
        String lastnumber = "0";

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String phoneNumber = null;
                int id = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ?",
                            new String[]{name}, null);

                    Log.i(TAG, "Name: " + name);

                    contactInfos.add(new UserContactData("",name,"PortSaid,Egypt",id));
                    Log.w("feen", String.valueOf(contactInfos.size()));


                    while (pCur.moveToNext()) {
                        int phoneType = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        phoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        if (phoneNumber.contains(lastnumber))
                        {

                        }
                        else {
                            phoneNumber = phoneNumber.replaceAll("\\s", "");
                            lastnumber = phoneNumber;

                            switch (phoneType) {
                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                    Log.i(TAG, "Mobile Number: " + phoneNumber);
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                    Log.i(TAG, "Home Number: " + phoneNumber);
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                    Log.i(TAG, "Work Number: " + phoneNumber);
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                                    Log.i(TAG, "Other Number: " + phoneNumber);
                                    break;
                                default:
                                    break;
                            }
                        }
                        //Log.i(TAG, "Phone Number: " + phoneNumber);
                    }
                    Log.i(TAG, "------------------------------------------------------------------------------------------");
                    pCur.close();
                }
            }

        }

        if(cur!=null){
            cur.close();
        }

        return contactInfos;
    }

}
