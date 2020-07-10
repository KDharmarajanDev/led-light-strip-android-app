package com.ledlightscheduler.arduinopackets.packets;

import com.ledlightscheduler.MainActivity;

public abstract class SerialPacket {

    public abstract String serialize();

    public abstract SerialPacket deserialize(String input);

    public abstract void handle(MainActivity activity);
}