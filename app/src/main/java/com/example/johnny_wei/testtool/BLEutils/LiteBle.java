package com.example.johnny_wei.testtool.BLEutils;

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
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.util.Log;


import com.example.johnny_wei.testtool.BLEutils.callback.IBLEScanCallback;
import com.example.johnny_wei.testtool.BLEutils.callback.IBLECallback;
import com.example.johnny_wei.testtool.config.globalConfig;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static android.bluetooth.BluetoothDevice.TRANSPORT_LE;
import static com.example.johnny_wei.testtool.config.globalConfig.PERMISSION_REQUEST_COARSE_LOCATION;


public class LiteBle  {
    public static LiteBle StaticLiteble;
    private Context mContext = null;
    IBLECallback IbleCB = null;
    IBLEScanCallback IbleScanCB = null;
    private BluetoothManager mbluetoothManager;
    private BluetoothAdapter mbluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private String mBluetoothDeviceAddress;

    //Add in API 21
    private BluetoothLeScanner mBluetoothLeScanner;

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

    public static final int CB_STATE_CHARA_READ_SUCCESS = 0x10;
    public static final int CB_STATE_CHARA_READ_FAIL = 0x11;

    public static final int CB_STATE_CHARA_WRITE_SUCCESS = 0x12;
    public static final int CB_STATE_CHARA_WRITE_FAIL = 0x13;

    public static final int CB_STATE_CHARA_CHANGED_SUCCESS = 0x14;
    public static final int CB_STATE_CHARA_CHANGED_FAIL = 0x15;

    public static final int CB_STATE_DESC_READ_SUCCESS = 0x16;
    public static final int CB_STATE_DESC_READ_FAIL = 0x17;

    public static final int CB_STATE_DESC_WRITE_SUCCESS = 0x18;
    public static final int CB_STATE_DESC_WRITE_FAIL = 0x19;

    public LiteBle(Context context) {
        this.mContext = context = context.getApplicationContext();
        mbluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mbluetoothAdapter = mbluetoothManager.getAdapter();
        StaticLiteble = this;

        Log.w(TAG,"LiteBle open thread id:" + Thread.currentThread().getId());
    }

    public void setCallback(IBLECallback cb)
    {
        IbleCB = cb;
    }

    public void listenScanCallback(IBLEScanCallback cb)
    {
        IbleScanCB = cb;
    }


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
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopLeScan(scanCallback);
                fireScanCallback(STATE_DISCONNECTED);
                isMainThread();
            }
        }, scanTime);

        connectionState = STATE_SCANNING;
        mbluetoothAdapter.startLeScan(scanCallback);
        fireScanCallback(connectionState);
    }

    void fireScanCallback(int connectionState) {
        if (IbleScanCB != null) {
            if (connectionState == STATE_SCANNING) {
                IbleScanCB.OnScanStart();
            } else if (connectionState == STATE_DISCONNECTED) {
                IbleScanCB.OnScanStop();
            }
        }
    }

    public void stopLeScan(BluetoothAdapter.LeScanCallback scanCallback) {
        mbluetoothAdapter.stopLeScan(scanCallback);
        if (connectionState == STATE_SCANNING) {
            connectionState = STATE_DISCONNECTED;
        }
        fireScanCallback(connectionState);
    }

    @RequiresApi(21)
    public void startAPI21_LeScan(final ScanCallback scanCallback, final long scanTime) {
        mBluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
        // Stops scanning after a pre-defined scan period.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBluetoothLeScanner.stopScan(scanCallback);
                isMainThread();
                fireScanCallback(STATE_DISCONNECTED);
            }
        }, scanTime);

        connectionState = STATE_SCANNING;
        mBluetoothLeScanner.startScan(scanCallback);
        fireScanCallback(connectionState);
    }

    @RequiresApi(21)
    public void startAPI21_LeScan(final ScanCallback scanCallback, List<ScanFilter> filters, ScanSettings settings, final long scanTime) {
        mBluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
        // Stops scanning after a pre-defined scan period.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBluetoothLeScanner.stopScan(scanCallback);
                isMainThread();
                fireScanCallback(STATE_DISCONNECTED);
            }
        }, scanTime);

        connectionState = STATE_SCANNING;
        mBluetoothLeScanner.startScan(filters, settings, scanCallback);
        fireScanCallback(connectionState);
    }

    @RequiresApi(21)
    public void stopAPI21_LeScan(ScanCallback scanCallback) {
        mBluetoothLeScanner.stopScan(scanCallback);
        if (connectionState == STATE_SCANNING) {
            connectionState = STATE_DISCONNECTED;
        }
        fireScanCallback(connectionState);
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
        SystemClock.sleep(3000);
        enableBluetooth();
        SystemClock.sleep(3000);
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

    /*------------  Service  ------------ */
    public BluetoothGattService getService(BluetoothGatt gatt, String serviceUUID) {
        return gatt.getService(UUID.fromString(serviceUUID));
    }

    /*------------  Characteristic服务  ------------ */
    public BluetoothGattCharacteristic getCharacteristic(BluetoothGattService service, String charactUUID) {
        if (service != null) {
            return service.getCharacteristic(UUID.fromString(charactUUID));
        }
        return null;
    }

    public BluetoothGattCharacteristic getCharacteristic(BluetoothGatt gatt, String serviceUUID, String charactUUID) {
        BluetoothGattService service = gatt.getService(UUID.fromString(serviceUUID));
        if (service != null) {
            return service.getCharacteristic(UUID.fromString(charactUUID));
        }
        return null;
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
                if (Build.VERSION.SDK_INT >= 23) {
                    Log.w(TAG,"connection with preferred TRANSPORT_LE.");
                    mBluetoothGatt = device.connectGatt(mContext, false, mGattCallback, TRANSPORT_LE);
                } else {
                    mBluetoothGatt = device.connectGatt(mContext, false, mGattCallback);
                }
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
                sendCallback(STATE_DISCONNECTED);//in case of gatt not send onConnectionStateChange callback
            }
        });
    }

    public boolean enableCharacteristicNotify(
            final String str_uuid_service,
            final String characteristicUUID,
            final String descriptorUUID)
    {
        Log.d(TAG,"setCharacteristicNotify");
        if (mbluetoothAdapter == null || mBluetoothGatt == null) {
            Log.e(TAG,"BluetoothAdapter null");
            return false;
        }

        BluetoothGattService service = getService(mBluetoothGatt, str_uuid_service);
        if (service == null) {
            Log.e(TAG,  "service is null");
            return false;
        }

        BluetoothGattCharacteristic chara = getCharacteristic(service, characteristicUUID);
        if (chara == null) {
            Log.e(TAG, "BluetoothGattCharacteristic is null");
            return false;
        }

        if (!mBluetoothGatt.setCharacteristicNotification(chara, true)) {
            Log.e(TAG,  "setCharacteristicNotification fail");
            return false;
        }

        BluetoothGattDescriptor descriptor = chara.getDescriptor(UUID.fromString(descriptorUUID));

        if (null != descriptor) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            if (!mBluetoothGatt.writeDescriptor(descriptor)) {
                Log.e(TAG, "writeDescriptor fail");
                return false;
            }
        } else {
            Log.e(TAG,  "null descriptor");
            return false;
        }
        return true;
    }

    /**
     * write data to characteristic
     */
    public boolean writeDataToCharacteristic(
            final String serviceUUID,
            final String characteristicUUID,
            byte[] bytes) {
        Log.d(TAG,"writeDataToCharacteristic:" + characteristicUUID);
        BluetoothGattService service = getService(mBluetoothGatt, serviceUUID);
        if (service == null) {
            Log.e(TAG,  "service is null");
            return false;
        }

        BluetoothGattCharacteristic chara = getCharacteristic(service, characteristicUUID);
        if (chara == null) {
            Log.e(TAG, "BluetoothGattCharacteristic is null");
            return false;
        }

        chara.setValue(bytes);
        if (!mBluetoothGatt.writeCharacteristic(chara)) {
            Log.e(TAG, "write characteristic fail");
        }

        return true;

    }
    /**
     * read characteristic data
     */
    public boolean readCharacteristic(final String serviceUUID, final String characteristicUUID) {
        Log.d(TAG, "readCharacteristic uuid:" + characteristicUUID);

        if (mbluetoothAdapter == null || mBluetoothGatt == null) {
            Log.d(TAG, "BluetoothAdapter null");
            return false;
        }
        UUID service_uuid = UUID.fromString(serviceUUID);
        UUID chara_uuid = UUID.fromString(characteristicUUID);
        BluetoothGattService service = mBluetoothGatt.getService(service_uuid);

        if (service == null) {
            Log.e(TAG, "service is null");
            return false;
        }

        BluetoothGattCharacteristic chara = service.getCharacteristic(chara_uuid);
        if (chara == null) {
            Log.e(TAG, "BluetoothGattCharacteristic is null");
            return false;
        }

        mBluetoothGatt.readCharacteristic(chara);
        return true;
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
                    sendCallback(STATE_CONNECTED);
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    connectionState = STATE_DISCONNECTED;
                    Log.d(TAG, "Disconnected from GATT server.");
                    sendCallback(STATE_DISCONNECTED);
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
                if(CBnotNULL()) {
                    IbleCB.SrvDiscoverSuccessCB();
                }
            } else {
                if(CBnotNULL()) {
                    String err = "";
                    IbleCB.SrvDiscoverFailCB(err + status);
                }
                printConnectException(gatt, status);
            }
            super.onServicesDiscovered(gatt, status);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d(TAG, "onCharacteristicRead");
            if (BluetoothGatt.GATT_SUCCESS == status) {
                if(CBnotNULL()) {
                    IbleCB.readCharacteristicSuccessCB(
                            characteristic.getUuid().toString(),
                            characteristic.getValue());
                }
            } else {
                if(CBnotNULL()) {
                    IbleCB.readCharacteristicFailCB(characteristic.getUuid().toString(), status);
                }
                Log.e(TAG, "onCharacteristicRead fail");
            }
            //printbytes(characteristic.getValue());
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d(TAG, "onCharacteristicWrite");
            if (BluetoothGatt.GATT_SUCCESS == status) {
                if(CBnotNULL()) {
                    IbleCB.writeCharacteristicSuccessCB(
                            characteristic.getUuid().toString(),
                            characteristic.getValue());
                }
            } else {
                if(CBnotNULL()) {
                    IbleCB.writeCharacteristicFailCB(characteristic.getUuid().toString(), status);
                }
                Log.e(TAG, "onCharacteristicWrite fail");
            }
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.d(TAG, "onCharacteristicChanged");
            if(CBnotNULL()) {
                IbleCB.CharaValueChangedSuccessCB(
                        characteristic.getUuid().toString(),
                        characteristic.getValue()
                );
            }
            printbytes(characteristic.getValue());
            super.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            Log.d(TAG, "onDescriptorRead");
            if (BluetoothGatt.GATT_SUCCESS == status) {
                if(CBnotNULL()) {
                    IbleCB.readDescSuccessCB(
                            descriptor.getUuid().toString(),
                            descriptor.getValue());
                }
            } else {
                if(CBnotNULL()) {
                    IbleCB.readDescFailCB(descriptor.getUuid().toString(), status);
                }
                Log.e(TAG, "onDescriptorRead fail");
            }
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            Log.d(TAG, "onDescriptorWrite");
            if (BluetoothGatt.GATT_SUCCESS == status) {
                if(CBnotNULL()) {
                    IbleCB.writeDescSuccessCB(
                            descriptor.getUuid().toString(),
                            descriptor.getValue());
                }
            } else {
                if(CBnotNULL()) {
                    IbleCB.writeDescFailCB(descriptor.getUuid().toString(), status);
                }
                Log.e(TAG, "onDescriptorRead fail");
            }
            super.onDescriptorWrite(gatt, descriptor, status);
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            Log.d(TAG, "onMtuChanged");
            super.onMtuChanged(gatt, mtu, status);
        }

        @Override
        public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            Log.w(TAG, "onPhyRead");
            Log.d(TAG, "txPhy:" + txPhy);
            Log.d(TAG, "rxPhy:" + rxPhy);
            Log.d(TAG, "status:" + status);
            super.onPhyRead(gatt, txPhy, rxPhy, status);
        }

        @Override
        public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            Log.w(TAG, "onPhyUpdate !!!");
            Log.d(TAG, "txPhy:" + txPhy);
            Log.d(TAG, "rxPhy:" + rxPhy);
            Log.d(TAG, "status:" + status);
            super.onPhyUpdate(gatt, txPhy, rxPhy, status);
        }
    };

    void printConnectException(BluetoothGatt bluetoothGatt, int gattStatus)
    {
        String errStr = "";
        Log.e(TAG, "ConnectException bluetoothGatt=" + bluetoothGatt);
        if (gattStatus == globalConfig.GATT_CONN_TERMINATE_LOCAL_HOST) {
            errStr = "GATT_CONN_TERMINATE_LOCAL_HOST";
            Log.w(TAG, "GATT_CONN_TERMINATE_LOCAL_HOST, resetConnectState !");
        } else if (gattStatus == globalConfig.GATT_CONN_TIMEOUT) {
            errStr = "GATT_CONN_TIMEOUT";
            Log.e(TAG, "GATT_CONN_TIMEOUT!");
        } else if (gattStatus == globalConfig.GATT_INTERNAL_ERROR) {
            errStr = "GATT_INTERNAL_ERROR";
            Log.e(TAG, "GATT_INTERNAL_ERROR!");
        } else {
            errStr = "unknow: " + gattStatus;
            Log.e(TAG, "ConnectException received: " + gattStatus);
        }
        if(CBnotNULL()) {
            IbleCB.ConnectFailCB(errStr);
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

    public void printbytes(byte bytes[]){
        String printStr = "";
        for (byte data : bytes) {
            printStr = printStr + String.format("%02x", data);
        }
        Log.d(TAG, "printbytes:" + printStr);
    }

    private void sendCallback(int state)
    {
        switch (state) {
            case STATE_CONNECTED:
                IbleCB.ConnectedCB();
                break;
            case STATE_DISCONNECTED:
                IbleCB.DisConnectCB();
                break;
            default:
                break;
        }
    }

    private boolean CBnotNULL()
    {
        return (IbleCB != null);
    }

}