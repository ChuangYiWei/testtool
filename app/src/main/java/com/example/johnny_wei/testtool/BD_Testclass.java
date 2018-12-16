package com.example.johnny_wei.testtool;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class BD_Testclass extends AppCompatActivity {

    String TAG = getClass().getSimpleName();
    LinkedList<BDAutoTest> AutoTestList;
    private String BD_TEST_NAME_WRITE = "BD_TEST_NAME_WRITE";
    private String BD_TEST_ADDR_WRITE = "BD_TEST_ADDR_WRITE";
    Context mcontext;

    //com.example.johnny_wei.testtool.ble
    private boolean mScanning;
    private static final long SCAN_PERIOD = 5000;
    private Handler m_userHandler;
    private BluetoothAdapter mBluetoothAdapter;

    BD_Testclass(Context context)
    {
        mcontext = context;
        AutoTestList = new LinkedList<BDAutoTest>();
        init();
    }

    private void init() {

        HandlerThread ht_thread = new HandlerThread("name");
        ht_thread.start();

        m_userHandler = new Handler(ht_thread.getLooper());

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) mcontext.getSystemService(mcontext.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            m_userHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi,
                                     byte[] scanRecord) {
                    String name = device.getName();
                    if (name != null) {
                        if (device.getName().equals("2018_1031_17")) {
                            //AddAdvIntvTime();
                            //parseAdv(scanRecord);
                            Log.d(TAG, "device name:" + device.getName() + ", device address:" + device.getAddress() + Integer.toString(rssi));
                            //Log.d(TAG, "rssi:" + Integer.toString(rssi));
//                            Log.d(TAG, "scan record length:" + scanRecord.length);
//                            Log.d(TAG, "scan record:" + byteArrayToHex(scanRecord));
                        }
                    }
                }
            };

    public void BD_Scan()
    {
        scanLeDevice(true);
        SystemClock.sleep(10000);
    }

    public void BD_NameWrite()
    {
        getTestlist(BD_TEST_NAME_WRITE);

        while(AutoTestList.size() > 0) {
            BDAutoTest testitem = AutoTestList.getFirst();
            Log.d(TAG,"gettestName:"+testitem.gettestName());
            Log.d(TAG,"getdataStr:"+testitem.getdataStr());
            Log.d(TAG,"write cmd:"+testitem.getwriteCmd());

            String writeCmd = testitem.getwriteCmd().split(":")[0];
            String writeSubStrCmd[] = {};
            if (writeCmd.contains("+")) {
                writeSubStrCmd = writeCmd.split("\\+");
            }
            else
            {
                writeSubStrCmd[0] = writeCmd;
            }
            int i;
            for(i=0;i<writeSubStrCmd.length;i++)
            {
                Log.d(TAG,"i:"+ Integer.toString(i)+ "==>" + writeSubStrCmd[i]);
            }

            Log.d(TAG,"write expected cmd:"+testitem.getwriteCmd().split(":")[1]);


            Log.d(TAG,"getwriteCmd:"+testitem.getwriteCmd());
            Log.d(TAG,"getreadCmd:"+testitem.getreadCmd());
            AutoTestList.removeFirst();
        }
    }

    private void getTestlist(String testname) {
        try {
            InputStream input = mcontext.getAssets().open("bd_autotest.xml");
            List<BDAutoTest> list = ParserAutotest.getAutoTestItems(input);
            for (BDAutoTest item : list) {
                //check specific test name
                if(item.gettestName().equals(testname)) {
                    AutoTestList.add(item);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void BD_AddrWrite()
    {
        Log.d(TAG,"BD_TEST_ADDR_WRITE");
        getTestlist(BD_TEST_ADDR_WRITE);

        while(AutoTestList.size() > 0) {
            BDAutoTest testitem = AutoTestList.getFirst();
            Log.d(TAG,"gettestName:"+testitem.gettestName());
            Log.d(TAG,"getdataStr:"+testitem.getdataStr());
            Log.d(TAG,"write cmd:"+testitem.getwriteCmd());

            String writeCmd = testitem.getwriteCmd().split(":")[0];
            String writeSubStrCmd[] = {""};
            if (writeCmd.contains("+")) {
                writeSubStrCmd = writeCmd.split("\\+");
            }
            else
            {
                writeSubStrCmd[0] = writeCmd;
            }
            int i;
            for(i=0;i<writeSubStrCmd.length;i++)
            {
                Log.d(TAG,"i:"+ Integer.toString(i)+ "==>" + writeSubStrCmd[i]);
            }

            Log.d(TAG,"write expected cmd:"+testitem.getwriteCmd().split(":")[1]);


            Log.d(TAG,"getwriteCmd:"+testitem.getwriteCmd());
            Log.d(TAG,"getreadCmd:"+testitem.getreadCmd());
            AutoTestList.removeFirst();
        }
    }
}
