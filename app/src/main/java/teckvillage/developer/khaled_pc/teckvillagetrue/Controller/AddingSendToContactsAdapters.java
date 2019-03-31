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
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.SMS_MessagesChat;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.Get_User_Contacts;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.UserContactData;

import static teckvillage.developer.khaled_pc.teckvillagetrue.SendToActivity.showTextHint;


/**
 * Created by khaled-pc on 2/13/2019.
 */

public class AddingSendToContactsAdapters extends RecyclerView.Adapter<AddingSendToContactsAdapters.ViewHolder>  {


    Context context;
    private List<UserContactData> mDataArray;
    public AddingSendToContactsAdapters(Context context, List<UserContactData> mDataArray) {
        this.mDataArray=mDataArray;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_adding_send_to_contact, parent, false);
                return new ViewHolder(v2);
        }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final UserContactData contactData=mDataArray.get(position);
        holder.contactName2.setText(contactData.usercontacName);
        holder.removeContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int myPosition=mDataArray.indexOf(contactData);
                mDataArray.remove(contactData);
                notifyItemRemoved(myPosition);
                showTextHint();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataArray.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView contactCircleImageView2;
        TextView contactName2;
        ImageView removeContact;
        ViewHolder(View itemView) {
            super(itemView);
            contactCircleImageView2=itemView.findViewById(R.id.contact_img);
            contactName2=itemView.findViewById(R.id.contact_name);
            removeContact=itemView.findViewById(R.id.remove_Contact);
        }
    }

}