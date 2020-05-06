package com.BLE.johnny_wei.testtool;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.BLE.johnny_wei.testtool.xmlpull.BLE_testItem;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class BD_Testclass2 extends AppCompatActivity {

    String TAG = getClass().getSimpleName();
    LinkedList<BDAutoTest2> AutoTestList2;
    private String BD_TEST_INTV_WRITE = "BD_TEST_INTV_WRITE";
    private String BD_TEST_NAME_WRITE = "BD_TEST_NAME_WRITE";
    private String BD_TEST_ADDR_WRITE = "BD_TEST_ADDR_WRITE";


    private final int INTV_WRITE = 0x0;
    private final int NAME_WRITE = 0x1;

    Context mcontext;

    //com.example.johnny_wei.testtool.ble
    private boolean mScanning;
    private static final long SCAN_PERIOD = 5000;
    private Handler m_userHandler;
    private BluetoothAdapter mBluetoothAdapter;

    BD_Testclass2(Context context)
    {
        mcontext = context;
        AutoTestList2 = new LinkedList<BDAutoTest2>();
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

    private void scanLeDevice(final boolean enable,final long scanTime) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            m_userHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, scanTime);

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
                    checkAdvData(device, rssi, scanRecord);
                }
            };

    public void checkAdvData(final BluetoothDevice device, final int rssi,
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

    public void BD_IntvWrite()
    {
        getTestlist(BD_TEST_INTV_WRITE);

        while(AutoTestList2.size() > 0) {
            BDAutoTest2 testitem = AutoTestList2.getFirst();
            int res = -1;
            if( 0 != test_IntvWrite(testitem)){
                //do something, update fail
                Log.d(TAG, "test fail");
            }

            AutoTestList2.removeFirst();
        }
    }

    public int funcA(BLE_testItem testItem)
    {
        Log.d(TAG, "\nfuncA----------------------S");
        Log.d(TAG, "gettestName:" + testItem.gettestName());
        Log.d(TAG, "data:" + testItem.getdataStr());
        Log.d(TAG, "cmd" + testItem.getCmd());
        testItem.popCmd();
        Log.d(TAG, "cmd:" + testItem.getCmd());
        Log.d(TAG, "funcA----------------------E");
        return 97;
    }

    public int funcB(BLE_testItem testItem)
    {
        Log.d(TAG, "\nfunc----------------------S");
        Log.d(TAG, "gettestName:" + testItem.gettestName());
        Log.d(TAG, "data:" + testItem.getdataStr());
        Log.d(TAG, "cmd" + testItem.getCmd());
        testItem.popCmd();
        Log.d(TAG, "cmd:" + testItem.getCmd());
        Log.d(TAG, "func----------------------E");
        return 97;
    }

    public int test_IntvWrite(BDAutoTest2 testitem)
    {
        //uart
        Log.d(TAG, "S----------------------");
        Log.d(TAG, "gettestName:" + testitem.gettestName());
        Log.d(TAG, "getdataStr:" + testitem.getdataStr());

        Log.d(TAG, "E----------------------");
        while(testitem.cmdSize() > 0)
        {
            String cmd = testitem.getCmd().split(":")[0];
            String evt = testitem.getCmd().split(":")[1];
            Log.d(TAG, "cmd:" + cmd + "evt:" + evt);

            testitem.popCmd();
        }

        //com.example.johnny_wei.testtool.ble
        long advIntv = Long.parseLong(testitem.getdataStr());
        long scanTime = 5000;
        scanLeDevice(true, scanTime);
        SystemClock.sleep(scanTime);
        //xmlandinvoke.mbleService.connect("20:18:10:31:17:56");

        return 0;
    }


    private void getTestlist(String testname) {
        try {
            AutoTestList2.clear();
            InputStream input = mcontext.getAssets().open("bd_autotest2.xml");
            List<BDAutoTest2> list = ParserAutotest2.getAutoTestItems(input);
            for (BDAutoTest2 item : list) {
                //check specific test name
                //Log.d(TAG,"test name:"+item.gettestName());
                if(item.gettestName().equals(testname)) {
                    AutoTestList2.add(item);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void BD_NameWrite()
    {
        getTestlist(BD_TEST_NAME_WRITE);

    }

    public void BD_AddrWrite() {
        Log.d(TAG, "BD_TEST_ADDR_WRITE");
        getTestlist(BD_TEST_ADDR_WRITE);
    }
}
