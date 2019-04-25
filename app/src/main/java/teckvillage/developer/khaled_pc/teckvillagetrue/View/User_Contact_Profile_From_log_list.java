package teckvillage.developer.khaled_pc.teckvillagetrue.View;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import teckvillage.developer.khaled_pc.teckvillagetrue.Make_Phone_Call;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.ApiAccessToken;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.BodyNumberModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.FetchedUserData;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.WhoCallerApi;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.retrofitHead;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.SMS_MessagesChat;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.Get_Calls_Log;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.database.Database_Helper;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.database.tables.block;

public class User_Contact_Profile_From_log_list extends AppCompatActivity {

    TextView nameofcontact,tag,phonenumee;
    CircleImageView profile_pic;
    LinearLayout makecall,sendmessage,makeblock;
    View backarrow,moreoption;
    String number;
    ImageView blockimagemake;
    TextView blockred;
    Database_Helper db;

    boolean Bloceduser=false;
    private RadioButton radioSelectedButton;
    static boolean  isfrommycontact;
    TextView country,email;
    RelativeLayout countrylay,emaillay,phonelay;
    RelativeLayout moreoptionusercontact,backarrowlay;

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
        backarrowlay=findViewById(R.id.contacnerbackarrow);
        moreoption=findViewById(R.id.moreoptionusercontact);
        phonenumee=findViewById(R.id.contactuserphonenumber);
        blockimagemake=findViewById(R.id.blockimagered);
        blockred=findViewById(R.id.blocktextred);
        country=findViewById(R.id.location);
        countrylay=findViewById(R.id.loactionlayout);
        email=findViewById(R.id.emailusercontact);
        emaillay=findViewById(R.id.emaillayoutcontanier);
        phonelay=findViewById(R.id.phoneNumbercontanierlay);
        moreoptionusercontact=findViewById(R.id.contacnerMoreoption);


        db=new Database_Helper(this);

        number=getIntent().getStringExtra("ContactNUm");

        if(number==null){
            Toast.makeText(this, "Error Loading", Toast.LENGTH_LONG).show();
        }else {

            //getContactDetails(id);
            if(get_calls_log.contactExists(number)){
                isfrommycontact=true;
                nameofcontact.setText(get_calls_log.contactsName);
                phonelay.setVisibility(View.VISIBLE);
                phonenumee.setText(number);
            }else {
                isfrommycontact=false;
                nameofcontact.setText(number);
                phonelay.setVisibility(View.VISIBLE);
                phonenumee.setText(number);
            }

            UpdateDataAfterRequest(number);

        }


        //If Phone number in block list
        if(db.CheckPhoneNumberInBlockList(phonenumee.getText().toString().trim())){
                blockimagemake.setImageResource(R.drawable.ic_rejected_call);
                blockred.setTextColor(Color.parseColor("#f53131"));
                Bloceduser=true;

            }




        moreoptionusercontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(isfrommycontact){

                    PopupMenu popupMenu=new PopupMenu(User_Contact_Profile_From_log_list.this,moreoptionusercontact);
                    popupMenu.getMenuInflater().inflate(R.menu.more_option_user_contact,popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getTitle().equals("Remove Contact")){
                                DeleteContact(getContactidFromPhoneNumber(phonenumee.getText().toString()),nameofcontact.getText().toString());
                            }else  if(item.getTitle().equals("Share")){
                                shareContact(phonenumee.getText().toString(),nameofcontact.getText().toString());
                            }else  if(item.getTitle().equals("Copy Name")){
                                copyName(nameofcontact.getText().toString());
                            } else  if(item.getTitle().equals("Search the Web")){
                                searchOnWeb(nameofcontact.getText().toString());
                            } else  if(item.getTitle().equals("Edit")){
                                EditContact(getContactidFromPhoneNumber(phonenumee.getText().toString()));
                            }else  if(item.getTitle().equals("Copy Contact")){
                                CopyContact(nameofcontact.getText().toString(),phonenumee.getText().toString());
                            } else  if(item.getTitle().equals("Copy Number")){
                                CopyNumber(phonenumee.getText().toString());
                            }

                            return false;
                        }
                    });
                    popupMenu.show();

                }else {
                    PopupMenu popupMenu=new PopupMenu(User_Contact_Profile_From_log_list.this,moreoptionusercontact);
                    popupMenu.getMenuInflater().inflate(R.menu.more_option_not_contact,popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getTitle().equals("Save")){
                                save(nameofcontact.getText().toString(),nameofcontact.getText().toString());
                            }else  if(item.getTitle().equals("Share")){
                                shareContact(phonenumee.getText().toString(),phonenumee.getText().toString());
                            }else  if(item.getTitle().equals("Search the Web")){
                                searchOnWeb(nameofcontact.getText().toString());
                            } else  if(item.getTitle().equals("Copy Number")){
                                CopyNumber(phonenumee.getText().toString());
                            }

                            return false;
                        }
                    });
                    popupMenu.show();

                }

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
                if(phonenumee.getText().toString()!=null&&phonenumee.getText().toString().length()>0){
                    if (ContextCompat.checkSelfPermission(User_Contact_Profile_From_log_list.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(User_Contact_Profile_From_log_list.this, new String[]{Manifest.permission.CALL_PHONE},12);
                    }
                    else
                    {
                        Make_Phone_Call.makephonecall(User_Contact_Profile_From_log_list.this,phonenumee.getText().toString());
                    }
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


        makeblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Bloceduser){

                    Dialog_Ensure_Delete_From_Block();

                }else {

                    if(isfrommycontact){
                        Dialog_Block_In_MyContact(nameofcontact.getText().toString());
                    }else {
                        Dialog_Block_Not_In_MyContact(nameofcontact.getText().toString());
                    }

                }


            }
        });



    }

    private void save(String s, String s1) {

        // Creates a new Intent to insert a contact
        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        // Sets the MIME type to match the Contacts Provider
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, s);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, s1);

        startActivity(intent);
    }

    private void shareContact(String s, String s1) {
        String shareBody = s+","+s1;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Who Caller?");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent,"Choose App"));
    }


    void Dialog_Block_Not_In_MyContact(String Contactname){

        final AlertDialog.Builder builder = new AlertDialog.Builder(User_Contact_Profile_From_log_list.this);
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_radiobutton_block, null);
        final RadioGroup group = dialogView.findViewById(R.id.radioPersonGroup);
        builder.setMessage("Are you sure you want to block "+Contactname+"?")
                .setView(dialogView)
                .setCancelable(false)
                .setPositiveButton("BLOCK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        int selectedId = group.getCheckedRadioButtonId();
                        radioSelectedButton = (RadioButton) group.findViewById(selectedId);
                        Insert_Number_To_Blocklist(radioSelectedButton.getText().toString());


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


    void Dialog_Block_In_MyContact(String Contactname){

        final AlertDialog.Builder builder = new AlertDialog.Builder(User_Contact_Profile_From_log_list.this);
        builder.setMessage("Are you sure you want to block "+Contactname+"?")
                .setCancelable(false)
                .setPositiveButton("BLOCK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Insert_Number_To_Blocklist("Personal");

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


    void Delete_Number_From_BlockList(){

        String num=phonenumee.getText().toString();
        if(num != null&& !num.isEmpty()){
            db.deleteBlockByNumber(phonenumee.getText().toString());
            blockimagemake.setImageResource(R.drawable.ic_block_primary_24dp);
            blockred.setTextColor(Color.parseColor("#0089c0"));
            Toast.makeText(User_Contact_Profile_From_log_list.this, "Number removed from your block list", Toast.LENGTH_SHORT).show();
            Bloceduser=false;
        }else {
            Toast.makeText(User_Contact_Profile_From_log_list.this, "number not found", Toast.LENGTH_LONG).show();
        }

    }

    void Insert_Number_To_Blocklist(String Type){

            String num=phonenumee.getText().toString();
            String name=nameofcontact.getText().toString();

            if(num != null&& !num.isEmpty()){
                //change Person to person
                if(Type.equals("Personal")){
                    Type="personal";
                }if(Type.equals("Business")){
                    Type="business";
                }

                long inserted= db.insertBlock(name,num,Type);
                //ensure that item inserted in database
                if(inserted != -1){
                    ChangePreVariableUploadBlockList();//upload block list in mainActivity
                    blockimagemake.setImageResource(R.drawable.ic_rejected_call);
                    blockred.setTextColor(Color.parseColor("#f53131"));
                    Bloceduser=true;
                    Toast.makeText(User_Contact_Profile_From_log_list.this, "added to your block list", Toast.LENGTH_SHORT).show();
                }

            }

    }


    void ChangePreVariableUploadBlockList(){
        SharedPreferences sharedPref = getSharedPreferences("WhoCaller?", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("UploadBlockList",true);
        editor.commit();
    }


    void Dialog_Ensure_Delete_From_Block(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(User_Contact_Profile_From_log_list.this);
        builder.setMessage("Are you sure you want to unblock this number?")
                .setCancelable(false)
                .setPositiveButton("UNBLOCK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Delete_Number_From_BlockList();

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


    void UpdateDataAfterRequest(final String number){

        if (CheckNetworkConnection.hasInternetConnection(User_Contact_Profile_From_log_list.this)) {

            if (ConnectionDetector.hasInternetConnection(User_Contact_Profile_From_log_list.this)) {

                BodyNumberModel bodyNumberModel = new BodyNumberModel(number);
                Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                Call<FetchedUserData> userDataCall = whoCallerApi.fetchUserData(ApiAccessToken.getAPIaccessToken(User_Contact_Profile_From_log_list.this), bodyNumberModel);

                userDataCall.enqueue(new Callback<FetchedUserData>() {
                    @Override
                    public void onResponse(Call<FetchedUserData> call, Response<FetchedUserData> response) {

                        if (response.isSuccessful()) {

                            FetchedUserData fetchedUserData =response.body();

                            if(!isfrommycontact){
                                //Update UI UserName
                                if(fetchedUserData.getUser_name()!=null){
                                    Log.w("sues",fetchedUserData.getUser_name());
                                    nameofcontact.setText(fetchedUserData.getUser_name());
                                }else {
                                    if(fetchedUserData.getName()!=null){
                                        Log.w("sues",fetchedUserData.getName());
                                        nameofcontact.setText(fetchedUserData.getName());
                                    }else {
                                        nameofcontact.setText(number);
                                    }
                                }
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


                            //Update Tag UI
                            if(fetchedUserData.getTag_id()!=0){
                                Log.w("sues",fetchedUserData.getUser_tag_id());
                                tag.setVisibility(View.VISIBLE);
                            }else {
                                tag.setVisibility(View.GONE);
                            }

                            //Update UI Country
                            if(fetchedUserData.getCountry()!=null){
                                countrylay.setVisibility(View.VISIBLE);
                                country.setText(fetchedUserData.getCountry());
                            }else {
                                countrylay.setVisibility(View.GONE);
                            }

                            //Update UI Image
                            if(fetchedUserData.getUser_img()!=null){
                                Picasso.with(User_Contact_Profile_From_log_list.this)
                                        .load("http://whocaller.net/uploads/" +fetchedUserData.getUser_img())
                                        .into(profile_pic, new com.squareup.picasso.Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError() {

                                            }
                                        });
                            }else {
                                if(fetchedUserData.isIs_spam()){
                                    profile_pic.setImageDrawable(getResources().getDrawable(R.drawable.ic_nurse_red));
                                }
                            }


                            Log.w("sues",fetchedUserData.getPhone());
                            //updateUI(fetchedUserData.getVcard_email());

                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<FetchedUserData> call, Throwable t) {

                    }
                });
            }else {

            }
        }else{

        }

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

    private void CopyNumber( String num) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", num);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this,"Copied to clipboard",Toast.LENGTH_SHORT).show();
    }


    private void CopyContact(String s,String alContactsPhoneNumbers) {

        String text=s+","+alContactsPhoneNumbers;
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this,"Copied to clipboard",Toast.LENGTH_SHORT).show();
    }

    void  copyName(String name) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", name);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this,"Copied to clipboard",Toast.LENGTH_SHORT).show();
    }

    String getContactidFromPhoneNumber(String num) {
        String contactid26 = null;
        ContentResolver contentResolver = getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(num));
        Cursor cursor = contentResolver.query(
                        uri,
                        new String[] {ContactsContract.Contacts._ID},
                        null,
                        null,
                        null);

        if(cursor!=null) {
            while(cursor.moveToNext()){
                contactid26 = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
            }
            cursor.close();
        }

        return contactid26;
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
        builder.setTitle("Delete "+name).setMessage("Are you Sure you want to delete this contact?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();


    }

    private void background_process(String id) {
        ArrayList ops = new ArrayList(); String[] args = new String[] {id};
        // if id is raw contact id
        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI).withSelection(ContactsContract.RawContacts._ID + "=?", args) .build());

        // if id is contact id
        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI).withSelection(ContactsContract.RawContacts.CONTACT_ID + "=?", args) .build());
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Toast.makeText(this,"The Contact has been removed from your contacts",Toast.LENGTH_SHORT).show();
            finish();//finish activity
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    void EditContact(String contactID){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactID));
        intent.setData(uri);
        startActivity(intent);

    }
}
