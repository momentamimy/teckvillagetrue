package teckvillage.developer.khaled_pc.teckvillagetrue.model;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import teckvillage.developer.khaled_pc.teckvillagetrue.Permission;

/**
 * Created by khaled-pc on 2/20/2019.
 */

public class Get_Calls_Log {
    private static final int REQUESTCODE = 3;
    Context context;
    Permission permission;
    ArrayList<LogInfo> loglist;


    public Get_Calls_Log(Context context) {
        this.context = context;
        permission = new Permission(context);
    }


    public boolean CheckPermission() {
        boolean check;

        //read contacts
        if (!permission.hasPhoneContactsPermission(Manifest.permission.READ_CALL_LOG)) {
            check = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                permission.requestPermission(Manifest.permission.READ_CALL_LOG, REQUESTCODE);
            }

        } else {
            check = true;
            //Toast.makeText(context, "Contact data has been printed in the android monitor log..", Toast.LENGTH_SHORT).show();
        }
        return check;
    }


    public ArrayList<LogInfo> getCallDetails() {
        loglist = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        @SuppressLint("MissingPermission") Cursor managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");
        int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int mobileType = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NUMBER_TYPE);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        sb.append("Call Details :");
        while (managedCursor.moveToNext()) {
            String phName = managedCursor.getString(name);
            int phmobileType = managedCursor.getInt(mobileType);
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            //Date Format  "dd-MM-yyyy h:mm a"
            SimpleDateFormat formatter = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
            String dateStringhour = formatter.format(new Date(Long.parseLong(callDate)));

            String dir = null;
            int dircode = Integer.parseInt(callType);

            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
                case 5:
                    dir = "Other";
                    break;
            }

            String typephone = null;
            switch (phmobileType) {
                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                    typephone = "Mobile";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                    typephone = "Home";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                    typephone = "Work";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                    typephone = "Other";
                    break;
                default:
                    break;
            }

            sb.append("\nName phone Number:--- " + phName + "\nPhone Number:--- " + phNumber + "\nPhone Number Type:--- " + typephone + " \nCall Type:--- "
                    + dir + " \nCall Date:--- " + dateStringhour
                    + " \nCall duration in sec :--- " + callDuration);
            sb.append("\n----------------------------------");

            //if cash name is null return name from Contacts
            if (phName == null) {
                if (phNumber != null) {
                    if(contactExists(phNumber)){
                        phName = getContactName(phNumber);
                    }else {
                        if(phNumber.equals("")){
                            phName = "Unknown";
                        }else {
                            phName = phNumber;
                        }

                    }

                } else {
                    phName = "Unknown Number";
                }
            }

            loglist.add(new LogInfo(null, phName, dir, callDayTime, typephone, dateStringhour, phNumber));
        }
        managedCursor.close();
        return loglist;

    }


    String getContactName(final String phoneNumber) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName = "";
        try {


            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    contactName = cursor.getString(0);
                }
                cursor.close();
            }





        } catch (Exception E) {
            Log.w("Error", E.getMessage());
        }
        return contactName;

    }

     boolean contactExists(String number) {
        // number is the phone number
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] mPhoneNumberProjection = {  ContactsContract.PhoneLookup.NUMBER };
        Cursor cur = null;
        try {

         cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);

            if (cur.moveToFirst()) {
                return true;
            }
        }catch (Exception e) {

            return false;
        }finally {
            if (cur != null)
                cur.close();
        }
        return false;
    }


}
