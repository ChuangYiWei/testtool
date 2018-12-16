package com.example.johnny_wei.testtool;


import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class xmlandinvoke extends AppCompatActivity {
    String TAG = getClass().getSimpleName();
    LinkedList<String> testFuncList;

    private static BD_Testclass2 bd_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bd_obj = new BD_Testclass2(this);
        testFuncList = new LinkedList<String>();

        try {
            invokeTest2();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void invokeTest() throws IllegalAccessException {
        AssetManager asset = getAssets();
        Log.d(TAG, "invokeTest start");
        //parse test item
        try {
            InputStream input = asset.open("bd_testitems.xml");
            List<BDTestitem> list = Parsertesitem.getTestItems(input);
            for (BDTestitem item : list) {
                testFuncList.add(item.getName());
                Log.d(TAG,item.getName());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        while(testFuncList.size() > 0) {
            Log.d(TAG, "---------------------" + testFuncList.getFirst());
            Method method;
            try {
                //call function in bd_obj class
                method = bd_obj.getClass().getDeclaredMethod(testFuncList.getFirst());
                method.invoke(bd_obj);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            testFuncList.removeFirst();
        }
        Log.d(TAG, "done");

    }


    private void invokeTest2() throws IllegalAccessException {
        AssetManager asset = getAssets();
        Log.d(TAG, "invokeTest start");
        //parse test item
        try {
            InputStream input = asset.open("76xx.xml");
            List<BLE_testItem> list = XmlParser.getSPItems(input);
            for (BLE_testItem item : list) {
                Method method;
                try {
                    //call function in bd_obj class
                    method = bd_obj.getClass().getDeclaredMethod(item.gettestName(), BLE_testItem.class);
                    int ret = (int)method.invoke(bd_obj, item);
                    Log.d(TAG, "ret:" + ret);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        Log.d(TAG, "done");

    }
}
