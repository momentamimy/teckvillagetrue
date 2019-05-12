package com.developer.whocaller.net.Controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.whocaller.net.Model.database.tables.Tags;
import com.developer.whocaller.net.UserProfileActivity;

import java.util.List;

import teckvillage.developer.khaled_pc.teckvillagetrue.R;


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