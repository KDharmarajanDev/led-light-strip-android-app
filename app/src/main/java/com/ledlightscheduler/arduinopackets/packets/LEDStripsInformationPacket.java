package com.ledlightscheduler.arduinopackets.packets;

import com.ledlightscheduler.MainActivity;
import com.ledlightscheduler.arduinopackets.DeserializerHandler;
import com.ledlightscheduler.ledstriputilities.generators.SequentialGenerator;
import com.ledlightscheduler.ledstriputilities.ledstrips.SingleColorLEDStrip;

import java.util.ArrayList;

public class LEDStripsInformationPacket extends SerialPacket{

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

    public String serialize(){
        StringBuilder builder = new StringBuilder("SET ");
        for(int i = 0; i < ledStrips.size(); i++){
            builder.append(ledStrips.get(i).serialize());
            if(i < ledStrips.size()-1){
                builder.append(",");
            }
        }
        return builder.toString();
    }

    public LEDStripsInformationPacket deserialize(String input){
        DeserializerHandler handler = new DeserializerHandler(input);
        ArrayList<SingleColorLEDStrip> ledStrips = new ArrayList<>();
        String currLEDStrip = handler.getNextItemInBrackets();
        while(currLEDStrip != ""){
            ledStrips.add(SingleColorLEDStrip.deserialize(currLEDStrip));
            currLEDStrip = handler.getNextItemInBrackets();
        }
        return new LEDStripsInformationPacket(ledStrips);
    }

    @Override
    public void handle(MainActivity activity){
        activity.setLEDStrips(ledStrips);
    }
}
