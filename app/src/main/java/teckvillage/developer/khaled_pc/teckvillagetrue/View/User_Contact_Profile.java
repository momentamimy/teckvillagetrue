package teckvillage.developer.khaled_pc.teckvillagetrue.View;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import teckvillage.developer.khaled_pc.teckvillagetrue.Make_Phone_Call;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.SMS_MessagesChat;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.database.Database_Helper;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.database.tables.block;

public class User_Contact_Profile extends AppCompatActivity {

    TextView nameofcontact,tag,phonenumee;
    CircleImageView profile_pic;
    LinearLayout makecall,sendmessage,makeblock;
    View backarrow,moreoption;
    long id;
    Database_Helper db;
    ImageView blockimagemake;
    TextView blockred;
    List<block> BlockInfo;
    boolean Bloceduser=false;


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
        blockimagemake=findViewById(R.id.blockimagered);
        blockred=findViewById(R.id.blocktextred);
        db=new Database_Helper(this);

        BlockInfo=new ArrayList<block>();


        id=getIntent().getLongExtra("ContactID",0);

        if(id==0){
            Toast.makeText(this, "Error Loading", Toast.LENGTH_LONG).show();
        }else {

            getContactDetails(id);

        }


        BlockInfo=db.getAllBlocklist();

        for(int i=0;i < BlockInfo.size();i++){

            if(phonenumee.getText().toString().equalsIgnoreCase(BlockInfo.get(i).getNumber())){
                blockimagemake.setImageResource(R.drawable.ic_rejected_call);
                blockred.setTextColor(Color.parseColor("#f53131"));
                Bloceduser=true;
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

                if(phonenumee.getText().toString()!=null&&phonenumee.getText().toString().length()>0){

                    if (ContextCompat.checkSelfPermission(User_Contact_Profile.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(User_Contact_Profile.this, new String[]{Manifest.permission.CALL_PHONE},12);
                    }
                    else
                    {
                        Make_Phone_Call.makephonecall(User_Contact_Profile.this,phonenumee.getText().toString());
                    }
                }



            }
        });


        sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_Contact_Profile.this,SMS_MessagesChat.class);
                intent.putExtra("LogSMSName",nameofcontact.getText().toString());
                String num=phonenumee.getText().toString();
                if(num != null&& !num.isEmpty()){
                    intent.putExtra("LogSMSAddress",num);
                    startActivity(intent);
                }else {
                    Toast.makeText(User_Contact_Profile.this, "Can't send message to this number", Toast.LENGTH_LONG).show();
                }
            }
        });


        makeblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(Bloceduser){

                        Dialog_Ensure_Delete_From_Block();

                    }else {

                        Dialog_Block(nameofcontact.getText().toString());
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


    void Dialog_Ensure_Delete_From_Block(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(User_Contact_Profile.this);
        builder.setMessage("Are you sure you want to unblock this number?")
                .setCancelable(false)
                .setPositiveButton("UNBLOCK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Delete_Number_From_BlockListhere();

                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }

    private void Delete_Number_From_BlockListhere() {
        db.deleteBlockByID((int) id);
        blockimagemake.setImageResource(R.drawable.ic_block_primary_24dp);
        blockred.setTextColor(Color.parseColor("#0089c0"));
        Bloceduser=false;
        Toast.makeText(User_Contact_Profile.this, "Number removed from your block list", Toast.LENGTH_SHORT).show();

    }

    void Insert_Number_To_Blocklist(){

        String num=phonenumee.getText().toString();
        String name=nameofcontact.getText().toString();
        if(num != null&& !num.isEmpty()){
            db.insertBlock(name,num,"Person");
            blockimagemake.setImageResource(R.drawable.ic_rejected_call);
            blockred.setTextColor(Color.parseColor("#f53131"));
            Bloceduser=true;
        }

    }


    void Dialog_Block(String Contactname){

        final AlertDialog.Builder builder = new AlertDialog.Builder(User_Contact_Profile.this);
        builder.setMessage("Are you sure you want to block "+Contactname+"?")
                .setCancelable(false)
                .setPositiveButton("BLOCK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Insert_Number_To_Blocklist();

                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

}
