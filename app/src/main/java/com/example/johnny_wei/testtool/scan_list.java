package com.example.johnny_wei.testtool;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.johnny_wei.testtool.adapter.RowItem;
import com.example.johnny_wei.testtool.adapter.ScanListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class scan_list extends AppCompatActivity {

    String TAG = getClass().getSimpleName();
    ListView listView;
    List<RowItem> rowItems;
    ScanListAdapter mAdapter;

    private  HashMap<String, Integer> mdevMap;
    Activity thisActivity = this;

    private LiteBle liteBluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_list);

        liteBluetooth = new LiteBle(thisActivity);
        liteBluetooth.enableBluetoothIfDisabled(thisActivity, 1);
        mdevMap = new HashMap<String, Integer>();
        setupView();
        scan();
    }

    void scan()
    {
        // Device scan callback.
        liteBluetooth.startLeScan(mLeScanCallback, 10000);
    }

    private void setupView() {
        listView = (ListView) findViewById(R.id.ble_list);
        rowItems = new ArrayList<>();
        mAdapter = new ScanListAdapter(this, rowItems);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Item " + (position + 1) + ": " + rowItems.get(position),
                        Toast.LENGTH_SHORT);
                toast.show();

            }
        });
    }

    //user implement this
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1  && resultCode == RESULT_OK){
            Log.d(TAG,"open bluetooth OK");
        }else if(requestCode == 1 && resultCode == RESULT_CANCELED){
            Toast.makeText(thisActivity,"please open bluetooth !! ",Toast.LENGTH_LONG).show();
            finish();
        }
    }


    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi,
                                     byte[] scanRecord) {
                    String devAddr = device.getAddress();
                    Log.i(TAG, "device name:" + device.getName() + ", device address:" + device.getAddress() + Integer.toString(rssi));
                    if (null != devAddr) {
                        //if we don't have devAddr yet, add devaddr/rssi as key/value
                        if ((!mdevMap.containsKey(devAddr) ) ) {
                            RowItem item = new RowItem(device.getName(), device.getAddress(), Integer.toString(rssi));
                            rowItems.add(item);
                            mdevMap.put(device.getAddress(), rssi);
                            (mAdapter).notifyDataSetChanged();
                        }
                        else if(mdevMap.containsKey(devAddr))
                        {
                            //update rssi if different
                            updateRssi(devAddr, rssi);
                        }
//                        Log.i(TAG, "device name:" + device.getName() + ", device address:" + device.getAddress() + Integer.toString(rssi));
                    }
                }
                void updateRssi(String devAddr, int rssi)
                {
                    if(mdevMap.get(devAddr) != rssi)
                    {
                        mdevMap.put(devAddr,rssi);
                        int listsize = rowItems.size();
                        listsize = listsize -1;//index start from 0
                        while(listsize >= 0)
                        {
                            if(rowItems.get(listsize).getstrL2().equals(devAddr))
                            {
                                Log.d(TAG, "update rssi:" + rssi);
                                rowItems.get(listsize).setstrL3(Integer.toString(rssi));
                            }
                            (mAdapter).notifyDataSetChanged();
                            listsize--;
                        }
                    }
                }
    };
}
