package com.developer.whocaller.net;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class EditSpeedDialActivity extends AppCompatActivity {

    ImageView backButton;
    ListView speed_dial_edit_list;
    SpeedDialEditAdapter speedDialEditAdapter;
    List<EditSpeedModel> editSpeedModels;
    SharedPreferences sharedPreferences;
    String circleNum="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_speed_dial);
        backButton=findViewById(R.id.back_Speed_dial);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sharedPreferences = getSharedPreferences("number", Context.MODE_PRIVATE);
        speed_dial_edit_list=findViewById(R.id.speed_dial_edit_list);
        editSpeedModels=new ArrayList<>();
        for (int i=2;i<=9;i++)
        {
            editSpeedModels.add(new EditSpeedModel(String.valueOf(i)));
        }
        speedDialEditAdapter = new SpeedDialEditAdapter( editSpeedModels,this);
        speed_dial_edit_list.setAdapter(speedDialEditAdapter);
        speed_dial_edit_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                circleNum=String.valueOf(position+2);
                MyCustomSpeedDialog(circleNum);
            }
        });
    }

    public void refresh()
    {
        speedDialEditAdapter = new SpeedDialEditAdapter( editSpeedModels,this);
        speed_dial_edit_list.setAdapter(speedDialEditAdapter);
    }

    public static final int PICK_CONTACT=1;
    Dialog MyDialogSpeedDial;
    public void MyCustomSpeedDialog(final String number){
        MyDialogSpeedDial = new Dialog(this);
        MyDialogSpeedDial.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialogSpeedDial.setContentView(R.layout.long_press_dialog);
        Window window = MyDialogSpeedDial.getWindow();
        window.setLayout(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        MyDialogSpeedDial.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView speedDialTitle=MyDialogSpeedDial.findViewById(R.id.speed_dial_title);
        speedDialTitle.setText("Speed dial #"+number);
        TextView assignContact=MyDialogSpeedDial.findViewById(R.id.assign_contact);
        assignContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent,PICK_CONTACT);
            }
        });
        TextView assignPhoneNumber=MyDialogSpeedDial.findViewById(R.id.assign_phone_number);
        assignPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomAssignNumDialog(number);
            }
        });
        TextView removeSpeedDial=MyDialogSpeedDial.findViewById(R.id.edit_speed_dial);
        removeSpeedDial.setText("remove");
        removeSpeedDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("#"+circleNum,"");
                editor.commit();
                MyDialogSpeedDial.dismiss();
                refresh();
            }
        });
        MyDialogSpeedDial.show();
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        if ((reqCode == PICK_CONTACT) && (resultCode == RESULT_OK)) {
            Cursor MyCursor = null;
            try {
                Uri uri = data.getData();
                MyCursor = getContentResolver().query(uri, new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER }, null, null, null);
                if (MyCursor != null && MyCursor.moveToNext()) {
                    String phone = MyCursor.getString(0);
                    Log.d("Phone",phone);

                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("#"+circleNum,phone);
                    editor.commit();
                    refresh();
                    MyDialogSpeedDial.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if ((reqCode == PICK_CONTACT) && (resultCode == RESULT_CANCELED))
        {

            //MyDialogSpeedDial.dismiss();
        }
    }

    Dialog MyDialogAssignNum;
    public void MyCustomAssignNumDialog(final String number) {
        MyDialogAssignNum = new Dialog(this);
        MyDialogAssignNum.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialogAssignNum.setContentView(R.layout.assign_number_speed_dial_dialog);
        MyDialogAssignNum.show();
        Window window = MyDialogAssignNum.getWindow();
        window.setLayout(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        MyDialogAssignNum.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView speedDialTitle=MyDialogAssignNum.findViewById(R.id.speed_dial_title);
        speedDialTitle.setText("Speed dial #"+number);
        final EditText phoneNum=MyDialogAssignNum.findViewById(R.id.edit_phone_number);
        final TextInputLayout inputLayout=MyDialogAssignNum.findViewById(R.id.input_layout_phone_number);
        TextView OK,Cancel;
        OK=MyDialogAssignNum.findViewById(R.id.btn_submit);
        Cancel=MyDialogAssignNum.findViewById(R.id.btn_close);
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(phoneNum.getText().toString().trim()))
                {
                    inputLayout.setError("Empty Field");
                }
                else
                {
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("#"+number,phoneNum.getText().toString().trim());
                    editor.commit();
                    MyDialogAssignNum.dismiss();
                    MyDialogSpeedDial.dismiss();
                }
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogAssignNum.dismiss();
            }
        });
    }
}
