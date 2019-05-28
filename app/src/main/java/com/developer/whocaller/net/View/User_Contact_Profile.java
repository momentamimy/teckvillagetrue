package com.developer.whocaller.net.View;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.whocaller.net.Controller.LocaleHelper;
import com.developer.whocaller.net.Make_Phone_Call;
import com.developer.whocaller.net.Model.database.Database_Helper;
import com.developer.whocaller.net.Model.database.tables.Tags;
import com.developer.whocaller.net.Model.retrofit.ApiAccessToken;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.BodyNumberModel;
import com.developer.whocaller.net.R;
import com.developer.whocaller.net.SMS_MessagesChat;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.FetchedUserData;
import com.developer.whocaller.net.Model.retrofit.WhoCallerApi;
import com.developer.whocaller.net.Model.retrofit.retrofitHead;


public class User_Contact_Profile extends AppCompatActivity {

    TextView nameofcontact,tag,phonenumee;
    CircleImageView profile_pic;
    LinearLayout makecall,sendmessage,makeblock;
    View backarrow,moreoption;
    long id;
    ImageView blockimagemake;
    TextView blockred;

    boolean Bloceduser=false;
    boolean Userhasimg=false;
    TextView country,email,phonenumberfield;
    RelativeLayout countrylay,emaillay;
    ArrayList<String> alContactsPhoneNumbers = new ArrayList<String>();
    RelativeLayout child;
    TextView phone;
    RelativeLayout moreoptionusercontact,backarrowlay;
    Database_Helper db;

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
        backarrowlay=findViewById(R.id.contacnerbackarrow);
        moreoption=findViewById(R.id.moreoptionusercontact);
        phonenumee=findViewById(R.id.contactuserphonenumber);
        blockimagemake=findViewById(R.id.blockimagered);
        blockred=findViewById(R.id.blocktextred);
        country=findViewById(R.id.location);
        countrylay=findViewById(R.id.loactionlayout);
        email=findViewById(R.id.emailusercontact);
        emaillay=findViewById(R.id.emaillayoutcontanier);
        moreoptionusercontact=findViewById(R.id.contacnerMoreoption);

        db=new Database_Helper(this);



        id=getIntent().getLongExtra("ContactID",0);

        if(id==0){
            Toast.makeText(this, "Error Loading", Toast.LENGTH_LONG).show();
        }else {

            getContactDetails(id);

        }



        for(int i=0;i < alContactsPhoneNumbers.size();i++){

            if(db.CheckPhoneNumberInBlockList(alContactsPhoneNumbers.get(i))){
                blockimagemake.setImageResource(R.drawable.ic_rejected_call);
                blockred.setTextColor(Color.parseColor("#f53131"));
                Bloceduser=true;
                break;
            }


        }



        //Three Dots
        moreoptionusercontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu=new PopupMenu(User_Contact_Profile.this,moreoptionusercontact);
                popupMenu.getMenuInflater().inflate(R.menu.more_option_user_contact,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals(getString(R.string.remove_contact))){
                            DeleteContact(String.valueOf(id),nameofcontact.getText().toString());
                        }else  if(item.getTitle().equals(getString(R.string.share))){
                            shareContact(String.valueOf(id),nameofcontact.getText().toString());
                        }else  if(item.getTitle().equals(getString(R.string.copy_name))){
                            copyName(nameofcontact.getText().toString());
                        } else  if(item.getTitle().equals(getString(R.string.search_the_web))){
                            searchOnWeb(nameofcontact.getText().toString());
                        } else  if(item.getTitle().equals(getString(R.string.edit))){
                            EditContact(String.valueOf(id));
                        }else  if(item.getTitle().equals(getString(R.string.copy_contact))){
                            CopyContact(nameofcontact.getText().toString(),alContactsPhoneNumbers);
                        } else  if(item.getTitle().equals(getString(R.string.copy_number))){
                            CopyNumber(alContactsPhoneNumbers);
                        }

                        return false;
                    }
                });
                popupMenu.show();

            }
        });


        backarrowlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        makecall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(alContactsPhoneNumbers!=null&alContactsPhoneNumbers.size()>0){

                    if (ContextCompat.checkSelfPermission(User_Contact_Profile.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(User_Contact_Profile.this, new String[]{Manifest.permission.CALL_PHONE},12);
                    }
                    else
                    {
                        DisplayArraylistOfPhonenumberDialogForCall(alContactsPhoneNumbers, nameofcontact.getText().toString());

                    }
                }



            }
        });


        sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alContactsPhoneNumbers!=null&alContactsPhoneNumbers.size()>0){
                    DisplayArraylistOfPhonenumberDialogForMessage(alContactsPhoneNumbers,nameofcontact.getText().toString());
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

    private void CopyContact(String s, ArrayList<String> alContactsPhoneNumbers) {
        StringBuilder rBuilder = new StringBuilder();
        for(int i=0;i<alContactsPhoneNumbers.size();i++){
            rBuilder.append(alContactsPhoneNumbers.get(i)+",");
        }

        String text=s+","+rBuilder.toString();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, R.string.Copied_to_clipboard,Toast.LENGTH_SHORT).show();
    }

    private void CopyNumber( ArrayList<String> alContactsPhoneNumbers) {
        StringBuilder rBuilder = new StringBuilder();
        for(int i=0;i<alContactsPhoneNumbers.size();i++){
            rBuilder.append(alContactsPhoneNumbers.get(i)+",");
        }

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", rBuilder.toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this,R.string.Copied_to_clipboard,Toast.LENGTH_SHORT).show();
    }

    public void getContactDetails(long contactId) {
        String phoneNumber=null;
        String name=null;
        String avatarUri=null;
        Log.d("Details", "---");
        Log.d("Details", "Contact : " + contactId);
        final Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] {
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                        ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER},
                ContactsContract.Data.CONTACT_ID + "=?",
                new String[] {String.valueOf(contactId)}, null);

        try {
            final int idxAvatarUri = phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.PHOTO_URI);
            final int idxName = phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            final int idxPhone = phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);

            int type;
            while (phoneCursor.moveToNext()) {
                //Check Contact has Phone Numbers
                if(Integer.parseInt(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER))) > 0) {

                    type=phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                    phoneNumber = phoneCursor.getString(idxPhone);
                    phoneNumber = phoneNumber.replaceAll("\\s", "");
                    phoneNumber = phoneNumber.replace("-", "");

                    if (!alContactsPhoneNumbers.contains(phoneNumber))
                    {
                        //List Of Phone Numbers
                        alContactsPhoneNumbers.add(phoneNumber);
                        Log.d("Details", "Phone number: " + phoneNumber);

                        //add new TExtview
                        LinearLayoutCompat main_layout = (LinearLayoutCompat)findViewById(R.id.phonenumberparent);
                        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        child = (RelativeLayout) inflater.inflate(R.layout.phone_number_item, null);
                        phone=child.findViewById(R.id.contactuserphonenumber);
                        TextView phonetype=child.findViewById(R.id.typephone);
                        //set type
                        switch (type) {
                            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                phonetype.setText("Mobile");
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                phonetype.setText("Home");
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                phonetype.setText("Work");
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                                phonetype.setText("Other");
                                break;
                            default:
                                phonetype.setText("Other");
                                break;
                        }

                        phone.setText(phoneNumber);
                        main_layout.addView(child);

                    }


                }

                name = phoneCursor.getString(idxName);
                avatarUri = phoneCursor.getString(idxAvatarUri);


                Log.d("Details", "Name: " + name);
                Log.d("Details", "Avatar URI: " + avatarUri);


            }
        } finally {
            phoneCursor.close();
        }

        nameofcontact.setText(name);
        //phonenumee.setText(phoneNumber);
        if(avatarUri!=null&&!avatarUri.isEmpty()){
            profile_pic.setImageURI(Uri.parse(avatarUri));
            Userhasimg=true;
        }

        //request
        if(alContactsPhoneNumbers.size()>0){
            UpdateDataAfterRequest(alContactsPhoneNumbers.get(0));
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
        builder.setMessage(R.string.unblock_user_dialog)
                .setCancelable(false)
                .setPositiveButton(R.string.unblock_captail, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Delete_Number_From_BlockListhere();

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }

    private void Delete_Number_From_BlockListhere() {
        if(alContactsPhoneNumbers != null&&alContactsPhoneNumbers.size()>0){
            //remove all numbers belong to this contact from blocklist
            for(int i=0;i<alContactsPhoneNumbers.size();i++){
                 db.deleteBlockByNumber(alContactsPhoneNumbers.get(i));
            }

            blockimagemake.setImageResource(R.drawable.ic_block_primary_24dp);
            blockred.setTextColor(Color.parseColor("#0089c0"));
            Bloceduser=false;
            Toast.makeText(User_Contact_Profile.this, R.string.remove_number_toast, Toast.LENGTH_SHORT).show();

        }

    }

    void Insert_Number_To_Blocklist(){


        String name=nameofcontact.getText().toString();
        long inserted = 0;
        if(alContactsPhoneNumbers != null&&alContactsPhoneNumbers.size()>0){
            for(int i=0;i<alContactsPhoneNumbers.size();i++){
                inserted= db.insertBlock(name,alContactsPhoneNumbers.get(i),"personal");
            }
            if(inserted != -1){
                ChangePreVariableUploadBlockList();//upload block list in mainActivity
                blockimagemake.setImageResource(R.drawable.ic_rejected_call);
                blockred.setTextColor(Color.parseColor("#f53131"));
                Bloceduser=true;
                Toast.makeText(User_Contact_Profile.this, R.string.added_taost, Toast.LENGTH_SHORT).show();
            }

        }

    }


    void ChangePreVariableUploadBlockList(){
        SharedPreferences sharedPref = getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("UploadBlockList",true);
        editor.commit();
    }

    void Dialog_Block(String Contactname){

        final AlertDialog.Builder builder = new AlertDialog.Builder(User_Contact_Profile.this);
        builder.setMessage(getString(R.string.areyousure_dialog)+Contactname+"?")
                .setCancelable(false)
                .setPositiveButton(R.string.blockcap, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Insert_Number_To_Blocklist();

                    }
                })
                .setNegativeButton(R.string.cancel_cap, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    void UpdateDataAfterRequest(String number){

        if (CheckNetworkConnection.hasInternetConnection(User_Contact_Profile.this)) {

            if (ConnectionDetector.hasInternetConnection(User_Contact_Profile.this)) {

            BodyNumberModel bodyNumberModel = new BodyNumberModel(number);
            Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
            WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
            Call<FetchedUserData> userDataCall = whoCallerApi.fetchUserData(ApiAccessToken.getAPIaccessToken(User_Contact_Profile.this), bodyNumberModel);

            userDataCall.enqueue(new Callback<FetchedUserData>() {
                @Override
                public void onResponse(Call<FetchedUserData> call, Response<FetchedUserData> response) {
                    if (response.isSuccessful()) {

                        FetchedUserData fetchedUserData =response.body();

                        if(fetchedUserData.getName()!=null){
                            Log.w("sues",fetchedUserData.getName());
                        }


                        if(fetchedUserData.getUser_tag_id()!=null){
                            Log.w("sues",fetchedUserData.getUser_tag_id());
                            tag.setVisibility(View.VISIBLE);
                            db=new Database_Helper(User_Contact_Profile.this);

                            Tags tags= db.getTagtByID(Long.parseLong(fetchedUserData.getUser_tag_id()));
                            tag.setText(tags.getTagname());

                        }else {

                            tag.setVisibility(View.GONE);
                        }

                        //Update UI Email
                        //First check User Email
                        if(fetchedUserData.getUser_email()!=null){
                            emaillay.setVisibility(View.VISIBLE);
                            email.setText(fetchedUserData.getUser_email());

                        }else {

                            if(fetchedUserData.getVcard_email()!=null){
                                emaillay.setVisibility(View.VISIBLE);
                                email.setText(fetchedUserData.getVcard_email());
                            }else {
                                emaillay.setVisibility(View.GONE);
                            }

                        }



                        //Update UI Country
                        if(fetchedUserData.getCountry()!=null){
                            countrylay.setVisibility(View.VISIBLE);
                            country.setText(fetchedUserData.getCountry());
                        }else {
                            countrylay.setVisibility(View.GONE);
                        }

                        //Load pic if Contact didnt have image
                        if(!Userhasimg){

                            //Update UI Image
                            if(fetchedUserData.getUser_img()!=null){
                                Picasso.with(User_Contact_Profile.this)
                                        .load("http://whocaller.net/whocallerAdmin/uploads/" +fetchedUserData.getUser_img())
                                        .into(profile_pic, new com.squareup.picasso.Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError() {
                                                profile_pic.setImageDrawable(getResources().getDrawable(R.drawable.ic_nurse));
                                            }
                                        });
                            }else {
                                if(fetchedUserData.isIs_spam()){
                                    profile_pic.setImageDrawable(getResources().getDrawable(R.drawable.ic_nurse_red));
                                }
                            }

                        }


                        Log.w("sues",fetchedUserData.getPhone());
                        //updateUI(fetchedUserData.getVcard_email());
                    }

                }

                @Override
                public void onFailure(Call<FetchedUserData> call, Throwable t) {

                }
            });
        }
    }

    }


    void DisplayArraylistOfPhonenumberDialogForCall(ArrayList<String> phonenumbers,String name){

        //Create sequence of items
        final CharSequence[] phonenums = phonenumbers.toArray(new String[phonenumbers.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Call - "+name);
        dialogBuilder.setItems(phonenums, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //String selectedText = phonenums[item].toString();
                //Selected item in listview
                //Open Dialog With Phone Number That is Selected From This Dialog
                Make_Phone_Call.makephonecall(User_Contact_Profile.this,phonenums[item].toString());//make call

            }
        });
        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();
        //Show the dialog
        alertDialogObject.show();

    }


    void DisplayArraylistOfPhonenumberDialogForMessage(ArrayList<String> phonenumbers, final String name){

        //Create sequence of items
        final CharSequence[] phonenums = phonenumbers.toArray(new String[phonenumbers.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(getString(R.string.Send_Message_dialog)+name);
        dialogBuilder.setItems(phonenums, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //String selectedText = phonenums[item].toString();
                //Selected item in listview
                //Open Dialog With Phone Number That is Selected From This Dialog
                Intent intent = new Intent(User_Contact_Profile.this,SMS_MessagesChat.class);
                intent.putExtra("LogSMSName",name);
                intent.putExtra("LogSMSAddress",phonenums[item].toString());
                startActivity(intent);


            }
        });
        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();
        //Show the dialog
        alertDialogObject.show();

    }


    void DeleteContact(final String id, String name){

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        background_process(id);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        dialog.dismiss();
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete+name).setMessage(R.string.dialog_delete_Contact_content).setPositiveButton(R.string.yes, dialogClickListener).setNegativeButton(R.string.no, dialogClickListener).show();


    }

    private void background_process(String id) {
        ArrayList ops = new ArrayList(); String[] args = new String[] {id};
        // if id is raw contact id
        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI).withSelection(ContactsContract.RawContacts._ID + "=?", args) .build());

        // if id is contact id
        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI).withSelection(ContactsContract.RawContacts.CONTACT_ID + "=?", args) .build());
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Toast.makeText(this, R.string.toast_remove,Toast.LENGTH_SHORT).show();
            finish();//finish activity
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }


    private void shareContact(String id,String name) {
        String lookupKey="";
        Cursor cur = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, new String[] { ContactsContract.Contacts.LOOKUP_KEY }, ContactsContract.Contacts._ID + " = " + id, null, null);

        if(cur!=null&& cur.moveToFirst()&&cur.getCount()>0)
        {
            lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
        }

        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
        cur.close();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(ContactsContract.Contacts.CONTENT_VCARD_TYPE);
        intent.putExtra(Intent.EXTRA_STREAM, uri );
        intent.putExtra(Intent.EXTRA_SUBJECT, name); // put the name of the contact here
        startActivity(intent);
    }


    void  copyName(String name) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", name);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this,R.string.Copied_to_clipboard,Toast.LENGTH_SHORT).show();
    }

    void searchOnWeb(String name){

        String escapedQuery = null;
        try {
            escapedQuery = URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Uri uri = Uri.parse("http://www.google.com/#q=" + escapedQuery);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    void EditContact(String contactID){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactID));
        intent.setData(uri);
        startActivity(intent);

    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

}
