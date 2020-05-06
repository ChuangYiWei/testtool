package com.BLE.johnny_wei.testtool.utils;

import android.util.Log;

public class strUtil {

    private static String TAG = "strUtil";
    //get_version
    public static final int _B_SHOW_VERSION_      =0x0001;
    public static final int _C_LOW_BYTE_FIRST_    =0x0001;
    public static final int _C_ADD_0_BACK_        =0x0002;
    //bin2Hex
    public static final int _D_LOW_BIT_FIRST_     =0x0001;
    public static final int _D_NIBBLE_SWITCH_     =0x0002;
    public static final int _D_PADDING_FRONT_     =0x0004;//0001->000000001

    //int2String
    public static final int _E_DECIMAL_                =0x0001;
    public static final int _E_REMOVE_0_               =0x0002;//remove all 0 infront
    public static final int _E_NO_SIGN_                =0x0004;
    public static final int _E_HEXADECIMAL_            =0x0008;
    public static final int _E_NULL_RETURN_0_          =0x0010;
    public static final int _E_REMOVE_0_KEEP_LAST_     =0x0020;//keep last 0 infront
    public static final int _E_ADD_0_FRONT_            =0x0040;//padding 0 at first


    private static final byte[] bytearray={0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0A,0x0B,0x0C,0x0D,0x0E,0x0F};
    public static final String[] NibbleHex={"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};

    public static String bytes2String(byte[] inputbyte){
        String back = "";
        if (inputbyte != null) {
            if (inputbyte.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(inputbyte.length);
                for (byte byteChar : inputbyte) {
                    stringBuilder.append(String.format("%02X", byteChar));
                }
                back = stringBuilder.toString();
            }
        }
        return back;
    }

    public static byte[] string2Bytes(String inString,int fun){
        byte[] newbyte=new byte[0];
        byte[] tmpbyte=new byte[0];

        if(inString==null || inString=="")//return len=0 array
            return tmpbyte;

        if(inString.length()%2==1){ //odd
            if((fun & _C_ADD_0_BACK_)>0){
                inString=inString+"0";
            }
            else{
                inString="0"+inString;
            }
        }

        newbyte=new byte[(inString.length()/2)];
        tmpbyte=inString.getBytes();
        int k=0;
        for(int i=0;i<newbyte.length;i++){
            if((fun&_C_LOW_BYTE_FIRST_)==_C_LOW_BYTE_FIRST_){
                k=(newbyte.length-1)-i;
            }
            else if(i>0){
                k++;
            }
            newbyte[k]|=getHalfHex(tmpbyte[i*2]);
            newbyte[k]<<=4;
            newbyte[k]|=getHalfHex(tmpbyte[i*2+1]);
//System.out.println("0:newbyte["+i+"]:"+newbyte[i]);
        }

        return newbyte;
    }

    public static byte[] string2Bytes(String inString) {
        return string2Bytes(inString, 0x00);
    }


    public static byte getHalfHex(byte inputbyte){

        if (inputbyte >= 0x30 && inputbyte <= 0x39) {//0~9
            return (byte) (inputbyte - 0x30);
        } else if (inputbyte >= 0x41 && inputbyte <= 0x46) {//A~F

            return (byte) bytearray[inputbyte - 0x41 + 10];
        } else if (inputbyte >= 0x61 && inputbyte <= 0x66) {//a~f

            return (byte) bytearray[inputbyte - 0x61 + 10];
        } else {

            return (byte) 0x00;
        }
    }

    public static String int2String(int inI,int fun){
        int digi = 16, digix2 = 256;
        long tmpI0 = 0;
        String rString = "";
        boolean minus = false;

        {
            if (inI < 0) {
                minus = true;
                if ((fun & _E_NO_SIGN_) > 0) {
                    inI &= 0x7FFFFFFF;
                } else {
                    inI = -inI;
                }
            }
        }
        if ((fun & _E_DECIMAL_) > 0) {
            digi = 10;
            digix2 = 100;
        }

        do {
            tmpI0 = (inI % (digix2));

            if (rString.length() == 6 && (fun & _E_NO_SIGN_) > 0)//MSB
            {
                rString = NibbleHex[(int) ((tmpI0 / digi) | 0x8)] + NibbleHex[(int) (tmpI0 % digi)] + rString;
            } else {
                rString = NibbleHex[(int) (tmpI0 / digi)] + NibbleHex[(int) (tmpI0 % digi)] + rString;
            }
//Log.d("dbg","rString.1="+rString);
//dbgMsg.save("pConvertType.int2String.rString.0="+rString);
            inI /= (digix2);
//dbgMsg.save("pConvertType.int2String.inI.0="+inI);
//System.out.println("ins="+ins);
        }
        while (inI > 0);


        if ((fun & _E_ADD_0_FRONT_) > 0) {
            while ((rString.length() % 2) != 0) {
                rString = "0" + rString;
            }
        }


        if ((fun & _E_REMOVE_0_KEEP_LAST_) > 0) {
            while (rString.indexOf("0") == 0 && rString.length() > 1) {
                rString = rString.substring(1, rString.length());
            }
        } else if ((fun & _E_REMOVE_0_) > 0) {
            while (rString.indexOf("0") == 0) {
                rString = rString.substring(1, rString.length());
            }
        }

//dbgMsg.save("pConvertType.int2String.rString.1="+rString);
        if (minus) {
            if ((fun & _E_NO_SIGN_) > 0) {
                while (rString.length() < 8) {
                    if (rString.length() == 7)//MSB
                    {
                        rString = NibbleHex[8] + rString;
                    } else {
                        rString = NibbleHex[0] + rString;
                    }
                }
            } else {
                rString = "-" + rString;
            }
        }
        return rString;

    }

    public static String GetsubByteStr(String byteStr,int byte_start_pos,int read_size) {
        //ex:string = "040F0400011D04040C08000000081D00D307"
        //byte_start_pos=3, read_size=2 will get "0001"
        int startIdx = byte_start_pos*2; //byte 3
        int EndIdx = startIdx + (read_size*2);
        if (byteStr.length() < EndIdx) {
            Log.e(TAG,"invalid byte str size");
            return "";
        }
//        Log.d(TAG,"substatus:" + byteStr.substring(startIdx,EndIdx));
        return byteStr.substring(startIdx,EndIdx);
    }

}
