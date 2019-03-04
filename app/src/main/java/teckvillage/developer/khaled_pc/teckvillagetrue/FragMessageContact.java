package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.CustomListViewAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.Get_User_Contacts;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.MessageInfo;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.UserContactData;


public class FragMessageContact extends Fragment {

    Get_User_Contacts get_user_contacts;
    ArrayList<UserContactData> userContactData;

    private static FragMessageOthers inst;
    ArrayList<String> smsMessagesList = new ArrayList<String>();
    ArrayList<MessageInfo> messageInfos=new ArrayList<>();
    ListView smsListView;
    // ArrayAdapter arrayAdapter;
    CustomListViewAdapter customListViewAdapter;


    public FragMessageContact() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_message_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        get_user_contacts=new Get_User_Contacts(getActivity());
        userContactData=get_user_contacts.getContactListFormessage();

        smsListView = (ListView) view.findViewById(R.id.SMSList);

        //arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, smsMessagesList);
        customListViewAdapter=new CustomListViewAdapter(messageInfos,getActivity());
        smsListView.setAdapter(customListViewAdapter);

        // Add SMS Read Permision At Runtime
        // Todo : If Permission Is Not GRANTED
        if(ContextCompat.checkSelfPermission(getActivity().getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {

            // Todo : If Permission Granted Then Show SMS
            refreshSmsInbox();

        } else {
            // Todo : Then Set Permission
            final int REQUEST_CODE_ASK_PERMISSIONS = 123;
            ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    public void refreshSmsInbox() {
        ArrayList<MessageInfo> infos=new ArrayList<>();
        userContactData=get_user_contacts.getContactListFormessage();

        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/"), null, null, null, null);
        int indextype=smsInboxCursor.getColumnIndex("type");
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        int dateColumn = smsInboxCursor.getColumnIndex("date");

        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        customListViewAdapter.clear();
        //arrayAdapter.clear();
        do {
            if (!smsInboxCursor.getString(indextype).equals("5")) {


                Calendar calendar = Calendar.getInstance();
                Long timestamp = Long.parseLong(smsInboxCursor.getString(dateColumn));
                calendar.setTimeInMillis(timestamp);
                String stringDate = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(calendar.get(Calendar.MONTH) + 1) + "/" + String.valueOf(calendar.get(Calendar.YEAR));

                MessageInfo info = new MessageInfo(null, smsInboxCursor.getString(indexAddress), smsInboxCursor.getString(indexBody), stringDate, smsInboxCursor.getString(indexAddress));
                boolean Add = true;
                for (int i = 0; i < messageInfos.size(); i++) {
                    if (!TextUtils.isEmpty(messageInfos.get(i).logName))
                        if (messageInfos.get(i).logName.equals(smsInboxCursor.getString(indexAddress))) {
                            Add = false;
                        }
                }

                if (Add) {
                    messageInfos.add(info);
                }


                //  arrayAdapter.add(str);
            }
        } while (smsInboxCursor.moveToNext());
        for (int i = 0; i < messageInfos.size(); i++) {
            for (int j = 0; j < userContactData.size(); j++) {
                if (!TextUtils.isEmpty(messageInfos.get(i).logName))
                    if (messageInfos.get(i).logName.contains(userContactData.get(j).phoneNum)) {
                        messageInfos.get(i).setLogName(userContactData.get(j).usercontacName);
                        infos.add(messageInfos.get(i));
                    }

            }
        }
        messageInfos=infos;
        customListViewAdapter=new CustomListViewAdapter(messageInfos,getActivity());
        smsListView.setAdapter(customListViewAdapter);
        smsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getContext(),SMS_MessagesChat.class);
                intent.putExtra("LogSMSName",messageInfos.get(position).logName);
                intent.putExtra("LogSMSAddress",messageInfos.get(position).logAddress);
                startActivity(intent);
            }
        });
    }
}
