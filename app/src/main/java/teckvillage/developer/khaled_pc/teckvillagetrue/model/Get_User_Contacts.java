package teckvillage.developer.khaled_pc.teckvillagetrue.model;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;


import java.util.ArrayList;

import teckvillage.developer.khaled_pc.teckvillagetrue.Permission;

/**
 * Created by khaled-pc on 2/18/2019.
 */

public class Get_User_Contacts {
    private static final int REQUESTCODE =1 ;
    Context context;
    ArrayList<UserContactData> contactInfos;
    Permission permission;

    private static final String TAG ="fields" ;

    public Get_User_Contacts(Context context) {
        this.context=context;
        permission=new Permission(context);
    }

    public  boolean CheckPermission(){
        boolean check;

        //read contacts
        if(!permission.hasPhoneContactsPermission(Manifest.permission.READ_CONTACTS))
        {
            check=false;
            permission.requestPermission(Manifest.permission.READ_CONTACTS,REQUESTCODE);


        }else {
            check=true;
            //Toast.makeText(context, "Contact data has been printed in the android monitor log..", Toast.LENGTH_SHORT).show();
        }
        return check;
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

    public ArrayList<UserContactData> getContactListFormessage() {
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

                    while (pCur.moveToNext()) {
                        int phoneType = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        phoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        contactInfos.add(new UserContactData("",name,"PortSaid,Egypt",id,phoneNumber));


                    }
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
