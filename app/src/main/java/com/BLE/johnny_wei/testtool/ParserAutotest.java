package com.BLE.johnny_wei.testtool;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ParserAutotest {
    //采用XmlPullParser来解析XML文件
    public static List<BDAutoTest> getAutoTestItems(InputStream inStream) throws Throwable
    {
        List<BDAutoTest> BDAutoTestList = null;
        BDAutoTest BDAutoTest = null;

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
                    BDAutoTestList = new ArrayList<BDAutoTest>();
                    break;

                case XmlPullParser.START_TAG:
                    String name = parser.getName();
                    Log.d("START_TAG getName() : ",name);
                    if("BD_Autotest".equals(name))
                    {
                        BDAutoTest = new BDAutoTest();
                    }
                    if(BDAutoTest!=null)
                    {
                        if("testName".equals(name))
                        {
                            BDAutoTest.settestName(parser.nextText());
                        }
                        if("data".equals(name))
                        {
                            BDAutoTest.setdataStr(parser.nextText());
                        }
                        if("writeCmd".equals(name))
                        {
                            BDAutoTest.setwriteCmd(parser.nextText());
                        }
                        if("readCmd".equals(name))
                        {
                            BDAutoTest.setreadCmd(parser.nextText());
                        }
                    }
                    break;

                case XmlPullParser.END_TAG:

                    String name2 = parser.getName();
                    Log.d("END_TAG getName() : ",name2);
                    if("BD_Autotest".equals(parser.getName()))
                    {
                        BDAutoTestList.add(BDAutoTest);
                        BDAutoTest = null;
                    }
                    break;
                default:
                    break;
            }
            eventType = parser.next();
        }
        return BDAutoTestList;
    }

}
