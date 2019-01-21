package com.example.johnny_wei.testtool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class write extends AppCompatActivity {

    public static final String ENCODING="UTF-8";//常量，代表編碼格式。
    String fileName="test.txt";//文件名稱
    String message="你好，這是一個關於文件I/O的示例。";//寫入和讀出的數據信息
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        writeFileData(fileName, message);//創建文件並寫入數據
        String result= readFileData(fileName);//獲得從文件讀入的數據
        tv = (TextView) findViewById(R.id.tv);
        tv.setText(result);


    }

    public String readFileData(String fileName){
        String result = "";
        try {
//            FileInputStream fin = openFileInput(fileName);//獲得FileInputStream對象
//            int length = fin.available();//獲取文件長度
            File file = new File(fileName);
            FileInputStream fin = new FileInputStream(file);//獲得FileInputStream對象
            int length = fin.available();//獲取文件長度
            byte[] buffer = new byte[length];//創建byte數組用於讀入數據

            fin.read(buffer);
            result = new String(buffer, 0,buffer.length);
            fin.close();//關閉文件輸入流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }



    public void writeFileData(String fileName,String message){

        try {
            FileOutputStream fos = openFileOutput(fileName,MODE_PRIVATE);//獲得FileOutputStream對象
            byte[] bytes = message.getBytes();//將要寫入的字符串轉換為byte數組
            fos.write(bytes);//將byte數組寫入文件
            fos.close();//關閉FileOutputStream對象
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
