package com.hik.iothub.bean;

import com.alibaba.fastjson.JSONArray;

import java.util.Map;

public class ActionInfo {
    private String action;  //打印（PRINTER_PRINT）、读卡（RFID_READER_READ）、写卡（RFID_READER_WRITE）
    private String params;  //jsonString格式

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    //    //读写卡，单 读卡则无参数，写卡参数如下
//--"params":{
//        "epc":"",   //写入的epc值
//                "password":"",  //如有则写入
//    }
////打印，根据cmd指令、和替换的变量内容，解析并生成最终的打印指令， 是否动态传参经实际验证后再定
//--"params":{
//        "cmd":"",  // 打印指令
//                "variable":[{"0":"名称","1":"编号"}],  //占位符替换内容k、v形式
//    }
}
