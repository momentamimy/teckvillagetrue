package com.developer.whocaller.net.Controller;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.whocaller.net.Model.retrofit.ApiAccessToken;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.GroupChatResultModel;
import com.developer.whocaller.net.R;
import com.developer.whocaller.net.View.CheckNetworkConnection;
import com.developer.whocaller.net.View.ConnectionDetector;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import com.developer.whocaller.net.Chat_MessagesChat;
import com.developer.whocaller.net.Model.Get_User_Contacts;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.BodyUpdateGroup;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.RoomModel;
import com.developer.whocaller.net.Model.retrofit.WhoCallerApi;
import com.developer.whocaller.net.Model.retrofit.retrofitHead;



/**
 * Created by khaled-pc on 2/13/2019.
 */

public class AddContactsToChatGroupAdapter extends RecyclerView.Adapter<AddContactsToChatGroupAdapter.ViewHolder>  {


    Context context;
    private List<RoomModel> mDataArray;
    Get_User_Contacts get_user_contacts;
    int[] groupUsers;
    int groupId;
    String name;
    Dialog dialog;
    public AddContactsToChatGroupAdapter(Context context, List<RoomModel> mDataArray,int groupId,String name,Dialog dialog,ProgressDialog mProgressDialog) {
        this.mDataArray=mDataArray;
        this.context=context;
        this.groupUsers=Chat_MessagesChat.groupUsers;
        this.groupId=groupId;
        this.name=name;
        this.dialog=dialog;
        this.mProgressDialog=mProgressDialog;
        get_user_contacts=new Get_User_Contacts(context);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_contacts_row, parent, false);
                    return new ViewHolder(v2);
    }

    ProgressDialog mProgressDialog;
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
            if (holder.contactName2 != null) {
                final RoomModel data = mDataArray.get(position);
                for (int i=0;i<groupUsers.length;i++)
                {
                    if (data.getId()==groupUsers[i])
                    {
                        holder.contactCircleImageView2.setImageResource(R.drawable.ic_correct);
                    }
                }


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //API Request
                        //Check wifi or data available
                        if (CheckNetworkConnection.hasInternetConnection(context)) {

                            //Check internet Access
                            if (ConnectionDetector.hasInternetConnection(context)) {
                                Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                                WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                                final int[]newGroupUsers=new int[groupUsers.length+1];
                                for (int i=0;i<groupUsers.length;i++)
                                {
                                    newGroupUsers[i]=groupUsers[i];
                                }
                                newGroupUsers[groupUsers.length]=data.getId();
                                Call<GroupChatResultModel> updateGroupChat = whoCallerApi.updateGroupChat(groupId, ApiAccessToken.getAPIaccessToken(context),new BodyUpdateGroup(name,newGroupUsers));

                                mProgressDialog.setIndeterminate(true);
                                mProgressDialog.setMessage("Loading...");
                                mProgressDialog.setCancelable(false);
                                mProgressDialog.show();
                                updateGroupChat.enqueue(new Callback<GroupChatResultModel>() {
                                    @Override
                                    public void onResponse(Call<GroupChatResultModel> call, Response<GroupChatResultModel> response) {
                                        if (response.isSuccessful())
                                        {
                                            Log.d("adadfqkopmv","success");
                                            dialog.dismiss();
                                            if (mProgressDialog.isShowing())
                                                mProgressDialog.dismiss();
                                            Chat_MessagesChat.groupUsers=newGroupUsers;
                                            Chat_MessagesChat.ChatStatusChanged=true;
                                        }
                                        else
                                        {
                                            Log.d("adadfqkopmv","not success");
                                            dialog.dismiss();
                                            if (mProgressDialog.isShowing())
                                                mProgressDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<GroupChatResultModel> call, Throwable t) {
                                        Log.d("adadfqkopmv","failed");
                                        dialog.dismiss();
                                        if (mProgressDialog.isShowing())
                                            mProgressDialog.dismiss();
                                    }
                                });
                            }
                        }
                    }
                });
                holder.contactName2.setText(data.getName());
                holder.sms.setVisibility(View.INVISIBLE);

            }
    }

    @Override
    public int getItemCount() {
        return mDataArray.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView contactCircleImageView2;
        TextView contactName2;
        TextView country2;
        TextView letter;
        ImageView sms;
          ViewHolder(View itemView) {
            super(itemView);
            contactCircleImageView2=itemView.findViewById(R.id.usercontactimg);
            contactName2=itemView.findViewById(R.id.nameofcontacts);
            country2=itemView.findViewById(R.id.country);
            letter=itemView.findViewById(R.id.letter);
            sms=itemView.findViewById(R.id.chat_user_contact);
        }
    }

}