package com.ledlightscheduler;

import com.ledlightscheduler.ledstriputilities.ledstates.Color;
import com.ledlightscheduler.ledstriputilities.ledstates.LEDState;
import com.ledlightscheduler.ledstriputilities.ledstates.TransitionLEDState;

import org.junit.Test;

import static org.junit.Assert.*;

public class LEDStateDeserializationUnitTest {

    @Test
    public void zero_duration_blue_deserialization_test(){
        assertEquals(LEDState.deserialize("[0,[0,0,255]]"),new LEDState(new Color(0,0,255),0));
    }

    @Test
    public void two_second_duration_white_deserialization_test(){
        assertEquals(LEDState.deserialize("[2000,[255,255,255]]"),new LEDState(new Color(255,255,255),2000));
    }

    @Test
    public void three_second_duration_purple_deserialization_test(){
        assertEquals(LEDState.deserialize("[3000,[128,0,128]]"),new LEDState(new Color(128,0,128),3000));
    }

    @Test
    public void empty_led_state_deserialization(){
        assertEquals(LEDState.deserialize("[0,[0,0,0]]"), new LEDState());
    }

    @Test
    public void twenty_second_duration_green_deserialization_test(){
        assertEquals(LEDState.deserialize("[20000,[0,255,0]]"),new LEDState(new Color(0,255,0),20000));
    }

    //TransitionLEDState Testing
    @Test
    public void trans_zero_duration_blue_deserialization_test(){
        assertEquals(LEDState.deserialize("[0,[0,0,255],[0,0,255]]"),new TransitionLEDState(
                new Color(0,0,255),new Color(0,0,255),0));
    }

    @Test
    public void trans_two_second_duration_white_deserialization_test(){
        assertEquals(LEDState.deserialize("[2000,[255,255,255],[255,255,255]]"),new TransitionLEDState(
                new Color(255,255,255),new Color(255,255,255),2000));
    }

    @Test
    public void trans_three_second_duration_purple_deserialization_test(){
        assertEquals(LEDState.deserialize("[3000,[128,0,128],[128,0,128]]"),new TransitionLEDState(
                new Color(128,0,128),new Color(128,0,128),3000));
    }

    @Test
    public void trans_twenty_second_duration_green_deserialization_test(){
        assertEquals(LEDState.deserialize("[20000,[0,255,0],[0,255,0]]")
                ,new TransitionLEDState(new Color(0,255,0),new Color(0,255,0),20000));
    }
}