package com.example.johnny_wei.testtool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.example.johnny_wei.testtool.R;
import java.util.List;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
    private List<ServiceGroup> ServiceGroupData;
    private List<List<charaItem>> charaItemData;
    private Context mContext;

    public CustomExpandableListAdapter(List<ServiceGroup> ServiceGroupData,
                                       List<List<charaItem>> charaItemData,
                                       Context mContext)
    {
        this.ServiceGroupData = ServiceGroupData;
        this.charaItemData = charaItemData;
        this.mContext = mContext;
    }

    @Override
    public int getGroupCount() {
        return ServiceGroupData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return charaItemData.get(groupPosition).size();
    }

    @Override
    public ServiceGroup getGroup(int groupPosition) {
        return ServiceGroupData.get(groupPosition);
    }

    @Override
    public charaItem getChild(int groupPosition, int childPosition) {
        return charaItemData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        CustomExpandableListAdapter.ViewHolderGroup groupHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_expand_group, parent, false);
            groupHolder = new CustomExpandableListAdapter.ViewHolderGroup();
            groupHolder.tv_service_name = convertView.findViewById(R.id.tv_service_name);
            groupHolder.tv_service_uuid = convertView.findViewById(R.id.tv_service_uuid);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (CustomExpandableListAdapter.ViewHolderGroup) convertView.getTag();
        }

        groupHolder.tv_service_name.setText(ServiceGroupData.get(groupPosition).GetServiceName());
        groupHolder.tv_service_uuid.setText(ServiceGroupData.get(groupPosition).GetUUID());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        CustomExpandableListAdapter.ViewHolderItem itemHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_expand_item, parent, false);
            itemHolder = new CustomExpandableListAdapter.ViewHolderItem();

            itemHolder.CharacteristicName = convertView.findViewById(R.id.tv_chara_name);
            itemHolder.UUID = convertView.findViewById(R.id.tv_chara_uuid);
            itemHolder.property = convertView.findViewById(R.id.tv_chara_prop);
            itemHolder.value = convertView.findViewById(R.id.tv_chara_val);
            itemHolder.descriptorNum = convertView.findViewById(R.id.tv_chara_descNum);
            convertView.setTag(itemHolder);
        }else{
            itemHolder = (CustomExpandableListAdapter.ViewHolderItem) convertView.getTag();
        }

        itemHolder.CharacteristicName.setText(charaItemData.get(groupPosition).get(childPosition).GetCharaName());
        itemHolder.UUID.setText(charaItemData.get(groupPosition).get(childPosition).GetUUID());
        //use "" because of integer content
        itemHolder.property.setText("" + charaItemData.get(groupPosition).get(childPosition).Getproperty());
        itemHolder.value.setText("" + charaItemData.get(groupPosition).get(childPosition).Getvalue());
        itemHolder.descriptorNum.setText("" + charaItemData.get(groupPosition).get(childPosition).GetDescriptorNum());

        return convertView;
    }

    //设置子列表是否可選中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class ViewHolderGroup{
        private TextView tv_service_name;
        private TextView tv_service_uuid;
    }

    private static class ViewHolderItem{
        private TextView CharacteristicName;
        private TextView UUID;
        private TextView property;
        private TextView value;
        private TextView descriptorNum;
    }
}
