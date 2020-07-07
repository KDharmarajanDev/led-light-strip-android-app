package com.ledlightscheduler.arduinopackets.packets;

public class GetInformationSerialPacket extends SerialPacket {

    public GetInformationSerialPacket(){}

    public String serialize(){
        return "GET";
    }

    public GetInformationSerialPacket deserialize(String input){
        if(input.contains("GET")){
            return new GetInformationSerialPacket();
        }
        return null;
    }

}
