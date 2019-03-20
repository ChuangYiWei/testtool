package com.example.johnny_wei.testtool.adapter;

public class ServiceGroup {

    private String serviceName;
    private String UUID;

    public ServiceGroup() {
    }

    public ServiceGroup(String serviceName, String UUID) {
        this.serviceName = serviceName;
        this.UUID = UUID;
    }

    public String GetServiceName() {
        return serviceName;
    }

    public void SetServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String GetUUID() {
        return UUID;
    }

    public void SetUUID(String serviceName) {
        this.UUID = UUID;
    }
}
