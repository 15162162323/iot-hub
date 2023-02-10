package com.hik.iothub.controller;

import com.alibaba.fastjson.JSON;
import com.hik.iothub.bean.ActionRequest;
import com.hik.iothub.bean.ActionResponse;
import com.hik.iothub.bean.DeviceInfo;
import com.hik.iothub.device.handler.DeviceActionUtils;
import com.hik.iothub.device.plugins.hik.HikRfidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * iothub 服务API
 *
 * 提供标准统一的接口规范
 */
@RestController
@RequestMapping("/iothub/api")
public class HubApiController {
    private static final Logger LOG = LoggerFactory.getLogger(HubApiController.class);
    private HikRfidUtils hikRfidUtils= new HikRfidUtils();
    /**
     * 查询服务状态 正常则为UP  异常为DOWN
     * @return
     */
    @GetMapping("/health")
    public ResponseEntity<String> health(){
        //可添加其它逻辑判断服务是否正常
        return ResponseEntity.ok("UP");
    }

    @PostMapping("/health/device")
    public ResponseEntity deviceStatus(@RequestBody List<DeviceInfo> deviceInfoList){
        //检查连接设备状态，后续扩展设备状态检查接口，探测设备连接状态，并返回
        for (DeviceInfo deviceInfo : deviceInfoList) {
            Boolean deviceStatus = DeviceActionUtils.deviceStatus(deviceInfo);
            deviceInfo.setStatus(deviceStatus);
        }
        return ResponseEntity.ok(deviceInfoList);
    }

    /**
     * 指令执行接口、打印、读卡、写卡
     * @param actionRequest
     * @return
     */
    @PostMapping("/action/execute")
    public ResponseEntity<ActionResponse> action(@RequestBody ActionRequest actionRequest){
        LOG.info("请求参数: {}",JSON.toJSONString(actionRequest));
        ActionResponse response = DeviceActionUtils.process(actionRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public ResponseEntity<String> read(){
        //可添加其它逻辑判断服务是否正常
        hikRfidUtils.connect("5");
//        String epc = hikRfidUtils.readEpc(2, 6, "EPC");
        return ResponseEntity.ok("epc");
    }

}
