package com.hik.iothub.mqtt;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hik.iothub.bean.ActionRequest;
import com.hik.iothub.bean.ActionResponse;
import com.hik.iothub.bean.MqttConfig;
import com.hik.iothub.device.handler.DeviceActionUtils;
import org.apache.commons.lang3.ThreadUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Mqtt消息处理类
 */
public class MqttHandler {
    private final Log LOG = LogFactory.getLog(MqttHandler.class);

    /**
     * 1.MQTT获取监听消息后，将消息体进行拆包，重组，转发至相应的设备适配器，执行设备指令
     * 2.处理完成结果经过消息转发至结果接收方进行消费 ，注意分布式的情况
     * 3.调用方待接收成功消息后，将结果反馈给前端进行后续处理
     */

    private String instanceId;
    private String endPoint;
    private String accessKey;
    private String secretKey;
    private String clientId;
    private String parentTopic;
    private int qosLevel;
    private int port=1883;

    private MqttClient mqttClient;

    private ConnectionOptionWrapper connectionOptionWrapper;

    public MqttHandler() {
    }

    public MqttHandler(MqttConfig mqttConfig) {
        this.instanceId = mqttConfig.getInstanceId();
        this.endPoint = mqttConfig.getEndPoint();
        this.accessKey = mqttConfig.getAccessKey();
        this.secretKey = mqttConfig.getSecretKey();
        this.clientId = mqttConfig.getClientId();
        this.parentTopic = mqttConfig.getParentTopic();
        this.qosLevel = mqttConfig.getQosLevel();
    }

    public MqttHandler(String instanceId, String endPoint, String accessKey, String secretKey, String clientId, String parentTopic, int qosLevel) {
        this.instanceId = instanceId;
        this.endPoint = endPoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.clientId = clientId;
        this.parentTopic = parentTopic;
        this.qosLevel = qosLevel;
    }
    public boolean createMqttClient(){
        try {
            this.connectionOptionWrapper = new ConnectionOptionWrapper(instanceId, accessKey, secretKey, clientId);
            final MemoryPersistence memoryPersistence = new MemoryPersistence();
            //创建客户端连接
            this.mqttClient = new MqttClient("tcp://" + endPoint + ":"+port, clientId, memoryPersistence);
            //设置超时时间
            this.mqttClient.setTimeToWait(10000);
            this.mqttClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    //客户端连接成功
                    LOG.info("MQTT客户端连接成功");
                }
                @Override
                public void connectionLost(Throwable throwable) {
                    throwable.printStackTrace();
                }
                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) {
                    String message = new String(mqttMessage.getPayload());
                    LOG.info("receive msg from topic -->" + topic + " , body is -->" + message);
                    if(topic.contains("p2p")){
                        //消息回调方法
                        //如果是消息数组则遍历指令集
                        if(message.endsWith("]")){
                            List<ActionRequest> actionRequests = JSONArray.parseArray(message, ActionRequest.class);
                            for (ActionRequest actionRequest : actionRequests) {
                                action(actionRequest);
                            }
                        }else{
                            //否则认为是单条指令请求
                            ActionRequest actionRequest = JSONObject.parseObject(message, ActionRequest.class);
                            action(actionRequest);
                        }

                    }
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    LOG.info("send msg succeed topic is <--c: " + iMqttDeliveryToken.getTopics()[0]);
                }
            });
            this.mqttClient.connect(this.connectionOptionWrapper.getMqttConnectOptions());
            LOG.info("MQTT客户端创建成功");
            return true;
        } catch (NoSuchAlgorithmException e) {
            LOG.error("MQTT客户端参数异常",e);
        } catch (InvalidKeyException e) {
            LOG.error("MQTT客户端认证异常",e);
        } catch (MqttSecurityException e) {
            LOG.error("MQTT安全异常",e);
        } catch (MqttException e) {
            LOG.error("MQTT客户端连接异常",e);
        }
        LOG.error("MQTT客户端创建失败");
        return false;
    }

    private void action(ActionRequest actionRequest){
        //参数校验，对actionRequest 对象进行参数校验
        String sourceClientId = actionRequest.getSourceClientId();
        ActionResponse response = DeviceActionUtils.process(actionRequest);
        String responseMessage = JSONObject.toJSONString(response);
        //执行结束后发布响应消息至发送方
        sendResponseMessage(responseMessage,sourceClientId);
    }

    /**
     * 返回消息给发送方
     */
    public void sendResponseMessage(String responseMessage,String sourceClientId){
        final String p2pSendTopic = parentTopic + "/p2p/" + sourceClientId;
        LOG.info("返回消息至topic:"+p2pSendTopic+",msg body "+responseMessage);
        MqttMessage message = new MqttMessage(responseMessage.getBytes());
//        message.setQos(qosLevel);
        try {
            this.mqttClient.publish(p2pSendTopic, message);
        } catch (MqttException e) {
            LOG.error("mqtt 消息发送异常",e);
        }
    }

}
