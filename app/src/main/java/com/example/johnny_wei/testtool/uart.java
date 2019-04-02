package com.example.johnny_wei.testtool;

import android.content.Context;
import android.hardware.usb.UsbManager;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.johnny_wei.testtool._00_util.commonutil;
import com.example.johnny_wei.testtool.driver_pl2303.PL2303Driver;
import com.example.johnny_wei.testtool.utils.strUtil;

import static com.example.johnny_wei.testtool.common.pUart._ST_CHIP_NOT_SUPPORT_;
import static com.example.johnny_wei.testtool.common.pUart._ST_NOT_CONNECTED_;
import static com.example.johnny_wei.testtool.common.pUart._ST_NOT_ENMERATE_;
import static com.example.johnny_wei.testtool.common.pUart._ST_OPEN_FAIL_;
import static com.example.johnny_wei.testtool.common.pUart._ST_SUCCESS_;
import static com.example.johnny_wei.testtool.common.pUart._ST_UST_HOST_NOT_SUPPORT_;
import static com.example.johnny_wei.testtool.common.pUart._ST_WRITE_FAIL_;

import static com.example.johnny_wei.testtool.utils.strUtil.bytes2String;


public class uart extends AppCompatActivity {
    //common
    String TAG = getClass().getSimpleName();
    EditText ed_uart;
    //uart setting
    PL2303Driver mSerial;

    TextView tv_rx;
    private PL2303Driver.BaudRate       mBaudrate       = PL2303Driver.BaudRate.B115200;
    private PL2303Driver.DataBits       mDataBits       = PL2303Driver.DataBits.D8;
    private PL2303Driver.StopBits       mStopBits       = PL2303Driver.StopBits.S1;
    private PL2303Driver.Parity         mParity         = PL2303Driver.Parity.NONE;
    private PL2303Driver.FlowControl    mFlowControl    = PL2303Driver.FlowControl.OFF;
    private static final int                       mBaudratei = 115200;
    private static final int           mBaudratei115200 = 115200;
    private static final String ACTION_USB_PERMISSION = "com.prolific.pl2303hxdsimpletest.USB_PERMISSION";
    private final byte[] bytearray={0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0A,0x0B,0x0C,0x0D,0x0E,0x0F};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uart);

        init();
        setupView();
    }

    private void init() {

        initPL2303();
    }

    private void setupView() {
        ed_uart = findViewById(R.id.ed_uart);
        tv_rx = findViewById(R.id.tv_rx);
    }
    @Override
    protected void onResume() {
        setBaudRate(mBaudratei115200);
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 500);
    }

    @Override
    protected void onDestroy() {
        if (mSerial != null) {
            mSerial.end();
            mSerial = null;
        }
        super.onDestroy();
    }

    private int initPL2303() {
        mSerial = new PL2303Driver((UsbManager) this.getSystemService(Context.USB_SERVICE),
                this, ACTION_USB_PERMISSION);
        if (mSerial == null) {
            commonutil.wdbgLogcat(TAG, 2, "open pl2303 fail");
            return _ST_UST_HOST_NOT_SUPPORT_;
        }
        if (!mSerial.PL2303USBFeatureSupported()) {
            commonutil.wdbgLogcat(TAG, 2, "usb feature not support");
            return _ST_UST_HOST_NOT_SUPPORT_;
        }
        //connect need wait for a while,>=2
        int i;
        for (i = 0; i < 10; i++) {
            if (mSerial.isConnected()) {
                break;
            } else {
                if (!mSerial.enumerate()) {
                    commonutil.wdbgLogcat(TAG, 2, "\"no more devices found\"");
                    return _ST_NOT_ENMERATE_;
                } else {
                    Log.d(TAG, "onResume:enumerate succeeded!");
                }
            }
        }

        return 0;
    }

    public int setBaudRate(int baudRate) {
        Log.d(TAG, "init port setting");
        commonutil.wdbgLogcat(TAG,0,"[setBaudRate]" + Integer.toString(baudRate));
        int result = -1;
        if (mSerial.isConnected()) {
            result = mSerial.InitByPortSetting(baudRate, mDataBits, mStopBits, mParity, mFlowControl);
            if (result == 0) {
                if (!mSerial.PL2303Device_IsHasPermission()) {
                    commonutil.wdbgLogcat(TAG,2,"[setBaudRate]have no permission");
                    return _ST_OPEN_FAIL_;
                }
                if (mSerial.PL2303Device_IsHasPermission() && (!mSerial.PL2303Device_IsSupportChip())) {
                    commonutil.wdbgLogcat(TAG,2,"[setBaudRate]not support chip");
                    return _ST_CHIP_NOT_SUPPORT_;
                }
                return _ST_SUCCESS_;
            } else {
                commonutil.wdbgLogcat(TAG,2,"[setBaudRate]InitByPortSetting fail");
                return _ST_OPEN_FAIL_;
            }
        } else {
            commonutil.wdbgLogcat(TAG,2,"[setBaudRate]serial not connected");
            return _ST_NOT_CONNECTED_;
        }

    }

    public int uart_tx_command(String command)
    {
        byte[] txByte = strUtil.string2Bytes(command);
        if (null == mSerial) {
            return _ST_UST_HOST_NOT_SUPPORT_;
        }

        if (!mSerial.isConnected()) {
            return _ST_NOT_CONNECTED_;
        }

        int res = mSerial.write(txByte, txByte.length);
        if (res < 0) {
            return _ST_WRITE_FAIL_;
        }

        return _ST_SUCCESS_;
    }

    public byte[] uart_rx() {
        byte[] rByte = new byte[0];

        if (null == mSerial)
            return rByte;
        if (!mSerial.isConnected())
            return rByte;

        int len;
        byte[] rByte1;
        rByte1 = new byte[1024];//max=4096

        len = mSerial.read(rByte1);
        if (len > 0) {
            rByte = new byte[len];
            for (int i = 0; i < len; i++) {
                rByte[i] = rByte1[i];
            }
            tv_rx.setText( strUtil.bytes2String(rByte));
            Log.d(TAG, "rx:" + "len :0x" + strUtil.int2String(rByte.length, 0) + " byte : " + bytes2String(rByte));
        }

        return rByte;
    }

    public void clk_tx(View view) {
        int result = -1;
        ed_uart = (EditText) findViewById(R.id.ed_uart);
        String txString = ed_uart.getText().toString();

        result = uart_tx_command(txString);
        if(_ST_SUCCESS_ != result)
        {
            Log.d(TAG,"tx fail");
        }
    }

    public void clk_rx(View view) {
            uart_rx();
    }
}
