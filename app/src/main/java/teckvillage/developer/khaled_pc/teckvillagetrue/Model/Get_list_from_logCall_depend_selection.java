package teckvillage.developer.khaled_pc.teckvillagetrue.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by khaled-pc on 3/5/2019.
 */

public class Get_list_from_logCall_depend_selection {

    ArrayList<LogInfo> loglist;
    Context context;

    public Get_list_from_logCall_depend_selection(Context context) {
        this.context = context;
    }

    public ArrayList<LogInfo> get_Outgoing_list(String selection) {
        loglist = new ArrayList<>();


        String[] projection = {
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.CACHED_NUMBER_TYPE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE,
                CallLog.Calls._ID,
        };

        @SuppressLint("MissingPermission") Cursor managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, selection, null, CallLog.Calls.DATE + " DESC");
        int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int mobileType = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NUMBER_TYPE);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);


        String phName, phNumber, callDate, dateStringhour;
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
                    typephone = "Other";
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

            loglist.add(new LogInfo(null, phName, dir, callDayTime, typephone, dateStringhour, phNumber,1,log_call_id));
        }

        managedCursor.close();
        return loglist;

    }
}
