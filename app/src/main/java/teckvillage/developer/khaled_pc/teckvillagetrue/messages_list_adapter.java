package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by gaber on 12/08/2018.
 */

public class messages_list_adapter extends RecyclerView.Adapter<messages_list_adapter.MyViewHolder> {

private Context context;
private List<sms_messages_model> contact_list;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView text_sms;
        public TextView date_sms;
        public ImageView type_icon;
        public MyViewHolder(View view) {
        super(view);
            text_sms=(TextView) view.findViewById(R.id.message);
            date_sms=(TextView) view.findViewById(R.id.time);
            type_icon=view.findViewById(R.id.type_icon);

    }
}


    public messages_list_adapter(Context context, List<sms_messages_model> contact_list) {
        this.context = context;
        this.contact_list = contact_list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==1){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_sms_chat_send, parent, false);
            return new MyViewHolder(itemView);
        }else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_sms_chat_receive, parent, false);
            return new MyViewHolder(itemView);
        }

    }


    @Override
    public int getItemViewType(int position) {
        sms_messages_model data=contact_list.get(position);
        if (data.address.equals(context.getSharedPreferences("logged_in",Context.MODE_PRIVATE).getString("phone",""))){
            return 1;
        }else {
            return 0;
        }

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        sms_messages_model data = contact_list.get(position);
        Date date=new Date(data.date);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        String dateText = df2.format(date);
        holder.date_sms.setText(dateText);
        holder.text_sms.setText(data.body);
         if (data.int_Type==5)
         {
             if (data.status==31)
             {
                //progress
                holder.type_icon.setImageResource(R.drawable.ic_progress_24dp);
             }
             else
             {
                 holder.type_icon.setImageResource(R.drawable.ic_close_gray_24dp);
                 //not_sent
             }
         }
         else if (data.int_Type==2)
         {
             holder.type_icon.setImageResource(R.drawable.ic_check_black_24dp);
         }
         //holder.date_sms.setText(dateText);
         /*if (holder.getItemViewType()==1) {
             Drawable img = context.getResources().getDrawable(R.drawable.seen);
             img.setBounds(0, 0, 33, 33);
             holder.date_sms.setCompoundDrawables(img, null, null, null);
         }*/
        ContentValues values = new ContentValues();
        values.put("read", true);
        context.getContentResolver().update(Uri.parse("content://sms/inbox"), values, "_id=" + data.id, null);



    }

    @Override
    public int getItemCount() {
        return contact_list.size();
    }

}


