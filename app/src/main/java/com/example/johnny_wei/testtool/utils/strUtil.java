package com.example.johnny_wei.testtool.utils;

public class strUtil {


    public static final int _C_LOW_BYTE_FIRST_    =0x0001;
    public static final int _C_ADD_0_BACK_        =0x0002;
    private static final byte[] bytearray={0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0A,0x0B,0x0C,0x0D,0x0E,0x0F};

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

    public byte[] string2Bytes(String inString) {
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



}
