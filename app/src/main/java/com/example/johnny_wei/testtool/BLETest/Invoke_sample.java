package com.example.johnny_wei.testtool.BLETest;

import com.example.johnny_wei.testtool.invok;

public class Invoke_sample {

    private int age;
    private String name;
    protected int height;
    public String school;

    Invoke_sample(){
        this.name = "Invoke_sample";
        age = 22;
    }

    public Invoke_sample(String name) {
        this.name = name;
    }

    private String showName(String _name) {
        return "My name is " + _name;
    }

    public String showName2() {
        return "My name is johnny";
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public String Afunc(invok.MYClass myClass) {
        return "return in Afunc, myClass name is: " + myClass.name;
    }

    @Override
    public String toString() {
        return "My name is " + name + ", i'm " + age + " years old";
    }
}

