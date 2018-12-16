package com.example.johnny_wei.testtool;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ParserAutotest2 {
    //采用XmlPullParser来解析XML文件
    public static List<BDAutoTest2> getAutoTestItems(InputStream inStream) throws Throwable
    {
        List<BDAutoTest2> BDAutoTestList2 = null;
        BDAutoTest2 BDAutoTest = null;

        XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = pullFactory.newPullParser();

        parser.setInput(inStream, "UTF-8");
        int eventType = parser.getEventType();

        while(eventType!=XmlPullParser.END_DOCUMENT)
        {
            switch (eventType)
            {
                //触发开始文档事件
                case XmlPullParser.START_DOCUMENT:
                    BDAutoTestList2 = new ArrayList<BDAutoTest2>();
                    break;

                case XmlPullParser.START_TAG:
                    String name = parser.getName();
                    //Log.d("START_TAG getName() : ", name);
                    if ("BD_Autotest".equals(name)) {
                        BDAutoTest = new BDAutoTest2();
                    }
                    if (BDAutoTest != null) {
                        if ("testName".equals(name)) {
                            BDAutoTest.settestName(parser.nextText());
                        }
                        if ("data".equals(name)) {
                            BDAutoTest.setdataStr(parser.nextText());
                        }
                        if ("cmd".equals(name)) {
                            BDAutoTest.addCmd(parser.nextText());
                        }
                    }
                    break;

                case XmlPullParser.END_TAG:

                    String name2 = parser.getName();
                    //Log.d("END_TAG getName() : ",name2);
                    if("BD_Autotest".equals(parser.getName()))
                    {
                        BDAutoTestList2.add(BDAutoTest);
                        BDAutoTest = null;
                    }
                    break;
                default:
                    break;
            }
            eventType = parser.next();
        }
        return BDAutoTestList2;
    }

}
