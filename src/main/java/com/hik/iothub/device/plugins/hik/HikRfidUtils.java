package com.hik.iothub.device.plugins.hik;

import com.alibaba.fastjson.JSON;
import com.hik.iothub.device.plugins.cykeo.PcUtils;
import com.hiklife.rfidSdk.Model.Rfm.*;
import com.hiklife.rfidSdk.RfmAPI;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

/**
 * 海康桌面读写器开发工具类
 */
public class HikRfidUtils {
    private static final Logger LOG = LoggerFactory.getLogger(HikRfidUtils.class);

    private static RfmAPI INSTANCE = new RfmAPI();
    public static Boolean isConnected = false;

    /**
     *
     * @param comPort
     * @return
     */
    public static boolean connect(String comPort){
        if(isConnected){
            return true;
        }
        ConnectParam connectParam = new ConnectParam();
        connectParam.baudRate = 115200;
        //参数形式 如 COM5
        if(comPort!=null){
            connectParam.com = comPort.replace("COM","");
        } else {
            connectParam.com = "5";
        }
        LOG.info("连接参数{}", JSON.toJSONString(connectParam));

        int ret = INSTANCE.RfmConnect(connectParam, new RfmCallback() {
            @Override
            public void Connected(DeviceInfo deviceInfo) {
                HikRfidUtils.isConnected=true;
                LOG.info("Connected");
            }

            @Override
            public void Disconnected(DeviceInfo deviceInfo) {
                HikRfidUtils.isConnected=false;
                LOG.info("Disconnected");
            }

            @Override
            public void InvReport(ApiResultParamsAsync<InventoryData> apiResultParamsAsync) {
            }

            @Override
            public void MemAccessReport(ApiResultParamsAsync<MemoryAccessData> apiResultParamsAsync) {
            }

            @Override
            public void ErrorReport(ApiResultParamsAsync<EndResult> apiResultParamsAsync) {
            }
        });
        if(ret!=-1){
            isConnected=true;
        }
        return isConnected;
    }

    public static void close(){
        if(isConnected&&INSTANCE!=null){
            INSTANCE.RFMDisconnect(-1);
        }
    }

    /**
     * 读取EPC
     * @return
     */
    public static String readEpc(){
        String epc="";
        ApiParam<ReadParams> apiParam = new ApiParam();

        apiParam.params = new ReadParams();
        apiParam.params.bank = 1; //bank == "EPC" ? 1 : bank == "TID" ? 2 : 3
        apiParam.params.offset = 1;
        apiParam.params.length = 1;

        BigInteger bigint = new BigInteger("00000000", 16);
        apiParam.params.accessPassword = bigint.intValue();

        ApiResultParams<AccessResult> apiResult = new ApiResultParams<>();

        int ret = INSTANCE.RFMRead(apiParam, apiResult);
        if (ret == 0 && apiResult.code == 0) {
            if (apiResult.result.count > 0) {
                //根据pc值 计算epc数据
                String pcdata = apiResult.result.accessResultItemList.get(0).accessData.data;
                int epcLength = getEPCLength(pcdata);
                String epcData = apiResult.result.accessResultItemList.get(0).inventoryData.epc;
                int epcLen = epcData.length();
                if(epcLen>epcLength*4){
                    epc = epcData.substring(0,epcLength*4);
                }else{
                    epc = epcData;
                }
                LOG.info("epc value : {}",epc);
            } else {
                LOG.info("No Tag");
            }
        } else {
            LOG.info("Get Error");
        }
        return epc;
    }

    private static int getEPCLength(String pc){
        switch (pc) {
            case "2000":return 4;
            case "2800": return 5;
            default:return 6;
        }
    }


    /**
     * 写入epc数据
     * 同时写入pc+epc信息
     * @param data
     * @param hexPassword
     * @return
     */
    public static boolean writeEpc(String data,String hexPassword){
        ApiParam<WriteParams> apiParam = new ApiParam();
        apiParam.params = new WriteParams();
        apiParam.params.bank = 1;
        //起始位为1  写入 pc+epc
        apiParam.params.offset = 1;

        if(!StringUtils.isNotEmpty(hexPassword)){
            //为空则给默认值
            hexPassword = "00000000";
        }
        BigInteger bigint = new BigInteger(hexPassword, 16);
        apiParam.params.accessPassword = bigint.intValue();

        String sWriteHexData = data; // 写入数据 （16进制）

        int iWordLen = PcUtils.getValueLen(sWriteHexData);

        // PC值为EPC区域的长度标识（前5个bit标记长度），参考文档说明
        sWriteHexData = PcUtils.getPc(iWordLen) + PcUtils.padLeft(sWriteHexData.toUpperCase(), 4 * iWordLen, '0'); // PC值+数据内容
        LOG.info("PC+EPC {}",sWriteHexData);

        //length为 pc+epc位长度
        apiParam.params.length = 1+iWordLen;
        apiParam.params.data = sWriteHexData;

        ApiResultParams<AccessResult> apiResult = new ApiResultParams<>();
        int ret = INSTANCE.RFMWrite(apiParam, apiResult);
        if (ret == 0 && apiResult.code == 0
                && apiResult.result.accessResultItemList.get(0).accessData.macErrCode.equals(0)
                && apiResult.result.accessResultItemList.get(0).accessData.tagErrCode.equals(0)) {
            LOG.info("Write Successfully："+sWriteHexData);
            return true;
        } else {
            LOG.info("Write failed");
            return false;
        }
    }


    public static void main(String[] args) throws InterruptedException {
        HikRfidUtils.connect("COM5");
        writeEpc("15975694617602048020","00000000");
        String epc = HikRfidUtils.readEpc();
        System.out.println(epc);
    }

}
