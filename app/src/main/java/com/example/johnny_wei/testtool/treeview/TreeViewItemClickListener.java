package com.example.johnny_wei.testtool.treeview;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

public class TreeViewItemClickListener implements AdapterView.OnItemClickListener {

    /** adapter */
    private TreeViewAdapter treeViewAdapter;

    public TreeViewItemClickListener(TreeViewAdapter treeViewAdapter) {
        this.treeViewAdapter = treeViewAdapter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Log.d("jjj","position:" + position);
        //点击的item代表的元素
        Element element = (Element) treeViewAdapter.getItem(position);
        //树中的元素
        ArrayList<Element> elements = treeViewAdapter.getElements();
        //元素的数据源
        ArrayList<Element> elementsData = treeViewAdapter.getElementsData();

        //点击没有子项的item直接返回
        if (!element.isHasChildren()) {
            return;
        }

        if (element.isExpanded()) {
            element.setExpanded(false);
            //删除节点内部对应子节点数据，包括子节点的子节点...
            ArrayList<Element> elementsToDel = new ArrayList<Element>();
            for (Element e : elements) {
                Log.d("jjj","before delete.getContentText():" + e.getContentText());
            }
            for (int i = position + 1; i < elements.size(); i++) {
                if (element.getLevel() >= elements.get(i).getLevel())
                    break;
                elementsToDel.add(elements.get(i));
//                Log.d("jjj","to be delete:" + elements.get(i).getContentText());
            }
            elements.removeAll(elementsToDel);
            for (Element e : elements) {
                Log.d("jjj","after delete .getContentText():" + e.getContentText());
            }
            treeViewAdapter.notifyDataSetChanged();
        } else {
            element.setExpanded(true);
            for (Element e : elements) {
                Log.d("jjj","before add .getContentText():" + e.getContentText());
            }
            //从数据源中提取子节点数据添加进树，注意这里只是添加了下一级子节点，为了简化逻辑
            int i = 1;//注意这里的计数器放在for外面才能保证计数有效
            for (Element e : elementsData) {
                if (e.getParendId() == element.getId()) {
                    e.setExpanded(false);
                    elements.add(position + i, e);
                    Log.d("jjj","to be add:" + elements.get(position + i).getContentText());
                    i ++;
                }
            }
            for (Element e : elements) {
                Log.d("jjj","after add .getContentText():" + e.getContentText());
            }
            treeViewAdapter.notifyDataSetChanged();
        }
    }



}
