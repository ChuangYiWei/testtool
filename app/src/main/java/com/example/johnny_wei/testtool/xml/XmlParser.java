package com.example.johnny_wei.testtool.xml;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {
    public static List<BLE_testItem> getTestItems(InputStream inStream) throws Throwable
    {
        List<BLE_testItem> BLE_testItemList = null;
        BLE_testItem obj_ble_testitem = null;
        boolean item_enable  = false;
        boolean select_mode  = false;
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
                        Log.d("START_TAG", parser.getName());
                        if (parser.getName().equals("UART")) {
                            Log.d("uart","new ArrayList<BLE_testItem>");
                            BLE_testItemList = new ArrayList<BLE_testItem>();
                            select_mode = true;
                        }
                        if (select_mode == true) {
                            if (parser.getName().equals("item")) {
                                Log.d("item", "meet item new obj");
                                Log.d("item enable", parser.getAttributeValue(null, "enable"));
                                obj_ble_testitem = new BLE_testItem();
                            } else if (parser.getName().equals("func_name")) {
                                obj_ble_testitem.settestName(parser.nextText());
                            } else if (parser.getName().equals("data")) {
                                Log.d("data", "data");
                                obj_ble_testitem.setdataStr(parser.nextText());
                            } else if (parser.getName().equals("cmd")) {
                                Log.d("cmd", "cmd");
                                obj_ble_testitem.addCmd(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        Log.d("END_TAG", parser.getName());

                        if (parser.getName().equals("item")) {
                            Log.d("/item", "/item");
                            BLE_testItemList.add(obj_ble_testitem);
                            obj_ble_testitem = null;
                        } else if (parser.getName().equals("UART")) {
                            Log.d("/UART", "select_mode false");
                            select_mode = false;
                        }
                        break;
                    default:
                        break;
                }

                eventType = parser.next();
            }
            eventType = parser.next();
        }
        return BLE_testItemList;
    }

    public static List<BLE_testItem> getSPItems(InputStream inStream) throws Throwable
    {
        List<BLE_testItem> BLE_testItemList = null;
        BLE_testItem obj_ble_testitem = null;
        boolean item_enable  = false;

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
                        if (parser.getName().equals("SPI")) {
                            Log.d("uart","new ArrayList<BLE_testItem>");
                            BLE_testItemList = new ArrayList<BLE_testItem>();
                        }
                        else if (parser.getName().equals("item")) {
                            Log.d("item","meet item new obj");
                            Log.d("item enable",parser.getAttributeValue(null, "enable"));
                            obj_ble_testitem = new BLE_testItem();
                        } else if (parser.getName().equals("func_name")) {
                            obj_ble_testitem.settestName(parser.nextText());
                        } else if (parser.getName().equals("data")) {
                            Log.d("data","data");
                            obj_ble_testitem.setdataStr(parser.nextText());
                        } else if (parser.getName().equals("cmd")) {
                            Log.d("name", "name");
                            obj_ble_testitem.addCmd(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            Log.d("/item", "/item");
                            BLE_testItemList.add(obj_ble_testitem);
                            obj_ble_testitem = null;
                        }
                        break;
                    default:
                        break;
                }

                eventType = parser.next();
            }
            eventType = parser.next();
        }
        return BLE_testItemList;
    }
}
