package com.example.johnny_wei.testtool.csv_util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSV_ReaderUtil {
    Context context;
    String fileName;
    List<String[]> rows = new ArrayList<>();

    public CSV_ReaderUtil(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    public List<String[]> readCSV() {
        BufferedReader br = null;
        try {
            File file = new File(fileName);
            br = new BufferedReader(new FileReader(file));
            String line;
            String csvSplitBy = ",";
            br.readLine();//read first line
            while ((line = br.readLine()) != null) {
                String[] row = line.split(csvSplitBy);
                //do not add line with empty string
                if (!row[0].equals("")) {
                    rows.add(row);
                }
            }
            br.close();

            return rows;
        } catch (IOException e){
            Log.e("CSV_ReaderUtil", "read csv IOException");
            return null;
        }
    }
}
