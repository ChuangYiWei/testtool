package com.example.johnny_wei.testtool;

public class BDAutoTest {
    private String testName;
    private String dataStr;
    private String writeCmd;
    private String readCmd;


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

    public String getwriteCmd() {
        return writeCmd;
    }
    public void setwriteCmd(String writeCmd) {
        this.writeCmd = writeCmd;
    }

    public String getreadCmd() {
        return readCmd;
    }
    public void setreadCmd(String readCmd) {
        this.readCmd = readCmd;
    }

}
