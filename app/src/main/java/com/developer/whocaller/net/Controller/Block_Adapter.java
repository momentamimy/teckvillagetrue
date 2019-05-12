package com.developer.whocaller.net.Controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import com.developer.whocaller.net.View.BlockList;
import com.developer.whocaller.net.View.Block_List_Holder;
import com.developer.whocaller.net.View.User_Contact_Profile_From_log_list;
import com.developer.whocaller.net.Model.database.Database_Helper;
import com.developer.whocaller.net.Model.database.tables.block;

public class Block_Adapter extends RecyclerView.Adapter<Block_List_Holder> {

    private List<block> itemList;
    Context context;
    Database_Helper db;

    public Block_Adapter(Context context, List<block> itemList)
    {
        this.context=context;
        this.itemList=itemList;

    }

    @Override
    public Block_List_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_block_list_recycle, null);
        Block_List_Holder rbv = new Block_List_Holder(layoutView);
        return rbv;
    }

    @Override
    public void onBindViewHolder(Block_List_Holder holder, final int position) {

           String name=itemList.get(position).getName();
           if(name != null&&name.length()>0){
               holder.blocklistphonename.setText(name);
           }else {
               holder.blocklistphonename.setText(itemList.get(position).getNumber());
           }

          holder.phonenumber_block.setText(itemList.get(position).getNumber());
          //Remove from block list
          holder.removefromblock.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  onDeleteClick(context, itemList.get(position).getName(),itemList.get(position).getId(), position);

              }
          });

          holder.layoutcontainer.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(context,User_Contact_Profile_From_log_list.class);
                  String num=itemList.get(position).getNumber();
                  if(num != null&& !num.isEmpty()){
                      intent.putExtra("ContactNUm",num);
                      context.startActivity(intent);
                  }else {
                      Toast.makeText(context, "Can't open profile to this number", Toast.LENGTH_LONG).show();
                  }
              }
          });

        }


    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public void onDeleteClick(final Context context, String number, final int id, final int position) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage("Unblock"+"'"+number+"' ?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                db=new Database_Helper(context);
                db.deleteBlockByID(id);
                itemList.remove(position);
                notifyDataSetChanged();
                Update(itemList.size());
                dialog.dismiss();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    public void Update(int size){
        TextView txtView = (TextView) ((BlockList)context).findViewById(R.id.emtyrecycleblocklist);
        if (size <= 0) {

            txtView.setVisibility(View.VISIBLE);

        }

    }

}
