package com.ledlightscheduler.arduinopackets.packets;

public class GetInformationSerialPacket extends SerialPacket {

    boolean isGettingAllInformation;

    public GetInformationSerialPacket(){

    }

    public GetInformationSerialPacket(boolean isGettingAllInformation){
        this.isGettingAllInformation = isGettingAllInformation;
    }

    public String serialize(){
        return "";
    }

    public GetInformationSerialPacket deserialize(String input){
        return null;
    }

}
