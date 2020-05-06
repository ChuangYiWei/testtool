package com.BLE.johnny_wei.testtool.BLEutils;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;


public class Ble_data {
    public int type;
    public int property;
    public BluetoothGattService service;
    public BluetoothGattCharacteristic characteristic;
    public BluetoothGattDescriptor descriptor;

}
