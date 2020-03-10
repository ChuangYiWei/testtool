package com.example.johnny_wei.testtool.BLEutils.callback;

/**
 * LiteBLECallback is an abstract extension of IBLECallback.
 */
public abstract class LiteBLECallback implements IBLECallback
{
    //connection
    //abstract must implement
    abstract public void ConnectedCB();
    abstract public void DisConnectCB();
    abstract public void ConnectFailCB(String reason);


    public void SrvDiscoverSuccessCB(){}
    public void SrvDiscoverFailCB(String reason){}

    //read chara uuid
    public void readCharacteristicSuccessCB(String UUID, byte[] CBData)
    {

    }
    public void readCharacteristicFailCB(String UUID, int status)
    {

    }

    //write chara uuid
    public void writeCharacteristicSuccessCB(String UUID, byte[] CBData)
    {

    }
    public void writeCharacteristicFailCB(String UUID, int status)
    {

    }

    //chara uuid value changed
    public void CharaValueChangedSuccessCB(String UUID, byte[] CBData){}

    //read desc uuid
    public void readDescSuccessCB(String UUID, byte[] CBData){}
    public void readDescFailCB(String UUID, int status){}

    //write desc uuid
    public void writeDescSuccessCB(String UUID, byte[] CBData){}
    public void writeDescFailCB(String UUID, int status){}

    //mtu change
    public void onMtuChangedSuccessCB(int mtu){}
    public void onMtuChangedFailCB(int mtu){}

    //phy read
    public void onPhyReadSuccessCB(int txPhy, int rxPhy){}
    public void onPhyReadFailCB(int txPhy, int rxPhy){}

    //phy update
    public void onPhyUpdateSuccessCB(int txPhy, int rxPhy){}
    public void onPhyUpdateFailCB(int txPhy, int rxPhy){}

}
