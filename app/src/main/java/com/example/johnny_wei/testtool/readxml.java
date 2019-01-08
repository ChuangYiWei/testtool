package com.example.johnny_wei.testtool;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.johnny_wei.testtool.xmlpull.BLE_testItem;
import com.example.johnny_wei.testtool.xmlpull.XmlParser;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class readxml extends AppCompatActivity {
    String TAG = getClass().getSimpleName();
    LinkedList<String> testFuncList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readxml);
        testFuncList = new LinkedList<String>();

        readxml("76xx.xml");

    }

    private void readxml(String filename)  {
        AssetManager asset = getAssets();
        Log.d(TAG, "invokeTest start");
        //parse test item
        try {
            InputStream input = asset.open(filename);
//            List<BLE_testItem> list = XmlParser.getTestItems(input);
            List<BLE_testItem> list = XmlParser.getTestItems(input,"UART");
            if(list == null)
            {
                Log.e(TAG,"list is null");
                return;
            }
            for (BLE_testItem item : list) {
                Log.d(TAG,item.gettestName());
                Log.d(TAG,item.getdataStr());
                Log.d(TAG,Integer.toString(item.cmdSize()));
                Log.d(TAG,item.getCmd());
                item.popCmd();
                Log.d(TAG,item.getCmd());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        Log.d(TAG, "done");
    }


}
