package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by khaled-pc on 4/13/2019.
 */

public class RecycleviewResult_SearchAPIHolder  extends RecyclerView.ViewHolder {


    public CircleImageView contactCircleImageView2;
    public TextView contactNamee;
    public TextView countryy;
    public ImageView chaticonn;
    public RelativeLayout container_chaticon,openProfile;


    public RecycleviewResult_SearchAPIHolder(View itemView) {
        super(itemView);
        contactCircleImageView2=itemView.findViewById(R.id.usercontactimg);
        contactNamee=itemView.findViewById(R.id.nameofcontacts);
        countryy=itemView.findViewById(R.id.country);
        chaticonn=itemView.findViewById(R.id.chat_user_contact);
        container_chaticon=itemView.findViewById(R.id.container_lay_chat_user_contact);
        openProfile=itemView.findViewById(R.id.fir_layCon);

    }
}
