package com.BLE.johnny_wei.testtool.BLEutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ble_AdvUtil {
    public static List<String> parseScanRecord(final byte[] scanRecord) {

        List<String> advertiseList = new ArrayList<>();
        int index = 0;
        while (index < scanRecord.length) {

            final int length = scanRecord[index++];
            //Done once we run out of records
            if (length == 0) break;

            final int type = scanRecord[index] & 0xFF;

            //Done if our record isn't a valid type
            if (type == 0) break;

            final byte[] data = Arrays.copyOfRange(scanRecord, index + 1, index + length);

            String strLength = "length:" + "0x" + String.format("%02x", length).toUpperCase() + "\n";
            String strType = "type: " + "0x" + String.format("%02x", type).toUpperCase() ;
            strType += Advtype2String(type) + "\n";
            String strData = "data:" + "0x" + byteArrayToHex(data) + "\n";

            advertiseList.add(strLength + strType + strData);
            //Advance
            index += length;
        }
        return advertiseList;
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length);
        for(byte b: a)
            sb.append(String.format("%02x", b).toUpperCase());
        return sb.toString();
    }

    private static String Advtype2String(int type)
    {
        String retStr = "";
        switch(type)
        {
            case BLE_GAP_AD_TYPE_FLAGS:
                retStr = "[discoverAbility]";
                break;
            case BLE_GAP_AD_TYPE_COMPLETE_LOCAL_NAME:
                retStr = "[Complete local name]";
                break;
            case BLE_GAP_AD_TYPE_MANUFACTURER_SPECIFIC_DATA:
                retStr = "[Manufacturer data]";
                break;
            case BLE_GAP_AD_TYPE_128BIT_SERVICE_UUID_COMPLETE:
                retStr = "[128bit service UUIDs]";
                break;
            case BLE_GAP_AD_TYPE_SERVICE_DATA:
                retStr = "[Service Data 16-bit UUID]";
                break;
            case BLE_GAP_AD_TYPE_16BIT_SERVICE_UUID_MORE_AVAILABLE:
                retStr = "[Partial list of 16 bit service UUIDs]";
                break;
            case BLE_GAP_AD_TYPE_16BIT_SERVICE_UUID_COMPLETE:
                retStr = "[Complete list of 16 bit service UUIDs]";
                break;
            case BLE_GAP_AD_TYPE_32BIT_SERVICE_UUID_MORE_AVAILABLE:
                retStr = "[Partial list of 32 bit service UUID]";
                break;
            case BLE_GAP_AD_TYPE_32BIT_SERVICE_UUID_COMPLETE:
                retStr = "[Complete list of 32 bit service UUIDs]";
                break;
            case BLE_GAP_AD_TYPE_128BIT_SERVICE_UUID_MORE_AVAILABLE:
                retStr = "[Partial list of 128 bit service UUIDs]";
                break;
            case BLE_GAP_AD_TYPE_SHORT_LOCAL_NAME:
                retStr = "[Short local device name]";
                break;
            case BLE_GAP_AD_TYPE_TX_POWER_LEVEL:
                retStr = "[Transmit power level]";
                break;
            case BLE_GAP_AD_TYPE_CLASS_OF_DEVICE:
                retStr = "[Class of device]";
                break;
            case BLE_GAP_AD_TYPE_SIMPLE_PAIRING_HASH_C:
                retStr = "[Simple Pairing Hash C-256]";
                break;
            case BLE_GAP_AD_TYPE_SIMPLE_PAIRING_RANDOMIZER_R:
                retStr = "[Simple Pairing Randomizer R-256]";
                break;
            case BLE_GAP_AD_TYPE_SECURITY_MANAGER_TK_VALUE:
                retStr = "[Security Manager TK Value]";
                break;
            case BLE_GAP_AD_TYPE_SECURITY_MANAGER_OOB_FLAGS:
                retStr = "[Security Manager Out Of Band Flags]";
                break;
            case BLE_GAP_AD_TYPE_SLAVE_CONNECTION_INTERVAL_RANGE:
                retStr = "[Slave Connection Interval Range]";
                break;
            case BLE_GAP_AD_TYPE_SOLICITED_SERVICE_UUIDS_16BIT:
                retStr = "[List of 16-bit Service Solicitation UUIDs]";
                break;
            case BLE_GAP_AD_TYPE_SOLICITED_SERVICE_UUIDS_128BIT:
                retStr = "[List of 128-bit Service Solicitation UUIDs]";
                break;
            case BLE_GAP_AD_TYPE_PUBLIC_TARGET_ADDRESS:
                retStr = "[Public Target Address]";
                break;
            case BLE_GAP_AD_TYPE_RANDOM_TARGET_ADDRESS:
                retStr = "[Random Target Address]";
                break;
            case BLE_GAP_AD_TYPE_APPEARANCE:
                retStr = "[Appearance]";
                break;
            case BLE_GAP_AD_TYPE_ADVERTISING_INTERVAL:
                retStr = "[Advertising Interval]";
                break;
            case BLE_GAP_AD_TYPE_LE_BLUETOOTH_DEVICE_ADDRESS:
                retStr = "[LE Bluetooth Device Address]";
                break;
            case BLE_GAP_AD_TYPE_LE_ROLE:
                retStr = "[LE Role]";
                break;
            case BLE_GAP_AD_TYPE_SIMPLE_PAIRING_HASH_C256:
                retStr = "[Simple Pairing Hash C-256]";
                break;
            case BLE_GAP_AD_TYPE_SIMPLE_PAIRING_RANDOMIZER_R256:
                retStr = "[Simple Pairing Randomizer R-256.]";
                break;
            case BLE_GAP_AD_TYPE_SERVICE_DATA_32BIT_UUID:
                retStr = "[Service Data - 32-bit UUID]";
                break;
            case BLE_GAP_AD_TYPE_SERVICE_DATA_128BIT_UUID:
                retStr = "[Service Data - 128-bit UUID]";
                break;
            case BLE_GAP_AD_TYPE_3D_INFORMATION_DATA:
                retStr = "[3D Information Data.]";
                break;
            default:
                break;

        }
        return retStr;
    }

    public static final int BLE_GAP_AD_TYPE_FLAGS = 0x01;//< Flags for discoverAbility.
    public static final int BLE_GAP_AD_TYPE_16BIT_SERVICE_UUID_MORE_AVAILABLE = 0x02;//< Partial list of 16 bit service UUIDs.
    public static final int BLE_GAP_AD_TYPE_16BIT_SERVICE_UUID_COMPLETE = 0x03;//< Complete list of 16 bit service UUIDs.
    public static final int BLE_GAP_AD_TYPE_32BIT_SERVICE_UUID_MORE_AVAILABLE = 0x04;//< Partial list of 32 bit service UUIDs.
    public static final int BLE_GAP_AD_TYPE_32BIT_SERVICE_UUID_COMPLETE = 0x05;//< Complete list of 32 bit service UUIDs.
    public static final int BLE_GAP_AD_TYPE_128BIT_SERVICE_UUID_MORE_AVAILABLE = 0x06;//< Partial list of 128 bit service UUIDs.
    public static final int BLE_GAP_AD_TYPE_128BIT_SERVICE_UUID_COMPLETE = 0x07;//< Complete list of 128 bit service UUIDs.
    public static final int BLE_GAP_AD_TYPE_SHORT_LOCAL_NAME = 0x08;//< Short local device name.
    public static final int BLE_GAP_AD_TYPE_COMPLETE_LOCAL_NAME = 0x09;//< Complete local device name.
    public static final int BLE_GAP_AD_TYPE_TX_POWER_LEVEL = 0x0A;//< Transmit power level.
    public static final int BLE_GAP_AD_TYPE_CLASS_OF_DEVICE = 0x0D;//< Class of device.
    public static final int BLE_GAP_AD_TYPE_SIMPLE_PAIRING_HASH_C = 0x0E;//< Simple Pairing Hash C.
    public static final int BLE_GAP_AD_TYPE_SIMPLE_PAIRING_RANDOMIZER_R = 0x0F;//< Simple Pairing Randomizer R.
    public static final int BLE_GAP_AD_TYPE_SECURITY_MANAGER_TK_VALUE = 0x10;//< Security Manager TK Value.
    public static final int BLE_GAP_AD_TYPE_SECURITY_MANAGER_OOB_FLAGS = 0x11;//< Security Manager Out Of Band Flags.
    public static final int BLE_GAP_AD_TYPE_SLAVE_CONNECTION_INTERVAL_RANGE = 0x12;//< Slave Connection Interval Range.
    public static final int BLE_GAP_AD_TYPE_SOLICITED_SERVICE_UUIDS_16BIT = 0x14;//< List of 16-bit Service Solicitation UUIDs.
    public static final int BLE_GAP_AD_TYPE_SOLICITED_SERVICE_UUIDS_128BIT = 0x15;//< List of 128-bit Service Solicitation UUIDs.
    public static final int BLE_GAP_AD_TYPE_SERVICE_DATA = 0x16;//< Service Data - 16-bit UUID.
    public static final int BLE_GAP_AD_TYPE_PUBLIC_TARGET_ADDRESS = 0x17;//< Public Target Address.
    public static final int BLE_GAP_AD_TYPE_RANDOM_TARGET_ADDRESS = 0x18;//< Random Target Address.
    public static final int BLE_GAP_AD_TYPE_APPEARANCE = 0x19;//< Appearance.
    public static final int BLE_GAP_AD_TYPE_ADVERTISING_INTERVAL = 0x1A;//< Advertising Interval.
    public static final int BLE_GAP_AD_TYPE_LE_BLUETOOTH_DEVICE_ADDRESS = 0x1B;//< LE Bluetooth Device Address.
    public static final int BLE_GAP_AD_TYPE_LE_ROLE = 0x1C;//< LE Role.
    public static final int BLE_GAP_AD_TYPE_SIMPLE_PAIRING_HASH_C256 = 0x1D;//< Simple Pairing Hash C-256.
    public static final int BLE_GAP_AD_TYPE_SIMPLE_PAIRING_RANDOMIZER_R256 = 0x1E;//< Simple Pairing Randomizer R-256.
    public static final int BLE_GAP_AD_TYPE_SERVICE_DATA_32BIT_UUID = 0x20;//< Service Data - 32-bit UUID.
    public static final int BLE_GAP_AD_TYPE_SERVICE_DATA_128BIT_UUID = 0x21;//< Service Data - 128-bit UUID.
    public static final int BLE_GAP_AD_TYPE_3D_INFORMATION_DATA = 0x3D;//< 3D Information Data.
    public static final int BLE_GAP_AD_TYPE_MANUFACTURER_SPECIFIC_DATA = 0xFF;//< Manufacturer Specific Data.
}
