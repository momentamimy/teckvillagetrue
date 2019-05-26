package com.developer.whocaller.net.View;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.developer.whocaller.net.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class Block_List_Holder extends RecyclerView.ViewHolder {

    public CircleImageView logCircleImageView;
    public TextView blocklistphonename,phonenumber_block;
    public RelativeLayout removefromblock;
    public LinearLayout layoutcontainer;


    public Block_List_Holder(View itemView) {
        super(itemView);
        logCircleImageView=itemView.findViewById(R.id.contact_img_log_recycle);
        blocklistphonename=itemView.findViewById(R.id.name_of_blocklist);
        phonenumber_block=itemView.findViewById(R.id.num_blocklist);
        removefromblock=itemView.findViewById(R.id.Removefromblock);
        layoutcontainer=itemView.findViewById(R.id.onclicklayout);

    }
}
