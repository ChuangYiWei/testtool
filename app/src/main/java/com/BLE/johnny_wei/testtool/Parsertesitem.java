package com.BLE.johnny_wei.testtool;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.List;

public class Parsertesitem {
    public static List<BDTestitem> getTestItems(InputStream inStream) throws Throwable
    {
        List<BDTestitem> BDTestitemList = null;
        BDTestitem BDTestitem = null;


        XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = pullFactory.newPullParser();

        parser.setInput(inStream, "UTF-8");
        //产生第一个事件
        int eventType = parser.getEventType();
        //只要不是文档结束事件，就一直循环
        while(eventType!=XmlPullParser.END_DOCUMENT)
        {
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        Log.d("START_DOCUMENT","START_DOCUMENT");
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("students")) {
                           Log.d("students","students");
                           //datas = new ArrayList<>();
                        } else if (parser.getName().equals("student")) {
                            Log.d("student","student");
                            //bean = new Bean1();
                        } else if (parser.getName().equals("name")) {
                            Log.d("name","name");
//                            Log.d("name tag",parser.getAttributeValue(null, "sex"));
//                            Log.d("name tag",(parser.nextText()));
                        } else if (parser.getName().equals("address")) {
                            Log.d("address","address");
//                            Log.d("address tag",(parser.nextText()));
                        }else if(parser.getName().equals("age")){
                            Log.d("age","age");
//                            Log.d("age tag",(parser.nextText()));
                        }else if (parser.getName().equals("nickName")) {
                            Log.d("nickName","nickName");
//                            Log.d("nickName tag",(parser.nextText()));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("student")) {
                            Log.d("END_TAG", "add data");

                        }
                        break;
                    default:
                        break;
                }

                eventType = parser.next();
            }



            eventType = parser.next();
        }
        return BDTestitemList;
    }
}
