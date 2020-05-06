package com.BLE.johnny_wei.testtool.utils;

import android.util.Log;

public class HCI_Util {

    private static final String TAG = "HCI_Util";

    public final static int READ_REMOTE_VERSION_INFORMATION_COMPLETE_EVENT = 0x0C;
    public final static int COMMAND_COMPLETE_EVENT = 0x0E;
    public final static int COMMAND_STATUS_EVENT = 0x0F;

    static public boolean HCI_check_evt_success(String hci_evt_str) {

        byte[] hci_evt = strUtil.string2Bytes(hci_evt_str);
        int pos = 0;
        int total_len = hci_evt.length;
        if (total_len < 4) {
            return false;
        }
        byte status = (byte) 0xFF;
        while (pos < total_len) {
            int len = hci_evt[pos + 2];
            Log.d(TAG, "len is : " + len);
            int evt = hci_evt[pos + 1];

            switch (evt) {
                case COMMAND_COMPLETE_EVENT://cmd complete
                    status = hci_evt[pos + 6];
                    break;
                case COMMAND_STATUS_EVENT://cmd status
                case READ_REMOTE_VERSION_INFORMATION_COMPLETE_EVENT://Read Remote Version Information Complete
                    status = hci_evt[pos + 3];
                    break;
                default:
                    Log.e(TAG, "can't find event");
                    break;
            }
            Log.d(TAG, "evt is: " + evt + ",status is:" + status);
            if (status != 0x00) {
                return false;
            }
            pos = pos + (len + 3);
            Log.d(TAG, "pos : " + pos);
        }
        return true;
    }
}
