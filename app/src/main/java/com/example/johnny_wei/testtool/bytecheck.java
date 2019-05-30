package com.example.johnny_wei.testtool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.johnny_wei.testtool.utils.DevUtil;
import com.example.johnny_wei.testtool.utils.strUtil;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class bytecheck extends AppCompatActivity {
    private final byte[] bytearray={0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0A,0x0B,0x0C,0x0D,0x0E,0x0F};

    private final String TAG = getClass().getSimpleName();
    LinkedList<String> testStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bytecheck);

        String service = "0000180f-0000-1000-8000-00805f9b34fb";
        int startIdx = 4;
        int EndIdx = 7+1;
        Log.d(TAG,service.substring(startIdx,EndIdx));

        String [] strings = {"11","22"};
        Log.d(TAG,strings[0]);
        Log.d(TAG,strings[1]);

        GetsubByteStr("040F0400011D04040C08000000081D00D307",3,2);
        GetsubByteStr("040F0400011D04040C08000000081D00D307",0,1);
        GetsubByteStr("040E07012D0C000E0006",0,7);
        SetsubByteStr();

//        HCI_check_evt_success("040E0C01032000254D000000000000");
//        HCI_check_evt_success("040F0400011D04040C08000000081D00D307");
//        HCI_check_evt_success("040E07012D0C000E0006");
        read_remote_version();
//        substr();
//        splitData();
//        comparephyaddr();
//        checkLength();
//        checkUARTLength();

    }

    void read_remote_version()
    {
        String str = "040C08000000081D00D307";
        String ver =  GetsubByteStr(str,6,1);
        String Manufacturer_Name =  GetsubByteStr(str,7,2);
        String sub_ver =  GetsubByteStr(str,9,2);

        Log.d(TAG,"ver:" + ver);
    }

    public final static int READ_REMOTE_VERSION_INFORMATION_COMPLETE_EVENT = 0x0C;
    public final static int COMMAND_COMPLETE_EVENT = 0x0E;
    public final static int COMMAND_STATUS_EVENT = 0x0F;
    boolean HCI_check_evt_success(String hci_evt_str) {

        byte[] hci_evt = strUtil.string2Bytes(hci_evt_str);
        int pos = 0;
        int total = hci_evt.length;
        byte status = (byte) 0xFF;
        while (pos < total) {
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

    private String SetsubByteStr()
    {
//        String orgstr = "013020020E00";
        String orgstr = "01302002XXXX";
        String newStr = orgstr.replace("XXXX","0900");
        Log.d(TAG,"after replace:" + newStr);
        return newStr;
    }

    private String GetsubByteStr(String byteStr,int byte_start_pos,int read_size) {
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

    private void substr() {
        String test = "UUID:0x2A19\n value:63";
        Log.d("jjj","test:" + test);
        Log.d("jjj","fist appear 2a19=" + test.indexOf("2A19"));
        Log.d("jjj","fist appear value=" + test.indexOf("value:"));
        Log.d("jjj","fist appear value=" + test.lastIndexOf("value:"));
        Log.d("jjj","substr=" + test.substring(0,test.indexOf("val")));
    }


    void test_str()
    {
        final byte[] bytearray={0x00,0x01,0x02};
        Log.d(TAG,strUtil.bytes2String(bytearray));
        string2Bytes("123");
        Log.d(TAG, "brand:" + DevUtil.GetBrand());
        Log.d(TAG, "dev model:" + DevUtil.GetDeviceModel());
        Log.d(TAG, "sys ver:" + DevUtil.GetSystemVersion());
        Log.d(TAG, "SDK ver:" + DevUtil.GetVersionSDK());
        DevUtil.printDeviceInfo();
        return;
    }

    final int DATASIZE = 20;
    void splitData()
    {
        LinkedList<byte[]> byte_list = new LinkedList<>();

         final byte[] bytearray={0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0A,0x0B,0x0C,0x0D,0x0E,0x0F,
                 0x10,0x11,0x12,0x13,0x14,0x15,0x16,0x17,0x18,0x19,0x1A,0x1B,0x1C,0x1D,0x1E,0x1F,
                 0x20,0x21,0x22,0x23,0x24,0x25,0x26,0x27,0x28,0x29,0x2A,0x2B,0x2C,0x2D,0x2E,0x2F};
        int split_num = bytearray.length/DATASIZE;
        Log.d(TAG, "split_num:" + split_num);
        int num = 0;
        int idx = 0;

        for(num=0;num<split_num;num++)
        {
            byte[] currentData = new byte[DATASIZE];
            System.arraycopy(bytearray, idx, currentData, 0, DATASIZE);
            byte_list.addLast(currentData);
            Log.d(TAG, "currentData:" + bytes2String(currentData));
            idx+=DATASIZE;
        }
        //last one

        int last_one_size = bytearray.length%DATASIZE;
        byte[] last_one = new byte[last_one_size];
        System.arraycopy(bytearray, idx, last_one, 0, last_one_size);
        byte_list.addLast(last_one);
        Log.d(TAG, "last_one:" + bytes2String(last_one));
        while(byte_list.size() > 0)
        {
            Log.d(TAG, "byte_list:" + bytes2String(byte_list.poll()));
        }
    }


    private boolean checkUARTLength() {
        byte[] rev = {0x21,0x36,0x03,0x01,0x02,0x03};
        byte[] fakerev = {0x36,0x03,0x01,0x02,0x03};
        String revStr = bytes2String(rev);
        String fakerevStr = bytes2String(fakerev);
        Log.d(TAG, "revStr:" + revStr);
        Log.d(TAG, "fakerevStr:" + fakerevStr);
        if(rev[0] == 0x21 || rev[0] == 0x26) {
            return true;
        }
        else {
            return false;
        }

    }

    private void checkLength(){
        byte[] rev = {0x69,0x21,0x33,0x06,0x13,0x15,0x10,0x18,0x20,0x11};
        byte[] revfake = {0x21,0x33,0x06,0x13,0x15,0x10,0x18,0x20,0x11};

        Log.d(TAG, "byte length:" + rev.length);
        String revStr = bytes2String(rev);
        Log.d(TAG, "revStr:" + revStr);

        int length = (rev[0] & 0xF | ((rev[0] & 0x10))) + 1;//add header itself
        if ((rev[0] & 0x1F) == 0) {
            Log.d(TAG, "length: 32 byte");
        }
        if(length != rev.length)
        {
            Log.e(TAG, "length" + Integer.toString(length) + "is not equal to" + Integer.toString(rev.length));
        }
        else{
            Log.d(TAG, "length" + Integer.toString(length) + " is equal to " + Integer.toString(rev.length));
        }

        Log.d(TAG, "byte length:" + rev.length);
        String revStrfake = bytes2String(revfake);
        Log.d(TAG, "revStr:" + revStr);

        length = (revfake[0] & 0xF | ((revfake[0] & 0x10))) + 1;//add header itself
        if ((revfake[0] & 0x1F) == 0) {
            Log.d(TAG, "length: 32 byte");
        }
        if(length != revfake.length)
        {
            Log.e(TAG, "length" + Integer.toString(length) + " is not equal to" + Integer.toString(revfake.length));
        }
        else{
            Log.d(TAG, "length" + Integer.toString(length) + " is equal to " + Integer.toString(revfake.length));
        }


    }

    private void comparephyaddr() {
        String revStr = "6C57010100940420000400B0A0";
        String mstr = "6C57010000940420000400B0A0";
        if(revStr.startsWith("6C57")) {
            String revStrsub1 = revStr.substring(0,6);//0x6C5701
            String revStrsub2 = revStr.substring(10, revStr.length());//skip two byte reserved length
            String strsub1 = mstr.substring(0, 6);
            String strsub2 = mstr.substring(10, mstr.length());
            Log.d(TAG, "revStrsub1 :" + revStrsub1);
            Log.d(TAG, "revStrsub2 :" + revStrsub2);
            Log.d(TAG, "strsub1 :" + strsub1);
            Log.d(TAG, "strsub2 :" + strsub2);
            if ((revStrsub1.equals(strsub1)) && (revStrsub2.equals(strsub2))) {
                Log.d(TAG, "jjjj :" );
            }
            String strsubaddr = mstr.substring(10, 18);
            String strsubdata = mstr.substring(18, mstr.length());


            Log.d(TAG, "strsubaddr :" + strsubaddr);
            Log.d(TAG, "strsubdata :" + strsubdata);
            Log.d(TAG, "string2Bytes:" + bytes2String(string2Bytes(strsubdata)));

            Log.d(TAG, "strsubdata length:" + strsubdata.length());

            byte[] bytes = string2Bytes(strsubdata);
            Log.d(TAG, "bytes:" + bytes2String(bytes));

            for(byte b:bytes){
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("0x%2x", b));
                Log.d(TAG, sb.toString());
//                if((b & 0x4) == 0x4)
//                {
//                    Log.d(TAG, "0x4");
//                    StringBuilder sb = new StringBuilder();
//                    sb.append(String.format("0x%2x", b));
//                    Log.d(TAG, sb.toString());
//                }
            }

            if((bytes[0] & (byte)0x04) == 0x04)
            {
                Log.d(TAG, "ccc");
            }
        }
        else{
            Log.d(TAG, "not start with 6c57");
        }
    }

    private String strEndiaOrderChane(String strsubdata) {
        String retStr = "";
        if(strsubdata.equals("")) {
            return retStr;
        }
        StringBuilder sb = new StringBuilder(strsubdata.length());
        int i;
        for(i=0;i<strsubdata.length()-1;i+=2) {
            sb.append(strsubdata.substring((strsubdata.length() - (i + 2)), (strsubdata.length() - i)));
        }
        return sb.toString();
    }

    public String bytes2String(byte[] inputbyte){
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

    public byte[] string2Bytes(String inString){
        return string2Bytes(inString,0x00);
    }

    public static final int _C_ADD_0_BACK_        =0x0002;
    public static final int _C_LOW_BYTE_FIRST_    =0x0001;

    public byte[] string2Bytes(String inString,int fun){
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

    public byte getHalfHex(byte inputbyte){

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
