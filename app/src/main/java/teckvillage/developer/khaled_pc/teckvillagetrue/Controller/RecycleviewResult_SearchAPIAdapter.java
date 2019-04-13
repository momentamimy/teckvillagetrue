package teckvillage.developer.khaled_pc.teckvillagetrue.Controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.RecycleviewResult_SearchAPIHolder;
import teckvillage.developer.khaled_pc.teckvillagetrue.SMS_MessagesChat;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.LogHolder;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.User_Contact_Profile;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.User_Contact_Profile_From_log_list;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.JSON_Mapping.Item_Search;

/**
 * Created by khaled-pc on 4/13/2019.
 */

public class RecycleviewResult_SearchAPIAdapter extends  RecyclerView.Adapter<RecycleviewResult_SearchAPIHolder>  {

    private List<Item_Search> itemList;
    Context context;


    public RecycleviewResult_SearchAPIAdapter(Context context, List<Item_Search> itemList)
    {
        this.context=context;
        this.itemList=itemList;

    }

    @Override
    public RecycleviewResult_SearchAPIHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_contacts_row, null);
        RecycleviewResult_SearchAPIHolder rbv = new RecycleviewResult_SearchAPIHolder(layoutView);
        return rbv;
    }

    @Override
    public void onBindViewHolder(RecycleviewResult_SearchAPIHolder holder, final int position) {



        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)holder.contactNamee.getLayoutParams();

        if(itemList.get(position).getPhone()!=null&&itemList.get(position).getPhone().length()>0){
            holder.countryy.setVisibility(View.VISIBLE);
            holder.countryy.setText(itemList.get(position).getPhone());//Display phone or country
            holder.contactNamee.setText(itemList.get(position).getName());//Display name
            // if false remove center:
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
            holder.contactNamee.setLayoutParams(layoutParams);

        }else {
            holder.countryy.setVisibility(View.GONE);
            holder.contactNamee.setText(itemList.get(position).getName());//Display name
            //set contactName Center in parent if country visiable
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            holder.contactNamee.setLayoutParams(layoutParams);
        }



        holder.container_chaticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(context,SMS_MessagesChat.class);
                intent.putExtra("LogSMSName",itemList.get(position).getName());
                intent.putExtra("LogSMSAddress",itemList.get(position).getPhone());
                context.startActivity(intent);

            }
        });

        holder.openProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(context,User_Contact_Profile_From_log_list.class);
                intent2.putExtra("ContactNUm",itemList.get(position).getPhone());
                context.startActivity(intent2);
            }
        });


    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }



}
