package com.hik.iothub.bean;

public class DeviceInfo {

    private String deviceId;
    /*设备名称*/
    private String deviceName;
    /*设备类型 PRINTER、RFIDREADER*/
    private String deviceType;

    //设备型号
    private String model;
    /*设备品牌 POSTEK、ZEBRA 、CYKEO*/
    private String deviceMake;
    /*连接方式 NETWORK  USB*/
    private String accessMode;
    /*ip*/
    private String ip;
    /*端口号*/
    private Integer port;
    /*串口号*/
    private String serialPort;

    private Boolean status;

    private String statusCode;

    private String statusMsg;


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceMake() {
        return deviceMake;
    }

    public void setDeviceMake(String deviceMake) {
        this.deviceMake = deviceMake;
    }

    public String getAccessMode() {
        return accessMode;
    }

    public void setAccessMode(String accessMode) {
        this.accessMode = accessMode;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getSerialPort() {
        return serialPort;
    }

    public void setSerialPort(String serialPort) {
        this.serialPort = serialPort;
    }

}
