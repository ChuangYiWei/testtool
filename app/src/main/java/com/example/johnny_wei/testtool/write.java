package com.example.johnny_wei.testtool;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.johnny_wei.testtool.BLEutils.BleUtil;
import com.example.johnny_wei.testtool.log.dbgLog;
import com.example.johnny_wei.testtool.utils.AlertDialogUtil;
import com.example.johnny_wei.testtool.utils.DevUtil;
import com.example.johnny_wei.testtool.utils.permissionUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import static com.example.johnny_wei.testtool.config.globalConfig.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE;

public class write extends AppCompatActivity {

    public static final String ENCODING = "UTF-8";//常量，代表編碼格式。
    String className = getClass().getSimpleName();

    private TextView tv;
    Activity mActivity = this;
    permissionUtil reqpermission;
    Context mContex = this;
    static final String PATH_SD = Environment.getExternalStorageDirectory().getAbsolutePath();

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    final String CURRENT_PATH = "./";

    List<String> string_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(className,"onCreate");

        storage_setup();
        //set debug to true
        //dbgLog.setDebug(true);

//        writefile();
//        readline("HID_CMD/HID_KB.txt");
//
//        List<String> mouse_list = new ArrayList<>();
//        readline("HID_CMD/mouse_cmd.txt", mouse_list);

        assetCopy();
    }

    void assetCopy()
    {
        //copyAssets();
        copyAssetsToFiles(this,"01_CONFIG","01_CONFIG");
    }

    private void copyAssetsToFiles(Context context,String assetSrcDir, String SDcardDestDir) {
        String[] files;
        try {
            //注意：在assets文件夹下影藏了三个带文件的文件夹，分别是images、sounds、webkit
            //返回数组files里面会包含这三个文件夹
//            files = context.getResources().getAssets().list("");
            files = context.getResources().getAssets().list(assetSrcDir);
        } catch (IOException e1) {
            return;
        }

        String File_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        File mWorkingPath = new File(File_PATH, SDcardDestDir);
        if (!mWorkingPath.exists()) {
            mWorkingPath.mkdirs();
        }

        for (int i = 0; i < files.length; i++) {
            try {
                String fileName = files[i];
                Log.d(className, " fileName:" + fileName);
                File outFile = new File(mWorkingPath, fileName);
                if (outFile.exists()) {
                    Log.d(className,fileName + " exist, skip");
                    continue;
                }

                InputStream in = context.getAssets().open(assetSrcDir + "/" + fileName);
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                in.close();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


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
            Log.d(className, "string_list " + i + " :" + str_list.get(i));
            String[] axis = str_list.get(i).split(",");
            Log.d(className, "axis :" + axis[0]);
            Log.d(className, "axis1 :" + axis[1]);
            Log.d(className, "axis1 :" + axis[2]);
            Log.d(className, "axis1 :" + axis[3]);
        }
    }

    //filename is folder+file under sd card root dir
    void readline(String filename)
    {
        StringBuilder text = new StringBuilder();
        try {
            File file = new File(PATH_SD,filename);

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                Log.e(className, line);
                string_list.add(line);
                text.append(line);
                text.append('\n');
            }
            br.close() ;
        }catch (IOException e) {
            e.printStackTrace();
        }
        //save string in list
        for(int i=0;i<string_list.size();i++)
        {
            Log.d(className, "string_list" + i + ":" + string_list.get(i));
        }

        int idx=1;
        String data = string_list.get(idx).substring(8, string_list.get(idx).length());
        dbgLog.d("write", data);//tag, message

    }

    //find largest number after "debug_x"
    private int findLargetExtNum(String folderName) {
        int MaxNum = -1;
        int current = MaxNum;
        try {
            File directory = new File(Environment.getExternalStorageDirectory(), folderName);
            //get all the files from a directory
            File[] fList = directory.listFiles();

            for (File file : fList) {
                System.out.println(file.getName());
                //find largest file num extension
                current = Integer.parseInt(file.getName().substring(file.getName().indexOf("_") + 1));
                if(current > MaxNum)
                {
                    MaxNum = current;
                }
            }
        }
        catch (Exception e)
        {
            Log.e(className,e.toString());
            return -1;
        }
        return current;
    }

    private void writefile() {
        //dbgLog.setPath("D_DEBUG/debug_01");//create file 789,folder is 123/456
        dbgLog.d("warring", "111111111111111111111");//tag, message
        dbgLog.d("warring", "222222222222222222222");//tag, message
        dbgLog.d("warring", "333333333333333333333");//tag, message
        dbgLog.d("warring", "444444444444444444444");//tag, message
        dbgLog.d("warring", "555555555555555555555");//tag, message


        //Log.d(className,"largest:" + findLargetExtNum("D_DEB"));
//      GetFilebyAddsuffix("debug_01");

//        DevUtil.printDeviceInfo();
        //dbgLog.deletefolder("123");
//        dbgLog.deletefile("1");
//        createFoler("log_debug");
    }

    private String GetFilebyAddsuffix(String filename) {
        //debug_01 => debug_02
//        String filename = "debug_01";
        int indexof_ = filename.indexOf("_");
        int num = Integer.parseInt(filename.substring(indexof_ + 1));
        num = num + 1;
        String new_filename = filename.substring(0, indexof_ + 1) + num;
        Log.d("jjj", "new_filename:" + new_filename);
        return new_filename;
    }

    void storage_setup()
    {
        reqpermission = new permissionUtil(mActivity);
        DevUtil.request_WritePermissions(mActivity);
    }

    /*create folder under sd path*/
    private boolean createFoler(String folderName) {
        if (!checkExternalMedia()) {
            return false;
        }

        String absPath = PATH_SD + File.separator + folderName;
        Log.d(className, "create Folder path is " + absPath);
        File folder = new File(absPath);

        if (!folder.exists()) {
            boolean success = folder.mkdirs();
            if (success) {
                Log.d(className, "create folder " + absPath + " success");
                return true;
            } else {
                Log.d(className, "create folder " + absPath + "fail");
                return false;
            }
        } else {
            Log.d(className, "folder exist");
        }

        return true;
    }

    public void writeDebug(String filename, String data) {

        Date dt = new Date();
        String dts = sdf.format(dt);
        data = dts + " : " + data;
        try {
            String absPath = PATH_SD + File.separator + CURRENT_PATH + File.separator + filename;
            FileOutputStream output = new FileOutputStream(absPath, true);
            output.write(data.getBytes());  //write()寫入字串，並將字串以byte形式儲存。利用getBytes()將字串內容換為Byte
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*check external storage permission*/
    private boolean checkExternalMedia() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Log.d(className, "sd card mounted with read/write access");
            return true;
        } else {
            Log.d(className, "mount fail state : " + state);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d("Permission", "onRequestPermissionsResult");
        if (grantResults.length == 0) {
            Log.e(className, "grantResults size is 0");
            return;
        }
        switch (requestCode) {
            case PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.d(className, "storage location permission granted");
                } else {
                    AlertDialogUtil.simpleDialog(mActivity,
                            "Functionality limited",
                            "storage access has not been granted, debug log disable");
                }
            }
            break;
        }
    }





}
