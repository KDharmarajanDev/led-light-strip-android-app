package com.ledlightscheduler.arduinopackets.packets;

import com.ledlightscheduler.ledstriputilities.generators.SequentialGenerator;
import com.ledlightscheduler.ledstriputilities.ledstrips.SingleColorLEDStrip;

import java.util.ArrayList;

public class LEDStripsInformationPacket {

    private ArrayList<SingleColorLEDStrip> ledStrips;

    public LEDStripsInformationPacket(){
        ledStrips = new ArrayList<>();
    }

    public LEDStripsInformationPacket(ArrayList<SingleColorLEDStrip> ledStrips){
        this.ledStrips = ledStrips;
    }

    public ArrayList<SingleColorLEDStrip> getLEDStrips(){
        return ledStrips;
    }
}
