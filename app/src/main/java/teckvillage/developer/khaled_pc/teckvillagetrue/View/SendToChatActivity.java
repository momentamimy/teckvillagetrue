package teckvillage.developer.khaled_pc.teckvillagetrue.View;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import teckvillage.developer.khaled_pc.teckvillagetrue.Chat_MessagesChat;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.AddingSendToChatContactsAdapters;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.SendToChatContactsAdapters;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.SMS_MessagesChat;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.Get_User_Contacts;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.ApiAccessToken;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.JSON_Mapping.DataReceived;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.WhoCallerApi;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.retrofitHead;

public class SendToChatActivity extends AppCompatActivity {

    ImageView back;
    EditText sendToEditText;
    Get_User_Contacts get_user_contacts;
    RecyclerView mRecyclerView;
    public List<DataReceived> userContactsData;
    public SendToChatContactsAdapters sendToContactsAdapters;
    static RecyclerView mRecyclerViewMutilpleContacts;
    LinearLayoutManager lLayout;
    ImageView multipleUsers;
    public static boolean MultipleRecivers;
    public static ArrayList<DataReceived> addingUserContactsData;
    public static AddingSendToChatContactsAdapters addingSendToContactsAdapters;
    static TextView textHint;
    static FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_chat);
        MultipleRecivers=false;
        userContactsData=new ArrayList<>();
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
        addingSendToContactsAdapters= new AddingSendToChatContactsAdapters(this,addingUserContactsData);
        mRecyclerViewMutilpleContacts.setAdapter(addingSendToContactsAdapters);

        //userContactsData=new LetterComparator().sortListforChat();
        sendToContactsAdapters=new SendToChatContactsAdapters(this,userContactsData);
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

                    Intent intent=new Intent(getApplicationContext(), SMS_MessagesChat.class);
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
                        DataReceived user=addingUserContactsData.get(0);
                        Intent intent=new Intent(getApplicationContext(), Chat_MessagesChat.class);
                        intent.putExtra("UserName", user.getName());
                        intent.putExtra("UserAddress", user.getFull_phone());
                        intent.putExtra("UserID", user.getId());
                        startActivity(intent);
                    }
                    else
                    {
                        //Check wifi or data available
                        if (CheckNetworkConnection.hasInternetConnection(SendToChatActivity.this)) {

                            //Check internet Access
                            if (ConnectionDetector.hasInternetConnection(SendToChatActivity.this)) {
                                int[] usersId=new int[addingUserContactsData.size()];
                                for (int i=0;i<addingUserContactsData.size();i++)
                                {
                                    usersId[i]=addingUserContactsData.get(i).getId();
                                }
                                MyCustomCreatGroupDialog(usersId);

                            }
                        }
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
                        sendToContactsAdapters=new SendToChatContactsAdapters(getApplicationContext(),userContactsData);
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





        //API Request
        //Check wifi or data available
        if (CheckNetworkConnection.hasInternetConnection(SendToChatActivity.this)) {

            //Check internet Access
            if (ConnectionDetector.hasInternetConnection(SendToChatActivity.this)) {
                Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                Call<List<DataReceived>> MessageWhenOpenChat = whoCallerApi.getallChatContact(ApiAccessToken.getAPIaccessToken(SendToChatActivity.this));

                MessageWhenOpenChat.enqueue(new Callback<List<DataReceived>>() {
                    @Override
                    public void onResponse(Call<List<DataReceived>> call, Response<List<DataReceived>> response) {
                        if (response.isSuccessful())
                        {
                            userContactsData= response.body();

                            sendToContactsAdapters=new SendToChatContactsAdapters(getApplicationContext(),userContactsData);
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            mRecyclerView.setAdapter(sendToContactsAdapters);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<DataReceived>> call, Throwable t) {
                    }
                });
            }
        }
    }

    public static void addingMulipleUserContacts(Context context, DataReceived data)
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

    private List<DataReceived> SortSearchContacts(String num) {
        ArrayList<DataReceived> infos = new ArrayList<>();
        for (int i = 0; i < userContactsData.size(); i++) {
            if (userContactsData.get(i).getName().contains(num)) {
                infos.add(userContactsData.get(i));
            }
        }
        int position;
        for (int i = 0; i < infos.size(); i++) {
            position = infos.get(i).getName().indexOf(num);
            if (infos.get(0).getName().indexOf(num) > position) {
                DataReceived inf = infos.get(i);
                infos.remove(i);
                infos.add(0, inf);
            }
        }

        sendToContactsAdapters=new SendToChatContactsAdapters(getApplicationContext(),infos);
        mRecyclerView.setAdapter(sendToContactsAdapters);
        return infos;
    }


    Dialog MyDialogCreatGroup=null;
    public void MyCustomCreatGroupDialog(final int[] usersId) {
        MyDialogCreatGroup = new Dialog(this);
        MyDialogCreatGroup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialogCreatGroup.setContentView(R.layout.create_group_chat_dialog);
        Window window = MyDialogCreatGroup.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        MyDialogCreatGroup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final EditText Group;
        TextView Cancel,Ok;

        Group=MyDialogCreatGroup.findViewById(R.id.edit_Group);

        Cancel=MyDialogCreatGroup.findViewById(R.id.btn_close);
        Ok=MyDialogCreatGroup.findViewById(R.id.btn_submit);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogCreatGroup.dismiss();
            }
        });

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*GroupBodyModel bodyModel=new GroupBodyModel(usersId,Group.getText().toString());

                Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                Call<GroupChatResultModel> GroupChat = whoCallerApi.createGroup(ApiAccessToken.getAPIaccessToken(SendToChatActivity.this),bodyModel);
                GroupChat.enqueue(new Callback<GroupChatResultModel>() {
                    @Override
                    public void onResponse(Call<GroupChatResultModel> call, Response<GroupChatResultModel> response) {
                        Log.d("meeeeesheeey22", response.body().getGroup().getName());
                        GroupChatModel groupChatModel=response.body().getGroup();
                        Intent intent=new Intent(getApplicationContext(), Chat_MessagesChat.class);
                        intent.putExtra("UserName", groupChatModel.getName());
                        intent.putExtra("UserAddress", "GroupChat");
                        intent.putExtra("UserID", groupChatModel.getId());
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<GroupChatResultModel> call, Throwable t) {
                        Log.d("meeeeesheeey22", t.getMessage());
                    }
                });*/
                ArrayList<String> UsersNames_IDs=new ArrayList<>();
                for (int i=0;i<addingUserContactsData.size();i++)
                {
                    UsersNames_IDs.add(addingUserContactsData.get(i).getName()+"::"
                            +addingUserContactsData.get(i).getId());
                }
                Intent intent=new Intent(getApplicationContext(), Chat_MessagesChat.class);
                intent.putExtra("UserName", "mashy");
                intent.putExtra("UserAddress", "GroupChat");
                intent.putExtra("UserID", 46);
                intent.putExtra("userList",UsersNames_IDs);
                startActivity(intent);
                MyDialogCreatGroup.dismiss();
            }
        });
        MyDialogCreatGroup.show();
    }
}
