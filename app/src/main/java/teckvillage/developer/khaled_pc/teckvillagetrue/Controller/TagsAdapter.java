package teckvillage.developer.khaled_pc.teckvillagetrue.Controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import teckvillage.developer.khaled_pc.teckvillagetrue.Chat_MessagesChat;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.Get_User_Contacts;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.database.tables.Tags;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.RoomModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.UserProfileActivity;

import static teckvillage.developer.khaled_pc.teckvillagetrue.View.SendToChatActivity.MultipleRecivers;
import static teckvillage.developer.khaled_pc.teckvillagetrue.View.SendToChatActivity.addingMulipleUserContacts;


/**
 * Created by khaled-pc on 2/13/2019.
 */

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder>  {

    List<Tags> mDataArray;
    static Context context;

    public TagsAdapter(Context context, List<Tags> mDataArray) {
        this.mDataArray=mDataArray;
        this.context=context;

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tag, parent, false);
                    return new ViewHolder(v2);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tagiem.setText(mDataArray.get(position).getTagname());

    }

    @Override
    public int getItemCount() {
        return mDataArray.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tagiem;

          ViewHolder(View itemView) {
            super(itemView);

            tagiem=itemView.findViewById(R.id.itemtagtextview);
            tagiem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((UserProfileActivity) context).ItemClick(getAdapterPosition());
                }
            });

        }
    }

}