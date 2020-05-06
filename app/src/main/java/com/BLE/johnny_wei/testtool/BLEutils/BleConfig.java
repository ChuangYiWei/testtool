package com.BLE.johnny_wei.testtool.BLEutils;

public class BleConfig {
    private static BleConfig instance;
    private int DEFAULT_SCAN_TIME = 5000;
    private int DEFAULT_RETRY_COUNT = 3;

    private BleConfig() {
    }

    public static BleConfig getInstance() {
        if (instance == null) {
            synchronized (BleConfig.class) {
                if (instance == null) {
                    instance = new BleConfig();
                }
            }
        }
        return instance;
    }
}
