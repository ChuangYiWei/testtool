package com.example.johnny_wei.testtool;

import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.johnny_wei.testtool.utils.DevUtil.printDeviceInfo;

public class collection extends AppCompatActivity {
    String TAG = getClass().getSimpleName();

    private List<Map<String, Object>> mData;

    //
    LinkedList<Integer> data_list = new LinkedList<Integer>();
    int capacity = 10;
    int update_sample = 1;
    int add_sample = 2;
    //
    final PC pc = new PC();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkedmap);
        init_data();
        // Start both threads

        //-------------------------------------------------------
//        t1.start();
//        SystemClock.sleep(1000);
//        t2.start();
        //-------------------------------------------------------
        //LinkedHashMap_demo();
//        demo();
        hashmapDemo();
        printDeviceInfo();
    }
    //-------------------------------------------------------
    void hashmapDemo()
    {
        //1.先加入支援的hci cmd function
        //2.csv抓到時對應function
        String org_cmd = "01030C00";
        Log.d(TAG,"parsing first 6 chara:" + org_cmd.substring(0,6));
        Map<String, String> map = new HashMap<String, String>();
        map.put("01130C","Write_Local_Name_Command");
        map.put("01030C","HCI_Reset");
        Log.d(TAG,"key set:" + map.keySet().toString());
        Log.d(TAG,"entry set:" + map.entrySet().toString());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println("key= " + entry.getKey() + ", value=" + entry.getValue());
        }
        Log.d(TAG,"get key 01030C00:" +map.get("01030C"));
        Log.d(TAG,"get key not exit :" +map.get("xxx"));

    }
    //--------------------------------------------------------
    void init_data()
    {
        for(int i=0;i<capacity;i++) {
            data_list.add(i);
        }
        Log.d(TAG,"list size:" + data_list.size());
    }

    void read_data(int readsize)
    {}

    void set_data(byte[] bytes,int size)
    {}
    int g_int =capacity;
    public void produce() throws InterruptedException
    {
            synchronized (this)
            {
                System.out.println("produce enter");
                // producer thread waits while list
                // is full
//                while (data_list.size() == capacity) {
//                for(int i =0;i<update_sample;i++) {
//                    data_list.removeFirst();
//                }
                for(int i =0;i<add_sample;i++) {
                    data_list.add(g_int);
                    Log.d("produce", "g_int:" + g_int);
                    g_int++;
                }
//                    System.out.println("produce wait");
//                    wait();
//                }
                // notifies the consumer thread that now it can start consuming
                System.out.println("Wake up consumer thread");
//                notify();
            }
    }

    // Function called by consumer thread
    public void consume() throws InterruptedException
    {
            synchronized (this)
            {

                if (data_list.size() >= capacity) {
                    for (int i = 0; i < data_list.size(); i++) {
                        Log.d("consume", data_list.get(i).toString());
                    }
                }
                //remove first nth data
                for (int i = 0; i < update_sample; i++) {
                    data_list.removeFirst();
                }

                // consumer thread waits while list
                // is empty
//                while (data_list.size()== 0)
//                    wait();
//                for(int i=0;i<data_list.size();i++) {
//                    Log.d("consume",data_list.get(i).toString());
//                }
//
//                // Wake up producer thread
//                System.out.println("Wake up producer thread");
//                notify();
            }
    }

    // Create producer thread
    Thread t1 = new Thread(new Runnable()
    {
        @Override
        public void run()
        {
            while (true) {
                try {
                    produce();
                    System.out.println("produce");
                    //pc.produce();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SystemClock.sleep(2000);
            }
        }
    });

    // Create consumer thread
    Thread t2 = new Thread(new Runnable()
    {
        @Override
        public void run()
        {
            while (true) {
                try {
                    consume();
                    //pc.consume();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SystemClock.sleep(1000);
            }
        }
    });


    //--------------------------------------------------------

    public static class PC
    {
        // Create a list shared by producer and consumer
        // Size of list is 2.
        LinkedList<Integer> list = new LinkedList<>();
        int capacity = 5;
        int value = 0;
        // Function called by producer thread
        public void produce() throws InterruptedException
        {

            //while (true)
            {
                synchronized (this)
                {
                    // producer thread waits while list
                    // is full
                    while (list.size()==capacity) {
                        System.out.println("produce full");
                        wait();
                    }
                    System.out.println("Producer produced-"
                            + value);

                    // to insert the jobs in the list
                    list.add(value++);

                    // notifies the consumer thread that
                    // now it can start consuming
                    System.out.println("notifies the consumer thread");
                    notify();
                    // makes the working of program easier
                    // to  understand

                }
            }
        }

        // Function called by consumer thread
        public void consume() throws InterruptedException
        {
            //while (true)
            {
                synchronized (this)
                {
                    // consumer thread waits while list
                    // is empty
                    while (list.size()==0) {
                        System.out.println("consume wait");
                        wait();
                    }
                    //to retrive the ifrst job in the list
                    int val = list.removeFirst();

                    System.out.println("Consumer consumed-"
                            + val);

                    // Wake up producer thread
                    System.out.println("Wake up producer thread");
                    notify();

                }
            }
        }
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

