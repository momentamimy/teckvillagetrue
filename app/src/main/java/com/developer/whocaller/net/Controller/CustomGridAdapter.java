package com.developer.whocaller.net.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.whocaller.net.Model.GridListDataModel;

import java.util.List;

import com.developer.whocaller.net.R;

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
        ImageView icon=gridview.findViewById(R.id.grid_letter_ImageView);

        num.setText(itemList.get(position).num);
        if (position==0)
        {
            icon.setVisibility(View.VISIBLE);
            letter.setText("");
            letter.setVisibility(View.GONE);
        }
        else
        {
            letter.setText(itemList.get(position).letter);
            icon.setVisibility(View.GONE);
            letter.setVisibility(View.VISIBLE);
        }


        return gridview;
    }
}
