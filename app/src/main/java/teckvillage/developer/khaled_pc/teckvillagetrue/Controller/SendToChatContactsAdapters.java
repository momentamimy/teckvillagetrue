package teckvillage.developer.khaled_pc.teckvillagetrue.Controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import teckvillage.developer.khaled_pc.teckvillagetrue.Chat_MessagesChat;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.RoomModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.Get_User_Contacts;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.DataReceived;

import static teckvillage.developer.khaled_pc.teckvillagetrue.View.SendToChatActivity.MultipleRecivers;
import static teckvillage.developer.khaled_pc.teckvillagetrue.View.SendToChatActivity.addingMulipleUserContacts;


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
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MultipleRecivers == false) {
                            final String num = get_user_contacts.getPhoneNumber(mDataArray.get(position).getName(), context).replace(" ", "");
                            Intent intent = new Intent(context, Chat_MessagesChat.class);
                            intent.putExtra("UserName", data.getName());
                            //intent.putExtra("UserAddress", data.getFull_phone());
                            intent.putExtra("UserID", data.getId());
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