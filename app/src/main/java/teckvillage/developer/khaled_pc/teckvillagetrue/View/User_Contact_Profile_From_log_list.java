package teckvillage.developer.khaled_pc.teckvillagetrue.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.SMS_MessagesChat;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.Get_Calls_Log;

public class User_Contact_Profile_From_log_list extends AppCompatActivity {

    TextView nameofcontact,tag,phonenumee;
    CircleImageView profile_pic;
    LinearLayout makecall,sendmessage,makeblock;
    View backarrow,moreoption;
    String number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__contact__profile);

        Get_Calls_Log get_calls_log=new Get_Calls_Log(this);
        nameofcontact=findViewById(R.id.name);
        profile_pic=findViewById(R.id.profilepic);
        tag=findViewById(R.id.tag);
        makecall=findViewById(R.id.makecall);
        sendmessage=findViewById(R.id.sendmessage);
        makeblock=findViewById(R.id.makeblock);
        backarrow=findViewById(R.id.backarrow);
        moreoption=findViewById(R.id.moreoptionusercontact);
        phonenumee=findViewById(R.id.contactuserphonenumber);

        number=getIntent().getStringExtra("ContactNUm");

        if(number==null){
            Toast.makeText(this, "Error Loading", Toast.LENGTH_LONG).show();
        }else {

            //getContactDetails(id);
            if(get_calls_log.contactExists(number)){
                nameofcontact.setText(get_calls_log.contactsName);
                phonenumee.setText(number);
            }else {
                nameofcontact.setText(number);
                phonenumee.setText(number);
            }



        }


        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        makecall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                if(phonenumee.getText().toString()!=null&&phonenumee.getText().toString().length()>0){
                    intent.setData(Uri.parse("tel:" + phonenumee.getText().toString()));
                    if (ActivityCompat.checkSelfPermission(User_Contact_Profile_From_log_list.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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


            }
        });


        sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_Contact_Profile_From_log_list.this,SMS_MessagesChat.class);
                intent.putExtra("LogSMSName",nameofcontact.getText().toString());
                String num=phonenumee.getText().toString();
                if(num != null&& !num.isEmpty()){
                    intent.putExtra("LogSMSAddress",num);
                    startActivity(intent);
                }else {
                    Toast.makeText(User_Contact_Profile_From_log_list.this, "Can't send message to this number", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void getContactDetails(long contactId) {
        String phoneNumber=null;
        String name=null;
        String avatarUri=null;
        Log.d("Details", "---");
        Log.d("Details", "Contact : " + contactId);
        final Cursor phoneCursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] {
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                },
                ContactsContract.Data.CONTACT_ID + "=?",
                new String[] {String.valueOf(contactId)}, null);

        try {
            final int idxAvatarUri = phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.PHOTO_URI);
            final int idxName = phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            final int idxPhone = phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);


            while (phoneCursor.moveToNext()) {
                phoneNumber = phoneCursor.getString(idxPhone);
                 name = phoneCursor.getString(idxName);
                 avatarUri = phoneCursor.getString(idxAvatarUri);

                Log.d("Details", "Phone number: " + phoneNumber);
                Log.d("Details", "Name: " + name);
                Log.d("Details", "Avatar URI: " + avatarUri);




            }
        } finally {
            phoneCursor.close();
        }

        nameofcontact.setText(name);
        phonenumee.setText(phoneNumber);
        if(avatarUri!=null&&!avatarUri.isEmpty()){
            profile_pic.setImageURI(Uri.parse(avatarUri));
        }



        final Cursor emailCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                new String[] {ContactsContract.CommonDataKinds.Email.ADDRESS,},
                ContactsContract.Data.CONTACT_ID + "=?",
                new String[] {String.valueOf(contactId)},
                null);

        try {
            final int idxAddress = emailCursor.getColumnIndexOrThrow(
                    ContactsContract.CommonDataKinds.Email.ADDRESS);
            while (emailCursor.moveToNext()) {
                String address = emailCursor.getString(idxAddress);
                Log.d("Details", "Email: " + address);
            }
        } finally {
            emailCursor.close();
        }

    }
}
