package teckvillage.developer.khaled_pc.teckvillagetrue.Model;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import teckvillage.developer.khaled_pc.teckvillagetrue.Permission;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.database.Database_Helper;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.Send_Top_Ten_Contacts_JSON;

/**
 * Created by khaled-pc on 2/20/2019.
 */

public class Get_Calls_Log {
    private static final int REQUESTCODE = 3;
    Context context;
    Permission permission;
    ArrayList<LogInfo> loglist;
    public String contactsName = "";
    public  int TypeOfNumph;

    ListView list_view2;
    ArrayList<Integer> listdata2;
    ArrayList<Integer> key;
    ArrayList<Integer> value;
    int maxIndex = 0;
    Integer val;

    HashMap<String, Integer> map = new HashMap<String, Integer>();


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
        String lastnumber = "0";
        int numofcall = 0;
        //StringBuffer sb = new StringBuffer();
        String[] projection = {
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.CACHED_NUMBER_TYPE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION,
                CallLog.Calls._ID,
        };

        @SuppressLint("MissingPermission") Cursor managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, CallLog.Calls.DATE + " DESC");
        int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int mobileType = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NUMBER_TYPE);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        //sb.append("Call Details :");

        String phName, phNumber, callDate, callDuration, dateStringhour;
        String dir = null;
        String typephone = null;
        int phmobileType, callType;
        Date callDayTime;
        long log_call_id;

        //Date Format  "dd-MM-yyyy h:mm a"
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a", Locale.ENGLISH);

        while (managedCursor.moveToNext()) {
            log_call_id = managedCursor.getLong(managedCursor.getColumnIndex(CallLog.Calls._ID));
            phName = managedCursor.getString(name);
            phmobileType = managedCursor.getInt(mobileType);
            phNumber = managedCursor.getString(number);
            callType = managedCursor.getInt(type);
            callDate = managedCursor.getString(date);
            callDayTime = new Date(Long.valueOf(callDate));
            //callDuration = managedCursor.getString(duration);
            dateStringhour = formatter.format(new Date(Long.parseLong(callDate)));


            switch (callType) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;

                case CallLog.Calls.REJECTED_TYPE:
                    dir = "REJECTED";
                    break;

                case CallLog.Calls.VOICEMAIL_TYPE:
                    dir = "VOICEMAIL";
                    break;

                case CallLog.Calls.BLOCKED_TYPE:
                    dir = "BLOCKED";
                    break;

                case CallLog.Calls.ANSWERED_EXTERNALLY_TYPE:
                    dir = "ANSWERED";
                    break;
            }


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
                case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                    typephone = "Work Fax";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                    typephone = "Home Fax";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN:
                    typephone = "Main";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
                    typephone = "Pager";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM:
                    typephone = "Custom";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                    typephone = "Other";
                    break;
                default:
                    break;
            }

           /* sb.append("\nName phone Number:--- " + phName + "\nPhone Number:--- " + phNumber + "\nPhone Number Type:--- " + typephone + " \nCall Type:--- "
                    + dir + " \nCall Date:--- " + dateStringhour
                    + " \nCall duration in sec :--- " + callDuration);
            sb.append("\n----------------------------------");  */

            //if cash name is null return name from Contacts
            if (phName == null || phName.equals("")) {
                if (phNumber != null) {

                    if (phNumber.equals("")) {

                        phName = "Unknown";

                    } else {

                         phName = phNumber;
                           /*
                        if (contactExists(phNumber)) {
                            //phName = getContactName(phNumber);
                            phName = contactsName;
                            switch (TypeOfNumph) {
                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                    typephone = "Mobile";
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                    typephone = "Home";
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                    typephone = "Work";
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                                    typephone = "Work Fax";
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                                    typephone = "Home Fax";
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN:
                                    typephone = "Main";
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
                                    typephone = "Pager";
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM:
                                    typephone = "Custom";
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                                    typephone = "Other";
                                    break;
                                default:
                                    typephone = "Other";
                                    break;

                            }

                        } else {
                            if (phNumber.equals("")) {
                                phName = "Unknown";

                            } else {
                                phName = phNumber;
                            }
                        }*/
                    }

                } else {
                    phName = "Unknown Number";
                }
            }

            loglist.add(new LogInfo(null, phName, dir, callDayTime, typephone, dateStringhour, phNumber,0,log_call_id));
        }

        managedCursor.close();
        return loglist;

    }


    public String getContactName(final String phoneNumber) {
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

    public  Bitmap retrieveContactPhoto(String number) {
        ContentResolver contentResolver = context.getContentResolver();
        String contactId = null;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};

        Cursor cursor = contentResolver.query(
                        uri,
                        projection,
                        null,
                        null,
                        null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
            }
            cursor.close();
        }

        Bitmap photo = null;
        if (!TextUtils.isEmpty(contactId))
        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactId)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
            }

            if (inputStream != null)
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return photo;
    }

    public  boolean contactExists(String number) {
        // number is the phone number
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] mPhoneNumberProjection = {ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME,ContactsContract.PhoneLookup.TYPE};
        Cursor cur = null;

        try {

            cur = context.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);

            if (cur.moveToFirst()) {

                contactsName = cur.getString(1);
                TypeOfNumph  =cur.getInt(2);

                return true;
            }

        } catch (Exception e) {
            return false;
        } finally {
            if (cur != null)
                cur.close();
        }
        return false;
    }




    @SuppressLint("LongLogTag")
    public List<ContactInfo> getTopTenContacts() {

        List<ContactInfo> contactInfos=new ArrayList<>();

        String[] projection = {
                CallLog.Calls.NUMBER,
        };

        //String selectedQuery=CallLog.Calls._ID + " in (SELECT " + CallLog.Calls._ID + " FROM calls WHERE type != " + CallLog.Calls.VOICEMAIL_TYPE + " GROUP BY " + CallLog.Calls.NUMBER+")";

        @SuppressLint("MissingPermission") Cursor managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, CallLog.Calls.TYPE + "=" + CallLog.Calls.OUTGOING_TYPE, null, CallLog.Calls.DATE + " DESC ");

        if (managedCursor != null &&managedCursor.getCount()>0) {

            int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);

            String phName, typephone;
            int phmobileType;
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

            while (managedCursor.moveToNext()) {
                //phName = managedCursor.getString(name);
                //phmobileType = managedCursor.getInt(mobileType);
                String phNumber = managedCursor.getString(number);


                if (phNumber.startsWith("+2")) {
                    phNumber = phNumber.substring(2);

                } else if (phNumber.startsWith("+")) {

                    try {
                        // phone must begin with '+'
                        Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phNumber, "");
                        phNumber = String.valueOf(numberProto.getNationalNumber());

                    } catch (NumberParseException e) {
                        System.err.println("NumberParseException was thrown: " + e.toString());
                    }
                }

                Log.e("Phone number", phNumber + "");


                if (map.get(phNumber) == null) {
                    map.put(phNumber + "", 1);
                } else {
                    Integer scount = map.get(phNumber).intValue();
                    scount++;
                    map.put(phNumber + "", scount);

                    //Log.e("map values", map + "");
                }
            }

            managedCursor.close();

            HashSet<String> numbersset = new HashSet<String>(map.keySet());

            ArrayList<String> numbers = new ArrayList<String>();
            ArrayList<Integer> counts = new ArrayList<Integer>();

            Iterator iterator = numbersset.iterator();

            while (iterator.hasNext()) {
                String no = iterator.next().toString();
                numbers.add(no);
                counts.add(map.get(no));
            }

            Log.e("number count ", numbers.size() + "|");
            Log.e("count count ", counts.size() + "|");

            //putting above data into hashmap
            HashMap<String, Integer> hm = new HashMap<String, Integer>();
            for (int i = 0; i < counts.size(); i++) {
                hm.put(numbers.get(i), counts.get(i));
            }

            //create list from elements of hashmaps
            List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(hm.entrySet());

            //sort the list
            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> stringIntegerEntry, Map.Entry<String, Integer> t1) {
                    return (t1.getValue()).compareTo(stringIntegerEntry.getValue());
                }
            });

            ArrayList<String> phonenumbers = new ArrayList<String>();

            HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
            for (Map.Entry<String, Integer> aa : list) {
                temp.put(aa.getKey(), aa.getValue());
                Log.e("descending", aa.getValue() + "");
                Log.e("descending", aa.getKey() + "");
                phonenumbers.add(aa.getKey());

            }

            //String temp2 = String.valueOf((list.subList(0, 10)));
            //Log.e("final",temp2+"");


            for (int i = 0; i < 10; i++) {

                contactExists(phonenumbers.get(i));


                switch (TypeOfNumph) {
                    case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                        typephone = "Mobile";
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                        typephone = "Home";
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                        typephone = "Work";
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                        typephone = "Work Fax";
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                        typephone = "Home Fax";
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN:
                        typephone = "Main";
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
                        typephone = "Pager";
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM:
                        typephone = "Custom";
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                        typephone = "Other";
                        break;
                    default:
                        typephone = "Other";
                        break;
                }

                if (contactsName == null) {
                    contactsName = phonenumbers.get(i);
                    typephone = "Other";
                }

                contactInfos.add(new ContactInfo(null, contactsName, typephone, phonenumbers.get(i)));
                contactsName = null;
            }

        }

       return  contactInfos;
    }


    public ArrayList<LogInfo> getMissed() {
        loglist = new ArrayList<>();

        String[] projection = {
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.CACHED_NUMBER_TYPE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE,
                CallLog.Calls._ID
        };

        @SuppressLint("MissingPermission") Cursor managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE, null, CallLog.Calls.DATE + " DESC");

        if (managedCursor != null &&managedCursor.getCount()>0) {

            int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
            int mobileType = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NUMBER_TYPE);
            int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
            int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);

            String phName, phNumber, callDate,  dateStringhour;
            String dir = null;
            String typephone = null;
            int phmobileType, callType;
            Date callDayTime;
            long log_call_id;

            //Date Format  "dd-MM-yyyy h:mm a"
            SimpleDateFormat formatter = new SimpleDateFormat("h:mm a", Locale.ENGLISH);

            while (managedCursor.moveToNext()) {
                log_call_id = managedCursor.getLong(managedCursor.getColumnIndex(CallLog.Calls._ID));
                phName = managedCursor.getString(name);
                phmobileType = managedCursor.getInt(mobileType);
                phNumber = managedCursor.getString(number);
                callType = managedCursor.getInt(type);
                callDate = managedCursor.getString(date);
                callDayTime = new Date(Long.valueOf(callDate));
                dateStringhour = formatter.format(new Date(Long.parseLong(callDate)));


                switch (callType) {
                    case CallLog.Calls.MISSED_TYPE:
                        dir = "MISSED";
                        break;
                }


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
                    case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                        typephone = "Work Fax";
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                        typephone = "Home Fax";
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN:
                        typephone = "Main";
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
                        typephone = "Pager";
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM:
                        typephone = "Custom";
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                        typephone = "Other";
                        break;
                    default:
                        break;
                }

                //if cash name is null return name from Contacts
                if (phName == null || phName.equals("")) {
                    if (phNumber != null) {

                        if (phNumber.equals("")) {

                            phName = "Unknown";

                        } else {

                            phName = phNumber;
                        }

                    } else {
                        phName = "Unknown Number";
                    }
                }
                loglist.add(new LogInfo(null, phName, dir, callDayTime, typephone, dateStringhour, phNumber, 1,log_call_id));
            }
        }

        if (managedCursor != null) {
            managedCursor.close();
        }
        return loglist;

    }

   static public  LogInfo getCalllogByID(Context context,long LogCallID) {

        LogInfo logInfo=new LogInfo();

        String[] projection = {
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.CACHED_NUMBER_TYPE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
        };

        @SuppressLint("MissingPermission") Cursor managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, CallLog.Calls._ID + "=" + LogCallID, null, null);

        String phName, phNumber, callDate,  dateStringhour;
        String typephone = null;
        int phmobileType;
        Date callDayTime;

        //Date Format  "dd-MM-yyyy h:mm a"
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a", Locale.ENGLISH);


        if((managedCursor == null|| managedCursor.getCount() <=0) ) {

            Log.e("GOPAL", "Empty Cursor");
            Database_Helper db=new Database_Helper(context);
            if(db.deleteLogCallIDFromHistory(LogCallID)){
                Log.w("Rowhistory","Deleted");
            }else {
                Log.w("Rowhistory","NotDeleted");
            }
            return null;

        } else {
                managedCursor.moveToFirst();
            do {

                    phName = managedCursor.getString(managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    phmobileType = managedCursor.getInt(managedCursor.getColumnIndex(CallLog.Calls.CACHED_NUMBER_TYPE));
                    phNumber = managedCursor.getString(managedCursor.getColumnIndex(CallLog.Calls.NUMBER));
                    callDate = managedCursor.getString(managedCursor.getColumnIndex(CallLog.Calls.DATE));
                    callDayTime = new Date(Long.valueOf(callDate));


                    // do what ever you want here

                    //callDuration = managedCursor.getString(duration);
                    dateStringhour = formatter.format(new Date(Long.parseLong(callDate)));


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
                        case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                            typephone = "Work Fax";
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                            typephone = "Home Fax";
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN:
                            typephone = "Main";
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
                            typephone = "Pager";
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM:
                            typephone = "Other";//Custom
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                            typephone = "Other";
                            break;
                        default:
                            break;
                    }

                    if (phName == null || phName.equals("")) {
                        if (phNumber != null) {

                            if (phNumber.equals("")) {

                                phName = "Unknown";

                            } else {

                                phName = phNumber;
                            }

                        } else {
                            phName = "Unknown Number";
                        }
                    }


                    logInfo.setImageUrl(null);
                    logInfo.setLogName(phName);
                    logInfo.setLogIcon("BLOCKED");
                    logInfo.setLogDate(callDayTime);
                    logInfo.setNumberType(typephone);
                    logInfo.setHour(dateStringhour);
                    logInfo.setNumber(phNumber);
                    logInfo.setNumberofcall(1);

                }while (managedCursor.moveToNext());
            }




        return logInfo;
    }


    @SuppressLint("LongLogTag")
    public ArrayList<Send_Top_Ten_Contacts_JSON> getTopTenContactsToServer() {

        ArrayList<Send_Top_Ten_Contacts_JSON> contactInfosserver=new ArrayList<>();

        String[] projection = {
                CallLog.Calls.NUMBER,
        };


        @SuppressLint("MissingPermission") Cursor managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, CallLog.Calls.TYPE + "=" + CallLog.Calls.OUTGOING_TYPE, null, CallLog.Calls.DATE + " DESC ");


        if (managedCursor != null &&managedCursor.getCount()>0) {

            int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);

            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

            while (managedCursor.moveToNext()) {

                String phNumber = managedCursor.getString(number);


                if (phNumber.startsWith("+2")) {
                    phNumber = phNumber.substring(2);

                } else if (phNumber.startsWith("+")) {

                    try {
                        // phone must begin with '+'
                        Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phNumber, "");
                        phNumber = String.valueOf(numberProto.getNationalNumber());

                    } catch (NumberParseException e) {
                        System.err.println("NumberParseException was thrown: " + e.toString());
                    }
                }

                Log.e("Phone number", phNumber + "");


                if (map.get(phNumber) == null) {
                    map.put(phNumber + "", 1);
                } else {
                    Integer scount = map.get(phNumber).intValue();
                    scount++;
                    map.put(phNumber + "", scount);

                    //Log.e("map values", map + "");
                }
            }

            managedCursor.close();

            HashSet<String> numbersset = new HashSet<String>(map.keySet());

            ArrayList<String> numbers = new ArrayList<String>();
            ArrayList<Integer> counts = new ArrayList<Integer>();

            Iterator iterator = numbersset.iterator();

            while (iterator.hasNext()) {
                String no = iterator.next().toString();
                numbers.add(no);
                counts.add(map.get(no));
            }

            Log.e("number count ", numbers.size() + "|");
            Log.e("count count ", counts.size() + "|");

            //putting above data into hashmap
            HashMap<String, Integer> hm = new HashMap<String, Integer>();
            for (int i = 0; i < counts.size(); i++) {
                hm.put(numbers.get(i), counts.get(i));
            }

            //create list from elements of hashmaps
            List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(hm.entrySet());

            //sort the list
            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> stringIntegerEntry, Map.Entry<String, Integer> t1) {
                    return (t1.getValue()).compareTo(stringIntegerEntry.getValue());
                }
            });

            ArrayList<String> phonenumbers = new ArrayList<String>();

            HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
            for (Map.Entry<String, Integer> aa : list) {
                temp.put(aa.getKey(), aa.getValue());
                Log.e("descending", aa.getValue() + "");
                Log.e("descending", aa.getKey() + "");
                phonenumbers.add(aa.getKey());

            }

            //String temp2 = String.valueOf((list.subList(0, 10)));
            //Log.e("final",temp2+"");


            for (int i = 0; i < 10; i++) {

                contactExists(phonenumbers.get(i));

                if (contactsName == null) {
                    contactsName = phonenumbers.get(i);

                }

                contactInfosserver.add(new Send_Top_Ten_Contacts_JSON( contactsName, phonenumbers.get(i)));
                contactsName = null;
            }

        }

        return  contactInfosserver;
    }

  }
