package com.developer.whocaller.net.Controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.whocaller.net.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import com.developer.whocaller.net.Chat_MessagesChat;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.RoomModel;

import com.developer.whocaller.net.Model.Get_User_Contacts;

import static com.developer.whocaller.net.View.SendToChatActivity.MultipleRecivers;
import static com.developer.whocaller.net.View.SendToChatActivity.addingMulipleUserContacts;


/**
 * Created by khaled-pc on 2/13/2019.
 */

public class SendToChatContactsAdapters extends RecyclerView.Adapter<SendToChatContactsAdapters.ViewHolder>  {


    Context context;
    private List<RoomModel> mDataArray;
    Get_User_Contacts get_user_contacts;
    public SendToChatContactsAdapters(Context context, List<RoomModel> mDataArray) {
        this.mDataArray=mDataArray;
        this.context=context;
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

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder.getItemViewType()==2||holder.getItemViewType()==3){

        }
        else {
            if (holder.contactName2 != null) {
                final RoomModel data = mDataArray.get(position);
                Picasso.with(context).load("http://whocaller.net/whocallerAdmin/uploads/"+data.getImg())
                        .fit().centerInside()
                        .into(holder.contactCircleImageView2, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() { }
                            @Override
                            public void onError() { holder.contactCircleImageView2.setImageResource(R.drawable.ic_nurse); }
                        });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MultipleRecivers == false) {

                            Intent intent = new Intent(context, Chat_MessagesChat.class);
                            intent.putExtra("UserName", data.getName());
                            intent.putExtra("UserAddress", data.getPhone());
                            intent.putExtra("UserID", data.getId());
                            intent.putExtra("ChatID", data.getChatRoomId());
                            intent.putExtra("UserImage", data.getImg());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                        } else if (MultipleRecivers = true) {
                            addingMulipleUserContacts(context, data);
                        }
                    }
                });
                holder.contactName2.setText(mDataArray.get(position).getName());
                holder.sms.setVisibility(View.INVISIBLE);
                holder.contactCircleImageView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    /*
                    mDataArray.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();
                    */
                    }
                });
            }
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