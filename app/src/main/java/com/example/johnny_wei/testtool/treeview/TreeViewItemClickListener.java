package com.example.johnny_wei.testtool.treeview;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Arrays;

public class TreeViewItemClickListener implements AdapterView.OnItemClickListener {

    private boolean DEBUG = false;
    /** adapter */
    private TreeViewAdapter treeViewAdapter;

    public TreeViewItemClickListener(TreeViewAdapter treeViewAdapter) {
        this.treeViewAdapter = treeViewAdapter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        //点击的item代表的元素
        Element element = (Element) treeViewAdapter.getItem(position);
        //树中的元素
        ArrayList<Element> elements = treeViewAdapter.getElements();
        //元素的数据源
        ArrayList<Element> elementsData = treeViewAdapter.getElementsData();
        Log.d("tree click","position:" + position+", choose:" + Arrays.toString(element.getContentText()));
        //点击没有子项的item直接返回
        if (!element.isHasChildren()) {
            return;
        }

        if (element.isExpanded()) {
            element.setExpanded(false);
            //删除节点内部对应子节点数据，包括子节点的子节点...
            ArrayList<Element> elementsToDel = new ArrayList<Element>();
            if(DEBUG) {printDebug("before add:", elements);}

            for (int i = position + 1; i < elements.size(); i++) {
                if (element.getLevel() >= elements.get(i).getLevel())
                    break;
                elementsToDel.add(elements.get(i));
            }
            elements.removeAll(elementsToDel);
            if(DEBUG) {printDebug("after add:", elements);}
            treeViewAdapter.notifyDataSetChanged();
        } else {
            element.setExpanded(true);
            if(DEBUG) {printDebug("before add", elements);}

            //从数据源中提取子节点数据添加进树，注意这里只是添加了下一级子节点，为了简化逻辑
            int i = 1;//注意这里的计数器放在for外面才能保证计数有效
            for (Element e : elementsData) {
                if (e.getParendId() == element.getId()) {
                    e.setExpanded(false);
                    elements.add(position + i, e);
                    i ++;
                }
            }

            if(DEBUG) {printDebug("after add", elements);}
            treeViewAdapter.notifyDataSetChanged();
        }
    }

    void printDebug(String prefix,ArrayList<Element> elements)
    {
        for (Element e : elements) {
            Log.d("tree click",prefix + Arrays.toString(e.getContentText()));
        }
    }



}
