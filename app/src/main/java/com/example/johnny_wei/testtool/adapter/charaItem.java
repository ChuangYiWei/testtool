package com.example.johnny_wei.testtool.adapter;

public class charaItem {

    private String CharacteristicName;
    private String UUID;
    private int property;
    private int value;
    private int descriptorNum;

    public charaItem(String CharateristicName,
                     String UUID,
                     int property,
                     int value,
                     int descriptorNum
    ) {
        this.CharacteristicName = CharateristicName;
        this.UUID = UUID;
        this.property = property;
        this.value = value;
        this.descriptorNum = descriptorNum;
    }

    public String GetCharaName() {
        return CharacteristicName;
    }

    public void SetCharaName(String CharaName) {
        this.CharacteristicName = CharaName;
    }

    public String GetUUID() {
        return UUID;
    }

    public void SetUUID(String UUID) {
        this.UUID = UUID;
    }

    public int Getproperty() {
        return property;
    }

    public void Setproperty(int property) {
        this.property = property;
    }

    public int Getvalue() {
        return value;
    }

    public void Setvalue() {
        this.value = property;
    }


    public int GetDescriptorNum() {
        return descriptorNum;
    }

    public void SetDescriptorNum(int descriptorNum) {
        this.descriptorNum =  descriptorNum;
    }
}
