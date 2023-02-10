package com.hik.iothub.device.adaptee;


import com.hik.iothub.bean.DeviceInfo;
import com.hik.iothub.device.plugins.cykeo.GClientUtils;
import com.hik.iothub.device.plugins.hik.HikRfidUtils;

public class HikReaderAdaptee implements IDeviceAdaptee {

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
        //连接的串口名称，从远端配置中获取  "COM8"
        HikRfidUtils.connect(serialPort);
        String epc = HikRfidUtils.readEpc();
//        HikRfidUtils.close();
        return epc;
    }

    /**
     * 循环读取
     * 盘点
     */
    @Override
    public void read(String serialPort) {

    }
    @Override
    public boolean write(String epc,String hexPassword,String serialPort) {
        HikRfidUtils.connect(serialPort);
        boolean success = HikRfidUtils.writeEpc(epc, hexPassword);
//        HikRfidUtils.close();
        return success;
    }

    @Override
    public String formatParams(String params) {
        return null;
    }

    @Override
    public Boolean status(DeviceInfo deviceInfo) {
        String serialPort = deviceInfo.getSerialPort();
        if (serialPort != null) {
            return HikRfidUtils.connect(serialPort);
        }
        return false;
    }
}
