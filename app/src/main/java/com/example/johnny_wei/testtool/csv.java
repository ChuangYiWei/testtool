package com.example.johnny_wei.testtool;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.johnny_wei.testtool.csv_util.CSV_ReaderUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class csv extends AppCompatActivity {
    static final String PATH_SD = Environment.getExternalStorageDirectory().getAbsolutePath();
    private final String TAG = getClass().getSimpleName();
    String config_folder = "01_config";
    String config_name = "test_config.csv";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csv);
        String filename = PATH_SD +
                          File.separator +
                          config_folder +
                          File.separator +
                          config_name;
        is_File_Exist(filename);
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
        }
    }

    boolean is_File_Exist(String absfileName)
    {
        File file = new File(absfileName);
        if (!file.exists()) {
            Log.d(TAG, "fileName: " + absfileName + " not exist");
            return false;
        }
        Log.d(TAG, "fileName: " + absfileName + " exist");
        return true;
    }

    //write csv
//    https://www.mkyong.com/java/how-to-export-data-to-csv-file-java/
}



