package com.ledlightscheduler;

import com.ledlightscheduler.ledstriputilities.generators.RandomGenerator;
import com.ledlightscheduler.ledstriputilities.generators.SequentialGenerator;
import com.ledlightscheduler.ledstriputilities.ledstates.Color;
import com.ledlightscheduler.ledstriputilities.ledstates.LEDState;
import com.ledlightscheduler.ledstriputilities.ledstates.TransitionLEDState;
import com.ledlightscheduler.ledstriputilities.ledstrips.SingleColorLEDStrip;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class LEDStripDeserializationUnitTest {

    @Test
    public void empty_led_strip_serialization_test(){
        assertEquals(SingleColorLEDStrip.deserialize("[0,0,0]"), new SingleColorLEDStrip(0,0,0, new ArrayList<>()));
    }

    @Test
    public void empty_gen_led_strip_serialization_test(){
        ArrayList<SequentialGenerator> generators = new ArrayList<>();
        generators.add(new SequentialGenerator(new ArrayList<>()));
        assertEquals(SingleColorLEDStrip.deserialize("[0,0,0,[0]]"), new SingleColorLEDStrip(0,0,0, generators));
    }

    @Test
    public void one_gen_led_strip_serialization_test(){
        ArrayList<SequentialGenerator> generators = new ArrayList<>();
        ArrayList<LEDState> states = new ArrayList<>();
        states.add(new LEDState());
        generators.add(new SequentialGenerator(states));
        assertEquals(SingleColorLEDStrip.deserialize("[0,0,0,[0,[0,[0,0,0]]]]"), new SingleColorLEDStrip(0,0,0, generators));
    }

    @Test
    public void two_gen_led_strip_serialization_test(){
        ArrayList<SequentialGenerator> generators = new ArrayList<>();
        ArrayList<LEDState> ledStates = new ArrayList<>();
        ledStates.add(new LEDState(new Color(255,0,0),1000));
        generators.add(new SequentialGenerator(ledStates));
        generators.add(new SequentialGenerator(ledStates));
        assertEquals(SingleColorLEDStrip.deserialize("[0,0,0,[0,[1000,[255,0,0]]],[0,[1000,[255,0,0]]]]"), new SingleColorLEDStrip(0,0,0, generators));
    }

    @Test
    public void three_gen_led_strip_serialization_test_with_different_pins(){
        ArrayList<SequentialGenerator> generators = new ArrayList<>();
        ArrayList<LEDState> ledStates = new ArrayList<>();
        ledStates.add(new LEDState(new Color(255,0,0),1000));
        generators.add(new SequentialGenerator(ledStates));
        generators.add(new SequentialGenerator(ledStates));
        generators.add(new SequentialGenerator(ledStates));
        assertEquals(SingleColorLEDStrip.deserialize("[1,2,3,[0,[1000,[255,0,0]]],[0,[1000,[255,0,0]]],[0,[1000,[255,0,0]]]]"), new SingleColorLEDStrip(1,2,3, generators));
    }

    @Test
    public void random_seq_gen_led_strip_serialization_test(){
        ArrayList<SequentialGenerator> generators = new ArrayList<>();
        //Gen1
        ArrayList<LEDState> ledStates = new ArrayList<>();
        ledStates.add(new LEDState(new Color(255,0,0),1000));
        ledStates.add(new LEDState(new Color(0,0,255),2000));
        ledStates.add(new LEDState(new Color(128,0,128),3000));
        generators.add(new RandomGenerator(ledStates));

        //Gen2
        ArrayList<LEDState> ledStates2 = new ArrayList<>();
        ledStates2.add(new TransitionLEDState(new Color(255,0,0),new Color(255,0,0),1000));
        ledStates2.add(new TransitionLEDState(new Color(255,0,0),new Color(255,0,0),1000));
        generators.add(new RandomGenerator(ledStates2));

        //Gen3
        ArrayList<LEDState> ledStates3 = new ArrayList<>();
        ledStates3.add(new TransitionLEDState(new Color(255,0,0),new Color(255,0,0),1000));
        generators.add(new SequentialGenerator(ledStates3));

        assertEquals(SingleColorLEDStrip.deserialize("[3,7,9,[1,[1000,[255,0,0]],[2000,[0,0,255]],[3000,[128,0,128]]],[1,[1000,[255,0,0],[255,0,0]],[1000,[255,0,0],[255,0,0]]],[0,[1000,[255,0,0],[255,0,0]]]]")
                , new SingleColorLEDStrip(3,7,9, generators));
    }
}
