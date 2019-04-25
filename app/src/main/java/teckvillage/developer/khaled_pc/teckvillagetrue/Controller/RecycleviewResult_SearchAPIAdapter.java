package teckvillage.developer.khaled_pc.teckvillagetrue.Controller;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import teckvillage.developer.khaled_pc.teckvillagetrue.View.User_Contact_Profile_From_log_list;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.Item_Search;

/**
 * Created by khaled-pc on 4/13/2019.
 */

public class RecycleviewResult_SearchAPIAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private List<Item_Search> itemList;
    Context context;

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private boolean isLoadingAdded = false;


    public RecycleviewResult_SearchAPIAdapter(Context context, List<Item_Search> itemList)
    {
        this.context=context;
        this.itemList=itemList;

    }

    public RecycleviewResult_SearchAPIAdapter(Context context) {
        this.context = context;
        this.itemList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       /* View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_contacts_row, null);
        RecycleviewResult_SearchAPIHolder rbv = new RecycleviewResult_SearchAPIHolder(layoutView);
        return rbv;*/

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new RecycleviewResult_SearchAPIHolder(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.user_contacts_row, parent, false);
        viewHolder =  new RecycleviewResult_SearchAPIHolder(v1);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        switch (getItemViewType(position)) {
            case ITEM:

                RecycleviewResult_SearchAPIHolder ContactVH = (RecycleviewResult_SearchAPIHolder) holder;

                //movieVH.textView.setText(movie.getTitle());
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)ContactVH.contactNamee.getLayoutParams();

                if(itemList.get(position).getPhone()!=null&&itemList.get(position).getPhone().length()>0){
                    ContactVH.countryy.setVisibility(View.VISIBLE);
                    ContactVH.countryy.setText(itemList.get(position).getPhone());//Display phone or country
                    if(itemList.get(position).getName()!=null){
                        ContactVH.contactNamee.setText(itemList.get(position).getName());//Display name
                        // if false remove center:
                        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
                        ContactVH.contactNamee.setLayoutParams(layoutParams);

                    }

                }else {
                    ContactVH.countryy.setVisibility(View.GONE);
                    if(itemList.get(position).getName()!=null){
                        ContactVH.contactNamee.setText(itemList.get(position).getName());//Display name
                        //set contactName Center in parent if country visiable
                        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                        ContactVH.contactNamee.setLayoutParams(layoutParams);
                    }

                }



                if(itemList.get(position).getPhone()!=null&&itemList.get(position).getPhone().length()>0){

                    ContactVH.container_chaticon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context,SMS_MessagesChat.class);
                            intent.putExtra("LogSMSName",itemList.get(position).getName());
                            intent.putExtra("LogSMSAddress",itemList.get(position).getPhone());
                            context.startActivity(intent);

                        }
                    });

                    ContactVH.openProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent2 = new Intent(context,User_Contact_Profile_From_log_list.class);
                            intent2.putExtra("ContactNUm",itemList.get(position).getPhone());
                            context.startActivity(intent2);
                        }
                    });



                }else {

                    if(itemList.get(position).getFull_phone()!=null&&itemList.get(position).getFull_phone().length()>0){
                        ContactVH.container_chaticon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context,SMS_MessagesChat.class);
                                intent.putExtra("LogSMSName",itemList.get(position).getName());
                                intent.putExtra("LogSMSAddress",itemList.get(position).getFull_phone());
                                context.startActivity(intent);

                            }
                        });

                        ContactVH.openProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent2 = new Intent(context,User_Contact_Profile_From_log_list.class);
                                intent2.putExtra("ContactNUm",itemList.get(position).getFull_phone());
                                context.startActivity(intent2);
                            }
                        });

                    }else {
                        Toast.makeText(context,"No Phone Number To This Name",Toast.LENGTH_SHORT).show();
                    }

                }



                break;
            case LOADING:
                      // Do nothing
                break;
        }




    }



    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == itemList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public List<Item_Search> getItemList() {
        return itemList;

    }

    public void setItemList(List<Item_Search> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

      /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(Item_Search mc) {
        itemList.add(mc);
        notifyItemInserted(itemList.size() - 1);
    }

    public void addAll(List<Item_Search> mcList) {
        for (Item_Search mc : mcList) {
            add(mc);
        }
    }

    public void remove(Item_Search city) {
        int position = itemList.indexOf(city);
        if (position > -1) {
            itemList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Item_Search());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = itemList.size() - 1;
        Item_Search item = getItem(position);

        if (item != null) {
            itemList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Item_Search getItem(int position) {
        return itemList.get(position);
    }
}
