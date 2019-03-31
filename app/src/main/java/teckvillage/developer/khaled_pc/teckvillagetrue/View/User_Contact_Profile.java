package teckvillage.developer.khaled_pc.teckvillagetrue.View;

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;

public class User_Contact_Profile extends AppCompatActivity {

    TextView nameofcontact,tag,phonenumee;
    CircleImageView profile_pic;
    LinearLayout makecall,sendmessage,makeblock;
    View backarrow,moreoption;
    long id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__contact__profile);

        nameofcontact=findViewById(R.id.name);
        profile_pic=findViewById(R.id.profilepic);
        tag=findViewById(R.id.tag);
        makecall=findViewById(R.id.makecall);
        sendmessage=findViewById(R.id.sendmessage);
        makeblock=findViewById(R.id.makeblock);
        backarrow=findViewById(R.id.backarrow);
        moreoption=findViewById(R.id.moreoptionusercontact);
        phonenumee=findViewById(R.id.contactuserphonenumber);

        id=getIntent().getLongExtra("ContactID",0);

        if(id==0){
            Toast.makeText(this, "Error Loading", Toast.LENGTH_LONG).show();
        }else {

            getContactDetails(id);

        }


        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
