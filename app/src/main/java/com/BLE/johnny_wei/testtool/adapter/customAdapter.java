package com.BLE.johnny_wei.testtool.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.BLE.johnny_wei.testtool.R;

import java.util.List;

public class customAdapter extends BaseAdapter {
    Context context;
    List<RowItem> rowItems;

    public customAdapter(Context context, List<RowItem> items) {
        this.context = context;
        this.rowItems = items;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView tv_l1;
        TextView tv_l2;
        TextView tv_l3;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.tv_l1 = (TextView) convertView.findViewById(R.id.L1);
            holder.tv_l2 = (TextView) convertView.findViewById(R.id.L2);
            holder.tv_l3 = (TextView) convertView.findViewById(R.id.L3);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RowItem rowItem = (RowItem) getItem(position);

        holder.tv_l1.setText(rowItem.getstrL1());
        holder.tv_l2.setText(rowItem.getstrL2());
        holder.tv_l3.setText(rowItem.getstrL3());

        return convertView;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }
}
