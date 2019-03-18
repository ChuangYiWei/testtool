package com.example.johnny_wei.testtool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class collection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkedmap);

        Map<String, String> map = new LinkedHashMap<String, String>();

        map.put("1", "11");
        map.put("22", "22");
        map.put("33", "33");

        Log.d("jjj","key set:" + map.keySet().toString());
        Log.d("jjj","entry set:" + map.entrySet().toString());

        //第一种：普遍使用，二次取值
        for (String key : map.keySet()) {
            System.out.println("key= "+ key + " and value= " + map.get(key));
        }

        //第二种
        //"通过Map.entrySet使用iterator遍历key和value
//        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<String, String> entry = it.next();
//            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
//        }

        //第三种：推荐，尤其是容量大时
        System.out.println("通过Map.entrySet遍历key和value");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }
        //第四种

        System.out.println("通过Map.values()遍历所有的value，但不能遍历key");
        for (String v : map.values()) {
            System.out.println("value= " + v);
        }


        Iterator it = map.entrySet().iterator();
        while (it.hasNext()){
            Log.d("jjj",it.next().toString());
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            Log.d("jjj",entry.getKey() + " = " + entry.getValue());
        }
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