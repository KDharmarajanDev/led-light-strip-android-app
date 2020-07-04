package com.ledlightscheduler.arduinopackets.packets;

public abstract class SerialPacket {

    public abstract String serialize();

    public abstract SerialPacket deserialize(String input);
}