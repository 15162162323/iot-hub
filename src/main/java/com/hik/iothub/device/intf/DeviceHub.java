package com.hik.iothub.device.intf;

import com.hik.iothub.bean.DeviceInfo;

public interface DeviceHub {
    /**
     * 打印机方法
     * @param accessMode
     * @param ip
     * @param port
     * @param cmd
     * @return
     */
    public boolean print(String accessMode, String ip, int port,String serialPort, String cmd);

    /**
     * 读写器方法
     * @param serialPort
     */
    public void read(String serialPort);
    public String readSingle(String serialPort);
    public boolean write(String epc,String hexPassword,String serialPort);

    public boolean deviceStatus(DeviceInfo deviceInfo);
}
