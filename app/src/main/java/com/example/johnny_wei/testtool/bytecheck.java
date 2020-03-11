package com.example.johnny_wei.testtool;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.johnny_wei.testtool.utils.DevUtil;
import com.example.johnny_wei.testtool.utils.strUtil;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class bytecheck extends AppCompatActivity {
    private final byte[] bytearray={0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0A,0x0B,0x0C,0x0D,0x0E,0x0F};
    private byte[] raw_data =
            {0x21, (byte) 0xDF, 0x60, 0x21, (byte) 0xDF,
             0x60, 0x21, (byte) 0xE0, 0x60, 0x21,
             (byte) 0xE0, 0x00, 0x21, (byte) 0xE2, 0x00,
             0x21, (byte) 0xE8, (byte) 0x80,  0x21, (byte) 0xEF
            };
    private byte[] raw_data1 =
            {(byte)0xF1, (byte) 0x21, (byte)0x21, (byte)0x21, (byte) 0xDF,
                    (byte)0x60,(byte) 0x21, (byte) 0xE0, (byte)0x60, (byte)0x21,
                    (byte) 0xE0, (byte)0x00, (byte)0x21, (byte) 0xE2, (byte)0x00,
                    (byte)0x21, (byte) (byte)0xE8, (byte) 0x80,  (byte)0x21, (byte) 0xEF
            };


    private byte[] test_array={(byte)0x80, 0x21, (byte) 0xE8,(byte)0x80};
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
        convertECG();
//        HCI_check_evt_success("040E0C01032000254D000000000000");
//        HCI_check_evt_success("040F0400011D04040C08000000081D00D307");
//        HCI_check_evt_success("040E07012D0C000E0006");
        //read_remote_version();
//        substr();
//        splitData();
//        comparephyaddr();
//        checkLength();
//        checkUARTLength();
/*
        String Keycode = "Abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()";
        List<String> keycode_list = new ArrayList<>();
        for(int i =0;i<Keycode.length();i++){
            keycode_list.add(Keycode);
        }
        String revKeycode = "Abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()";
        compareKeyCode(revKeycode,keycode_list);
*/
//        List<String> mouse_list = new ArrayList<>();
//        readline("HID_CMD/mouse_cmd.txt",mouse_list);

        //parse_change_intv_example

//        Get_HCI_Thrput_cmd(0,100,1000);
//        parse_hci_thrput_cmd("0162FC0300140A",0);
//        parse_hci_thrput_cmd("0162FC0300140A",1);

    }


    void parse_change_intv_example()
    {
        Log.d(TAG, "Get_HCI_change_interval_cmd:" + Get_HCI_change_interval_cmd(6, 7));
        Log.d(TAG, "Get_HCI_change_interval_cmd:" + Get_HCI_change_interval_cmd(17, 18));
        Log.d(TAG, "parsing_hci_get_interval_evt:" + parsing_hci_get_interval_evt("040E070164FC00200000", 0));
    }
    //type : 0 get packet length
    //type : 1 get data_size * 1000
    int parse_hci_thrput_cmd(String cmd, int type)
    {
        //ex:0162FC03001402
        int pos_len_start;
        int pos_len_end;
        int ret = 0;
        if(type ==0) {
            pos_len_start = 10;
            pos_len_end = 12;
            String str_pkt_len = cmd.substring(pos_len_start, pos_len_end);
            Log.d(TAG, "str_pkt_len:" + str_pkt_len);
            int pkt_len = Integer.parseInt(str_pkt_len, 16);
            Log.d(TAG, "pkt_len:" + pkt_len);
            ret = pkt_len;
        }
        else if(type==1) {
            pos_len_start = 12;
            pos_len_end = 14;
            String str_data_size = cmd.substring(pos_len_start, pos_len_end);
            Log.d(TAG, "str_data_size:" + str_data_size);
            int data_size = Integer.parseInt(str_data_size, 16);
            data_size = data_size * 1000;
            Log.d(TAG, "data_size:" + data_size);
            ret = data_size;
        }

        return ret;
    }

    //ex:0164FC0410002000
    String Get_HCI_change_interval_cmd(int min,int max)
    {
        if (min > max) {
            Log.w(TAG, "min can't be bigger than max");
        }
        String back = "";
        back = "0164FC04" +
                String.format("%02x",min).toUpperCase() + "00" +
                String.format("%02x",max).toUpperCase() + "00";
        Log.d(TAG, "min/max x 1.25ms:" + min*1.25 + "/" + max*1.25);
        return back;
    }
    /**
     * retrun connection interval(already *1.25) and Le connection update result
     * @param type
     * 0: interval,
     * 1: LE_result,
     */
    //ex:040E07xx64FC00 200000
    int parsing_hci_get_interval_evt(String evt,int type)
    {
        // 0:interval
        // 1:LE result
        int pos_intv_start = 14;
        int pos_intv_end = 16;
        String str_interval = evt.substring(pos_intv_start, pos_intv_end);
        int interval = (int) ((float) Integer.parseInt(str_interval) * 1.25);
        Log.d(TAG, "str_interval:" + str_interval);
        Log.d(TAG, "connection interval:" + interval);

        int pos_result_start = 18;
        int pos_result_end = 20;
        String str_le_result = evt.substring(pos_result_start, pos_result_end);

        int le_result = Integer.parseInt(str_le_result);
        Log.d(TAG, "LE result:" + str_le_result);
        Log.d(TAG, "le_result:" + le_result);
        return 0;
    }

    String Get_HCI_Thrput_cmd(int type, int pkt_length, int data_size) {
        String back = "";
        int covert2Uint1k = data_size/1000;
        //StringBuilder stringBuilder = new StringBuilder(HCI_BLE_THROUGHPUT_RX.length());
        if (type == 0) {
            back = "0162FC0300" +
                    String.format("%02x",pkt_length).toUpperCase()+
                    String.format("%02x",covert2Uint1k).toUpperCase();

            Log.d(TAG,"Get_HCI_Thrput_cmd is:" + back);
            return back;

        } else if (type == 1) {
            back = "0162FC0301" +
                    String.format("%02x",pkt_length).toUpperCase()+
                    String.format("%02x",covert2Uint1k).toUpperCase();
            Log.d(TAG,"Get_HCI_Thrput_cmd is:" + back);
            return back;
        }

        Log.d(TAG,"back is:" + back);
        return back;
    }
    static final String PATH_SD = Environment.getExternalStorageDirectory().getAbsolutePath();

    String axis;
    String op;
    String distance;
    //filename is folder+file under sd card root dir
    void readline(String filename,List<String> str_list)
    {
        StringBuilder text = new StringBuilder();
        try {
            File file = new File(PATH_SD,filename);

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                str_list.add(line);
                text.append(line);
                text.append('\n');
            }
            br.close() ;
        }catch (IOException e) {
            e.printStackTrace();
        }
        //save string in list
        for(int i=0;i<str_list.size();i++)
        {
            Log.d(TAG, "string_list " + i + " :" + str_list.get(i));
            String[] split_array = str_list.get(i).split(",");
            Log.d(TAG, "axis size :" + split_array.length);
            Log.d(TAG, "cmd :" + split_array[0]);
            if (split_array.length == 4) {
                axis     = split_array[1]; //x or y
                op       = split_array[2]; // + or -
                distance = split_array[3];
                Log.d(TAG, axis + op + distance);

                float expected_X= (float)0.0;
                float curX= (float)0.0;
                Log.d(TAG, "distance :" +  Float.parseFloat(distance));

            }

        }
    }

    boolean compareKeyCode(String revKeycode,List<String> keycode_list)
    {
        if(revKeycode.length() == 0 ||
                keycode_list.size() == 0)
        {
            Log.d(TAG,"size can't not be zero");
        }

        String keyCode = keycode_list.get(0);
        Log.d(TAG,"keyCode:" + keyCode);
        Log.d(TAG,"revKeycode:" + revKeycode);
        if(revKeycode.length() != keyCode.length())
        {
            Log.d(TAG,"size not match");
            Log.d(TAG,"revKeycode:" + revKeycode);
            Log.d(TAG,"real keycode:" + keyCode);
            return false;
        }

        for(int i =0;i<keyCode.length();i++){
            if(revKeycode.charAt(i) != keyCode.charAt(i))
            {
                Log.d(TAG,"revKeycode:" + revKeycode.charAt(i) + " is not equal to " + "keyCode:" + keyCode.charAt(i));
                return false;
            }
        }

        //show wrong key code
        return true;
    }

    void convertECG()
    {
        for(int i=0;i<18;i+=3)
        {
            int converted_data =0;
            converted_data = (0xFF & 0) << 24 |
                    (0xFF & raw_data1[i]) << 16 |
                    (0xFF & raw_data1[i + 1]) << 8 |
                    (0xFF & raw_data1[i + 2]);

//            Log.d(TAG,"raw_data[i]:" + String.format("0x%8x",raw_data1[i]<< 16));
//            Log.d(TAG,"raw_data[i+1]:" + String.format("0x%8x",raw_data1[i+1]<< 8));
//            Log.d(TAG,"raw_data[i+2]:" + String.format("0x%8x",raw_data1[i+2]));
//            Log.d(TAG,"converted_data:" + String.format("0x%8x",converted_data));
//            Log.d(TAG,"converted_data2:" + String.format("0x%8x",byteArrayToInt2(raw_data1,i)));
//            Log.d(TAG,"byteArrayToInt:" + String.format("0x%8x",byteArrayToInt(test_array,0)));
        }

        Long long_converted_data=0L;
        for (int i = 0; i < 16; i += 4) {

            Log.d(TAG,"convert_3byte_Long:" +String.format("0x%8x",convert_3byte_to_Long(raw_data1,i)));
            Log.d(TAG,"convert_3byte_Long string:" +Long.toString(convert_3byte_to_Long(raw_data1,i)));
            Log.d(TAG,"convert_4byte_Long:" + String.format("0x%8x",convert_4byte_to_Long(raw_data1,i)));
            Log.d(TAG,"convert_4byte_Long string:" + Long.toString(convert_4byte_to_Long(raw_data1,i)));
        }

        for(int i=0;i<18;i+=3)
        {
            Log.d(TAG,"convert_3byte_int:" + String.format("0x%8x",convert_3byte_to_int(raw_data1,i)));
            Log.d(TAG,"int:" + convert_3byte_to_int(raw_data1,i));
        }

        for(int i=0;i<16;i+=4)
        {
            Log.d(TAG,"convert_4byte_to_int:" + String.format("0x%8x",convert_4byte_to_int(raw_data1,i)));
            Log.d(TAG,"int:" + convert_4byte_to_int(raw_data1,i));
        }
    }

    Long convert_4byte_to_Long(byte[] bytedata, int index)
    {

           return (long)(0xFF & 0) << 64 |
                (long)(0xFF & 0) << 48 |
                (long)(0xFF & 0) << 40 |
                (long)(0xFF & 0) << 32 |
                (long)(0xFF & bytedata[index]) << 24 |
                (long)(0xFF & bytedata[index + 1]) << 16 |
                (long)(0xFF & bytedata[index + 2]) << 8 |
                (long)(0xFF & bytedata[index + 3]);
    }

    Long convert_3byte_to_Long(byte[] bytedata, int index)
    {
        return (long)(0xFF & 0) << 64 |
                (long)(0xFF & 0) << 48 |
                (long)(0xFF & 0) << 40 |
                (long)(0xFF & 0) << 32 |
                (long)(0xFF & 0) << 24 |
                (long)(0xFF & bytedata[index]) << 16 |
                (long)(0xFF & bytedata[index+1]) << 8 |
                (long)(0xFF & bytedata[index+2]);
    }

    int convert_3byte_to_int(byte[] bytedata, int index) {
        return
                (0xFF & bytedata[index]) << 24 |
                (0xFF & bytedata[index + 1]) << 16 |
                (0xFF & bytedata[index + 2]) << 8;
    }

    int convert_4byte_to_int(byte[] bytedata, int index) {
        return
                (0xFF & bytedata[index]) << 24 |
                        (0xFF & bytedata[index + 1]) << 16 |
                        (0xFF & bytedata[index + 2]) << 8 |
                        (0xFF & bytedata[index + 3]);
    }


    public static int byteArrayToInt(byte[] b, int index){
        return   b[index+3] & 0xFF |
                (b[index+2] & 0xFF) << 8 |
                (b[index+1] & 0xFF) << 16 |
                (b[index+0] & 0xFF) << 24;
    }
    public static int byteArrayToInt2(byte[] b, int index){
        return   //b[index+3] & 0xFF |
                (b[index+2] & 0xFF) << 0 |
                (b[index+1] & 0xFF) << 8 |
                (b[index+0] & 0xFF) << 16;
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

    /**
     * 蓝牙传输 16进制 高低位 读数的 转换
     *
     * @param data 截取数据源，字节数组
     * @param index 截取数据开始位置
     * @param count 截取数据长度，只能为2、4、8个字节
     * @param flag 标识高低位顺序，高位在前为true，低位在前为false
     * @return
     */
    public static long byteToLong(byte[] data, int index, int count, boolean flag) {
        long lg = 0;
        if (flag) {
            switch (count) {
                case 2:
                    lg = ((((long) data[index + 0] & 0xff) << 8)
                            | (((long) data[index + 1] & 0xff) << 0));
                    break;

                case 4:
                    lg = ((((long) data[index + 0] & 0xff) << 24)
                            | (((long) data[index + 1] & 0xff) << 16)
                            | (((long) data[index + 2] & 0xff) << 8)
                            | (((long) data[index + 3] & 0xff) << 0));
                    break;

                case 8:
                    lg = ((((long) data[index + 0] & 0xff) << 56)
                            | (((long) data[index + 1] & 0xff) << 48)
                            | (((long) data[index + 2] & 0xff) << 40)
                            | (((long) data[index + 3] & 0xff) << 32)
                            | (((long) data[index + 4] & 0xff) << 24)
                            | (((long) data[index + 5] & 0xff) << 16)
                            | (((long) data[index + 6] & 0xff) << 8)
                            | (((long) data[index + 7] & 0xff) << 0));
                    break;
            }
            return lg;
        } else {
            switch (count) {
                case 2:
                    lg = ((((long) data[index + 1] & 0xff) << 8)
                            | (((long) data[index + 0] & 0xff) << 0));
                    break;
                case 4:
                    lg = ((((long) data[index + 3] & 0xff) << 24)
                            | (((long) data[index + 2] & 0xff) << 16)
                            | (((long) data[index + 1] & 0xff) << 8)
                            | (((long) data[index + 0] & 0xff) << 0));
                    break;
                case 8:
                    lg = ((((long) data[index + 7] & 0xff) << 56)
                            | (((long) data[index + 6] & 0xff) << 48)
                            | (((long) data[index + 5] & 0xff) << 40)
                            | (((long) data[index + 4] & 0xff) << 32)
                            | (((long) data[index + 3] & 0xff) << 24)
                            | (((long) data[index + 2] & 0xff) << 16)
                            | (((long) data[index + 1] & 0xff) << 8)
                            | (((long) data[index + 0] & 0xff) << 0));
                    break;
            }
            return lg;
        }
    }
}
