package teckvillage.developer.khaled_pc.teckvillagetrue.View;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;


public class SMSMessageHolder extends RecyclerView.ViewHolder{

    public TextView message;

    public SMSMessageHolder(View itemView) {
        super(itemView);
        message=itemView.findViewById(R.id.message);
    }
}