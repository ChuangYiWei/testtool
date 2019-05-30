package com.example.johnny_wei.testtool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class collection extends AppCompatActivity {
    String TAG = getClass().getSimpleName();

    private List<Map<String, Object>> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkedmap);

        LinkedHashMap_demo();
//        demo();
    }

    void demo() {
        mData = getData();
        int cnt = mData.size();
        while( cnt-- > 0)
        {
            Log.w(TAG,(String)mData.get(cnt).get("title"));
        }
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "G1");
        map.put("info", "google 1");
//        map.put("img", R.drawable.i1);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "G2");
        map.put("info", "google 2");
//        map.put("img", R.drawable.i2);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "G3");
        map.put("info", "google 3");
//        map.put("img", R.drawable.i3);
        list.add(map);

        return list;
    }

    void LinkedHashMap_demo() {
        Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        //use ordered hashmap
        map.put(0, "uart");
        map.put(1, "spi");
        map.put(2, "hci");

        Log.d(TAG,"key set:" + map.keySet().toString());
        Log.d(TAG,"entry set:" + map.entrySet().toString());

        //第一种：普遍使用，二次取值
        for (Integer key : map.keySet()) {
            System.out.println("key= "+ key + " and value= " + map.get(key));
        }

        Log.d(TAG,"map.get(1):" + map.get(1));

        String[] get_array = test(map);
        Log.d("get_array:",get_array[0]);

        //第二种
        //"通过Map.entrySet使用iterator遍历key和value
//        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<String, String> entry = it.next();
//            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
//        }

        //第三种：推荐，尤其是容量大时
//        System.out.println("通过Map.entrySet遍历key和value");
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
//        }
        //第四种

//        System.out.println("通过Map.values()遍历所有的value，但不能遍历key");
//        for (String v : map.values()) {
//            System.out.println("value= " + v);
//        }
//
//
//        Iterator it = map.entrySet().iterator();
//        while (it.hasNext()){
//            Log.d(TAG,it.next().toString());
//        }
//
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            Log.d(TAG,entry.getKey() + " = " + entry.getValue());
//        }
    }

    String [] test(Map<Integer, String> map)
    {
        String[] thisIsAStringArray = new String[map.size()];

        //第一种：普遍使用，二次取值
        for (Integer key : map.keySet()) {
            System.out.println("key= "+ key + " and value= " + map.get(key));
            thisIsAStringArray[key] = map.get(key);
        }

        return thisIsAStringArray;
    }

    void hashdemo()
    {
        // Create a hash map
        HashMap hm = new HashMap();
        // Put elements to the map
        hm.put("Zara", new Double(3434.34));
        hm.put("Mahnaz", new Double(123.22));
        hm.put("Ayan", new Double(1378.00));
        hm.put("Daisy", new Double(99.22));
        hm.put("Qadir", new Double(-19.08));

        // Get a set of the entries
        Set set = hm.entrySet();
        // Get an iterator
        Iterator i = set.iterator();
        // Display elements
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            System.out.print(me.getKey() + ": ");
            System.out.println(me.getValue());
        }
        System.out.println();
        // Deposit 1000 into Zara's account
        double balance = ((Double)hm.get("Zara")).doubleValue();
        hm.put("Zara", new Double(balance + 1000));
        System.out.println("Zara's new balance: " +
                hm.get("Zara"));
    }
}
