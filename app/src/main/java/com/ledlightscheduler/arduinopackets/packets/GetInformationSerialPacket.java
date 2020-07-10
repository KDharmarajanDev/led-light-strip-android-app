package com.ledlightscheduler.arduinopackets.packets;

import com.ledlightscheduler.MainActivity;

public class GetInformationSerialPacket extends SerialPacket {

    public GetInformationSerialPacket(){}

    public String serialize(){
        return "GET";
    }

    public GetInformationSerialPacket deserialize(String input){
        return new GetInformationSerialPacket();
    }

    @Override
    public void handle(MainActivity activity){
        activity.sendPacket(new LEDStripsInformationPacket(activity.getLEDStrips()));
    }
}
