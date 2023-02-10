package com.hik.iothub.device.adaptee;

import com.hik.iothub.bean.DeviceConnectType;
import com.hik.iothub.bean.DeviceInfo;
import com.hik.iothub.device.plugins.postek.PostekPrinterUtils;
import org.apache.commons.lang3.StringUtils;


public class PostekPrinterAdaptee  implements IDeviceAdaptee {


    @Override
    public boolean print(String connectType, String ip, int port,String serialPort, String cmd) {
        /**
         * 判断打印方式
         */
        if(StringUtils.equalsIgnoreCase(connectType, DeviceConnectType.NETWORK.toString())){
            return PostekPrinterUtils.printNet(ip,port,cmd);
        }else{
            return PostekPrinterUtils.printUsb(ip,port,Integer.valueOf(serialPort),cmd);
        }
    }

    @Override
    public void read(String serialPort) {

    }

    @Override
    public String readSingle(String serialPort) {
        return null;
    }

    @Override
    public boolean write(String epc, String hexPassword, String serialPort) {

        return false;
    }

    @Override
    public String formatParams(String params) {
        String cmd = params;
        return cmd;
    }

    @Override
    public Boolean status(DeviceInfo deviceInfo) {
        String accessMode = deviceInfo.getAccessMode();
        String ip = deviceInfo.getIp();
        Integer port = deviceInfo.getPort();
        String serialPort = deviceInfo.getSerialPort();
        if (DeviceConnectType.NETWORK.toString().equalsIgnoreCase(accessMode)){
            return PostekPrinterUtils.getStatusNet(ip,port,serialPort);
        }else {
            return PostekPrinterUtils.getStatusUsb(ip,port,serialPort);
        }
    }
}
