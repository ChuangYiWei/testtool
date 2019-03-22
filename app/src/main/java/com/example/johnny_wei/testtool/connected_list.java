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
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.johnny_wei.testtool.config.LiteBLECallback;
import com.example.johnny_wei.testtool.treeview.Element;
import com.example.johnny_wei.testtool.treeview.TreeViewAdapter;
import com.example.johnny_wei.testtool.treeview.TreeViewItemClickListener;
import com.example.johnny_wei.testtool.utils.GattAttributes;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.johnny_wei.testtool.LiteBle.StaticLiteble;
import static com.example.johnny_wei.testtool.config.globalConfig.EXTRAS_DEVICE_ADDRESS;

public class connected_list extends AppCompatActivity {
    private String mDeviceAddress = null;
    private String TAG = getClass().getSimpleName();
    private String NEW_LINE = "\n";

    TextView tv_dev;
    TextView tv_status;


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

    void updateMac(final String mac)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_dev.setText(mac);
            }
        });
    }

    void updateStatus(final String status)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_status.setText(status);
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
        tv_dev = findViewById(R.id.tv_dev);
        tv_status = findViewById(R.id.tv_status);
        updateMac(mDeviceAddress);
        updateStatus(getString(R.string.CONNECTING));
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
            updateStatus(getString(R.string.CONNECTED));
        }

        @Override
        public void SrvDiscoverSuccessCB() {
            Log.w(TAG, "SrvDiscoverSuccessCB callback !!!");
            addService2View();
            createTreeView();
            updateStatus(getString(R.string.SRV_DISCOVER));
        }

        @Override
        public void SrvDiscoverFailCB(String reason) {
            updateStatus(getString(R.string.SRV_DISCOVER_FAIL));
        }

        @Override
        public void DisConnectCB() {
            Log.w(TAG, "disconnected callback !!!");
            updateStatus(getString(R.string.DIS_CONNECTED));
        }

        @Override
        public void ConnectFailCB(String reason) {
            Log.w(TAG, "ConnectFailCB callback !!! " + "reason:" + reason);
            updateStatus(getString(R.string.CONN_FAIL));
        }
    }

    void addElement(String[] contentText, int level, int id, int parendId,
                    boolean hasChildren, boolean isExpanded)
    {
        Element e = new Element(contentText,
                level,
                id,
                parendId,
                hasChildren,
                isExpanded);
        elements.add(e);
    }
    void addElementsdata(String[] contentText, int level, int id, int parendId,
                    boolean hasChildren, boolean isExpanded)
    {
        Element e = new Element(contentText,
                level,
                id,
                parendId,
                hasChildren,
                isExpanded);
        elementsData.add(e);
    }

    String [] GetServiceData(BluetoothGattService service)
    {
//        ex:String service = "0000180f-0000-1000-8000-00805f9b34fb";
        int startIdx = 4;
        int EndIdx = 7+1;
        //find service
        String UUID = service.getUuid().toString();
        String serviceName = GattAttributes.lookup(UUID, getString(R.string.UNKNOW_SRV));
        //128 to 16 bit uuid
        String UUID16bit = "UUDI " + "0x"+UUID.substring(startIdx,EndIdx).toUpperCase();
        String[] retStr = new String[] {serviceName, UUID16bit};
        return retStr;
    }

    String [] GetCharateristicData(BluetoothGattCharacteristic characteristic)
    {
//        ex:String service = "0000180f-0000-1000-8000-00805f9b34fb";
        int startIdx = 4;
        int EndIdx = 7 + 1;
        String UUID = characteristic.getUuid().toString();
        //find characteristic
        String charaName = GattAttributes.lookup(UUID, getString(R.string.UNKNOW_CHARA));
        //128 to 16 bit uuid
        String UUID16bit = "UUID:" + "0x" + UUID.substring(startIdx, EndIdx).toUpperCase()+ NEW_LINE;
        String property = "property:" +GattAttributes.GetProperty(characteristic.getProperties()) + NEW_LINE;
        //String value = "value:" + StaticLiteble.getBluetoothGatt().readCharacteristic(characteristic) + NEW_LINE;
        String[] retStr = new String[]{charaName, UUID16bit + property };
        return retStr;
    }

    String [] GetDescrData(BluetoothGattDescriptor descriptor)
    {
//        ex:String service = "0000180f-0000-1000-8000-00805f9b34fb";
        int startIdx = 4;
        int EndIdx = 7+1;
        String UUID = descriptor.getUuid().toString();
        //find descriptor
        String decrName =  GattAttributes.lookup(UUID, getString(R.string.UNKNOW_DESC));
        //128 to 16 bit uuid
        String UUID16bit = "UUID:"+"0x"+UUID.substring(startIdx,EndIdx).toUpperCase();
        String[] retStr = new String[] {decrName, UUID16bit};
        return retStr;
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
//                Log.w(TAG, "serv id: " + serviceID);
                addElement(GetServiceData(service),
                        Element.TOP_LEVEL,
                        serviceID, Element.NO_PARENT, true, false);
                ID = ID+1;
                for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                    Log.d(TAG, "  characteristic: " + characteristic.getUuid() + " value: " + Arrays.toString(characteristic.getValue()));

                    int charaID = ID;
//                    Log.w(TAG, "ID: " + ID + ",charaID:" + charaID+",serviceID:" + serviceID);
                    addElementsdata(GetCharateristicData(characteristic),
                            Element.TOP_LEVEL + 1,
                            ID, serviceID, true, false);
                    ID = ID+1;
                    for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
//                        Log.v(TAG, "        descriptor: " + descriptor.getUuid() + " value: " + Arrays.toString(descriptor.getValue()));
//                        Log.w(TAG, "ID: " + ID + ",charaID:" + charaID+",serviceID:" + serviceID);
                        addElementsdata(GetDescrData(descriptor),
                                Element.TOP_LEVEL + 2,
                                ID, charaID, false, false);
                        ID = ID+1;
                    }
                }
            }
        }
    }
}
