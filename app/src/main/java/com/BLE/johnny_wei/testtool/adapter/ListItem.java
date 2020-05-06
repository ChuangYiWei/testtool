package com.BLE.johnny_wei.testtool.adapter;

import android.text.TextUtils;

import java.util.HashMap;

public class ListItem {

    private HashMap<String,Object>  maps;

    final String NAME = "NAME";
    final String ADDR = "ADDR";
    final String RSSI = "RSSI";

    public ListItem(String DevName, String DevAddr, String DevRssi) {
        maps = new HashMap<String, Object>();
        if (TextUtils.isEmpty(DevName)) {
            DevName = "Unknow Device";
        }
        maps.put(NAME,DevName);
        maps.put(ADDR,DevAddr);
        maps.put(RSSI,DevRssi);
    }


    public String Get(String key) {
        if(maps.containsKey(key))
            return (String)maps.get(key);
        else
            return "UnKnow";
    }

    public void Set(String key,String val) {
        maps.put(key,val);
    }

    @Override
    public String toString() {
        return maps.toString();
    }
}
