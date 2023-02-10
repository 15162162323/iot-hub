package com.hik.iothub.device.adapter;

import com.hik.iothub.bean.DeviceInfo;
import com.hik.iothub.device.adaptee.IDeviceAdaptee;
import com.hik.iothub.device.adaptee.CykeoReaderAdaptee;
import com.hik.iothub.device.adaptee.PostekPrinterAdaptee;
import com.hik.iothub.device.adaptee.ZebraPrinterAdaptee;
import com.hik.iothub.device.intf.DeviceHub;
import org.apache.commons.lang3.StringUtils;

public class DeviceAdapter implements DeviceHub {
    private IDeviceAdaptee adaptee;

    public DeviceAdapter(IDeviceAdaptee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public boolean print(String accessMode, String ip, int port,String serialPort, String cmd) {
        return adaptee.print(accessMode,ip,port,serialPort,cmd);
    }

    @Override
    public String readSingle(String serialPort) {
        return adaptee.readSingle(serialPort);
    }

    @Override
    public void read(String serialPort) {
        adaptee.read(serialPort);
    }

    @Override
    public boolean write(String epc, String hexPassword, String serialPort) {
        return adaptee.write(epc, hexPassword, serialPort);
    }

    /**
     * 处理返回结果
     */
    public void handlerMessage(){

    }

    public String formatParams(String params){
        return adaptee.formatParams(params);
    }

    @Override
    public boolean deviceStatus(DeviceInfo deviceInfo) {
        return adaptee.status(deviceInfo);
    }
}
