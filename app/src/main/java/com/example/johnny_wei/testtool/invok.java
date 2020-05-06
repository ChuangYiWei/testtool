package com.example.johnny_wei.testtool;

import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.johnny_wei.testtool.BLETest.Invoke_sample;
import com.example.johnny_wei.testtool.BLETest.test_class1;
import com.example.johnny_wei.testtool.BLETest.test_class2;
import com.example.johnny_wei.testtool.utils.csv_util.CSV_ReaderUtil;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class invok extends AppCompatActivity {
    String TAG = getClass().getSimpleName();

    private static Invoke_sample invok_obj;
    private static test_class1 test_class1_obj;
    private static test_class2 test_class2_obj;
    private static Class<Invoke_sample> cls;
    Class clz;

    static final String PATH_SD = Environment.getExternalStorageDirectory().getAbsolutePath();
    String config_folder = "01_CONFIG";
    String config_name = "auto_config.csv";
    int itme_idx = 0;
    int group_idx = 2;
    int func_name_idx = 3;
    int cmd_idx = 6;
    int evt_idx= 7;
    List<String[]> rows = new ArrayList<>();

    Map<String, String> map = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invok);

        invok_obj = new Invoke_sample("Zhao");
        test_class1_obj = new test_class1("test_class1_obj");
        test_class2_obj = new test_class2("test_class2_obj");

        cls = (Class<Invoke_sample>) invok_obj.getClass();
        try {
            clz = Class.forName("com.example.johnny_wei.testtool.BLETest.Invoke_sample");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //invoke by csv
        invokebyCSC();

        //invoke test-----------------
        /*
        invokePrivateMothod3();
        invoke_test_function();
        */

//        creatClassByReflection();
//        invokePrivateMothod();
//        Log.d("jjj","start call method2");
//        invokePrivateMothod2();
    }
    /**
     * 利用反射创建对象
     */
    private static void creatClassByReflection() {
        try {
            Invoke_sample accpTeacher = (Invoke_sample) Class.forName("com.example.johnny_wei.testtool.BLETest.Invoke_sample")
                    .newInstance();
            Log.d("jjj",accpTeacher.toString());

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void invokePrivateMothod2() {
        try {
            // 获取方法名为showName，参数为String类型的方法
            //Method method = cls.getDeclaredMethod("showName2");
            Method method = clz.getDeclaredMethod("showName2");
            // 若调用私有方法，必须抑制java对权限的检查
            //method.setAccessible(true);
            // 使用invoke调用方法，并且获取方法的返回值，需要传入一个方法所在类的对象，new Object[]
            // {"Kai"}是需要传入的参数，与上面的String.class相对应
            String string = (String) method.invoke(invok_obj);
            Log.d("jjj2",string);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取并调用私有方法
     */
    private static void invokePrivateMothod() {
        try {
            // 获取方法名为showName，参数为String类型的方法
            Method method = cls.getDeclaredMethod("showName", String.class);
            // 若调用私有方法，必须抑制java对权限的检查
            method.setAccessible(true);
            // 使用invoke调用方法，并且获取方法的返回值，需要传入一个方法所在类的对象，new Object[]
            // {"Kai"}是需要传入的参数，与上面的String.class相对应
            String string = (String) method.invoke(invok_obj,
                    new Object[] { "Kai" });
            Log.d("jjj",string);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static class MYClass {
        public String name;

        MYClass() {
            this.name = "myclass's name";
        }
    }
    /**
     * 傳自定class
     */
    private void invokePrivateMothod3() {
        try {
            // 獲取方法名Afunc，参数是自定義MYClass class方法
            Method method = invok_obj.getClass().getDeclaredMethod("Afunc", MYClass.class);
            // 若调用私有方法，必须抑制java对权限的检查
            method.setAccessible(true);
            // 使用invoke调用方法，并且獲取方法的返回值，需要傳入一个方法所在class的對象
            String string = (String) method.invoke(invok_obj, new MYClass());
            Log.d(TAG,string);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void invoke_test_function() {
        try {

            String string_array[] = new String[] { "A", "B", "C", "D", "E" };
            test_strArray(string_array);
            // 獲取方法名test_func1，参数是自定義String []型態
            // 獲取方法名test_func2，参数是自String型態
            Method method1 = test_class1_obj.getClass().getDeclaredMethod("test_func1", String[].class);
            Method method2 = test_class1_obj.getClass().getDeclaredMethod("test_func2", String.class);

            // 若调用私有方法，必须抑制java对权限的检查
            method1.setAccessible(true);
            // 使用invoke调用方法，并且獲取方法的返回值，需要傳入一个方法所在class的對象
            //string array當參數要new Object[]{string_array},ref:https://stackoverflow.com/questions/15951521/invoke-method-with-an-array-parameter-using-reflection
            boolean ret1 = (boolean) method1.invoke(test_class1_obj,new Object[]{string_array});
            boolean ret2 = (boolean) method2.invoke(test_class1_obj,"1122");

            //test_class2
            String string_array2_1[] = new String[]{"F", "G"};
            Method method2_1 = test_class2_obj.getClass().getDeclaredMethod("test2_func1", String[].class);
            boolean ret2_1 = (boolean) method2_1.invoke(test_class2_obj, new Object[]{string_array2_1});

            Log.d(TAG,"ret is:"+ret1);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    void invokebyCSC()
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

//        map.put("010820","test_func1");
//        map.put("0171FC","test2_func1");
        for (int i = 0; i < rows.size(); i++) {
            String cmd = rows.get(i)[cmd_idx];
            String map_function = (rows.get(i)[func_name_idx]);
            Log.d(TAG, "cmd:" + cmd);
            Log.d(TAG, "map_function:" + map_function);
            //from 0108200403020105 get first 6 string and mapping to function we add in the hashmap
//            String map_function = map.get(rows.get(i)[cmd_idx].substring(0, 6));

            if (map_function == null) {
                Log.e(TAG, "map_function is null, cmd:" + rows.get(i)[cmd_idx]);
                continue;
            }
            try {
                if ((rows.get(i)[group_idx].equals("hci")))  {
                    Method method = test_class1_obj.getClass().getDeclaredMethod(map_function, String[].class);
                    boolean ret1 = (boolean) method.invoke(test_class1_obj, new Object[]{rows.get(i)});
                } else if (Integer.parseInt(rows.get(i)[group_idx]) == 1) {
                    Method method = test_class2_obj.getClass().getDeclaredMethod(map_function, String[].class);
                    boolean ret2 = (boolean) method.invoke(test_class2_obj, new Object[]{rows.get(i)});
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean test_strArray(String[] str) {
        Log.d(TAG,"xxxx");
        Log.d(TAG,"str[0]:" + str[0]);
        return true;
    }

    void readcsv(String filename)
    {

        CSV_ReaderUtil csvReader = new CSV_ReaderUtil(this, filename);
        rows = csvReader.readCSV();

        if (rows == null) {
            Log.e(TAG, "rows is null");
            return;
        }

        for (int i = 0; i < rows.size(); i++) {
            Log.d("row len:", String.format("row len %s", rows.get(i).length));
            Log.d("csv_test", String.format("row %s:%s,%s", i, rows.get(i)[0], rows.get(i)[1]));
            Log.d(TAG,"item:"+rows.get(i)[itme_idx]);
            Log.d(TAG,"group:"+rows.get(i)[group_idx]);
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
}
