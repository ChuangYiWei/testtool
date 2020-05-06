package com.BLE.johnny_wei.testtool;

import java.util.LinkedList;

public class BDAutoTest2 {
    private String testName;
    private String dataStr;
    LinkedList<String> cmdList;

    BDAutoTest2() {
        cmdList = new LinkedList<String>();
    }
    public String gettestName() {return testName;}
    public void settestName(String testName) {
        this.testName = testName;
    }

    public String getdataStr() {
        return dataStr;
    }
    public void setdataStr(String dataStr) {
        this.dataStr = dataStr;
    }

    public String getCmd() {
        return cmdList.getFirst();
    }

    public void addCmd(String cmd) {
        this.cmdList.add(cmd);
    }

    public String popCmd() {
        String cmd = cmdList.getFirst();
        cmdList.removeFirst();
        return cmd;
    }

    public int cmdSize() {
        return cmdList.size();
    }


}
