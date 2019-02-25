package teckvillage.developer.khaled_pc.teckvillagetrue.View;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;

public class LogHolder extends RecyclerView.ViewHolder {

    public CircleImageView logCircleImageView;
    public TextView logName;
    public ImageView logIcon;
    public TextView logDate;
    public TextView callType;
    public ProgressBar progressBar;
    public CircleImageView progressCircleImageView;
    public TextView datesection;
    public RelativeLayout item;

    public LogHolder(View itemView) {
        super(itemView);
        logCircleImageView=itemView.findViewById(R.id.contact_img_log_recycle);
        logName=itemView.findViewById(R.id.name_of_log);
        logIcon=itemView.findViewById(R.id.type_of_call);
        logDate=itemView.findViewById(R.id.date_of_call_log);
        callType=itemView.findViewById(R.id.type_of_num_log);
        progressBar=itemView.findViewById(R.id.progress);
        progressCircleImageView=itemView.findViewById(R.id.progress_contact_img);
        datesection=itemView.findViewById(R.id.datesection);
        item=itemView.findViewById(R.id.layoutlist);
    }
}
