package teckvillage.developer.khaled_pc.teckvillagetrue.View;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;

public class SearchLogHolder extends RecyclerView.ViewHolder {

    public CircleImageView logCircleImageView;
    public TextView logName;
    public TextView phoneNum;

    public SearchLogHolder(View itemView) {
        super(itemView);
        logCircleImageView=itemView.findViewById(R.id.contact_img_log_recycle);
        logName=itemView.findViewById(R.id.name_call_of_log);
        phoneNum=itemView.findViewById(R.id.number_call_log);
    }
}
