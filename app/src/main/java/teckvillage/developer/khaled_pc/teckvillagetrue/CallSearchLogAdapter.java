package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import teckvillage.developer.khaled_pc.teckvillagetrue.View.SearchLogHolder;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.LogInfo;

public class CallSearchLogAdapter extends RecyclerView.Adapter<SearchLogHolder> {
    private List<LogInfo> itemList;
    private Context context;
    private String num;

    public CallSearchLogAdapter(Context context, List<LogInfo> itemList, String num) {
        this.context = context;
        this.itemList = itemList;
        this.num = num;
    }

    @Override
    public SearchLogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_call_search, null);
        SearchLogHolder rbv = new SearchLogHolder(layoutView);
        return rbv;
    }


    @Override
    public void onBindViewHolder(SearchLogHolder holder, final int position) {
        holder.logName.setText(itemList.get(position).logName);
        Spannable spannable = new SpannableString(itemList.get(position).getNumber());

        spannable.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorPrimary)), itemList.get(position).getNumber().indexOf(num), itemList.get(position).getNumber().indexOf(num) + num.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.phoneNum.setText(spannable, TextView.BufferType.SPANNABLE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_CALL);
                intent1.setData(Uri.parse("tel:" + itemList.get(position).getNumber()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                }
                context.startActivity(intent1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
