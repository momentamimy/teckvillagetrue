package teckvillage.developer.khaled_pc.teckvillagetrue.Controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CallLog;
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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import teckvillage.developer.khaled_pc.teckvillagetrue.Make_Phone_Call;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.SMS_MessagesChat;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.LogHolder;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.User_Contact_Profile_From_log_list;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.LogInfo;

public class LogAdapter extends RecyclerView.Adapter<LogHolder> {
    private static final int TYPE_DATE = 1;
    private static final int TYPE_ITEM = 2;
    private List<LogInfo> itemList;
    private Context context;
    int numofcallvar;




    public LogAdapter(Context context, List<LogInfo> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public LogHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case TYPE_DATE:
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_section_for_date_recycler, null);
                LogHolder rbv = new LogHolder(layoutView);
                return rbv;
            case TYPE_ITEM:
                View layoutView2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_of_logs_recycleview, null);
                RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutView2.setLayoutParams(lp);
                LogHolder rbv2 = new LogHolder(layoutView2);
                return rbv2;
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
            viewType = TYPE_ITEM;
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
                    if(itemList.get(position).getNumber().equals("")){
                        OptionDialogWhenLongPressForUnknownNumber( itemList.get(position).getLogName(), position);
                    }else {
                        OptionDialogWhenLongPress(itemList.get(position).getLogName(),position);
                    }

                    return false;
                }
            });

            //assign long press
            holder.calllayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    //check if number unknown
                    if(itemList.get(position).getNumber().equals("")){
                        OptionDialogWhenLongPressForUnknownNumber( itemList.get(position).getLogName(), position);
                    }else {
                        OptionDialogWhenLongPress(itemList.get(position).getLogName(),position);
                    }

                    return false;
                }
            });

            numofcallvar=itemList.get(position).getNumberofcall();

            if(itemList.get(position).getNumberofcall()==1){
                holder.numbersofcallinminte.setVisibility(View.GONE);
            }else {
                holder.numbersofcallinminte.setVisibility(View.VISIBLE);
                holder.numbersofcallinminte.setText("("+itemList.get(position).getNumberofcall()+")");
            }


            holder.calllayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE},12);
                    }
                    else
                    {
                        Make_Phone_Call.makephonecall(context,itemList.get(position).getNumber());
                    }

                }
            });


            holder.chaticon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(context, "You Click me", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context,SMS_MessagesChat.class);
                    intent.putExtra("LogSMSName",itemList.get(position).logName);
                    String num=itemList.get(position).getNumber();
                    if(num != null&& !num.isEmpty()){
                        intent.putExtra("LogSMSAddress",num);
                        context.startActivity(intent);
                    }else {
                        Toast.makeText(context, "Can't send message to this number", Toast.LENGTH_LONG).show();
                    }

                }
            });

            holder.infoicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(context, "You Click me", Toast.LENGTH_LONG).show();
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

            if (itemList.get(position).imageUrl != null) {
                Picasso.with(context)
                        .load(itemList.get(position).imageUrl)
                        .into(holder.logCircleImageView, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                holder.progressBar.setVisibility(View.GONE);
                                holder.progressCircleImageView.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                holder.logCircleImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nurse));
                                holder.progressBar.setVisibility(View.GONE);
                                holder.progressCircleImageView.setVisibility(View.GONE);
                            }
                        });
            }
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
    private void DeleteCallLogByCall_ID(ArrayList<Long> arrayList) {
        try {
            for (int i = 0; i < arrayList.size(); i++) {
                context.getContentResolver().delete(Uri.withAppendedPath(CallLog.Calls.CONTENT_URI, String.valueOf( arrayList.get(i))), "", null);
            }
            notifyDataSetChanged();
        }
        catch (Exception ex) {
            System.out.print("Exception here "+ex.getMessage());
            ex.printStackTrace();
        }
    }


}
