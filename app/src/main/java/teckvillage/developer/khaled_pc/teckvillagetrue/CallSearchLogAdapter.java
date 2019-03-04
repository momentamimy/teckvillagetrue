package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.content.Context;
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
import teckvillage.developer.khaled_pc.teckvillagetrue.model.LogInfo;

public class CallSearchLogAdapter extends RecyclerView.Adapter<SearchLogHolder> {
    private List<LogInfo> itemList;
    private Context context;
    private String num;
    public CallSearchLogAdapter(Context context, List<LogInfo> itemList,String num) {
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
    public void onBindViewHolder(SearchLogHolder holder, int position) {
        holder.logName.setText(itemList.get(position).logName);
        Spannable spannable = new SpannableString(itemList.get(position).getNumber());

        spannable.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorPrimary)), itemList.get(position).getNumber().indexOf(num),itemList.get(position).getNumber().indexOf(num)+ num.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.phoneNum.setText(spannable, TextView.BufferType.SPANNABLE);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
