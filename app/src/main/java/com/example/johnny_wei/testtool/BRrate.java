package com.example.johnny_wei.testtool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BRrate extends AppCompatActivity {
    //baud rate
    private static final int mBaudratei2400 = 2400;
    private static final int mBaudratei9600 = 9600;
    private static final int mBaudratei14400 = 14400;
    private static final int mBaudratei19200 = 19200;
    private static final int mBaudratei38400 = 38400;
    private static final int mBaudratei57600 = 57600;
    private static final int mBaudratei115200 = 115200;
    private static final int mBaudratei256000 = 256000;//not support
    final int UART_MODE = 0;
    final int SPI_MODE = 1;
    LinkedList<Integer> BaudRateList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brrate);
        BaudRateList = new LinkedList<>();

        BaudRateList.add(mBaudratei115200);
        BaudRateList.add(mBaudratei2400);
        BaudRateList.add(mBaudratei9600);
        BaudRateList.add(mBaudratei14400);
        BaudRateList.add(mBaudratei19200);
        BaudRateList.add(mBaudratei38400);
        BaudRateList.add(mBaudratei57600);

        while (BaudRateList.size() > 0) {
            Log.w("jjj", GetBRwriteCmd(BaudRateList.getFirst(), SPI_MODE));
            Log.d("jjj", GetBRwriteCmd(BaudRateList.getFirst(), UART_MODE));
            BaudRateList.removeFirst();
        }
        Log.w("jjj", GetBRwriteEvt(mBaudratei115200, SPI_MODE));
        Log.d("jjj", GetBRwriteEvt(mBaudratei115200, UART_MODE));
        Log.d("jjj","===========================================");
        Log.w("jjj", GetBRreadCmd(mBaudratei115200, SPI_MODE));
        Log.w("jjj", GetBRreadCmd(mBaudratei115200, UART_MODE));
        Log.d("jjj","===========================================");
        Log.w("jjj", GetBRreadEvt(mBaudratei115200, SPI_MODE));
        Log.w("jjj", GetBRreadEvt(mBaudratei115200, UART_MODE));
        Log.d("jjj","===========================================");
        Log.w("jjj", GetBRupdateCmd(mBaudratei115200, SPI_MODE));
        Log.w("jjj", GetBRupdateCmd(mBaudratei115200, UART_MODE));
        Log.d("jjj","===========================================");
        Log.w("jjj", GetBRupdateEvt(mBaudratei115200, SPI_MODE));
        Log.w("jjj", GetBRupdateEvt(mBaudratei115200, UART_MODE));

    }


    private String GetBRwriteCmd(int BaudRate, int mode) {
        String br;
        switch (BaudRate) {
            case mBaudratei2400:
                br = "00000000";
                break;
            case mBaudratei9600:
                br = "01000000";
                break;
            case mBaudratei14400:
                br = "02000000";
                break;
            case mBaudratei19200:
                br = "03000000";
                break;
            case mBaudratei38400:
                br = "04000000";
                break;
            case mBaudratei57600:
                br = "05000000";
                break;
            case mBaudratei115200:
                br = "06000000";
                break;
            case mBaudratei256000:
                br = "07000000";
                break;
            default:
                br = "06000000";
                break;
        }
        final String A7 = "A7";
        String cmd = "253204";
        switch (mode) {
            case SPI_MODE:
                cmd = A7 + cmd + br;
                break;
            case UART_MODE:
                cmd = cmd + br;
                break;
            default:
                cmd = cmd + br;
                break;

        }
        return cmd;
    }

    private String GetBRwriteEvt(int BaudRate, int mode) {
        final String hdr = "63";
        String cmd = "263200";
        switch (mode) {
            case SPI_MODE:
                cmd = hdr + cmd;
                break;
            case UART_MODE:
                break;
            default:
                break;
        }
        return cmd;
    }

    private String GetBRreadCmd(int BaudRate, int mode) {
        final String hdr = "A2";
        String cmd = "2032";
        switch (mode) {
            case SPI_MODE:
                cmd = hdr + cmd;
                break;
            case UART_MODE:
                break;
            default:
                break;
        }
        return cmd;
    }

    private String GetBRreadEvt(int BaudRate, int mode) {
        String br;
        switch (BaudRate) {
            case mBaudratei2400:
                br = "00000000";
                break;
            case mBaudratei9600:
                br = "01000000";
                break;
            case mBaudratei14400:
                br = "02000000";
                break;
            case mBaudratei19200:
                br = "03000000";
                break;
            case mBaudratei38400:
                br = "04000000";
                break;
            case mBaudratei57600:
                br = "05000000";
                break;
            case mBaudratei115200:
                br = "06000000";
                break;
            case mBaudratei256000:
                br = "07000000";
                break;
            default:
                br = "06000000";
                break;
        }
        final String hdr = "67";
        String cmd = "213204";
        switch (mode) {
            case SPI_MODE:
                cmd = hdr + cmd + br;
                break;
            case UART_MODE:
                cmd = cmd + br;
                break;
            default:
                cmd = cmd + br;
                break;

        }
        return cmd;
    }

    private String GetBRupdateCmd(int BaudRate, int mode) {
        final String hdr = "A3";
        String cmd = "253F00";
        switch (mode) {
            case SPI_MODE:
                cmd = hdr + cmd;
                break;
            case UART_MODE:
                break;
            default:
                break;
        }
        return cmd;
    }

    private String GetBRupdateEvt(int BaudRate, int mode) {
        final String hdr = "63";
        String cmd = "263F00";
        switch (mode) {
            case SPI_MODE:
                cmd = hdr + cmd;
                break;
            case UART_MODE:
                break;
            default:
                break;
        }
        return cmd;
    }
}
