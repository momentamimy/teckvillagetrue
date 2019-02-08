package teckvillage.developer.khaled_pc.teckvillagetrue.View;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;

public class ContactHolder extends RecyclerView.ViewHolder {

    public CircleImageView contactCircleImageView;
    public TextView contactName;
    public TextView numberType;
    public ProgressBar progressBar;
    public CircleImageView progressCircleImageView;

    public ContactHolder(View itemView) {
        super(itemView);
        contactCircleImageView=itemView.findViewById(R.id.contact_img);
        contactName=itemView.findViewById(R.id.contact_name);
        numberType=itemView.findViewById(R.id.number_type);
        progressBar=itemView.findViewById(R.id.progress);
        progressCircleImageView=itemView.findViewById(R.id.progress_contact_img);
    }
}