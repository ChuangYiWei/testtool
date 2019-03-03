package com.example.johnny_wei.testtool;

import android.util.Log;

interface ICallback{
    public void callback(MyObj o);
    //onConnection callback
    //ondisconnect callback
}

class MyObj {
    public MyObj(){}
    public int data = 0;
    //gatt merber
}
//a calls b do some work, b will callback if work done.
class caller implements ICallback {
    MyObj mobj;
    Worker m_worker;
    caller()
    {
        m_worker = new Worker(this);
        m_worker.startWork();
    }

    @Override
    public void callback(MyObj obj){
        Log.d("caller","class caller been called , data is :" + obj.data);
        this.mobj = obj;
    }
}

//lite ble
class Worker {
    ICallback ic;
    MyObj mobj;
    Worker(ICallback ic){
        this.ic = ic;
        mobj = new MyObj();
    }

    public void startWork()
    {
        Log.d("AAA","Worker startWork enter");
        Thread t = new Thread(new Runnable() {
            public void run() {
                Log.d("AAA","startWork");
                mobj.data =33;
                ic.callback(mobj);
            }
        });
        t.start();
    }

/*
    //method 2
    abstract class absClass implements ICallback
    {
        public void callback(MyObj o) {
            Log.d("caller","absClass class  been called");
        }
    }


    class caller extends absClass {
        MyObj mobj;
        Worker m_worker;
        caller()
        {
            m_worker = new Worker(this);
            m_worker.startWork();
        }

//    @Override
//    public void callback(MyObj obj){
//        Log.d("caller","class caller been called , data is :" + obj.data);
//        this.mobj = obj;
//    }
*/
}

