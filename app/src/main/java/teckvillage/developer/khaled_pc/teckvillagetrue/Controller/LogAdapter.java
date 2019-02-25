package teckvillage.developer.khaled_pc.teckvillagetrue.Controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.LogHolder;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.LogInfo;

public class LogAdapter extends RecyclerView.Adapter<LogHolder> {
    private static final int TYPE_DATE = 1;
    private static final int TYPE_ITEM = 2;
    private List<LogInfo> itemList;
    private Context context;

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
    public void onBindViewHolder(final LogHolder holder, int position) {
        final LogInfo logInfo = itemList.get(position);
        if (holder.logName != null) {


            holder.calllayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + logInfo.getNumber()));
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                       context.startActivity(intent);
                }
            });


            holder.chaticon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "You Click me", Toast.LENGTH_LONG).show();

                }
            });

            holder.infoicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "You Click me", Toast.LENGTH_LONG).show();

                }
            });

            if (logInfo.imageUrl != null) {
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
            if (!TextUtils.isEmpty(logInfo.logIcon)) {
                if (logInfo.logIcon.equals("INCOMING")) {
                    holder.logIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_call_received_arrow));
                } else if (logInfo.logIcon.equals("OUTGOING")) {
                    holder.logIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_right_up));
                } else if (logInfo.logIcon.equals("MISSED")) {
                    holder.logIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_call_missed));
                } else if (logInfo.logIcon.equals("Other")) {
                    holder.logIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_call_missed));
                }
            } else {
                holder.logIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_call_missed));
            }
            holder.logDate.setText(logInfo.hour);
            holder.callType.setText(logInfo.callType);

        }

        if (holder.datesection != null) {
            holder.datesection.setText(logInfo.getDateString());
        }



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
