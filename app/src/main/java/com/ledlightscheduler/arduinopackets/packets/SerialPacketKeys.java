package com.ledlightscheduler.arduinopackets.packets;

public enum SerialPacketKeys {

    GET_INFORMATION("GET_INFORMATION", GetInformationSerialPacket.class);

    private Class<? extends SerialPacket> packetClass;
    private String key;

    SerialPacketKeys(String key, Class<? extends SerialPacket> packetClass){
        this.key = key;
        this.packetClass = packetClass;
    }

    public String getKey(){
        return key;
    }

    public Class<? extends SerialPacket> getPacketClass(){
        return packetClass;
    }

}
