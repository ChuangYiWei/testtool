package com.example.johnny_wei.testtool;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;

import com.example.johnny_wei.testtool.config.LiteBLECallback;
import com.example.johnny_wei.testtool.treeview.Element;
import com.example.johnny_wei.testtool.treeview.TreeViewAdapter;
import com.example.johnny_wei.testtool.treeview.TreeViewItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.johnny_wei.testtool.LiteBle.StaticLiteble;
import static com.example.johnny_wei.testtool.config.globalConfig.EXTRAS_DEVICE_ADDRESS;

public class connected_list extends AppCompatActivity {
    private String mDeviceAddress = null;
    private String TAG = getClass().getSimpleName();

    //all data
    private ArrayList<Element> elements;
    //data
    private ArrayList<Element> elementsData;
    GattCB gatt_cb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected_list);
        init();
        setupView();
        setup_BLE();
    }

    private void createTreeView() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final ListView treeview = (ListView) findViewById(R.id.servie_treeview);
        final TreeViewAdapter treeViewAdapter = new TreeViewAdapter(
                elements, elementsData, inflater);
        final TreeViewItemClickListener treeViewItemClickListener = new TreeViewItemClickListener(treeViewAdapter);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                treeview.setAdapter(treeViewAdapter);
                treeview.setOnItemClickListener(treeViewItemClickListener);;
            }
        });

    }

    private void setup_BLE() {
        gatt_cb = new GattCB();
        StaticLiteble.connect(mDeviceAddress);
    }

    private void init() {
        elements = new ArrayList<Element>();
        elementsData = new ArrayList<Element>();
    }

    private void setupView() {
        Intent intent = getIntent();
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
    }

    @Override
    protected void onDestroy() {
        StaticLiteble.disconnect();
        super.onDestroy();
    }

    private class GattCB extends LiteBLECallback {
        GattCB() {
            //this reference(pointer) is GattCB
            StaticLiteble.setCallback(this);
        }

        //implement callback
        @Override
        public void ConnectedCB() {

            Log.w(TAG, "OnConnect callback !!!");
        }

        @Override
        public void SrvDiscoverSuccessCB() {
            Log.w(TAG, "SrvDiscoverSuccessCB callback !!!");
            addService2View();
            createTreeView();
        }

        @Override
        public void DisConnectCB() {
            Log.w(TAG, "disconnected callback !!!");
        }

        @Override
        public void ConnectFailCB(String reason) {
            Log.w(TAG, "ConnectFailCB callback !!! " + "reason:" + reason);
        }
    }

    void addService2View()
    {
        BluetoothGatt gatt = StaticLiteble.getBluetoothGatt();
        int ID = 0;
        int serviceID = 0;
        if (gatt != null) {
            for (BluetoothGattService service : gatt.getServices()) {
                Log.i(TAG, "service: " + service.getUuid());

                serviceID = ID;
                Log.w(TAG, "serv id: " + serviceID);
                Element e1 = new Element(service.getUuid().toString(),
                                            Element.TOP_LEVEL,
                                            0,
                                            Element.NO_PARENT,
                                            true,
                                            false);
                elements.add(e1);
//                elementsData.add(e1);
                ID = ID+1;
                for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                    Log.d(TAG, "  characteristic: " + characteristic.getUuid() + " value: " + Arrays.toString(characteristic.getValue()));

                    int charaID = ID;
                    Log.w(TAG, "ID: " + ID + ",charaID:" + charaID+",serviceID:" + serviceID);
                    Element e2 = new Element(characteristic.getUuid().toString(),
                            Element.TOP_LEVEL + 1,
                            ID,
                            serviceID,
                            true,
                            false);
                    elementsData.add(e2);
                    ID = ID+1;
                    for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                        Log.v(TAG, "        descriptor: " + descriptor.getUuid() + " value: " + Arrays.toString(descriptor.getValue()));
                        Log.w(TAG, "ID: " + ID + ",charaID:" + charaID+",serviceID:" + serviceID);
                        Element e3 = new Element(descriptor.getUuid().toString(),
                                Element.TOP_LEVEL + 2,
                                ID,
                                charaID,
                                false,
                                false);
                        elementsData.add(e3);
                        ID = ID+1;
                    }
                }
            }
        }
        Log.d("TAG","element size:" + elements.size());
        int n = elements.size();
        int n1 = elementsData.size();
        Log.w("TAG","element size:" + n);
        Log.w("TAG","elementsData size:" + n1);
        n=n-1;
        while(n >= 0)
        {
            Log.w("TAG","element " + elements.get(n).getContentText());
            n--;
        }
        n1=n1-1;
        while(n1 >= 0)
        {
            Log.w("TAG","elementsData :" + elementsData.get(n1).getContentText());
            n1--;
        }
//        elements.get(0).getContentText()
    }
}
