package com.hik.iothub.device.handler;

import com.alibaba.fastjson.JSONObject;
import com.hik.iothub.bean.*;
import com.hik.iothub.device.adaptee.CykeoReaderAdaptee;
import com.hik.iothub.device.adaptee.HikReaderAdaptee;
import com.hik.iothub.device.adaptee.PostekPrinterAdaptee;
import com.hik.iothub.device.adaptee.ZebraPrinterAdaptee;
import com.hik.iothub.device.adapter.DeviceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;

/**
 * 请求接收后进行后处理并分发至相应的适配器进行执行
 */
public class DeviceActionUtils {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceActionUtils.class);

    public static ActionResponse process(ActionRequest actionRequest) {
        String requestId = actionRequest.getMessageId();
        //指令信息
        ActionInfo actionInfo = actionRequest.getActionInfo();
        //设备信息
        DeviceInfo deviceInfo = actionRequest.getDeviceInfo();

        //组装参数
        String action = actionInfo.getAction();
        //判断action类型
        ActionResponse response = new ActionResponse(requestId);

        //设备连接方式  打印机为NETWORK或USB 、读写器为USB默认使用serialPort参数
        String accessMode = deviceInfo.getAccessMode();
        String ip = deviceInfo.getIp();
        Integer port = deviceInfo.getPort();
        String serialPort = deviceInfo.getSerialPort();
        //设备类型，暂未使用后续可作为扩展参数（同厂商多设备的情况）
        String deviceType = deviceInfo.getDeviceType();
        //设备厂商
        String deviceMake = deviceInfo.getDeviceMake();
        //根据品牌选择适配器
        DeviceAdapter adapter = null;
        try {
            adapter = getAdapter(deviceMake);
            //打印机
            if (ActionTypeEnum.PRINTER_PRINT.name().equals(action)) {
                String params = actionInfo.getParams();
                //生成打印指令，处理请求参数
                String cmd = adapter.formatParams(params);
                LOG.info(cmd);
                try {
                    TimeUnit.MILLISECONDS.sleep(1200);
                } catch (InterruptedException e) {
                    LOG.error("sleep中断异常",e);
                }
                boolean print = adapter.print(accessMode, ip, port, serialPort, cmd);
                response.setCode(200);
                response.setSuccess(print);
                response.setMsg(print ? "打印成功" : "打印失败");
            }
            //读写器，同步单读
            if (ActionTypeEnum.RFID_READER_READ.name().equals(action)) {
                String epc = adapter.readSingle(serialPort);
                response.setCode(200);
                response.setData(epc);
                boolean res = epc != null;
                response.setSuccess(res);
                response.setMsg(res ? "读取成功" : "读取失败");
            }
            //读写器，同步写入epc数据
            if (ActionTypeEnum.RFID_READER_WRITE.name().equals(action)) {
                String params = actionInfo.getParams();
                JSONObject object = JSONObject.parseObject(params);

                String epc = object.getString("epc");
                String password = object.getString("password");//十六进制
                boolean write = adapter.write(epc, password, serialPort);
                response.setCode(200);
                response.setSuccess(write);
                response.setMsg(write ? "写入成功" : "写入失败");
            }
        } catch (Exception e) {
            LOG.error("获取设备适配器异常",e);
            response.setSuccess(false);
            response.setCode(200);
            response.setMsg("获取设备适配器异常");
        }
        return response;
    }


    private static DeviceAdapter getAdapter(String deviceMake) throws Exception {
        //根据设备类型和设备厂家选择对于的适配器
        DeviceAdapter deviceAdapter = null;
        if (DeviceMakeEnum.POSTEK.name().equals(deviceMake)) {
            PostekPrinterAdaptee postekPrinterAdaptee = new PostekPrinterAdaptee();
            deviceAdapter = new DeviceAdapter(postekPrinterAdaptee);
        }
        else if (DeviceMakeEnum.ZEBRA.name().equals(deviceMake)) {
            ZebraPrinterAdaptee zebraPrinterAdaptee = new ZebraPrinterAdaptee();
            deviceAdapter = new DeviceAdapter(zebraPrinterAdaptee);
        }
        else if (DeviceMakeEnum.CYKEO.name().equals(deviceMake)) {
            CykeoReaderAdaptee cykeoReaderAdaptee = new CykeoReaderAdaptee();
            deviceAdapter = new DeviceAdapter(cykeoReaderAdaptee);
        }
        else if (DeviceMakeEnum.HIK.name().equals(deviceMake)) {
            HikReaderAdaptee hikReaderAdaptee = new HikReaderAdaptee();
            deviceAdapter = new DeviceAdapter(hikReaderAdaptee);
        }else {
            LOG.error("{} 此设备未适配,请确认配置信息",deviceMake);
            throw new Exception("未适配的设备"+deviceMake);
        }
        return deviceAdapter;
    }

    /**
     * 获取设备状态
     * @return
     */
    public static Boolean deviceStatus(DeviceInfo deviceInfo){
        //根据参数判断 设备类型
        //检查返回设备状态
        String deviceMake = deviceInfo.getDeviceMake();
        DeviceAdapter adapter = null;
        try {
            adapter = getAdapter(deviceMake);
            return adapter.deviceStatus(deviceInfo);
        } catch (Exception e) {
            LOG.error("获取设备适配器异常",e);
            return false;
        }
    }
}
