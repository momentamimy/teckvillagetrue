package teckvillage.developer.khaled_pc.teckvillagetrue.Controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.UserContactData;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.JSON_Mapping.DataReceived;

import static teckvillage.developer.khaled_pc.teckvillagetrue.View.SendToChatActivity.showTextHint;


/**
 * Created by khaled-pc on 2/13/2019.
 */

public class AddingSendToChatContactsAdapters extends RecyclerView.Adapter<AddingSendToChatContactsAdapters.ViewHolder>  {


    Context context;
    private List<DataReceived> mDataArray;
    public AddingSendToChatContactsAdapters(Context context, List<DataReceived> mDataArray) {
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
        final DataReceived contactData=mDataArray.get(position);
        holder.contactName2.setText(contactData.getName());
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