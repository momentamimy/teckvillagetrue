package com.developer.whocaller.net.Controller;

import android.content.Context;
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
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.RoomModel;


import static com.developer.whocaller.net.View.SendToChatActivity.showTextHint;


/**
 * Created by khaled-pc on 2/13/2019.
 */

public class AddingSendToChatContactsAdapters extends RecyclerView.Adapter<AddingSendToChatContactsAdapters.ViewHolder>  {


    Context context;
    private List<RoomModel> mDataArray;
    public AddingSendToChatContactsAdapters(Context context, List<RoomModel> mDataArray) {
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
        final RoomModel contactData=mDataArray.get(position);
        holder.contactName2.setText(contactData.getName());
        Picasso.with(context).load("http://whocaller.net/whocallerAdmin/uploads/"+contactData.getImg())
                .fit().centerInside()
                .into(holder.contactCircleImageView2, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() { }
                    @Override
                    public void onError() { holder.contactCircleImageView2.setImageResource(R.drawable.ic_nurse); }
                });
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