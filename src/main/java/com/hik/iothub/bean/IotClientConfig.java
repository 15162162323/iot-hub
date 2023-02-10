package com.hik.iothub.bean;


import java.util.List;

public class IotClientConfig {
    /*客户端ID*/
    private String id;
    /*协议模式 MQTT、HTTP*/
    private String protocolMode;

    /*如果为MQTT则配置此项*/
    private MqttConfig mqttConfig;
    /*此客户端关联配置的设备*/
    private List<DeviceInfo> deviceInfoList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProtocolMode() {
        return protocolMode;
    }

    public void setProtocolMode(String protocolMode) {
        this.protocolMode = protocolMode;
    }

    public MqttConfig getMqttConfig() {
        return mqttConfig;
    }

    public void setMqttConfig(MqttConfig mqttConfig) {
        this.mqttConfig = mqttConfig;
    }

    public List<DeviceInfo> getDeviceInfoList() {
        return deviceInfoList;
    }

    public void setDeviceInfoList(List<DeviceInfo> deviceInfoList) {
        this.deviceInfoList = deviceInfoList;
    }
}
