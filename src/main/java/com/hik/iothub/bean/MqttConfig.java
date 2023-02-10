package com.hik.iothub.bean;

public class MqttConfig {

    private String instanceId;
    private String endPoint;
    private String accessKey;
    private String secretKey;
    private String parentTopic;
    private String clientId;
    private Integer qosLevel=0;

    public MqttConfig() {
    }


    public MqttConfig(String instanceId, String endPoint, String accessKey, String secretKey, String parentTopic, String clientId, Integer qosLevel) {
        this.instanceId = instanceId;
        this.endPoint = endPoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.parentTopic = parentTopic;
        this.clientId = clientId;
        this.qosLevel = qosLevel;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getParentTopic() {
        return parentTopic;
    }

    public void setParentTopic(String parentTopic) {
        this.parentTopic = parentTopic;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Integer getQosLevel() {
        return qosLevel;
    }

    public void setQosLevel(Integer qosLevel) {
        this.qosLevel = qosLevel;
    }
}
