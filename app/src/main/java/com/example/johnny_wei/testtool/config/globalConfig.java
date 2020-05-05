package com.example.johnny_wei.testtool.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class globalConfig {

    //BLE
    private int ENABLE_BT = 1;

    public static final int BLE5_API_LEVEL = 26;

    //device
    public static final int DUT = 0;
    public static final int MB = 1;

    //request code
    public static final int REQ_CODE_BLE_DEV_ACT = 2;
    public static final int REQ_CODE_TEST_ITEM_ACT = 3;

    public static final int REQUEST_CODE_LOCATION_SETTINGS = 2;

    //drop down menu
    public static final int SPI_MODE = 0;
    public static final int UART_MODE = 1;
    static final int AIR_UART_MODE = 2;
    static final int AIR_HCI_MODE = 3;

    //TODO:write log enable
    boolean writePermissionEnable = false;

    static final String SPI_XML_TAG = "UART";
    static final String UART_XML_TAG = "SPI";
    static final String AIR_UART_XML_TAG = "AIR_UART_CMD";
    static final String AIR_HCI_XML_TAG = "AIR_HCI_CMD";

    static String[] testModeArray = {"SPI mode","UART mode", "AIR UART CMD mode", "AIR HCI CMD mode"};

    //GATT action
    public static String ACTION_GATT_CONNECTED = "ACTION_GATT_CONNECTED";
    public static String ACTION_GATT_DISCONNECTED = "ACTION_GATT_DISCONNECTED";
    public static String ACTION_GATT_SERVICES_DISCOVERED = "ACTION_GATT_SERVICES_DISCOVERED";

    //GATT error code
    public static  int GATT_CONN_TERMINATE_LOCAL_HOST = 0x16;
    public static  int GATT_CONN_TIMEOUT = 0x08;
    public static  int GATT_INTERNAL_ERROR = 0x81;//129

    //broad cast
    static final String EXTRAS_ADDR = "EXTRAS_ADDR";
    public final static String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public final static String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    //UUID
    final static public String UUID_SERVICE = "0000fff0-0000-1000-8000-00805f9b34fb";
    final static public String UUID_NOTIFY_CHARA = "0000fff1-0000-1000-8000-00805f9b34fb";
    final static public String UUID_WRITE_DESCRIPTOR = "00002902-0000-1000-8000-00805f9b34fb";
    final static public String UUID_WRITE_CHARA = "0000fff2-0000-1000-8000-00805f9b34fb";

    final static public String UUID_BATTERY_SERVICE = "0000180f-0000-1000-8000-00805f9b34fb";
    final static public String UUID_BATTERY_LEVEL_CHARA = "00002a19-0000-1000-8000-00805f9b34fb";

    //ble location define
    public final static int PERMISSION_REQUEST_COARSE_LOCATION = 101;
    public final static int PERMISSION_REQUEST_BACKGROUND_LOCATION = 110;
    public final static int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 111;

    //write permission define
    public final static int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 102;

    //test case
    public static LinkedHashMap<String, String> map_testcase = new LinkedHashMap<String, String>() {{
        put("2.3.1", "2.3.1.csv");
        put("2.3.2", "2.3.2.csv");
        put("2.3.3", "2.3.3.csv");
        put("2.3.4", "2.3.4.csv");
        put("2.3.9 Loopack", "loopback.csv");
    }};

    public final static String EXTRAS_TEST_CASE = "TEST_CASE";
}
