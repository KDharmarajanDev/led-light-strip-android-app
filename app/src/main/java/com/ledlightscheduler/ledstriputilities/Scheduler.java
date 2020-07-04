package com.ledlightscheduler.ledstriputilities;

import com.ledlightscheduler.ledstriputilities.ledstates.Color;
import com.ledlightscheduler.ledstriputilities.ledstates.LEDState;
import com.ledlightscheduler.ledstriputilities.ledstrips.SingleColorLEDStrip;

public class Scheduler {

    private SingleColorLEDStrip ledStrip;

    public Scheduler(SingleColorLEDStrip ledStrip){
        this.ledStrip = ledStrip;
    }

    public SingleColorLEDStrip getLEDStrip(){
        return ledStrip;
    }

    public Color update(){
         long currentTime = System.currentTimeMillis();
         LEDState currState = ledStrip.getSequentialGenerator().getCurrentLEDState();
         if(ledStrip.getSequentialGenerator().getStartTime() + currState.getDuration() < currentTime){
             currState = ledStrip.getSequentialGenerator().getNextState();
         }
         return currState.getColor(currentTime - ledStrip.getSequentialGenerator().getStartTime());
    }

}
