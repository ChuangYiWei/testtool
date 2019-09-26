package com.example.johnny_wei.testtool;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class csv extends AppCompatActivity {
    static final String PATH_SD = Environment.getExternalStorageDirectory().getAbsolutePath();

    public class CSVReader {
        Context context;
        String fileName;
        List<String[]> rows = new ArrayList<>();

        public CSVReader(Context context, String fileName) {
            this.context = context;
            this.fileName = fileName;
        }

        public List<String[]> readCSV() throws IOException {
            //InputStream is = context.getAssets().open(fileName);
            //InputStreamReader isr = new InputStreamReader(is);
            File file = new File(PATH_SD,fileName);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String csvSplitBy = ",";

            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] row = line.split(csvSplitBy);
                rows.add(row);
            }
            return rows;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csv);
        String filename = "converted.csv";
        readcsv(filename);
    }

    void readcsv(String filename)
    {
        List<String[]> rows = new ArrayList<>();
        CSVReader csvReader = new CSVReader(csv.this, filename);
        try {
            rows = csvReader.readCSV();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < rows.size(); i++) {
            Log.d("row len:", String.format("row len %s", rows.get(i).length));
            Log.d("csv_test", String.format("row %s:%s,%s", i, rows.get(i)[0], rows.get(i)[1]));
        }
    }


    //write csv
//    https://www.mkyong.com/java/how-to-export-data-to-csv-file-java/
}



