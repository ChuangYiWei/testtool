package com.example.johnny_wei.testtool;

import android.app.Activity;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.johnny_wei.testtool.log.dbgLog;
import com.example.johnny_wei.testtool.utils.DevUtil;
import com.example.johnny_wei.testtool.utils.permissionUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class write extends AppCompatActivity {

    public static final String ENCODING="UTF-8";//常量，代表編碼格式。
    String className = getClass().getSimpleName();

    private TextView tv;
    Activity mActivity = this;
    permissionUtil reqpermission;

    static final String PATH_SD = Environment.getExternalStorageDirectory().getAbsolutePath();

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

     final String CURRENT_PATH = "./";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storage_setup();
        dbgLog.setDebug(true);
        dbgLog.setPath("123/456");
        dbgLog.e("ttt","123");

//        createFoler("log_debug");
//        writeDebug("abc","123");
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
        Log.d(className, "createFoler path is " + absPath);
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
            //Log.d(TAG, "sd card mounted with read/write access");
            return true;
        } else {
            Log.d(className, "mount fail state : " + state);
        }
        return false;
    }







}
