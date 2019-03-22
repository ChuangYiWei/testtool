/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.johnny_wei.testtool.utils;

import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class GattAttributes {
    //==================================================================

    private final String className = getClass().getSimpleName();

    final int GATT_PROP_BCAST = 0x01; //Permits broadcasts of the Characteristic Value.
    private static final int GATT_PROP_READ = 0x02;//Permits reads of the Characteristic Value.
    private static final int GATT_PROP_WRITE = 0x08;//Permits writes of the Characteristic Value with response.
    private static final int GATT_PROP_NOTIFY = 0x10;//Permits notifications of a Characteristic Value without acknowledgement.
    private static final int GATT_PROP_WRITE_NO_RSP = 0x04; // Permits writes of the Characteristic Value without response.
    private static final int GATT_PROP_INDICATE = 0x20;//Permits indications of a Characteristic Value with acknowledgement.
    private static final int GATT_PROP_AUTHEN = 0x40;// Permits signed writes to the Characteristic Value.
    private static final int GATT_PROP_EXTENDED = 0x80; //Additional characteristic properties are defined in the Characteristic Extended Properties Descriptor.
    //==================================================================

    private static HashMap<String, String> attributes = new HashMap();
    public static String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";


    static {

//        https://www.bluetooth.com/specifications/gatt/services
        // Sample Services.
        attributes.put("00001800-0000-1000-8000-00805f9b34fb", "Generic Access");


        attributes.put("00001801-0000-1000-8000-00805f9b34fb", "Generic Attribute");
        attributes.put("0000180d-0000-1000-8000-00805f9b34fb", "Heart Rate Service");
        attributes.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information Service");
        attributes.put("0000180f-0000-1000-8000-00805f9b34fb", "Battery Service");

        // Sample Characteristics.

        //0x1800 service
        attributes.put("00002a00-0000-1000-8000-00805f9b34fb", "Device Name");
        attributes.put("00002a01-0000-1000-8000-00805f9b34fb", "Appearance");

        //0x1801 Generic Attribute
        attributes.put("00002a05-0000-1000-8000-00805f9b34fb", "Service Changed");

        attributes.put("00002a19-0000-1000-8000-00805f9b34fb", "Battery Level");

        //service 0x180A
        attributes.put("00002a23-0000-1000-8000-00805f9b34fb", "System ID");
        attributes.put("00002a24-0000-1000-8000-00805f9b34fb", "Model Number String");
        attributes.put("00002a25-0000-1000-8000-00805f9b34fb", "Serial Number String");
        attributes.put("00002a26-0000-1000-8000-00805f9b34fb", "Firmware Revision String");
        attributes.put("00002a27-0000-1000-8000-00805f9b34fb", "Hardware Revision String");
        attributes.put("00002a28-0000-1000-8000-00805f9b34fb", "Software Revision String");
        attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
        attributes.put(HEART_RATE_MEASUREMENT, "Heart Rate Measurement");


        // Sample Descriptors
        attributes.put(CLIENT_CHARACTERISTIC_CONFIG, "Client Characteristic Configuration");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }

    public static String GetProperty(int property) {
        String retStr = "";
        if((property & GATT_PROP_READ) != 0)
            retStr = retStr + "Read ";

        if((property & GATT_PROP_WRITE) != 0)
            retStr = retStr + "Write ";

        if((property & GATT_PROP_NOTIFY) != 0)
            retStr = retStr + "Notify ";

        if((property & GATT_PROP_WRITE_NO_RSP) != 0)
            retStr = retStr + "No Response ";

        if((property & GATT_PROP_INDICATE) != 0)
            retStr = retStr + "Indicate ";

        if((property & GATT_PROP_AUTHEN) != 0)
            retStr = retStr + "Auth ";

        if((property & GATT_PROP_EXTENDED) != 0)
            retStr = retStr + "Extended ";

        return retStr;
    }
}
