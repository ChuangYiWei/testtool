package com.example.johnny_wei.testtool.callback;

public interface IBLECallback {
    //connection
    void ConnectedCB();
    void SrvDiscoverSuccessCB();
    void SrvDiscoverFailCB(String reason);
    void DisConnectCB();
    void ConnectFailCB(String reason);

    //read chara uuid
    void readCharacteristicSuccessCB(String UUID, byte[] CBData);
    void readCharacteristicFailCB(String UUID, int status);

    //write chara uuid
    void writeCharacteristicSuccessCB(String UUID, byte[] CBData);
    void writeCharacteristicFailCB(String UUID, int status);

    //chara uuid value changed
    void CharaValueChangedSuccessCB(String UUID, byte[] CBData);

    //read desc uuid
    void readDescSuccessCB(String UUID, byte[] CBData);
    void readDescFailCB(String UUID, int status);

    //write desc uuid
    void writeDescSuccessCB(String UUID, byte[] CBData);
    void writeDescFailCB(String UUID, int status);

    //    void OnServicesDiscovered();
}



