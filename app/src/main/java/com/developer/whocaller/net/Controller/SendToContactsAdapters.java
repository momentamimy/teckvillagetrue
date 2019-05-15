package com.developer.whocaller.net.Controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.whocaller.net.Model.UserContactData;
import com.developer.whocaller.net.SMS_MessagesChat;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import com.developer.whocaller.net.R;

import com.developer.whocaller.net.Model.Get_User_Contacts;

import static com.developer.whocaller.net.SendToActivity.MultipleRecivers;
import static com.developer.whocaller.net.SendToActivity.addingMulipleUserContacts;


/**
 * Created by khaled-pc on 2/13/2019.
 */

public class SendToContactsAdapters extends RecyclerView.Adapter<SendToContactsAdapters.ViewHolder>  {


    Context context;
    private List<UserContactData> mDataArray;
    Get_User_Contacts get_user_contacts;
    public SendToContactsAdapters(Context context, List<UserContactData> mDataArray) {
        this.mDataArray=mDataArray;
        this.context=context;
        get_user_contacts=new Get_User_Contacts(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataArray.get(position).getType()==2)
        {
            return 2;
        }
        else if (mDataArray.get(position).getType()==3)
        {
            return 3;
        }
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                if (viewType==3)
                {
                    View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact_on_whocaller, parent, false);
                    return new ViewHolder(v2);
                }
                else if (viewType==2)
                {
                    View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact_not_on_whocaller, parent, false);
                    return new ViewHolder(v2);
                }
                else
                {
                    View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_contacts_row, parent, false);
                    return new ViewHolder(v2);
                }
        }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder.getItemViewType()==2||holder.getItemViewType()==3){

        }
        else {
            if (holder.contactName2 != null) {
                final UserContactData data = mDataArray.get(position);
                final String name = mDataArray.get(position).usercontacName;
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MultipleRecivers == false) {
                            final String num = get_user_contacts.getPhoneNumber(mDataArray.get(position).usercontacName, context).replace(" ", "");
                            Intent intent = new Intent(context, SMS_MessagesChat.class);
                            intent.putExtra("LogSMSName", name);
                            intent.putExtra("LogSMSAddress", num);
                            context.startActivity(intent);

                        } else if (MultipleRecivers = true) {
                            addingMulipleUserContacts(context, data);
                        }
                    }
                });
                holder.contactName2.setText(mDataArray.get(position).usercontacName);
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