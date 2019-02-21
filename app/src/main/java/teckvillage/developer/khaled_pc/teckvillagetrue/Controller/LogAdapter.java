package teckvillage.developer.khaled_pc.teckvillagetrue.Controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.LogHolder;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.LogInfo;

public class LogAdapter extends RecyclerView.Adapter<LogHolder> {
    private List<LogInfo> itemList;
    private Context context;

    public LogAdapter(Context context,List<LogInfo> itemList)
    {
        this.context=context;
        this.itemList=itemList;
    }

    @Override
    public LogHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_of_logs_recycleview, null);
        LogHolder rbv = new LogHolder(layoutView);
        return rbv;
    }

    @Override
    public void onBindViewHolder(final LogHolder holder, int position) {
        LogInfo logInfo = itemList.get(position);
        if (logInfo.imageUrl!=null)
        {
            Picasso.with(context)
                    .load(logInfo.imageUrl)
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
        holder.logName.setText(logInfo.logName);
        if (!TextUtils.isEmpty(logInfo.logIcon))
        {
            if (logInfo.logIcon.equals("INCOMING"))
            {
                holder.logIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_call_received_arrow));
            }
            else if (logInfo.logIcon.equals("OUTGOING"))
            {
                holder.logIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_right_up));
            }
            else if (logInfo.logIcon.equals("MISSED"))
            {
                holder.logIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_call_missed));
            }
            else if (logInfo.logIcon.equals("Other"))
            {
                holder.logIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_call_missed));
            }
        }
        else
        {
            holder.logIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_call_missed));
        }
        holder.logDate.setText(String.valueOf(logInfo.logDate));
        holder.callType.setText(logInfo.callType);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
