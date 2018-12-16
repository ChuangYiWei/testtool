package com.example.johnny_wei.testtool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class invok extends AppCompatActivity {
    private static Person2 person;
    private static Class<Person2> cls;
    Class clz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invok);

        person = new Person2("Zhao");
        cls = (Class<Person2>) person.getClass();
        try {
            clz = Class.forName("com.example.johnny_wei.testtool.Person2");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

//        creatClassByReflection();
       // invokePrivateMothod();
//        Log.d("jjj","start call method2");
//        invokePrivateMothod2();
        invokePrivateMothod3();
    }
    /**
     * 利用反射创建对象
     */
    private static void creatClassByReflection() {
        try {
            Person2 accpTeacher = (Person2) Class.forName("com.example.johnny_wei.testtool.Person2")
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
            String string = (String) method.invoke(person);
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
            String string = (String) method.invoke(person,
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
    private static void invokePrivateMothod3() {
        try {
            // 获取方法名为showName，参数为String类型的方法
            Method method = person.getClass().getDeclaredMethod("Afunc", MYClass.class);
            // 若调用私有方法，必须抑制java对权限的检查
            method.setAccessible(true);
            // 使用invoke调用方法，并且获取方法的返回值，需要传入一个方法所在类的对象，new Object[]
            // {"Kai"}是需要传入的参数，与上面的String.class相对应
            //String string = (String) method.invoke(person, new Object[] {});
            String string = (String) method.invoke(person, new MYClass());
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

}
