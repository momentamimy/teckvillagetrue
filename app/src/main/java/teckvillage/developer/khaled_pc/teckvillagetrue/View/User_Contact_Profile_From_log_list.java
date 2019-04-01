package teckvillage.developer.khaled_pc.teckvillagetrue.View;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import teckvillage.developer.khaled_pc.teckvillagetrue.MainActivity;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.SMS_MessagesChat;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.Get_Calls_Log;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.database.Database_Helper;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.database.tables.block;

public class User_Contact_Profile_From_log_list extends AppCompatActivity {

    TextView nameofcontact,tag,phonenumee;
    CircleImageView profile_pic;
    LinearLayout makecall,sendmessage,makeblock;
    View backarrow,moreoption;
    String number;
    ImageView blockimagemake;
    TextView blockred;
    Database_Helper db;
    List<block> BlockInfo;
    boolean Bloceduser=false;
    private RadioButton radioSelectedButton;
    boolean isfrommycontact=false;

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
        blockimagemake=findViewById(R.id.blockimagered);
        blockred=findViewById(R.id.blocktextred);
        db=new Database_Helper(this);
        BlockInfo=new ArrayList<block>();

        number=getIntent().getStringExtra("ContactNUm");

        if(number==null){
            Toast.makeText(this, "Error Loading", Toast.LENGTH_LONG).show();
        }else {

            //getContactDetails(id);
            if(get_calls_log.contactExists(number)){
                isfrommycontact=true;
                nameofcontact.setText(get_calls_log.contactsName);
                phonenumee.setText(number);
            }else {
                isfrommycontact=false;
                nameofcontact.setText(number);
                phonenumee.setText(number);
            }



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


                /*if(Bloceduser){
                    String num=phonenumee.getText().toString();
                    if(num != null&& !num.isEmpty()){
                        db.deleteBlockByNumber(phonenumee.getText().toString());
                        blockimagemake.setImageResource(R.drawable.ic_block_primary_24dp);
                        blockred.setTextColor(Color.parseColor("#0089c0"));
                        Bloceduser=false;
                    }else {
                        Toast.makeText(User_Contact_Profile_From_log_list.this, "number not found", Toast.LENGTH_LONG).show();
                    }


                }else {
                    String num=phonenumee.getText().toString();
                    String name=nameofcontact.getText().toString();
                    if(num != null&& !num.isEmpty()){
                        db.insertBlock(name,num);
                        blockimagemake.setImageResource(R.drawable.ic_rejected_call);
                        blockred.setTextColor(Color.parseColor("#f53131"));
                    }

                }*/





            }
        });



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

                        Insert_Number_To_Blocklist("Person");

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
                db.insertBlock(name,num,Type);
                blockimagemake.setImageResource(R.drawable.ic_rejected_call);
                blockred.setTextColor(Color.parseColor("#f53131"));
                Bloceduser=true;
                Toast.makeText(User_Contact_Profile_From_log_list.this, "added to your block list", Toast.LENGTH_SHORT).show();
            }

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

}
