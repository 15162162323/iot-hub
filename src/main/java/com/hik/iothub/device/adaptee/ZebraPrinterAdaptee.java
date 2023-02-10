package com.hik.iothub.device.adaptee;

import com.hik.iothub.bean.DeviceConnectType;
import com.hik.iothub.bean.DeviceInfo;
import com.hik.iothub.device.plugins.postek.PostekPrinterUtils;
import com.hik.iothub.device.plugins.zebra.ZebraPrinterUtils;
import org.apache.commons.lang3.StringUtils;

public class ZebraPrinterAdaptee  implements IDeviceAdaptee {
    private static int defaultNetPort = 9100;
    private static int serialPort = 255;
    @Override
    public boolean print( String connectType,String ip, int port,String serialPort,String cmd) {
        /**
         * 判断打印方式
         */
        if(StringUtils.equalsIgnoreCase(connectType, DeviceConnectType.NETWORK.toString())){
            return ZebraPrinterUtils.printNet(ip,port,cmd,ZebraPrinterUtils.UTF8);
        }else{
            return ZebraPrinterUtils.printUsb(serialPort,cmd,ZebraPrinterUtils.UTF8);
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
        //斑马打印机命令动态替换
        String retCmd=params;

        return retCmd;
    }

    @Override
    public Boolean status(DeviceInfo deviceInfo) {
        String accessMode = deviceInfo.getAccessMode();
        String ip = deviceInfo.getIp();
        Integer port = deviceInfo.getPort();
        String serialPort = deviceInfo.getSerialPort();
        if (DeviceConnectType.NETWORK.toString().equalsIgnoreCase(accessMode)){
            return ZebraPrinterUtils.getStatusNet(ip,port);
        }else {
            return ZebraPrinterUtils.getStatusUsb(serialPort);
        }
    }
}
