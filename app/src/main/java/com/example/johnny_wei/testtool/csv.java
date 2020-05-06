package com.example.johnny_wei.testtool;
import android.content.pm.PackageManager;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.johnny_wei.testtool.utils.AlertDialogUtil;
import com.example.johnny_wei.testtool.utils.csv_util.CSV_WriteUtil;
import com.example.johnny_wei.testtool.utils.csv_util.CSV_ReaderUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.johnny_wei.testtool.config.globalConfig.PERMISSION_REQUEST_COARSE_LOCATION;
import static com.example.johnny_wei.testtool.config.globalConfig.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE;

public class csv extends AppCompatActivity {
    static final String PATH_SD = Environment.getExternalStorageDirectory().getAbsolutePath();
    private final String TAG = getClass().getSimpleName();
    String config_folder = "01_CONFIG";
    String config_name = "auto_config.csv";
    int itme_idx = 0;
    int func_name_idx = 1;
    int cmd_idx = 3;
    int evt_idx= 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csv);

//        csv_read_demo();
        csv_write_demo();

    }

    void csv_write_demo()
    {
        String csvFile = PATH_SD +
                File.separator +
                "report.csv";
        Log.d(TAG,"write csv file:"+ csvFile);
        try {
            String keys[]  ={"Date","Brand","Version"};
            List<String> rows = new ArrayList<>();

            FileWriter writer = new FileWriter(csvFile);
//            CSV_WriteUtil.writeLine(writer, Arrays.asList("a", "b", "c", "d"));
            //先寫第一行key
            CSV_WriteUtil.writeLine(writer, Arrays.asList(keys));

            //寫資料 add data
            rows.clear();
            rows.add("20200122");
            rows.add("oppo");
            rows.add("6.0.1");
            CSV_WriteUtil.writeLine(writer, rows);

            //flush從buffer寫到系統
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    void csv_read_demo()
    {
        String filename = PATH_SD +
                File.separator +
                config_folder +
                File.separator +
                config_name;
        //check file exist
        is_File_Exist(filename);
        //check list is null or not
        readcsv(filename);
    }

    void readcsv(String filename)
    {
        List<String[]> rows = new ArrayList<>();
        CSV_ReaderUtil csvReader = new CSV_ReaderUtil(csv.this, filename);
        rows = csvReader.readCSV();

        if (rows == null) {
            Log.e(TAG, "rows is null");
            return;
        }

        for (int i = 0; i < rows.size(); i++) {
            Log.d("row len:", String.format("row len %s", rows.get(i).length));
            Log.d("csv_test", String.format("row %s:%s,%s", i, rows.get(i)[0], rows.get(i)[1]));
            Log.d(TAG,"item:"+rows.get(i)[itme_idx]);
            Log.d(TAG,"func_name:"+rows.get(i)[func_name_idx]);
            Log.d(TAG,"cmd:"+rows.get(i)[cmd_idx]);
            Log.d(TAG,"evt:"+rows.get(i)[evt_idx]);
        }
    }

    boolean is_File_Exist(String absfileName) {
        File file = new File(absfileName);
        if (!file.exists()) {
            Log.d(TAG, "fileName: " + absfileName + " not exist");
            return false;
        }
        Log.d(TAG, "fileName: " + absfileName + " exist");
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d("Permission", "onRequestPermissionsResult");
        if (grantResults.length == 0) {
            Log.e(TAG, "grantResults size is 0");
            return;
        }
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    AlertDialogUtil.simpleDialog(this,
                            "Functionality limited",
                            "Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                }
                break;
            }
            case PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "storage location permission granted");
                } else {
                    AlertDialogUtil.simpleDialog(this,
                            "Functionality limited",
                            "storage access has not been granted, debug log disable");
                }
            }
            break;
        }
    }


    //write csv
//    https://www.mkyong.com/java/how-to-export-data-to-csv-file-java/
}



