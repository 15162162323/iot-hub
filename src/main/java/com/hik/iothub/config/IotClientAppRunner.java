package com.hik.iothub.config;

import com.aliyun.openservices.shade.com.alibaba.fastjson.JSONObject;
import com.aliyuncs.utils.IOUtils;
import com.hik.iothub.bean.IotClientConfig;
import com.hik.iothub.bean.MqttConfig;
import com.hik.iothub.bean.ProtocolMode;
import com.hik.iothub.mqtt.MqttHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


/**
 * 根据服务端配置信息，动态初始客户端资源
 * 1.获取当前客户端关联设备信息
 * 2.获取客户端协议模式 MQTT or HTTP
 * 3.根据不同的模式、设备 初始相应的资源依赖
 */
@Component
@Order(10)
public class IotClientAppRunner implements ApplicationRunner {
    private static final Logger LOG = LoggerFactory.getLogger(IotClientAppRunner.class);

    private IotClientConfig clientConfig;

    private MqttHandler mqttHandler;
    private static String CONFIG_FILE="resources/conf/application.properties";


    /**
     * 应用启动后执行此方法
     * @param args
     * url、选择的客户端ID
     * @throws Exception
     */

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String[] sourceArgs = args.getSourceArgs();
        Thread.sleep(2000);
        LOG.info("初始化客户端配置");
        String path=null;
        if(sourceArgs.length>0){
            path = sourceArgs[0];
        }
        this.clientConfig = getClientConfig(path);

        String protocolMode = this.clientConfig.getProtocolMode();
        if (ProtocolMode.MQTT.toString().equalsIgnoreCase(protocolMode)){
            LOG.info("以MQTT模式运行");
            MqttConfig mqttConfig = this.clientConfig.getMqttConfig();
            this.mqttHandler = new MqttHandler(mqttConfig);
            this.mqttHandler.createMqttClient();
        } else if (ProtocolMode.HTTP.toString().equalsIgnoreCase(protocolMode)){
            /**
             * 初始HTTP逻辑 TODO
             */
            LOG.info("以HTTP模式运行");
        }
        LOG.info("客户端启动完成");
    }

    /**
     * 获取配置
     * @param path
     * @return
     */
    private static IotClientConfig getClientConfig(String path){
        LOG.info("从本地配置文件【{}】获取客户端配置参数",CONFIG_FILE);
        if (path != null) {
            CONFIG_FILE = path;
        }
        IotClientConfig iotClientConfig = getClientConfigFromLocal(CONFIG_FILE);
        LOG.info("客户端配置参数：{}",JSONObject.toJSONString(iotClientConfig));
        return iotClientConfig;
    }

    private static IotClientConfig getClientConfigFromLocal(String path){
        //请求服务端获取配置参数、获取客户信息、设备信息、连接模式等
        IotClientConfig iotClientConfig = new IotClientConfig();

        Properties properties = loadProperties(path);

        String protocolMode = properties.getProperty("protocolMode");
        String id = properties.getProperty("id");
        iotClientConfig.setProtocolMode(protocolMode);
        iotClientConfig.setId(id);
        if (protocolMode.equalsIgnoreCase(ProtocolMode.MQTT.toString())){
            String instanceId = properties.getProperty("instanceId");
            String endPoint = properties.getProperty("endPoint");
            String accessKey = properties.getProperty("accessKey");
            String secretKey = properties.getProperty("secretKey");
            String parentTopic = properties.getProperty("parentTopic");
            String qosLevel = properties.getProperty("qosLevel", "0");
            String clientId = properties.getProperty("clientId");
            MqttConfig mqttConfig = new MqttConfig(instanceId, endPoint, accessKey, secretKey, parentTopic, clientId, Integer.parseInt(qosLevel));
            iotClientConfig.setMqttConfig(mqttConfig);
        }
        return iotClientConfig;
    }

    private static Properties loadProperties(String path) {
        Properties properties = new Properties();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(path);
            properties.load(fileInputStream);
        } catch (FileNotFoundException e) {
            LOG.error("文件不存在",e);
        } catch (IOException e) {
            LOG.error("文件读取异常",e);
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }
        return properties;
    }
}
