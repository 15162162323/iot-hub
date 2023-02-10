package com.hik.iothub.device.adaptee;


import com.hik.iothub.bean.DeviceInfo;
import com.hik.iothub.device.plugins.cykeo.GClientUtils;

public class CykeoReaderAdaptee implements IDeviceAdaptee {

    @Override
    public boolean print(String connectType, String ip, int port,String serialPort, String cmd) {
        return false;
    }

    /**
     * 单读
     * @return
     */
    @Override
    public String readSingle(String serialPort) {
        //连接的串口名称，从远端配置中获取  "COM8:115200"
        GClientUtils.connect(serialPort);
        GClientUtils.readSingle();
        String tempEpc = GClientUtils.getTempEpc();
        return tempEpc;
    }

    /**
     * 循环读取
     */
    @Override
    public void read(String serialPort) {

    }
    @Override
    public boolean write(String epc,String hexPassword,String serialPort) {
        GClientUtils.connect(serialPort);
        String write = GClientUtils.write(epc, hexPassword);
        GClientUtils.close();
        return write.equals("0")?true:false;
    }

    @Override
    public String formatParams(String params) {
        return null;
    }

    @Override
    public Boolean status(DeviceInfo deviceInfo) {
        String serialPort = deviceInfo.getSerialPort();
        if (serialPort != null) {
            return GClientUtils.getDeviceStatus(serialPort);
        }
        return false;
    }
}
