package com.hik.iothub.device.adaptee;

import com.hik.iothub.bean.DeviceInfo;

public interface IDeviceAdaptee {
    /**
     * 打印机方法
     * @param connectType
     * @param ip
     * @param port
     * @param cmd
     * @return
     */
    public boolean print(String connectType, String ip, int port,String serialPort, String cmd);

    /**
     * 读写器方法
     * @param serialPort
     */
    public void read(String serialPort);
    public String readSingle(String serialPort);
    public boolean write(String epc,String hexPassword,String serialPort);

    public String formatParams(String params);

    public Boolean status(DeviceInfo deviceInfo);
}
