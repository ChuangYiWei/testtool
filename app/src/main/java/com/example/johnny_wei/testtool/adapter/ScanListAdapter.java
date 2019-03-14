package com.example.johnny_wei.testtool.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.johnny_wei.testtool.R;

import java.util.List;

public class ScanListAdapter extends BaseAdapter {
    String TAG = getClass().getSimpleName();
    Context mContext;
    List<RowItem> rowItems;

    public ScanListAdapter(Context context, List<RowItem> items) {
        this.mContext = context;
        this.rowItems = items;
    }
    /*private view holder class*/
    private class ViewHolder {
        TextView tv_l1;
        TextView tv_l2;
        TextView tv_l3;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.blelist, null);
            holder = new ViewHolder();
            holder.tv_l1 = (TextView) convertView.findViewById(R.id.devName);
            holder.tv_l2 = (TextView) convertView.findViewById(R.id.devAddr);
            holder.tv_l3 = (TextView) convertView.findViewById(R.id.devRssi);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RowItem rowItem = (RowItem) getItem(position);
        Log.d(TAG,"position is:" + position);
        holder.tv_l1.setText(rowItem.getstrL1());
        holder.tv_l2.setText(rowItem.getstrL2());
        holder.tv_l3.setText(rowItem.getstrL3());

        return convertView;
    }
}
