package teckvillage.developer.khaled_pc.teckvillagetrue.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.GridListDataModel;

public class CustomGridAdapter extends BaseAdapter {

    Context context;
    List<GridListDataModel> itemList;

    public CustomGridAdapter(Context context, List<GridListDataModel> itemList)
    {
        this.context=context;
        this.itemList=itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridview=convertView;
        if (convertView ==null)
        {
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridview=inflater.inflate(R.layout.grid_item,null);
        }

        TextView num=gridview.findViewById(R.id.grid_num_textView);
        TextView letter=gridview.findViewById(R.id.grid_letter_textView);

        num.setText(itemList.get(position).num);
        letter.setText(itemList.get(position).letter);

        return gridview;
    }
}
