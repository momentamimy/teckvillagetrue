package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.AddContactsToChatGroupAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.SendToChatContactsAdapters;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.DataReceived;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.DataReceivedChatUsers;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.RoomModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.CheckNetworkConnection;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.ConnectionDetector;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.ApiAccessToken;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.ListMessagesChatModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.MessageBodyModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.MessageChatModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.MessageGroupBodyModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.WhoCallerApi;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.retrofitHead;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.SendToChatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class Chat_MessagesChat extends AppCompatActivity {

    ProgressBar progressPage;
    RecyclerView chat_RecyclerView;
    List<MessageChatModel> messageChatInfos;
    messages_list_chat_adapter chatAdapter;
    LinearLayoutManager llayout;

    TextView userName;
    CircleImageView userImg;

    LinearLayout Cant_Replay_Layout;

    RelativeLayout Message_Container;
    EditText messageSend;
    FloatingActionButton send;

    String name, address,Image;
    int[] groupUsers;
    int receiverID,chatRoomId,page=1;

    ImageView callIcon, settingIcon;

    ImageView DualSIM;

    boolean isGroup=false;

    private static Chat_MessagesChat inst;

    ProgressDialog mProgressDialog;

    public static boolean ChatStatusChanged=false;
    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    public static Chat_MessagesChat instance() {
        return inst;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_messages_chat);

        Log.d("mememmememkfsas",FirebaseInstanceId.getInstance().getToken());
        DualSIM=findViewById(R.id.Dual_Sim);
        DualSIM.setVisibility(View.GONE);
        LinearLayout toolbar = findViewById(R.id.toolbar);
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mProgressDialog = new ProgressDialog(Chat_MessagesChat.this);

        Cant_Replay_Layout = findViewById(R.id.Cant_Replay_Layout);
        Message_Container = findViewById(R.id.messageContainer);

        Cant_Replay_Layout.setVisibility(View.GONE);
        Message_Container.setVisibility(View.VISIBLE);

        callIcon = findViewById(R.id.call_sms_chat);
        settingIcon = findViewById(R.id.setting_sms_chat);

        userName = findViewById(R.id.UserName);
        userImg = findViewById(R.id.UserImage);

        name = getIntent().getStringExtra("UserName");
        address = getIntent().getStringExtra("UserAddress");
        Image = getIntent().getStringExtra("UserImage");
        receiverID = getIntent().getIntExtra("UserID",0);
        chatRoomId = getIntent().getIntExtra("ChatID",0);


        if (address.equals("GroupChat"))
        {
            isGroup=true;
            callIcon.setVisibility(View.GONE);
            settingIcon.setVisibility(View.GONE);
            userImg.setImageResource(R.drawable.ic_groupchat);
            groupUsers = getIntent().getIntArrayExtra("UserIDsList");
            Log.d("affafafa", String.valueOf(groupUsers[0]));

        }
        else
        {
            settingIcon.setVisibility(View.GONE);
            Picasso.with(getApplicationContext()).load("http://whocaller.net/uploads/"+Image)
                    .into(userImg, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            userImg.setImageResource(R.drawable.ic_nurse);
                        }
                    });
        }
        userName.setText(name);

        messageSend = findViewById(R.id.Message_Send);
        send = findViewById(R.id.fab);
        messageSend.setHint("");

        progressPage=findViewById(R.id.progressPage);
        chat_RecyclerView = findViewById(R.id.chat_SMS_messages);
        messageChatInfos=new ArrayList<>();
        chatAdapter = new messages_list_chat_adapter(Chat_MessagesChat.this, messageChatInfos, ApiAccessToken.getID(getApplicationContext()));
        llayout= new LinearLayoutManager(getApplicationContext());
        chat_RecyclerView.setLayoutManager(llayout);
        llayout.setStackFromEnd(true);
        chat_RecyclerView.setAdapter(chatAdapter);


        //getmessages();


        PusherOptions options = new PusherOptions();
        options.setCluster("mt1");
        Pusher pusher = new Pusher("6aa992fcfd9b1332708b", options);
        Channel channel;
        if (!isGroup)
        {
            channel = pusher.subscribe("chat-"+ApiAccessToken.getID(getApplicationContext())+"-"+receiverID);
            channel.bind("new-message", new SubscriptionEventListener() {
                @Override
                public void onEvent(String channelName, String eventName, final String data) {
                    Log.d("paaalleeeeez",data);
                    JSONObject jsonObject=null;
                    try {
                        jsonObject = new JSONObject(data);
                        JSONObject js= jsonObject.getJSONObject("message");
                        Log.d("paaalleeeeezla",js.getString("receiver_id"));

                        final MessageChatModel messageChatModel=new MessageChatModel(Integer.parseInt(js.getString("id")), Integer.parseInt(js.getString("sender_id")), Integer.parseInt(js.getString("receiver_id")), -1,Integer.parseInt(js.getString("chat_rooms_id")), js.getString("text"), js.getString("created_at"), js.getString("updated_at"));
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // Stuff that updates the UI
                                messageChatInfos.add(messageChatModel);
                                sortDate();
                                chatAdapter.notifyDataSetChanged();
                                chat_RecyclerView.smoothScrollToPosition(messageChatInfos.size()-1);
                                ChatStatusChanged=true;
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        else
        {
            channel = pusher.subscribe("chat-group-"+receiverID);
            channel.bind("new-message", new SubscriptionEventListener() {
                @Override
                public void onEvent(String channelName, String eventName, final String data) {
                    Log.d("paaalleeeeez",data);
                    JSONObject jsonObject=null;
                    try {
                        jsonObject = new JSONObject(data);
                        JSONObject js= jsonObject.getJSONObject("message");
                        Log.d("paaalleeeeezla",js.getString("group_id"));

                        if (Integer.parseInt(js.getString("sender_id"))!=ApiAccessToken.getID(getApplicationContext()))
                        {
                            final MessageChatModel messageChatModel=new MessageChatModel(Integer.parseInt(js.getString("id")), Integer.parseInt(js.getString("sender_id")), -1, Integer.parseInt(js.getString("group_id")),Integer.parseInt(js.getString("chat_rooms_id")), js.getString("text"), js.getString("created_at"), js.getString("updated_at"));
                            DataReceived sender =new DataReceived();
                            sender.setName(jsonObject.getString("sender"));
                            messageChatModel.setSender(sender);
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    // Stuff that updates the UI
                                    messageChatInfos.add(messageChatModel);
                                    sortDate();
                                    chatAdapter.notifyDataSetChanged();
                                    chat_RecyclerView.smoothScrollToPosition(messageChatInfos.size()-1);
                                    ChatStatusChanged=true;
                                }
                            });

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        pusher.connect();


        callIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isGroup) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + address));
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(intent);
                    }
                }
            }
        });
        settingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(Chat_MessagesChat.this, settingIcon);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.add_contact_to_chat:
                                // item one clicked
                                //openContactsDialog();
                                return true;
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.chat_menu);
                popupMenu.show();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messageSend.getText().toString().equals(""))
                {
                    if (CheckNetworkConnection.hasInternetConnection(Chat_MessagesChat.this)) {
                        if (!isGroup) {
                            Date date = new Date(System.currentTimeMillis());
                            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String dateText = df2.format(date);

                            String message = messageSend.getText().toString();
                            messageSend.setText("");
                            final MessageChatModel messageChatModel = new MessageChatModel(receiverID, message, dateText);
                            messageChatModel.setSender_id(ApiAccessToken.getID(getApplicationContext()));
                            messageChatModel.setId((int) System.currentTimeMillis());
                            messageChatModel.setStatus(true);
                            messageChatInfos.add(messageChatModel);
                            sortDate();
                            final int index =messageChatInfos.indexOf(messageChatModel);
                            chatAdapter.notifyDataSetChanged();
                            chat_RecyclerView.smoothScrollToPosition(messageChatInfos.size()-1);

                            //Check internet Access
                            if (ConnectionDetector.hasInternetConnection(Chat_MessagesChat.this)) {
                                Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                                WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                                Call<MessageChatModel> sendMessage = whoCallerApi.sendMessage(ApiAccessToken.getAPIaccessToken(Chat_MessagesChat.this), new MessageBodyModel(receiverID, chatRoomId,message));

                                sendMessage.enqueue(new Callback<MessageChatModel>() {
                                    @Override
                                    public void onResponse(Call<MessageChatModel> call, Response<MessageChatModel> response) {
                                        if (response.isSuccessful())
                                        {
                                            messageChatInfos.set(index, response.body());
                                            sortDate();
                                            chatAdapter.notifyDataSetChanged();
                                            chat_RecyclerView.smoothScrollToPosition(messageChatInfos.size() - 1);
                                            ChatStatusChanged=true;
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<MessageChatModel> call, Throwable t) {
                                        Log.d("tybtybtbyfsf", t.getMessage());
                                        messageChatInfos.remove(messageChatModel);
                                        sortDate();
                                        chatAdapter.notifyDataSetChanged();
                                        //chat_RecyclerView.smoothScrollToPosition(messageChatInfos.size() - 1);
                                    }
                                });
                            }
                        }
                        else
                        {
                            Date date = new Date(System.currentTimeMillis());
                            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String dateText = df2.format(date);

                            String message = messageSend.getText().toString();
                            messageSend.setText("");
                            final MessageChatModel messageChatModel = new MessageChatModel(receiverID, message, dateText);
                            messageChatModel.setSender_id(ApiAccessToken.getID(getApplicationContext()));
                            messageChatModel.setId((int) System.currentTimeMillis());
                            messageChatModel.setStatus(true);
                            messageChatInfos.add(messageChatModel);
                            sortDate();
                            final int index =messageChatInfos.indexOf(messageChatModel);
                            chatAdapter.notifyDataSetChanged();
                            chat_RecyclerView.smoothScrollToPosition(messageChatInfos.size()-1);

                            //Check internet Access
                            if (ConnectionDetector.hasInternetConnection(Chat_MessagesChat.this)) {
                                Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                                WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                                Call<MessageChatModel> sendMessage = whoCallerApi.sendGroupMessage(ApiAccessToken.getAPIaccessToken(Chat_MessagesChat.this), new MessageGroupBodyModel(receiverID,chatRoomId,message));

                                sendMessage.enqueue(new Callback<MessageChatModel>() {
                                    @Override
                                    public void onResponse(Call<MessageChatModel> call, Response<MessageChatModel> response) {
                                        if (response.isSuccessful()) {
                                            Log.d("tybtybtbyfsf", response.toString());
                                            messageChatInfos.set(index, response.body());
                                            sortDate();
                                            chatAdapter.notifyDataSetChanged();
                                            chat_RecyclerView.smoothScrollToPosition(messageChatInfos.size() - 1);
                                            ChatStatusChanged = true;
                                        }
                                        else
                                        {
                                            Log.d("tybtybtbyfsf", "failed");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<MessageChatModel> call, Throwable t) {
                                        Log.d("tybtybtbyfsf", t.getMessage());
                                        messageChatInfos.remove(messageChatModel);
                                        sortDate();
                                        chatAdapter.notifyItemRemoved(messageChatInfos.size() - 1);
                                        //chat_RecyclerView.smoothScrollToPosition(messageChatInfos.size() - 1);
                                    }
                                });
                            }
                        }
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"empty message",Toast.LENGTH_SHORT);
                }
            }
        });

        page=1;
        chat_RecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager=LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findFirstVisibleItemPosition();
                numberOfVisibleItems=layoutManager.findLastVisibleItemPosition()-layoutManager.findFirstVisibleItemPosition();
                    Log.d("ya3mya3myaaaa", lastVisible+"    "+totalItemCount);
                    if (lastVisible==0&&!Stop_Pages)
                    {
                        progressPage.setVisibility(View.VISIBLE);
                        page++;
                        getMessagesAPI();
                    }
                    else
                    {
                        progressPage.setVisibility(View.GONE);
                    }

            }
        });

        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        getMessagesAPI();
    }
    int numberOfVisibleItems=0;
    boolean Stop_Pages=false;
    public void  getMessagesAPI()
    {
        //Api_getChatMessages
        if (CheckNetworkConnection.hasInternetConnection(Chat_MessagesChat.this)) {

            //Check internet Access
            if (ConnectionDetector.hasInternetConnection(Chat_MessagesChat.this)) {
                if (!isGroup) {
                    Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                    WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                    Call<ListMessagesChatModel> MessageWhenOpenChat = whoCallerApi.getMessageWhenOpenChat(ApiAccessToken.getAPIaccessToken(Chat_MessagesChat.this), String.valueOf(receiverID),String.valueOf(chatRoomId),String.valueOf(page));


                    MessageWhenOpenChat.enqueue(new Callback<ListMessagesChatModel>() {
                        @Override
                        public void onResponse(Call<ListMessagesChatModel> call, Response<ListMessagesChatModel> response) {
                            int size=messageChatInfos.size()-1;
                            if (mProgressDialog.isShowing())
                                mProgressDialog.dismiss();
                            if (response.isSuccessful())
                            {
                                if (response.body().getMessages().size()==0)
                                {
                                    Stop_Pages=true;
                                    progressPage.setVisibility(View.GONE);
                                }
                                else
                                {
                                    messageChatInfos.addAll(0,response.body().getMessages());
                                    sortDate();
                                    if (page==1)
                                    {
                                        chatAdapter.notifyDataSetChanged();
                                    }
                                    else
                                    {
                                        chatAdapter.notifyItemRangeInserted(0,messageChatInfos.size()-1-size);
                                    }
                                    progressPage.setVisibility(View.GONE);
                                }
                            }
                            else
                                progressPage.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<ListMessagesChatModel> call, Throwable t) {
                            if (mProgressDialog.isShowing())
                                mProgressDialog.dismiss();
                            progressPage.setVisibility(View.GONE);}

                    });
                }
                else
                {
                    Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                    WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                    Call<ListMessagesChatModel> MessageWhenOpenGroupChat = whoCallerApi.getMessageWhenOpenGroupChat(ApiAccessToken.getAPIaccessToken(Chat_MessagesChat.this), String.valueOf(receiverID),String.valueOf(chatRoomId),String.valueOf(page));


                    MessageWhenOpenGroupChat.enqueue(new Callback<ListMessagesChatModel>() {
                        @Override
                        public void onResponse(Call<ListMessagesChatModel> call, Response<ListMessagesChatModel> response) {
                            if (mProgressDialog.isShowing())
                                mProgressDialog.dismiss();
                            int size=messageChatInfos.size()-1;
                            if (response.isSuccessful())
                            {
                                if (response.body().getMessages().size()==0)
                                {
                                    Stop_Pages=true;
                                    progressPage.setVisibility(View.GONE);
                                }
                                else
                                {
                                    messageChatInfos.addAll(response.body().getMessages());
                                    sortDate();
                                    sortDate();
                                    if (page==1)
                                    {
                                        chatAdapter.notifyDataSetChanged();
                                    }
                                    else
                                    {
                                        chatAdapter.notifyItemRangeInserted(0,messageChatInfos.size()-1-size);
                                    }
                                    progressPage.setVisibility(View.GONE);
                                }
                            }
                            else
                                progressPage.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<ListMessagesChatModel> call, Throwable t) {
                            if (mProgressDialog.isShowing())
                                mProgressDialog.dismiss();
                            progressPage.setVisibility(View.GONE);
                        }
                    });
                }
            }
        }
    }
    public void sortDate()
    {
        for (int i = 0; i < messageChatInfos.size(); i++) {
            if (messageChatInfos.get(i).isShowDayDate())
            {
                messageChatInfos.remove(i);
            }
        }
        long myDateDay = 0;
        for (int i = 0; i < messageChatInfos.size(); i++) {
            MessageChatModel model = messageChatInfos.get(i);
            Calendar calendar = covertTime(model.getCreated_at());
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTimeInMillis(myDateDay);
            if (calendar.get(Calendar.DAY_OF_YEAR) != calendar1.get(Calendar.DAY_OF_YEAR)) {
                myDateDay = calendar.getTimeInMillis();
                MessageChatModel newmodel = new MessageChatModel(receiverID, "", model.getCreated_at());
                newmodel.setShowDayDate(true);
                messageChatInfos.add(i, newmodel);
            }

        }
    }

    public Calendar covertTime(String strDate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = sdf.parse(strDate);
            Log.d("OPEN", String.valueOf(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    Dialog AddUserDialog;
    RecyclerView mRecyclerViewContacts;
    public AddContactsToChatGroupAdapter addContactsAdapters;
    List<RoomModel>userContactsData;
    public void openContactsDialog()
    {
        AddUserDialog = new Dialog(Chat_MessagesChat.this);
        AddUserDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        AddUserDialog.setContentView(R.layout.add_user_chat_group);
        Window window = AddUserDialog.getWindow();
        window.setLayout(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        AddUserDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mRecyclerViewContacts=AddUserDialog.findViewById(R.id.Send_To_RecyclerView);
        userContactsData=new ArrayList<>();

        //API Request
        //Check wifi or data available
        if (CheckNetworkConnection.hasInternetConnection(Chat_MessagesChat.this)) {

            //Check internet Access
            if (ConnectionDetector.hasInternetConnection(Chat_MessagesChat.this)) {
                Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                Call<DataReceivedChatUsers> MessageWhenOpenChat = whoCallerApi.getallChatContact(ApiAccessToken.getAPIaccessToken(Chat_MessagesChat.this));

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

                            addContactsAdapters=new AddContactsToChatGroupAdapter(getApplicationContext(),userContactsData,groupUsers,receiverID,name,AddUserDialog);
                            mRecyclerViewContacts.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            mRecyclerViewContacts.setAdapter(addContactsAdapters);
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
        AddUserDialog.show();
    }
}

