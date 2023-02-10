//package com.hik.iothub.device.plugins.postek;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.UnsupportedEncodingException;
//import java.lang.reflect.Method;
//import java.util.*;
//
//public class PostekPrinterUtils_BAK {
//    private static final Logger LOG = LoggerFactory.getLogger(PostekPrinterUtils_BAK.class);
//
//    static CDFPSKDll cdfpskdll = CDFPSKDll.Instance;
//
//
//    private static final String dataName="status:";
//    /**
//     * @param ip
//     * @param port 默认9100
//     * @param cmd  打印指令
//     */
//    public static boolean printNet(String ip, int port, String cmd) {
//        List<Map<String, String>> newList = new ArrayList<>();
//        Map mapHead = new HashMap<>();
//        mapHead.put("PTK_Connect_Timer", ip + "," + port + ",2");
//        newList.add(mapHead);
//        List<Map<String, String>> cmdList = format(cmd);
//        newList.addAll(cmdList);
//
//
//        Map mapPrintLabel = new HashMap();
//        mapPrintLabel.put("PTK_PrintLabel", "1,1");
//        Map mapEnd = new HashMap();
//        mapEnd.put("PTK_CloseConnect", "");
//        newList.add(mapPrintLabel);
//        newList.add(mapEnd);
//
//        String jsonCmd = JSON.toJSONString(newList);
//        /*连接打印机*/
//        boolean print = print(jsonCmd);
//        return print;
//    }
//
//    public static boolean printUsb(String ip, int port, int usbPort, String cmd) {
//        List<Map<String, String>> newList = new ArrayList<>();
//        Map mapHead = new HashMap<>();
//        mapHead.put("PTK_OpenUSBPort", usbPort);
//        newList.add(mapHead);
//
//        List<Map<String, String>> cmdList = format(cmd);
//        newList.addAll(cmdList);
//
//        Map mapPrintLabel = new HashMap();
//        mapPrintLabel.put("PTK_PrintLabel", "1,1");
//        Map mapEnd = new HashMap();
//        mapEnd.put("PTK_CloseUSBPort", "");
//        newList.add(mapPrintLabel);
//        newList.add(mapEnd);
//
//        String jsonCmd = JSON.toJSONString(newList);
//        /*连接打印机*/
//        boolean print = print(jsonCmd);
//        return print;
//    }
//
//    public static boolean getStatusNet(String ip, int port,String usbPort){
//
//        List<Map<String, String>> newList = new ArrayList<>();
//        Map mapHead = new HashMap<>();
//        mapHead.put("PTK_Connect_Timer", ip + "," + port + ",5");
//        newList.add(mapHead);
//
//        Map mapFeedback = new HashMap();
//        mapFeedback.put("PTK_FeedBack", "");
//        newList.add(mapFeedback);
//
//        Map mapReadData = new HashMap();
//        mapReadData.put("PTK_ReadData", dataName+",4");
//        newList.add(mapReadData);
//
//        Map mapEnd = new HashMap();
//        mapEnd.put("PTK_CloseConnect", "");
//        newList.add(mapEnd);
//
//        String jsonCmd = JSON.toJSONString(newList);
//        /*获取打印机状态*/
//        return getStatus(jsonCmd);
//    }
//
//    public static boolean getStatusUsb(String ip, int port,String usbPort){
//
//        List<Map<String, String>> newList = new ArrayList<>();
//        Map mapHead = new HashMap<>();
//        mapHead.put("PTK_OpenUSBPort", usbPort);
//        newList.add(mapHead);
//
//        Map mapUsbStatus = new HashMap();
//        mapUsbStatus.put("PTK_ErrorReport_USBInterrupt", dataName);
//
//        newList.add(mapUsbStatus);
//
//        Map mapEnd = new HashMap();
//        mapEnd.put("PTK_CloseUSBPort", "");
//        newList.add(mapEnd);
//
//        String jsonCmd = JSON.toJSONString(newList);
//        /*获取打印机状态*/
//        return getStatus(jsonCmd);
//    }
//
////    /**
////     * 执行打印指令
////     * @param url
////     * @param cmd
////     * @return
////     */
////    private static boolean print(String url, String cmd) {
////        RestTemplate client = new RestTemplate(getClientHttpRequestFactory(5000,5000));
////        HttpHeaders headers = new HttpHeaders();
////        HttpMethod method = HttpMethod.POST;
////        // 以表单的方式提交
////        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
////        //client.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
////
////        //封装请求参数
////        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
////        params.add("reqParam", "1");
////        params.add("printparams", cmd);
////
////        //将请求头部和参数合成一个请求
////        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
////
////        //执行HTTP请求，将返回的结构使用JSONObject类格式化
////        //ResponseEntity<JSONObject> response =client.exchange(url, method, requestEntity, JSONObject.class);
////        //执行HTTP请求，将返回的结构使用String类格式化
////        try {
////            ResponseEntity<String> response = client.exchange(url, method, requestEntity, String.class);
////            Map mapobject = JSON.parseObject(response.getBody(), Map.class);
////            String retval = mapobject.get("retval").toString();
////            return "0".equalsIgnoreCase(retval);
////        } catch (RestClientException e) {
////            e.printStackTrace();
////        }
////        return false;
////    }
//
//    private static boolean print(String cmd) {
//        String res = executePrintFromCDFPSKDll(cmd);
//        System.out.println(res);
//        Map mapobject = JSON.parseObject(res, Map.class);
//        String retval = mapobject.get("retval").toString();
//        return "0".equalsIgnoreCase(retval);
//    }
//
//    private static boolean getStatus(String cmd){
//        String res = executePrintFromCDFPSKDll(cmd);
//        Map mapobject = JSON.parseObject(res, Map.class);
//        String retval = mapobject.get("retval").toString();
//        if("0".equalsIgnoreCase(retval)){
//            Object receiveData = mapobject.get("ReceiveData");
//            String data = String.valueOf(receiveData);
//            String statusCode = data.replace(dataName, "");
//            LOG.info("打印机状态码:{},{}",statusCode,getStatusMsg(statusCode));
//            return true;
//        }
//        return false;
//    }
//
//    private static String executePrintFromCDFPSKDll(String printparams) {
//        System.setProperty("jna.encoding", "UTF-8");
//        String retMsg = "";
//        String receiveData = "";
//        int retVal = 0;
//        try {
//            if (printparams != null && !"".equals(printparams)) {
//                Class<?> clz = CDFPSKDll.class;
//                JSONArray jsonArray = JSONArray.parseArray(printparams);
//                for (Object o : jsonArray) {
//                    JSONObject obj = (JSONObject)o;
//                    Iterator<String> it = obj.keySet().iterator();
//                    while (it.hasNext()) {
//                        String key = it.next();
//                        String paramvalues = obj.get(key).toString();
//                        if (paramvalues != null) {
//                            Method m = null;
//                            Method[] methods = clz.getMethods();
//                            byte b;
//                            int n;
//                            Method[] arrayOfMethod1 = methods;
//                            for (Method method : arrayOfMethod1) {
//                                if (method.getName().equals(key))
//                                    m = method;
//                            }
//                            Class[] parameterTypes = null;
//                            try {
//                                parameterTypes = m.getParameterTypes();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            if (parameterTypes == null) {
//                                retVal = -1;
//                                boolean containtspace = key.contains(" ");
//                                if (containtspace) {
//                                    retMsg = String.valueOf(retMsg) + key + "contains space,please delete the space and try again.";
//                                    continue;
//                                }
//                                retMsg = String.valueOf(retMsg) + key + "not found,please check!";
//                                continue;
//                            }
//                            Object[] parameters = new Object[parameterTypes.length];
//                            String[] paramArray = paramvalues.split(",");
//                            for (int k = 0; k < paramArray.length; k++) {
//                                if (paramArray[k] != null && paramArray[k].length() > 2 && paramArray[k].startsWith("@") &&
//                                        paramArray[k].endsWith("@")) {
//                                    String paramTemp = paramArray[k].substring(1, paramArray[k].length() - 1);
////                                    paramArray[k] = getServletConfig().getServletContext().getRealPath(paramTemp);
//                                }
//                            }
//                            int pt = parameterTypes.length;
//                            int p = paramArray.length;
//                            if (p < pt) {
//                                retVal = -1;
//                                retMsg = String.valueOf(retMsg) + key + ":Missing parameters";
//                                continue;
//                            }
//                            int pp = p - pt;
//                            int acceptIndex = -1;
//                            String dataName = "";
//                            String dataName2 = "";
//                            byte[] data = new byte[10240];
//                            byte[] data2 = new byte[10240];
//                            int dataNum = 0;
//                            int[] data_int = new int[2];
//                            if (pp > 0 && parameterTypes != null && parameterTypes.length != 0) {
//                                for (int i1 = 0; i1 < pp; i1++)
//                                    paramArray[pt - 1] = String.valueOf(paramArray[pt - 1]) + "," + paramArray[pt + i1];
//                                if (key.equals("PTK_SendString"))
//                                    if (paramArray[pt - 1].startsWith("#") && paramArray[pt - 1].endsWith("#")) {
//                                        paramArray[pt - 1] = paramArray[pt - 1].substring(1, paramArray[pt - 1].length() - 1);
//                                    } else {
//                                        retVal = -1;
//                                        retMsg = String.valueOf(retMsg) + key + "--: " + "The command should start with # and end with #";
//                                        continue;
//                                    }
//                            }
//                            for (int j = 0; j < parameterTypes.length; j++) {
//                                if ("int".equals(parameterTypes[j].getSimpleName())) {
//                                    paramArray[j] = paramArray[j].replaceAll(" ", "");
//                                    try {
//                                        float toFloat = Float.valueOf(paramArray[j]).floatValue();
//                                        parameters[j] = Integer.valueOf(Math.round(toFloat));
//                                    } catch (Exception e) {
//                                        retVal = -1;
//                                        retMsg = String.valueOf(retMsg) + key + "--: " + e.getMessage().replaceAll("\\\"", "\\\\\"") + ", The parameter type must be a number" + "\\r\\n";
//                                    }
//                                } else if ("String".equals(parameterTypes[j].getSimpleName())) {
//                                    try {
//                                        parameters[j] = String.valueOf(paramArray[j]);
//                                    } catch (Exception e) {
//                                        System.out.println("字符串转换失败"+ e.getMessage());
//                                                retVal = -1;
//                                        retMsg = String.valueOf(retMsg) + key + "--: " + e.getMessage().replaceAll("\\\"", "\\\\\"");
//                                    }
//                                } else if ("char".equals(parameterTypes[j].getSimpleName())) {
//                                    paramArray[j] = paramArray[j].replaceAll(" ", "");
//                                    try {
//                                        parameters[j] = Character.valueOf(paramArray[j].charAt(0));
//                                    } catch (Exception e) {
//                                        retVal = -1;
//                                        retMsg = String.valueOf(retMsg) + key + "--: " + e.getMessage().replaceAll("\\\"", "\\\\\"") + ", The parameter type must be a char" + "\\r\\n";
//                                    }
//                                } else if ("short".equals(parameterTypes[j].getSimpleName())) {
//                                    paramArray[j] = paramArray[j].replaceAll(" ", "");
//                                    try {
//                                        parameters[j] = Short.valueOf(paramArray[j]);
//                                    } catch (Exception e) {
//                                        retVal = -1;
//                                        retMsg = String.valueOf(retMsg) + key + "--: " + e.getMessage().replaceAll("\\\"", "\\\\\"") + ", The parameter type must be a number" + "\\r\\n";
//                                    }
//                                } else if ("float".equals(parameterTypes[j].getSimpleName())) {
//                                    paramArray[j] = paramArray[j].replaceAll(" ", "");
//                                    try {
//                                        parameters[j] = Float.valueOf(paramArray[j]);
//                                    } catch (Exception e) {
//                                        retVal = -1;
//                                        retMsg = String.valueOf(retMsg) + key + "--: " + e.getMessage().replaceAll("\\\"", "\\\\\"") + ", The parameter type must be a float" + "\\r\\n";
//                                    }
//                                } else if ("byte[]".equals(parameterTypes[j].getSimpleName())) {
//                                    if (dataNum > 0) {
//                                        parameters[j] = data2;
//                                        dataName2 = paramArray[j];
//                                    } else {
//                                        parameters[j] = data;
//                                        dataName = paramArray[j];
//                                        acceptIndex = 1;
//                                    }
//                                    dataNum++;
//                                } else if ("int[]".equals(parameterTypes[j].getSimpleName())) {
//                                    parameters[j] = data_int;
//                                    dataName = paramArray[j];
//                                    acceptIndex = 2;
//                                } else if ("boolean".equals(parameterTypes[j].getSimpleName())) {
//                                    paramArray[j] = paramArray[j].replaceAll(" ", "");
//                                    if ("false".equals(paramArray[j])) {
//                                        parameters[j] = Boolean.valueOf(false);
//                                    } else if ("true".equals(paramArray[j])) {
//                                        parameters[j] = Boolean.valueOf(true);
//                                    } else {
//                                        parameters[j] = Boolean.valueOf(false);
//                                    }
//                                }
//                            }
//                            if (parameterTypes == null || parameterTypes.length == 0) {
//                                int retValTemp = Integer.valueOf(m.invoke(cdfpskdll, new Object[0]).toString()).intValue();
//                                retVal += retValTemp;
//                                if (retValTemp != 0)
//                                    retMsg = String.valueOf(retMsg) + key + "--: " + retValTemp + "  " + getErrorInfo(retValTemp) + "\\r\\n";
//                            } else {
//                                int retValTemp = 0;
//                                try {
//                                    retValTemp = Integer.valueOf(m.invoke(cdfpskdll, parameters).toString()).intValue();
//                                    retVal += retValTemp;
//                                    if (retValTemp != 0)
//                                        retMsg = String.valueOf(retMsg) + key + "--: " + retValTemp + "  " + getErrorInfo(retValTemp) + "\\r\\n";
//                                } catch (Exception e) {
//                                    retVal = -1;
//                                    continue;
//                                }
//                            }
//                            if (acceptIndex == 1) {
//                                String Data_UTF = GBK_to_UTF((new String(data, "GBK")).trim());
//                                receiveData = String.valueOf(receiveData) + dataName + Data_UTF;
//                                receiveData = receiveData.replace("$acceptMsg$", "");
//                                acceptIndex = -1;
//                            }
//                            if (dataNum == 2) {
//                                String Data_UTF = GBK_to_UTF((new String(data2, "GBK")).trim());
//                                receiveData = String.valueOf(receiveData) + dataName2 + Data_UTF;
//                                acceptIndex = -1;
//                            }
//                            if (acceptIndex == 2) {
//                                receiveData = String.valueOf(receiveData) + dataName + data_int[0];
//                                acceptIndex = -1;
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException e) {
//            closePort(printparams);
//            e.printStackTrace();
//            retVal = -1;
//            retMsg = String.valueOf(retMsg) + e.getMessage().replaceAll("\\\"", "\\\\\"");
//        } catch (UnsupportedEncodingException e) {
//            closePort(printparams);
//            e.printStackTrace();
//            retVal = -1;
//            retMsg = String.valueOf(retMsg) + e.getMessage().replaceAll("\\\"", "\\\\\"");
//        }
//        retMsg = printMessage(retVal, retMsg, receiveData);
//        return retMsg;
//    }
//
//    private static String printMessage(int retVal, String retMsg, String receiveData) {
//        return "{\"retval\":\"" + retVal + "\",\"msg\":\"" + retMsg + "\",\"ReceiveData\":\"" + receiveData + "\"}";
//    }
//
//    private static String getErrorInfo(int errorCode) {
//        String information = "";
//        byte[] errorInfo = new byte[2048];
//        int result = CDFPSKDll.Instance.PTK_GetErrorInfo(errorCode, errorInfo, 2048);
//        if (result == 0) {
//            try {
//                information = GBK_to_UTF((new String(errorInfo, "GBK")).trim());
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        } else if (result == -1) {
//            information = "解析错误代码失败,没有该错误代码";
//        } else if (result == 4) {
//            information = "错误信息过长无法显示";
//        } else {
//            information = "无法解析错误码返回值："+ result;
//        }
//        return information;
//    }
//
//    public static String GBK_to_UTF(String gbkStr) {
//        try {
//            return new String(getUTF8BytesFromGBKString(gbkStr), "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            throw new InternalError();
//        }
//    }
//
//    public static byte[] getUTF8BytesFromGBKString(String gbkStr) {
//        int n = gbkStr.length();
//        byte[] utfBytes = new byte[3 * n];
//        int k = 0;
//        for (int i = 0; i < n; i++) {
//            int m = gbkStr.charAt(i);
//            if (m < 128 && m >= 0) {
//                utfBytes[k++] = (byte)m;
//            } else {
//                utfBytes[k++] = (byte)(0xE0 | m >> 12);
//                utfBytes[k++] = (byte)(0x80 | m >> 6 & 0x3F);
//                utfBytes[k++] = (byte)(0x80 | m & 0x3F);
//            }
//        }
//        if (k < utfBytes.length) {
//            byte[] tmp = new byte[k];
//            System.arraycopy(utfBytes, 0, tmp, 0, k);
//            return tmp;
//        }
//        return utfBytes;
//    }
//
//    private static void closePort(String parameter) {
//        if (parameter.contains("PTK_OpenLogMode")) {
//            cdfpskdll.PTK_CloseLogMode();
//        } else if (parameter.contains("PTK_OpenUSBPort")) {
//            cdfpskdll.PTK_CloseUSBPort();
//        } else if (parameter.contains("PTK_OpenSerialPort")) {
//            cdfpskdll.PTK_CloseSerialPort();
//        } else if (parameter.contains("PTK_OpenPrinter")) {
//            cdfpskdll.PTK_ClosePrinter();
//        } else if (parameter.contains("PTK_OpenParallelPort")) {
//            cdfpskdll.PTK_CloseParallelPort();
//        } else if (parameter.contains("PTK_OpenTextPort")) {
//            cdfpskdll.PTK_CloseTextPort();
//        } else if (parameter.contains("PTK_Connect")) {
//            cdfpskdll.PTK_CloseConnect();
//        } else if (parameter.contains("PTK_Connect_Timer")) {
//            cdfpskdll.PTK_CloseConnect();
//        } else if (parameter.contains("PTK_OpenUSBPort_Buff")) {
//            cdfpskdll.PTK_CloseBuffPort();
//        } else if (parameter.contains("PTK_Connect_Timer_Buff")) {
//            cdfpskdll.PTK_CloseBuffPort();
//        } else if (parameter.contains("PTK_OpenPrinter_Buff")) {
//            cdfpskdll.PTK_CloseBuffPort();
//        } else if (parameter.contains("OpenPort")) {
//            cdfpskdll.ClosePort();
//        }
//    }
//
//
////    private static boolean getStatus(String url, String cmd) {
////        RestTemplate client = new RestTemplate(getClientHttpRequestFactory(2000,2000));
////        HttpHeaders headers = new HttpHeaders();
////        HttpMethod method = HttpMethod.POST;
////        // 以表单的方式提交
////        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
////        //client.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
////
////        //封装请求参数
////        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
////        params.add("reqParam", "1");
////        params.add("printparams", cmd);
////
////        //将请求头部和参数合成一个请求
////        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
////
////        //执行HTTP请求，将返回的结构使用JSONObject类格式化
////        //执行HTTP请求，将返回的结构使用String类格式化
////        try {
////            ResponseEntity<String> response = client.exchange(url, method, requestEntity, String.class);
////            Map mapobject = JSON.parseObject(response.getBody(), Map.class);
////            String retval = mapobject.get("retval").toString();
////            if("0".equalsIgnoreCase(retval)){
////                Object receiveData = mapobject.get("ReceiveData");
////                String data = String.valueOf(receiveData);
////                String statusCode = data.replace(dataName, "");
////                LOG.info("打印机状态码:{},{}",statusCode,getStatusMsg(statusCode));
////                return true;
////            }
////        } catch (RestClientException e) {
////            LOG.warn("打印机网络请求异常,请检查连接参数或设备服务状态");
////            LOG.error(e.getMessage());
////        }
////        return false;
////    }
//
//
//    /**
//     * 转换博思得指令
//     *
//     * @param cmd
//     * @return
//     */
//    private static List<Map<String, String>> format(String cmd) {
//        List<Map<String, String>> list = new ArrayList<>();
//        if(StringUtils.isNotEmpty(cmd)){
//            JSONArray objects = JSONArray.parseArray(cmd);
//            for (Object object : objects) {
//                JSONObject obj = (JSONObject) object;
//                for (Map.Entry<String, Object> stringObjectEntry : obj.entrySet()) {
//                    Map<String, String> map = new HashMap<>();
//                    String key = stringObjectEntry.getKey();
//                    Object value = stringObjectEntry.getValue();
//                    if (key != null) {
//                        map.put(key, String.valueOf(value));
//                    }
//                    list.add(map);
//                }
//            }
//        }
//        return list;
//    }
//
//    private static String getStatusMsg(String msg){
//        String statusStr = msg.replace("W10", "");
//        int i = Integer.parseInt(statusStr);
//        String statusMsg = "未知错误";
//        switch (i){
//            case 0: statusMsg="无错误";break;
//            case 1: statusMsg="语法错误";break;
//            case 4: statusMsg="正在打印中";break;
//            case 82: statusMsg="碳带检测出错";break;
//            case 83: statusMsg="标签检测出错";break;
//            case 86: statusMsg="切刀检测出错";break;
//            case 87: statusMsg="打印头未关闭";break;
//            case 88: statusMsg="暂停状态";break;
//            case 108: statusMsg="设置RFID写数据的方式和内容区域执行失败，输入参数错误";break;
//            case 109: statusMsg="RFID标签写入数据失败，已达到重试次数";break;
//            case 110: statusMsg="写入RFID数据失败，但未超过重试次数";break;
//            case 111: statusMsg="RFID标签校准失败";break;
//            case 112: statusMsg="设置RFID读取数据的方式和内容区域执行失败，输入参数错误";break;
//            case 116: statusMsg="读取RFID标签数据失败";break;
//            default: statusMsg="未知错误";break;
//        }
//        return statusMsg;
//    }
//
////    private static ClientHttpRequestFactory getClientHttpRequestFactory(int readTimeout,int connectTimeout){
////        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
////        //读取超时5秒,默认无限限制,单位：毫秒
////        factory.setReadTimeout(readTimeout);
////        //连接超时10秒，默认无限制，单位：毫秒
////        factory.setConnectTimeout(connectTimeout);
////        return factory;
////    }
//
//
//
//    public static void main(String[] args) {
////        JSONObject jsonObject = new JSONObject();
////        jsonObject.put("cmd", tempCmd2);
////        System.out.println(jsonObject.toJSONString());
////        printUsb("127.0.0.1","888",255,tempCmd2);
////        postek_print.PTK_Connect_Timer("192.168.0.200", 9100,5);
//        boolean b = printNet("192.168.0.200", 9100, tempCmd2);
////        boolean b = printUsb("192.168.0.200", 9100,255, tempCmd2);
//
//        boolean statusNet = getStatusNet("192.168.0.200", 9100, "255");
//        System.out.println(statusNet);
//    }
//
//    static String tempCmd2 = "[{\"PTK_ClearBuffer\":\"\"},{\"PTK_SetDarkness\":20},{},{\"PTK_SetDirection\":\"B\"},{\"PTK_SetLabelHeight\":\"900,24,0,false\"},{\"PTK_SetLabelWidth\":1200},{\"PTK_DrawRectangle\":\"23,5,4,817,204\"},{\"PTK_DrawLineOr\":\"24,51,791,4\"},{\"PTK_DrawLineOr\":\"25,87,791,4\"},{\"PTK_DrawLineOr\":\"24,126,791,4\"},{\"PTK_DrawLineOr\":\"24,163,359,4\"},{\"PTK_DrawLineOr\":\"381,51,4,151\"},{\"PTK_DrawLineOr\":\"573,52,4,77\"},{\"PTK_DrawLineOr\":\"171,53,4,150\"},{\"PTK_DrawText_TrueType\":\"286,6,44,0,Arial,1,400,0,0,0,资产管理中心\"},{\"PTK_DrawText_TrueType\":\"54,57,25,0,黑体,1,400,0,0,0,使用部门\"},{\"PTK_DrawText_TrueType\":\"227,57,25,0,黑体,1,400,0,0,0,财务部\"},{\"PTK_DrawText_TrueType\":\"54,94,25,0,黑体,1,400,0,0,0,资产编号\"},{\"PTK_DrawText_TrueType\":\"54,133,25,0,黑体,1,400,0,0,0,购买时间\"},{\"PTK_DrawText_TrueType\":\"54,168,25,0,黑体,1,400,0,0,0,启用时间\"},{\"PTK_DrawText_TrueType\":\"426,61,25,0,黑体,1,400,0,0,0,资产类型\"},{\"PTK_DrawText_TrueType\":\"217,97,25,0,黑体,1,400,0,0,0,A01252951\"},{\"PTK_DrawText_TrueType\":\"431,97,25,0,黑体,1,400,0,0,0,资产名称\"},{\"PTK_DrawText_TrueType\":\"624,95,25,0,黑体,1,400,0,0,0,彩色打印机\"},{\"PTK_DrawText_TrueType\":\"210,133,25,0,黑体,1,400,0,0,0,2021-07-22\"},{\"PTK_DrawText_TrueType\":\"210,170,25,0,黑体,1,400,0,0,0,2021-07-29\"},{\"PTK_DrawText_TrueType\":\"624,57,25,0,黑体,1,400,0,0,0,办公用品\"},{\"PTK_RWRFIDLabel\":\"1,0,0,4,1,12345706\"},{\"PTK_DrawText_TrueType\":\"429,148,25,0,黑体,1,400,0,0,0,使用人与保管人：王二\"}]";
////    String a="{\"cmd\":\"[{\\\"PTK_ClearBuffer\\\":\\\"\\\"},{\\\"PTK_SetDarkness\\\":20},{},{\\\"PTK_SetDirection\\\":\\\"B\\\"},{\\\"PTK_SetLabelHeight\\\":\\\"900,24,0,false\\\"},{\\\"PTK_SetLabelWidth\\\":1200},{\\\"PTK_DrawRectangle\\\":\\\"23,5,4,817,204\\\"},{\\\"PTK_DrawLineOr\\\":\\\"24,51,791,4\\\"},{\\\"PTK_DrawLineOr\\\":\\\"25,87,791,4\\\"},{\\\"PTK_DrawLineOr\\\":\\\"24,126,791,4\\\"},{\\\"PTK_DrawLineOr\\\":\\\"24,163,359,4\\\"},{\\\"PTK_DrawLineOr\\\":\\\"381,51,4,151\\\"},{\\\"PTK_DrawLineOr\\\":\\\"573,52,4,77\\\"},{\\\"PTK_DrawLineOr\\\":\\\"171,53,4,150\\\"},{\\\"PTK_DrawText_TrueType\\\":\\\"286,6,44,0,Arial,1,400,0,0,0,资产管理中心\\\"},{\\\"PTK_DrawText_TrueType\\\":\\\"54,57,25,0,黑体,1,400,0,0,0,使用部门\\\"},{\\\"PTK_DrawText_TrueType\\\":\\\"227,57,25,0,黑体,1,400,0,0,0,财务部\\\"},{\\\"PTK_DrawText_TrueType\\\":\\\"54,94,25,0,黑体,1,400,0,0,0,资产编号\\\"},{\\\"PTK_DrawText_TrueType\\\":\\\"54,133,25,0,黑体,1,400,0,0,0,购买时间\\\"},{\\\"PTK_DrawText_TrueType\\\":\\\"54,168,25,0,黑体,1,400,0,0,0,启用时间\\\"},{\\\"PTK_DrawText_TrueType\\\":\\\"426,61,25,0,黑体,1,400,0,0,0,资产类型\\\"},{\\\"PTK_DrawText_TrueType\\\":\\\"217,97,25,0,黑体,1,400,0,0,0,A01252951\\\"},{\\\"PTK_DrawText_TrueType\\\":\\\"431,97,25,0,黑体,1,400,0,0,0,资产名称\\\"},{\\\"PTK_DrawText_TrueType\\\":\\\"624,95,25,0,黑体,1,400,0,0,0,彩色打印机\\\"},{\\\"PTK_DrawText_TrueType\\\":\\\"210,133,25,0,黑体,1,400,0,0,0,2021-07-22\\\"},{\\\"PTK_DrawText_TrueType\\\":\\\"210,170,25,0,黑体,1,400,0,0,0,2021-07-29\\\"},{\\\"PTK_DrawText_TrueType\\\":\\\"624,57,25,0,黑体,1,400,0,0,0,办公用品\\\"},{\\\"PTK_RWRFIDLabel\\\":\\\"1,0,0,4,1,12345706\\\"},{\\\"PTK_DrawText_TrueType\\\":\\\"429,148,25,0,黑体,1,400,0,0,0,使用人与保管人：王二\\\"},{\\\"PTK_PrintLabel\\\":\\\"1,1\\\"}]\"}\n"
//}
