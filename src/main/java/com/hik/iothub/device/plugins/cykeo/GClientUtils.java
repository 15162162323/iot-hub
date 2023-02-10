package com.hik.iothub.device.plugins.cykeo;

import com.gg.reader.api.dal.GClient;
import com.gg.reader.api.dal.HandlerTagEpcLog;
import com.gg.reader.api.dal.HandlerTagEpcOver;
import com.gg.reader.api.protocol.gx.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RFID读写工具类
 */
public class GClientUtils {
    private static final Logger LOG = LoggerFactory.getLogger(GClientUtils.class);
    private final static GClient client = new GClient();
    private static String DEFAULT_SERIALPORT = "COM5";
    private static String tempEpc = "0";

    public static void setTempEpc(String epc) {
        GClientUtils.tempEpc = epc;
        LOG.info("读取成功:" + epc);
    }
    public static String getTempEpc() {
        return GClientUtils.tempEpc;
    }

    /**
     * 建立连接方法，判断是否已建立连接
     *
     * @param serialPort 串口名称，根据客户端实际串口定义 //COM5
     * @return
     */
    public static boolean connect(String serialPort) {
        String comSerPort = serialPort+":11520";
        Boolean isConnected = client.getConnectType() == 0;
        if (client != null && !isConnected) {
            isConnected = client.openSerial(comSerPort, 2000);
        }
        LOG.info("读写器设备连接状态:{}",isConnected);
        return isConnected;
    }

    public static boolean getDeviceStatus(String serialPort){
        boolean status = connect(serialPort);
        LOG.info("获取读写器设备状态:{}",status);
        return status;
    }

    /**
     * 单读
     *
     * @return
     */
    public static String readSingle() {
        return readSingle(null);
    }

    /**
     * 单读
     *
     * @param waitTime 单独间隔时间默认300ms
     * @return
     */
    public static String readSingle(Long waitTime) {
        if (waitTime == null) {
            waitTime = 300l;
        }
        String code = "";
        subscribeHandlerSingle(client);

        MsgBaseInventoryEpc msgBaseInventoryEpc = new MsgBaseInventoryEpc();
        msgBaseInventoryEpc.setAntennaEnable(EnumG.AntennaNo_1);
        //设置单读模式
        msgBaseInventoryEpc.setInventoryMode(EnumG.InventoryMode_Single);
//            client.sendUnsynMsg(msgBaseInventoryEpc);
        client.sendSynMsg(msgBaseInventoryEpc);

        if (0x00 == msgBaseInventoryEpc.getRtCode()) {
            code = "0";
            LOG.info("MsgBaseInventoryEpc[OK].");
        } else {
            code = "1";
            LOG.info(msgBaseInventoryEpc.getRtMsg());
        }
        try {
            //间隔时间
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return code;
    }


    public static String close() {
        String code = "";
        MsgBaseStop stopMsg = new MsgBaseStop();
        client.sendSynMsg(stopMsg);
        LOG.info("stopMsg.getRtCode()==>" + stopMsg.getRtCode());

        if (0 == stopMsg.getRtCode()) {
            code = "0";
            LOG.info("Stop successful.");
        } else {
            code = "1";
            LOG.info("Stop error.");
        }

        LOG.info("Close the connection");
        //client.close();//关闭连接
        return code;
    }


    /**
     * 订阅6c标签信息上报
     * 使用WebSocket方式上报数据
     *
     * @param client
     * @param toUserId
     */
    private static void subscribeHandler(GClient client, String toUserId) {
        client.onTagEpcLog = new HandlerTagEpcLog() {
            @Override
            public void log(String s, LogBaseEpcInfo logBaseEpcInfo) {
                if (null != logBaseEpcInfo && logBaseEpcInfo.getResult() == 0) {
                    System.out.println("read..." + logBaseEpcInfo);
                    if (toUserId != null) {
//                        try {
//                            WebSocketServer.sendInfo(JSON.toJSONString(logBaseEpcInfo), toUserId);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    }
                }
            }
        };

        client.onTagEpcOver = new HandlerTagEpcOver() {
            @Override
            public void log(String s, LogBaseEpcOver logBaseEpcOver) {
                System.out.println("HandlerTagEpcOver");
            }
        };

    }


    private static void subscribeHandlerSingle(GClient client) {
        client.onTagEpcLog = new HandlerTagEpcLog() {
            @Override
            public void log(String s, LogBaseEpcInfo logBaseEpcInfo) {
                if (null != logBaseEpcInfo && logBaseEpcInfo.getResult() == 0) {
                    System.out.println(logBaseEpcInfo);
                    String epc = logBaseEpcInfo.getEpc();
                    if (StringUtils.isNotEmpty(epc)) {
                        GClientUtils.setTempEpc(epc);
                    }
                }
            }
        };
    }

    /**
     * 写数据
     *
     * @param epc
     * @return 0/1 成功/失败
     */
    public static String write(String epc, String hexPassword) {
        String code = "";
        // 停止指令，空闲态
        MsgBaseStop msgBaseStop = new MsgBaseStop();
        client.sendSynMsg(msgBaseStop);

        if (0 == msgBaseStop.getRtCode()) {
            System.out.println("Stop successful.");
        } else {
            System.out.println("Stop error.");
        }

        MsgBaseWriteEpc msg = new MsgBaseWriteEpc();
        msg.setAntennaEnable(EnumG.AntennaNo_1);
        //字起始地址 第0个为CRC，不可写
        msg.setStart(1);//word
        //写EPC，数据默认为 hex 432
        msg.setArea(EnumG.WriteArea_Epc);
        String sWriteHexData = epc; // 写入数据 （16进制）

        int iWordLen = PcUtils.getValueLen(sWriteHexData);

        // PC值为EPC区域的长度标识（前5个bit标记长度），参考文档说明
        sWriteHexData = PcUtils.getPc(iWordLen) + PcUtils.padLeft(sWriteHexData.toUpperCase(), 4 * iWordLen, '0'); // PC值+数据内容
//            System.out.println(sWriteHexData);
        msg.setHexWriteData(sWriteHexData);
        // 若需要写入带特殊编码数据，请自行进行编码并使用 "BwriteData" 属性。
        // msg.setBwriteData(HexUtils.hexString2Bytes(sWriteHexData));
        client.sendSynMsg(msg);
        if (0 == msg.getRtCode()) {
            code = "0";
            System.out.println("Write successful.");
        } else {
            code = "1";
            System.out.println(msg.getRtMsg());
        }
        return code;
    }

    public static void main(String[] args) {
//        1585070697569837058
        connect("COM8:115200");
        write("15850706975698370580",null);
//        String s = readSingle(300l);
//        System.out.println(s);
//        try {
//            Thread.sleep(200);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("epc::" + GClientUtils.tempEpc);
        close();
    }

}
