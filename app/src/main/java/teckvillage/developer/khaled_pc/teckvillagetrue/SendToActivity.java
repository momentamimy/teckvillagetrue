package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.AddingSendToContactsAdapters;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.LetterComparator;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.SendToContactsAdapters;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.UserContactsAdapters;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.Get_User_Contacts;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.UserContactData;

public class SendToActivity extends AppCompatActivity {

    ImageView back;
    EditText sendToEditText;
    Get_User_Contacts get_user_contacts;
    RecyclerView mRecyclerView;
    public ArrayList<UserContactData> userContactsData;
    public SendToContactsAdapters sendToContactsAdapters;
    static RecyclerView mRecyclerViewMutilpleContacts;
    LinearLayoutManager lLayout;
    ImageView multipleUsers;
    public static boolean MultipleRecivers;
    public static ArrayList<UserContactData> addingUserContactsData;
    public static AddingSendToContactsAdapters addingSendToContactsAdapters;
    static TextView textHint;
    static FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to);
        MultipleRecivers=false;
        addingUserContactsData=new ArrayList<>();
        back=findViewById(R.id.back_Send_To);
        sendToEditText=findViewById(R.id.Send_To_edittext);
        mRecyclerView=findViewById(R.id.Send_To_RecyclerView);
        mRecyclerViewMutilpleContacts=findViewById(R.id.Send_To_multiple_contact_RecyclerView);
        multipleUsers=findViewById(R.id.mutiple_users);
        textHint=findViewById(R.id.TextHint);
        fab=findViewById(R.id.Send_message_contact_fab_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        get_user_contacts=new Get_User_Contacts(this);

        lLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewMutilpleContacts.setLayoutManager(lLayout);
        mRecyclerViewMutilpleContacts.setItemAnimator(new DefaultItemAnimator());
        addingSendToContactsAdapters= new AddingSendToContactsAdapters(this,addingUserContactsData);
        mRecyclerViewMutilpleContacts.setAdapter(addingSendToContactsAdapters);

        userContactsData=new LetterComparator().sortList(get_user_contacts.getContactListContactsRecycleview());
        sendToContactsAdapters=new SendToContactsAdapters(this,userContactsData);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(sendToContactsAdapters);

        multipleUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultipleRecivers=true;
                multipleUsers.setVisibility(View.GONE);
                textHint.setVisibility(View.VISIBLE);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MultipleRecivers)
                {
                    String num=sendToEditText.getText().toString().replace(" ","");
                    String name= (String) get_user_contacts.getContactName(num,getApplicationContext());

                    Intent intent=new Intent(getApplicationContext(),SMS_MessagesChat.class);
                    intent.putExtra("LogSMSAddress",num);
                    if (TextUtils.isEmpty(name))
                    {
                        intent.putExtra("LogSMSName",num);
                    }
                    else
                    {
                        intent.putExtra("LogSMSName",name);
                    }
                    startActivity(intent);
                }
                else
                {
                    if (addingUserContactsData.size()==1)
                    {
                        String name=addingUserContactsData.get(0).usercontacName;
                        Intent intent=new Intent(getApplicationContext(),SMS_MessagesChat.class);
                        intent.putExtra("LogSMSName",name);
                        String num=get_user_contacts.getPhoneNumber(name,getApplicationContext()).replace(" ","");
                        intent.putExtra("LogSMSAddress",num);
                        startActivity(intent);
                    }
                    else
                    {
                        ArrayList<String> name=new ArrayList<>(),num=new ArrayList<>();
                        for (int i=0;i<addingUserContactsData.size();i++)
                        {
                            name.add(addingUserContactsData.get(i).usercontacName);
                            num.add(get_user_contacts.getPhoneNumber(name.get(i),getApplicationContext()).replace(" ",""));
                        }
                        Intent intent=new Intent(getApplicationContext(),SMS_MessagesChat_MultipleContacts.class);
                        intent.putExtra("LogSMSName",name);
                        intent.putExtra("LogSMSAddress",num);
                        startActivity(intent);
                    }
                }

            }
        });
        sendToEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sendToText = sendToEditText.getText().toString();
                String regex = "\\d+";
                String st = sendToText.replace("+", "");
                st = st.replace(" ", "");

                if (!MultipleRecivers){
                    if (st.matches(regex)) {
                        fab.setVisibility(View.VISIBLE);
                        sendToContactsAdapters=new SendToContactsAdapters(getApplicationContext(),userContactsData);
                        mRecyclerView.setAdapter(sendToContactsAdapters);
                    } else {
                        fab.setVisibility(View.GONE);
                        SortSearchContacts(sendToText);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static void addingMulipleUserContacts(Context context, UserContactData data)
    {
        for (int i=0;i<addingUserContactsData.size();i++)
        {
            if (addingUserContactsData.get(i).equals(data))
            {
                Toast.makeText(context,"contact already selected",Toast.LENGTH_LONG).show();
                return;
            }
        }
        addingUserContactsData.add(data);
        addingSendToContactsAdapters.notifyItemInserted(addingUserContactsData.size()-1);
        mRecyclerViewMutilpleContacts.smoothScrollToPosition(addingUserContactsData.size()-1);
        mRecyclerViewMutilpleContacts.setVisibility(View.VISIBLE);
        textHint.setVisibility(View.GONE);
        fab.setVisibility(View.VISIBLE);
    }
    public static void showTextHint()
    {
        if (addingUserContactsData.size()==0)
        {
            mRecyclerViewMutilpleContacts.setVisibility(View.GONE);
            textHint.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
        }
    }

    private List<UserContactData> SortSearchContacts(String num) {
        ArrayList<UserContactData> infos = new ArrayList<>();
        for (int i = 0; i < userContactsData.size(); i++) {
            if (userContactsData.get(i).usercontacName.contains(num)) {
                infos.add(userContactsData.get(i));
            }
        }
        int position;
        for (int i = 0; i < infos.size(); i++) {
            position = infos.get(i).usercontacName.indexOf(num);
            if (infos.get(0).usercontacName.indexOf(num) > position) {
                UserContactData inf = infos.get(i);
                infos.remove(i);
                infos.add(0, inf);
            }
        }

        sendToContactsAdapters=new SendToContactsAdapters(getApplicationContext(),infos);
        mRecyclerView.setAdapter(sendToContactsAdapters);
        return infos;
    }
}
