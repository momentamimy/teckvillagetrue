package com.developer.whocaller.net.View;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.whocaller.net.Controller.AddingSendToChatContactsAdapters;
import com.developer.whocaller.net.Controller.SendToChatContactsAdapters;
import com.developer.whocaller.net.Model.retrofit.ApiAccessToken;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.BodyNumberModel;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.FetchedUserData;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.GroupBodyModel;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.GroupChatModel;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.GroupChatResultModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import com.developer.whocaller.net.Chat_MessagesChat;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.DataReceivedChatUsers;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.RoomModel;


import com.developer.whocaller.net.Model.retrofit.WhoCallerApi;
import com.developer.whocaller.net.Model.retrofit.retrofitHead;
import com.developer.whocaller.net.R;

import static com.developer.whocaller.net.AllChatRoomsFragment.ChatStatusChangedallchat;
import static com.developer.whocaller.net.Chat_MessagesChat.ChatStatusChanged;

public class SendToChatActivity extends AppCompatActivity {

    ImageView back;
    EditText sendToEditText;
    RecyclerView mRecyclerView;
    public List<RoomModel> userContactsData;
    public SendToChatContactsAdapters sendToContactsAdapters;
    static RecyclerView mRecyclerViewMutilpleContacts;
    LinearLayoutManager lLayout;
    ImageView multipleUsers;
    public static boolean MultipleRecivers;
    public static List<RoomModel> addingUserContactsData;
    public static AddingSendToChatContactsAdapters addingSendToContactsAdapters;
    static TextView textHint;
    static FloatingActionButton fab;

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_chat);

        mProgressDialog = new ProgressDialog(SendToChatActivity.this);
        //getUserDataApi(getApplicationContext(),"0121122");
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
                    /*
                    String num=sendToEditText.getText().toString().replace(" ","");
                    String name= (String) get_user_contacts.getContactName(num,getApplicationContext());

                    Intent intent=new Intent(getApplicationContext(), Chat_MessagesChat.class);
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
                    */
                }
                else
                {
                    if (addingUserContactsData.size()==1)
                    {
                        RoomModel user=addingUserContactsData.get(0);
                        Intent intent=new Intent(getApplicationContext(), Chat_MessagesChat.class);
                        intent.putExtra("UserName", user.getName());
                        intent.putExtra("UserAddress", user.getPhone());
                        intent.putExtra("UserID", user.getId());
                        intent.putExtra("ChatID", user.getChatRoomId());
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
                Call<DataReceivedChatUsers> MessageWhenOpenChat = whoCallerApi.getallChatContact(ApiAccessToken.getAPIaccessToken(SendToChatActivity.this));

                mProgressDialog = new ProgressDialog(SendToChatActivity.this);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();

                MessageWhenOpenChat.enqueue(new Callback<DataReceivedChatUsers>() {
                    @Override
                    public void onResponse(Call<DataReceivedChatUsers> call, Response<DataReceivedChatUsers> response) {
                        if (response.isSuccessful())
                        {
                            if (mProgressDialog.isShowing())
                                mProgressDialog.dismiss();

                            Log.d("dadadawqffsxzdf","sucees");
                            userContactsData= new ArrayList<>();

                            for (int i=0;i<response.body().getRoom().size();i++) {
                                RoomModel roomModel=response.body().getRoom().get(i);
                                if (roomModel.getType().equals("user")) { userContactsData.add(roomModel); }
                            }

                            sendToContactsAdapters=new SendToChatContactsAdapters(getApplicationContext(),userContactsData);
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            mRecyclerView.setAdapter(sendToContactsAdapters);
                        }
                        else
                        {
                            if (mProgressDialog.isShowing())
                                mProgressDialog.dismiss();
                            Log.d("dadadawqffsxzdf","failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<DataReceivedChatUsers> call, Throwable t) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("dadadawqffsxzdf","failure");
                    }
                });
            }
        }
    }

    public static void addingMulipleUserContacts(Context context, RoomModel data)
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

    private List<RoomModel> SortSearchContacts(String num) {
        ArrayList<RoomModel> infos = new ArrayList<>();
        for (int i = 0; i < userContactsData.size(); i++) {
            if (userContactsData.get(i).getName().contains(num)) {
                infos.add(userContactsData.get(i));
            }
        }
        int position;
        for (int i = 0; i < infos.size(); i++) {
            position = infos.get(i).getName().indexOf(num);
            if (infos.get(0).getName().indexOf(num) > position) {
                RoomModel inf = infos.get(i);
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

                MyDialogCreatGroup.dismiss();
                if (CheckNetworkConnection.hasInternetConnection(SendToChatActivity.this)) {

                    //Check internet Access
                    if (ConnectionDetector.hasInternetConnection(SendToChatActivity.this)) {
                GroupBodyModel bodyModel=new GroupBodyModel(usersId,Group.getText().toString());

                Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                Call<GroupChatResultModel> GroupChat = whoCallerApi.createGroup(ApiAccessToken.getAPIaccessToken(SendToChatActivity.this),bodyModel);
                        mProgressDialog = new ProgressDialog(SendToChatActivity.this);
                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setMessage("Loading...");
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.show();
                GroupChat.enqueue(new Callback<GroupChatResultModel>() {
                    @Override
                    public void onResponse(Call<GroupChatResultModel> call, Response<GroupChatResultModel> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("meeeeesheeey22", response.body().getGroup().getName());
                        GroupChatModel groupChatModel=response.body().getGroup();
                        int chatRoomId=Integer.parseInt(response.body().getChat_rooms_id());
                        Intent intent=new Intent(getApplicationContext(), Chat_MessagesChat.class);
                        Log.d("meeeeesheafafeey22", String.valueOf(response.body().getGroup().getId()));
                        intent.putExtra("UserName", groupChatModel.getName());
                        intent.putExtra("UserAddress", "GroupChat");
                        intent.putExtra("UserID", groupChatModel.getId());
                        intent.putExtra("ChatID", chatRoomId);
                        intent.putExtra("UserIDsList",usersId);
                        startActivity(intent);
                        finish();
                        ChatStatusChanged=true;
                        ChatStatusChangedallchat=true;
                    }

                    @Override
                    public void onFailure(Call<GroupChatResultModel> call, Throwable t) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("meeeeesheeey22", t.getMessage());
                    }
                });
                /*ArrayList<String> UsersNames_IDs=new ArrayList<>();
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
            */
                    }
                }
            }
        });
        MyDialogCreatGroup.show();
    }

    public void getUserDataApi(Context context, String number) {
        if (CheckNetworkConnection.hasInternetConnection(context)) {

            //Check internet Access
            if (ConnectionDetector.hasInternetConnection(context)) {

                BodyNumberModel bodyNumberModel = new BodyNumberModel(number);
                Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                Call<FetchedUserData> userDataCall = whoCallerApi.fetchUserData(ApiAccessToken.getAPIaccessToken(context), bodyNumberModel);

                userDataCall.enqueue(new Callback<FetchedUserData>() {
                    @Override
                    public void onResponse(Call<FetchedUserData> call, Response<FetchedUserData> response) {
                        if (response.isSuccessful())
                        {
                            if (TextUtils.isEmpty(response.body().getName()))
                            {
                                Log.d("userNamePaleeez", "userNamePaleeez");
                            }
                            else
                            {
                                Log.d("userNamePaleeez", response.body().getName());
                            }

                        }
                        else
                        {
                            Log.d("userNamePaleeez", "userNamePaleeez");
                        }
                    }

                    @Override
                    public void onFailure(Call<FetchedUserData> call, Throwable t) {
                        Log.d("userNamePaleeez", "failure");
                    }
                });
            }
        }
    }


}
