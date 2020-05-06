package com.BLE.johnny_wei.testtool;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ParserByPULL {
    //采用XmlPullParser来解析XML文件
    public static List<Student> getStudents(InputStream inStream) throws Throwable
    {
        List<Student> students = null;
        Student mStudent = null;

        //========创建XmlPullParser,有三种方式=======
        //方式一:使用工厂类XmlPullParserFactory
        XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = pullFactory.newPullParser();
        //方式二:使用Android提供的实用工具类android.util.Xml
        //XmlPullParser parser = Xml.newPullParser();
        //方式三：使用Resources的getXml()方法返回XmlResourceParser对象
        //XmlResourceParser Parser=getResources().getXml(R.xml.student);

        //用着三种中的任一种，只需把标签开头的类变一下即可。以第三个为例只需把XmlPullParser.END_DOCUMENT改为XmlResourceParser.END_DOCUMENT所        //有标签都这么改即可

        //解析文件输入流
        parser.setInput(inStream, "UTF-8");
        //产生第一个事件
        int eventType = parser.getEventType();
        //只要不是文档结束事件，就一直循环
        while(eventType!=XmlPullParser.END_DOCUMENT)
        {
            switch (eventType)
            {
                //触发开始文档事件
                case XmlPullParser.START_DOCUMENT:
                    students = new ArrayList<Student>();
                    break;
                //触发开始元素事件
                case XmlPullParser.START_TAG:
                    //获取解析器当前指向的元素的名称
                    String name = parser.getName();
                    Log.d("START_TAG getName() : ",name);
                    if("student".equals(name))
                    {
                        //通过解析器获取id的元素值，并设置student的id
                        mStudent = new Student();
                        mStudent.setId(parser.getAttributeValue(0));
                    }
                    if(mStudent!=null)
                    {
                        if("name".equals(name))
                        {
                            //获取解析器当前指向元素的下一个文本节点的值
                            mStudent.setName(parser.nextText());
                        }
                        if("age".equals(name))
                        {
                            //获取解析器当前指向元素的下一个文本节点的值
                            mStudent.setAge(new Short(parser.nextText()));
                        }
                        if("sex".equals(name))
                        {
                            //获取解析器当前指向元素的下一个文本节点的值
                            mStudent.setSex(parser.nextText());
                        }
                    }
                    break;
                //触发结束元素事件
                case XmlPullParser.END_TAG:
                    //
                    String name2 = parser.getName();
                    Log.d("END_TAG getName() : ",name2);
                    if("student".equals(parser.getName()))
                    {
                        students.add(mStudent);
                        mStudent = null;
                    }
                    break;
                default:
                    break;
            }
            eventType = parser.next();
        }
        return students;
    }

}
