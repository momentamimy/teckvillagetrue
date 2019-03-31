package teckvillage.developer.khaled_pc.teckvillagetrue.View;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;

public class Block_List_Holder extends RecyclerView.ViewHolder {

    public CircleImageView logCircleImageView;
    public TextView blocklistphonename,phonenumber_block;
    public RelativeLayout removefromblock;


    public Block_List_Holder(View itemView) {
        super(itemView);
        logCircleImageView=itemView.findViewById(R.id.contact_img_log_recycle);
        blocklistphonename=itemView.findViewById(R.id.name_of_blocklist);
        phonenumber_block=itemView.findViewById(R.id.num_blocklist);
        removefromblock=itemView.findViewById(R.id.Removefromblock);

    }
}
