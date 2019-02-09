package com.example.johnny_wei.testtool;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import com.example.johnny_wei.testtool._00_util.commonutil;

import java.lang.reflect.Method;
import java.net.ConnectException;
import java.util.Arrays;

import static com.example.johnny_wei.testtool.permision_test.PERMISSION_REQUEST_COARSE_LOCATION;

public class LiteBle {
    private Context mContext;
    private BluetoothManager mbluetoothManager;
    private BluetoothAdapter mbluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private String mBluetoothDeviceAddress;

    private final int REQUEST_ENABLE_BT = 1;
    private Handler m_userHandler;
    private Handler handler = new Handler(Looper.getMainLooper());

    String TAG = getClass().getSimpleName();

    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_SCANNING = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;
    public static final int STATE_SERVICES_DISCOVERED = 4;

    private int connectionState = STATE_DISCONNECTED;

    public LiteBle(Context context) {
        this.mContext = context = context.getApplicationContext();
        mbluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mbluetoothAdapter = mbluetoothManager.getAdapter();
        HandlerThread ht_thread = new HandlerThread("name");
        ht_thread.start();
        m_userHandler = new Handler(ht_thread.getLooper());
    }

    //todo:getinstance



    public void enableBluetoothIfDisabled(Activity activity, int requestCode) {

        if (!mbluetoothAdapter.isEnabled()) {
            //if API level > 23, we need to ask permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permission = activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
                //未取得權限，向使用者要求允許權限
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    activity.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                }
            }

            // Ensures Bluetooth is available on the device and it is enabled. If not,
            // displays a dialog requesting user permission to enable Bluetooth.
            if (!mbluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    public void startLeScan(final BluetoothAdapter.LeScanCallback scanCallback, final long scanTime) {
        // Stops scanning after a pre-defined scan period.
        m_userHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopLeScan(scanCallback);
                isMainThread();
            }
        }, scanTime);

        connectionState = STATE_SCANNING;
        mbluetoothAdapter.startLeScan(scanCallback);
    }

    public void stopLeScan(BluetoothAdapter.LeScanCallback scanCallback) {
        mbluetoothAdapter.stopLeScan(scanCallback);
        if (connectionState == STATE_SCANNING) {
            connectionState = STATE_DISCONNECTED;
        }
    }

    /**
     * return
     * {@link #STATE_DISCONNECTED}
     * {@link #STATE_SCANNING}
     * {@link #STATE_CONNECTING}
     * {@link #STATE_CONNECTED}
     * {@link #STATE_SERVICES_DISCOVERED}
     */
    public int getConnectionState() {
        return connectionState;
    }

    public void enableBluetooth() {
        mbluetoothAdapter.enable();
    }

    public void disableBluetooth() {
        Log.d(TAG,"disableBluetooth");
        mbluetoothAdapter.disable();
    }

    public boolean rebootBluetooth() {
        disconnect();
        disableBluetooth();
        SystemClock.sleep(5000);
        enableBluetooth();
        SystemClock.sleep(5000);
        return true;
    }

    public Context getContext() {
        return mContext;
    }

    public BluetoothManager getBluetoothManager() {
        return mbluetoothManager;
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return mbluetoothAdapter;
    }

    public BluetoothGatt getBluetoothGatt() {
        return mBluetoothGatt;
    }

    public boolean isConnected() {
        return connectionState >= STATE_CONNECTED;
    }

    public boolean isServiceDiscoered() {
        return connectionState == STATE_SERVICES_DISCOVERED;
    }

    private boolean isSuccess;


    public boolean connect(final String address) {
        Log.d(TAG,"=>connect addr: " + address);
        if (mbluetoothAdapter == null || address == null) {
            Log.d(TAG,"BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device. Try to reconnect.
        if (mBluetoothDeviceAddress != null
                && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    isSuccess = (mBluetoothGatt.connect()) ? true : false;
                }
            });

            if (!isSuccess) {
                Log.e(TAG,"reconnect fail");
                return false;
            } else {
                Log.d(TAG,"reconnect !");
                return true;
            }
        }

        final BluetoothDevice device = mbluetoothAdapter.getRemoteDevice(address);

        if (device == null) {
            Log.e(TAG,"there is no device");
            return false;
        }

        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                mBluetoothGatt = device.connectGatt(mContext, false, mGattCallback);
            }
        });

        connectionState = STATE_CONNECTING;
        mBluetoothDeviceAddress = address;
        Log.d(TAG,"Trying to create a new connection.");

        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The
     * disconnection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        Log.d(TAG,"=>disconnect");
        if (mbluetoothAdapter == null || mBluetoothGatt == null) {
            Log.e(TAG,"BluetoothAdapter not initialized");
            return;
        }

        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                mBluetoothGatt.disconnect();
                refresDeviceCache();
                mBluetoothGatt.close();
                connectionState = STATE_DISCONNECTED;
                mBluetoothGatt = null;
                Log.d(TAG, "closed BluetoothGatt ");
            }
        });
    }


    /*gatt callback*/
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.d(TAG, "onConnectionStateChange  status: " + status
                    + " ,newState: " + newState + "  ,thread: " + Thread.currentThread().getId());

            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.d(TAG, "Connected to GATT server. Attempting to start service discovery");
                    runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mBluetoothGatt.discoverServices();
                        }
                    });
                    connectionState = STATE_CONNECTED;
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    connectionState = STATE_DISCONNECTED;
                    Log.d(TAG, "Disconnected from GATT server.");
                }
            } else {
                printConnectException(gatt, status);
                disconnect();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d(TAG, "onServicesDiscovered.");
            if (BluetoothGatt.GATT_SUCCESS == status) {
                connectionState =  STATE_SERVICES_DISCOVERED;
                //todo
            } else {
                printConnectException(gatt, status);
            }
            super.onServicesDiscovered(gatt, status);
        }
    };

    void printConnectException(BluetoothGatt bluetoothGatt, int gattStatus)
    {
        Log.e(TAG, "ConnectException bluetoothGatt=" + bluetoothGatt);
        if (gattStatus == globalConfig.GATT_CONN_TERMINATE_LOCAL_HOST) {
            Log.w(TAG, "GATT_CONN_TERMINATE_LOCAL_HOST, resetConnectState !");
        } else if (gattStatus == globalConfig.GATT_CONN_TIMEOUT) {
            Log.e(TAG, "GATT_CONN_TIMEOUT!");
        } else if (gattStatus == globalConfig.GATT_INTERNAL_ERROR) {
            Log.e(TAG, "GATT_INTERNAL_ERROR!");
        } else {
            Log.e(TAG, "ConnectException received: " + gattStatus);
        }
    }

    public  void printServices(BluetoothGatt gatt) {
        if (gatt != null) {
            for (BluetoothGattService service : gatt.getServices()) {
                Log.i(TAG, "service: " + service.getUuid());
                for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                    Log.d(TAG, "  characteristic: " + characteristic.getUuid() + " value: " + Arrays.toString(characteristic.getValue()));
                    for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                        Log.v(TAG, "        descriptor: " + descriptor.getUuid() + " value: " + Arrays.toString(descriptor.getValue()));
                    }
                }
            }
        }
    }

    /**
     * There is a refresh() method in BluetoothGatt class but for now it's hidden. We will call it using reflections.
     */
    public boolean refresDeviceCache() {
        try {
            Method localMethod = BluetoothGatt.class.getMethod("refresh");
            if (localMethod != null) {
                Log.w(TAG, "refresh gatt");
                isSuccess = (boolean) localMethod.invoke(mBluetoothGatt);
                Log.d(TAG, "Refreshing result: " + isSuccess);
            }
        } catch (Exception localException) {
            Log.e(TAG, "An exception occured while refreshing device");
            isSuccess = false;
        }
//        SystemClock.sleep(200);
        return isSuccess;
    }


    public  boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public void runOnMainThread(Runnable runnable) {
        if (isMainThread()) {
            Log.d(TAG, "from main thread:");
            runnable.run();
        } else {
            Log.d(TAG, "not main thread:");
            handler.post(runnable);
        }
//        runOnMainThread(new Runnable() {
//            @Override
//            public void run() {
//                connect(device, autoConnect, callback);
//            }
//        });
    }
}