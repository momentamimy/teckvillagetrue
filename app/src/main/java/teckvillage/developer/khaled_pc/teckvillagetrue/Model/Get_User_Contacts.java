package teckvillage.developer.khaled_pc.teckvillagetrue.Model;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                Long id = cur.getLong(cur.getColumnIndex(ContactsContract.Contacts._ID));
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
                            phoneNumber = phoneNumber.replace("-","");
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
        List<String> names=new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        String[] projection=new String[] { ContactsContract.Contacts._ID, ContactsContract.Contacts.HAS_PHONE_NUMBER, ContactsContract.Contacts.DISPLAY_NAME};
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC ");
        String lastnumber = "0";

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String phoneNumber = null;
                long id = cur.getLong(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                names.add(name);
                String[] projection2=new String[] {ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER,ContactsContract.CommonDataKinds.Phone.NUMBER};
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            projection2,
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ?",
                            new String[]{name}, null);

                    Log.i(TAG, "Name: " + name);

                    while (pCur.moveToNext()) {
                        //int phoneType = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
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
    public String getContactName(final String phoneNumber, Context context)
    {
        Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName="";
        Cursor cursor=context.getContentResolver().query(uri,projection,null,null,null);

        if (cursor != null) {
            if(cursor.moveToFirst()) {
                contactName=cursor.getString(0);
            }
            cursor.close();
        }

        return contactName;
    }

    public String getPhoneNumber(String name, Context context) {
        String ret = null;
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'%" + name +"%'";
        String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor c = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, selection, null, null);
        if (c.moveToFirst()) {
            ret = c.getString(0);
        }
        c.close();
        if(ret==null)
            ret = "Unsaved";
        return ret;
    }

    //read all contacts fast
    public ArrayList<UserContactData> getContactListFaster() {
        String lastnumber = "0";
        Map<Long, List<String>> phones = new HashMap<>();
        contactInfos = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();

        // First build a mapping: contact-id > list of phones
        Cursor cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] { ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.NUMBER }, null, null, null);
        while (cur != null && cur.moveToNext()) {
            long contactId = cur.getLong(0);
            String phone = cur.getString(1);

            if (phone.contains(lastnumber))
            {
              //repeated num
            }
            else {
                List list;
                if (phones.containsKey(contactId)) {
                    list = phones.get(contactId);
                } else {
                    list = new ArrayList<String>();
                    phones.put(contactId, list);
                }
                phone = phone.replaceAll("\\s", "");
                phone = phone.replace("-", "");
                lastnumber = phone;
                list.add(phone);
            }

        }
        cur.close();

         // Next query for all contacts, and use the phones mapping
        cur = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[] { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME ,ContactsContract.Contacts.HAS_PHONE_NUMBER}, null, null, null);

        while (cur != null && cur.moveToNext()) {

            if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {

                    long id = cur.getLong(0);
                    String name = cur.getString(1);
                    List<String> contactPhones = phones.get(id);
                    // addContact(id, name, contactPhones);
                    contactInfos.add(new UserContactData("", name, "PortSaid,Egypt", id, contactPhones));

            }
        }

        return contactInfos;
    }


    public ArrayList<UserContactData> getContactListContactsRecycleview() {
        contactInfos = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();

        // Next query for all contacts, and use the phones mapping
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[] { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME ,ContactsContract.Contacts.HAS_PHONE_NUMBER}, null, null, null);

        while (cur != null && cur.moveToNext()) {

            if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0 ) {

                long id = cur.getLong(0);
                String name = cur.getString(1);
                if(!name.equals("")){
                    contactInfos.add(new UserContactData("", name, "PortSaid,Egypt", id));
                }
            }
        }


        if(cur!=null){
            cur.close();
        }

        return contactInfos;
    }




}
