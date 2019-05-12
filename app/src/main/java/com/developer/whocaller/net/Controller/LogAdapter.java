package com.developer.whocaller.net.Controller;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.developer.whocaller.net.Make_Phone_Call;
import com.developer.whocaller.net.Model.retrofit.ApiAccessToken;
import com.developer.whocaller.net.OpenDialPad;
import com.developer.whocaller.net.SMS_MessagesChat;
import com.developer.whocaller.net.View.User_Contact_Profile_From_log_list;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.BodyNumbersListModel;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.FetchedUserData;
import com.developer.whocaller.net.Model.retrofit.WhoCallerApi;
import com.developer.whocaller.net.Model.retrofit.retrofitHead;

import teckvillage.developer.khaled_pc.teckvillagetrue.R;

import com.developer.whocaller.net.View.LogHolder;
import com.developer.whocaller.net.Model.LogInfo;

public class LogAdapter extends RecyclerView.Adapter<LogHolder> {
    private static final int TYPE_DATE = 1;
    private static final int TYPE_ITEM = 2;
    private static final int MY_WRITE_LOG_REQUEST_CODE =221 ;
    private static final int TYPE_ITEM_Feched = 3;
    private static final int TYPE_ITEM_NotFeched = 4;
    private List<LogInfo> itemList;
    private Context context;
    int numofcallvar;

    private OpenDialPad openDialPad;

    String[] Numbers;

    public LogAdapter(Context context, List<LogInfo> itemList,OpenDialPad openDialPad) {
        this.context = context;
        this.itemList = itemList;
        this.openDialPad = openDialPad;

        Numbers=new String[itemList.size()];
        Log.d("dadqfqfcsdv", ""+itemList.size());
        for (int i=0;i<itemList.size();i++)
        {
            if (!TextUtils.isEmpty(itemList.get(i).logName))
            {
                if (itemList.get(i).logName.equals(itemList.get(i).getNumber()))
                {
                    Numbers[i]=itemList.get(i).getNumber();
                    itemList.get(i).setNeedToFech(true);
                }
            }
        }
        if (Numbers.length!=0)
        {
            getUserDataApi(context,Numbers);
        }

    }

    public LogAdapter(Context context, List<LogInfo> itemList) {
        this.context = context;
        this.itemList = itemList;


        Numbers=new String[itemList.size()];
        for (int i=0;i<itemList.size();i++)
        {
            if (!TextUtils.isEmpty(itemList.get(i).logName))
            {
                if (itemList.get(i).getNumber().equals(itemList.get(i).logName))
                {
                    Numbers[i]=itemList.get(i).getNumber();
                    itemList.get(i).setNeedToFech(true);
                }
            }
        }
        if (Numbers.length!=0)
        {
            getUserDataApi(context,Numbers);
        }
    }

    @Override
    public LogHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case TYPE_DATE:
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_section_for_date_recycler, null);
                LogHolder rbv = new LogHolder(layoutView);
                return rbv;
            case TYPE_ITEM:
                View layoutView1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_of_logs_recycleview, null);
                RecyclerView.LayoutParams lp1 = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutView1.setLayoutParams(lp1);
                LogHolder rbv1 = new LogHolder(layoutView1);
                return rbv1;
            case TYPE_ITEM_Feched:
                View layoutView2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_of_logs_recycleview, null);
                RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutView2.setLayoutParams(lp);
                LogHolder rbv2 = new LogHolder(layoutView2);
                return rbv2;
            case TYPE_ITEM_NotFeched:
                View layoutView4 = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_of_logs_recycleview, null);
                RecyclerView.LayoutParams lp4= new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutView4.setLayoutParams(lp4);
                LogHolder rbv4 = new LogHolder(layoutView4);
                return rbv4;
            default:
                View layoutView3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_of_logs_recycleview, null);
                RecyclerView.LayoutParams lp2 = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutView3.setLayoutParams(lp2);
                LogHolder rbv3 = new LogHolder(layoutView3);
                return rbv3;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if (itemList.get(position).getType() == TYPE_DATE) {
            viewType = TYPE_DATE;
        } else if (itemList.get(position).getType() == TYPE_ITEM) {
            if (itemList.get(position).isNeedToFech())
            {
                if (itemList.get(position).isUserFeched()){
                    viewType = TYPE_ITEM_Feched;
                }else{
                    viewType = TYPE_ITEM_NotFeched;
                }
            }
            else
            {
                viewType =TYPE_ITEM;
            }
        }

        return viewType;
    }


    @Override
    public void onBindViewHolder(final LogHolder holder, final int position) {


        if (holder.logName != null) {

            //assign long press
            holder.item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    //check if number  unknown
                    if (itemList.get(position).getNumber().equals("")) {
                        OptionDialogWhenLongPressForUnknownNumber(itemList.get(position).getLogName(), position);
                    } else {
                        OptionDialogWhenLongPress(itemList.get(position).getLogName(), position);
                    }

                    return false;
                }
            });

            //assign long press
            holder.calllayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    //check if number unknown
                    if (itemList.get(position).getNumber().equals("")) {
                        OptionDialogWhenLongPressForUnknownNumber(itemList.get(position).getLogName(), position);
                    } else {
                        OptionDialogWhenLongPress(itemList.get(position).getLogName(), position);
                    }

                    return false;
                }
            });

            numofcallvar = itemList.get(position).getNumberofcall();

            if (itemList.get(position).getNumberofcall() == 1) {
                holder.numbersofcallinminte.setVisibility(View.GONE);
            } else {
                holder.numbersofcallinminte.setVisibility(View.VISIBLE);
                holder.numbersofcallinminte.setText("(" + itemList.get(position).getNumberofcall() + ")");
            }


            holder.calllayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 12);
                    } else {
                        Make_Phone_Call.makephonecall(context, itemList.get(position).getNumber());
                    }

                }
            });


            holder.chaticon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(context, "You Click me", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, SMS_MessagesChat.class);
                    intent.putExtra("LogSMSName", itemList.get(position).logName);
                    String num = itemList.get(position).getNumber();
                    if (num != null && !num.isEmpty()) {
                        intent.putExtra("LogSMSAddress", num);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "Can't send message to this number", Toast.LENGTH_LONG).show();
                    }

                }
            });

            holder.infoicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(context, "You Click me", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, User_Contact_Profile_From_log_list.class);
                    String num = itemList.get(position).getNumber();
                    if (num != null && !num.isEmpty()) {
                        intent.putExtra("ContactNUm", num);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "Can't open profile to this number", Toast.LENGTH_LONG).show();
                    }
                }
            });


            holder.logName.setText(itemList.get(position).logName);
            if (!TextUtils.isEmpty(itemList.get(position).logIcon)) {
                switch (itemList.get(position).logIcon) {
                    case "INCOMING":
                        holder.logIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_call_received_arrow));
                        break;
                    case "OUTGOING":
                        holder.logIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_right_up));
                        break;
                    case "MISSED":
                        holder.logIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_call_missed));
                        break;
                    case "REJECTED":
                        holder.logIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_call_received_arrow));
                        break;
                    case "VOICEMAIL":
                        holder.logIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_voicemail));
                        break;
                    case "BLOCKED":
                        holder.logIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_blocked));
                        break;
                    case "ANSWERED":
                        holder.logIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_call_received_arrow));
                        break;
                }
            }

            holder.logDate.setText(itemList.get(position).hour);
            holder.callType.setText(itemList.get(position).getNumberType());
            LogInfo data =itemList.get(position);

            updatePhoto(data,holder);

        }


        if (holder.datesection != null) {
            holder.datesection.setText(itemList.get(position).getDateString());
        }



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }




    private void OptionDialogWhenLongPress(String title, final int position){
        final String[] Option = {"Edit number before call", "Delete from call log"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(Option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                if(Option[which].equals("Edit number before call")){
                  //open Dialer padd and set number
                    String number =itemList.get(position).getNumber();
                    Log.w("nmindialerpad",number);
                    //open dial pad
                    openDialPad.onClickEditNumber(number);


                }else if(Option[which].equals("Delete from call log")) {
                    DeleteCallLogByCall_ID(itemList.get(position).getCall_idarray());
                }

            }
        });
        builder.show();

    }

    private void OptionDialogWhenLongPressForUnknownNumber(String title, final int position){
        final String[] Option = { "Delete from call log"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(Option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on Option[which]
               if(Option[which].equals("Delete from call log")) {
                   DeleteCallLogByCall_ID(itemList.get(position).getCall_idarray());
                }

            }
        });
        builder.show();

    }



    //For delette speific number from call log
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void DeleteCallLogByCall_ID(ArrayList<Long> arrayList) {

        try {
            PackageManager pm = context.getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.WRITE_CALL_LOG, context.getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                for (int i = 0; i < arrayList.size(); i++) {
                    //context.getContentResolver().delete(Uri.withAppendedPath(CallLog.Calls.CONTENT_URI, String.valueOf(arrayList.get(i))), "", null);
                    int res = context.getContentResolver().delete(android.provider.CallLog.Calls.CONTENT_URI,"_ID = "+ String.valueOf(arrayList.get(i)),null);
                    if (res == 1) {
                        // Log delete
                        Log.w("LogDeleted","LogDeleted");

                    } else {
                        // Log not Delete
                        Log.w("NotDeleted","NotDeleted");
                    }
                }
                notifyDataSetChanged();
            }else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.WRITE_CALL_LOG}, MY_WRITE_LOG_REQUEST_CODE);
            }
        }
        catch (Exception ex) {
            System.out.print("Exception here "+ex.getMessage());
            ex.printStackTrace();
        }

    }

    public void removeAt(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemList.size());
    }

    List<FetchedUserData> fetchedUserDataList=new ArrayList<>();
    public void getUserDataApi(final Context context, String[] numbers) {

                BodyNumbersListModel bodyNumbersListModel = new BodyNumbersListModel(numbers);
                Retrofit retrofit = retrofitHead.retrofitRequestCash(context);
                WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                Call<List<FetchedUserData>> usersDataCall = whoCallerApi.fetchUsersData(ApiAccessToken.getAPIaccessToken(context), bodyNumbersListModel);

                usersDataCall.enqueue(new Callback<List<FetchedUserData>>() {
                    @Override
                    public void onResponse(Call<List<FetchedUserData>> call, Response<List<FetchedUserData>> response) {
                        if (response.raw().cacheResponse() != null) {
                            Log.e("MyNetwork", "response came from cache");
                        }

                        if (response.raw().networkResponse() != null) {
                            Log.e("MyNetwork", "response came from server");
                        }

                        if (response.isSuccessful())
                        {
                            for (int i=0;i<response.body().size();i++)
                            Log.d("userNamePaleeez", response.body().get(i).getName()+"  "+response.body().get(i).getPhone());
                            List<FetchedUserData> resp =response.body();
                            fetchedUserDataList=resp;

                            //Collections.reverse(fetchedUserDataList);
                            if (fetchedUserDataList.size()!=0) {
                                for (int i=0;i<itemList.size();i++) {
                                    if (itemList.get(i).isNeedToFech()) {
                                        for (int j=0;j<fetchedUserDataList.size();j++) {
                                            if (itemList.get(i).getNumber().equals(fetchedUserDataList.get(j).getFull_phone())
                                            || itemList.get(i).getNumber().equals(fetchedUserDataList.get(j).getPhone())) {

                                                if (!TextUtils.isEmpty(fetchedUserDataList.get(j).getName()))
                                                {
                                                    itemList.get(i).setLogName(fetchedUserDataList.get(j).getName());
                                                }
                                                if (!TextUtils.isEmpty(fetchedUserDataList.get(j).getUser_img()))
                                                {
                                                    itemList.get(i).setImageUrl(fetchedUserDataList.get(j).getUser_img());
                                                    itemList.get(i).setUserFeched(true);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            notifyItemRangeChanged(0,itemList.size()-1);
                        }
                        else
                        {
                            Log.d("onFailure", "other error");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<FetchedUserData>> call, Throwable t) {
                       // Log.d("onFailure", t.getMessage());
                    }
                });


    }

    public void updatePhoto(LogInfo data, final LogHolder holder)
    {
        if (!TextUtils.isEmpty(data.imageUrl))
        {
            if (!data.imageUrl.equals(""))
            {
                if (holder.getItemViewType()==TYPE_ITEM_Feched)
                {
                    Picasso.with(context).load("http://whocaller.net/whocallerAdmin/uploads/"+data.imageUrl)
                            .fit().centerInside()
                            .into(holder.logCircleImageView, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {

                                }
                                @Override
                                public void onError() {
                                    holder.logCircleImageView.setImageResource(R.drawable.ic_nurse);
                                }
                            });
                }
            }
        }
    }
}
