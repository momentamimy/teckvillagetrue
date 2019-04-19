package teckvillage.developer.khaled_pc.teckvillagetrue.Controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import teckvillage.developer.khaled_pc.teckvillagetrue.Chat_MessagesChat;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.LastMessageModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.MessageInfo;
import teckvillage.developer.khaled_pc.teckvillagetrue.SMS_MessagesChat;

public class CustomRecyclerViewChatAdapter extends RecyclerView.Adapter<CustomRecyclerViewChatAdapter.ViewHolder>{

    private List<LastMessageModel> dataSet;
    Context mContext;

    public CustomRecyclerViewChatAdapter(List<LastMessageModel> data, Context context) {
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        LastMessageModel info=dataSet.get(position);

        if (info.getType().equals("group"))
            return 1;
        else if (info.getType().equals("user"))
            return 0;
        else return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View convertView = inflater.inflate(R.layout.row_message, parent, false);
        ViewHolder viewHolder=new ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final LastMessageModel dataModel =dataSet.get(position);
        holder.logName.setText(dataModel.getName());
        holder.textMessage.setText(dataModel.getMessage());

        Calendar calendar=covertTime(dataModel.getDate().getDate());
        Calendar calendar1=Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_YEAR) == calendar1.get(Calendar.DAY_OF_YEAR) &&
                calendar.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR))
        {
            holder.messageDate.setText(String.format("%1$tI:%1$tM %1$Tp", calendar));
        }
        else if (calendar.get(Calendar.DAY_OF_YEAR) == calendar1.get(Calendar.DAY_OF_YEAR)-1 &&
                calendar.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR))
        {
            holder.messageDate.setText("yesterday");
        }
        else if (calendar.get(Calendar.DAY_OF_YEAR) > calendar1.get(Calendar.DAY_OF_YEAR)-5 &&
                calendar.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR))
        {
            holder.messageDate.setText(String.format("%1$tA", calendar));
        }
        else
        {
            holder.messageDate.setText(String.format("%1$tb %1$td %1$tY", calendar));
        }

        if (holder.getItemViewType()==0)
        {

            Picasso.with(mContext).load("http://whocaller.net/uploads/"+dataModel.getImg())
                    .into(holder.contactPhoto, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            holder.contactPhoto.setImageResource(R.drawable.ic_nurse);
                        }
                    });
        }
        else
        {
            holder.contactPhoto.setImageResource(R.drawable.ic_groupchat);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getItemViewType()==0)
                {
                    Intent intent = new Intent(mContext, Chat_MessagesChat.class);
                    intent.putExtra("UserName", dataModel.getName());
                    intent.putExtra("UserAddress", dataModel.getPhone());
                    intent.putExtra("UserID", dataModel.getId());
                    intent.putExtra("ChatID", dataModel.getChatRoomId());
                    intent.putExtra("UserImage", dataModel.getImg());
                    mContext.startActivity(intent);
                }
                else if (holder.getItemViewType()==1)
                {
                    Intent intent = new Intent(mContext, Chat_MessagesChat.class);
                    intent.putExtra("UserName", dataModel.getName());
                    intent.putExtra("UserAddress", "GroupChat");
                    intent.putExtra("UserID", dataModel.getId());
                    intent.putExtra("ChatID", dataModel.getChatRoomId());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView logName;
        TextView textMessage;
        TextView messageDate;
        ImageView notSentIcon;
        ImageView dot;
        ImageView contactPhoto;

        ViewHolder(View itemView) {
            super(itemView);
            logName=itemView.findViewById(R.id.name_of_log);
            textMessage=itemView.findViewById(R.id.text_sms_message);
            messageDate=itemView.findViewById(R.id.date_sms_message);
            dot=itemView.findViewById(R.id.DOT);
            notSentIcon = itemView.findViewById(R.id.NotSent_Icon);
            contactPhoto=itemView.findViewById(R.id.contact_img_log_recycle);
        }
    }
    public Calendar covertTime(String strDate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = sdf.parse(strDate);
            Log.d("OPEN", String.valueOf(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }


}
