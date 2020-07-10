package com.ledlightscheduler;

import com.ledlightscheduler.ledstriputilities.generators.RandomGenerator;
import com.ledlightscheduler.ledstriputilities.generators.SequentialGenerator;
import com.ledlightscheduler.ledstriputilities.ledstates.Color;
import com.ledlightscheduler.ledstriputilities.ledstates.LEDState;
import com.ledlightscheduler.ledstriputilities.ledstates.TransitionLEDState;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class GeneratorDeserializationUnitTest {

    @Test
    public void empty_generator_serialization_test(){
        assertEquals(SequentialGenerator.deserialize("[0]"), new SequentialGenerator(new ArrayList<>()));
    }

    @Test
    public void one_empty_led_state_in_gen_serialization_test(){
        ArrayList<LEDState> ledStates = new ArrayList<>();
        ledStates.add(new LEDState());
        assertEquals(SequentialGenerator.deserialize("[0,[0,[0,0,0]]]"), new SequentialGenerator(ledStates));
    }

    @Test
    public void red_one_sec_led_state_in_gen_serialization_test(){
        ArrayList<LEDState> ledStates = new ArrayList<>();
        ledStates.add(new LEDState(new Color(255,0,0),1000));
        assertEquals(SequentialGenerator.deserialize("[0,[1000,[255,0,0]]]"), new SequentialGenerator(ledStates));
    }

    @Test
    public void two_red_one_sec_led_state_in_gen_serialization_test(){
        ArrayList<LEDState> ledStates = new ArrayList<>();
        ledStates.add(new LEDState(new Color(255,0,0),1000));
        ledStates.add(new LEDState(new Color(255,0,0),1000));
        assertEquals(SequentialGenerator.deserialize("[0,[1000,[255,0,0]],[1000,[255,0,0]]]"), new SequentialGenerator(ledStates));
    }

    @Test
    public void red_one_sec_blue_two_sec_gen_serialization_test(){
        ArrayList<LEDState> ledStates = new ArrayList<>();
        ledStates.add(new LEDState(new Color(255,0,0),1000));
        ledStates.add(new LEDState(new Color(0,0,255),2000));
        assertEquals(SequentialGenerator.deserialize("[0,[1000,[255,0,0]],[2000,[0,0,255]]]"), new SequentialGenerator(ledStates));
    }

    @Test
    public void red_one_sec_blue_two_sec_purple_three_sec_gen_serialization_test(){
        ArrayList<LEDState> ledStates = new ArrayList<>();
        ledStates.add(new LEDState(new Color(255,0,0),1000));
        ledStates.add(new LEDState(new Color(0,0,255),2000));
        ledStates.add(new LEDState(new Color(128,0,128),3000));
        assertEquals(SequentialGenerator.deserialize("[0,[1000,[255,0,0]],[2000,[0,0,255]],[3000,[128,0,128]]]"), new SequentialGenerator(ledStates));
    }

    @Test
    public void random_empty_generator_serialization_test(){
        assertEquals(SequentialGenerator.deserialize("[1]"), new RandomGenerator(new ArrayList<>()));
    }

    //Random Generator Testing
    @Test
    public void random_one_empty_led_state_in_gen_serialization_test(){
        ArrayList<LEDState> ledStates = new ArrayList<>();
        ledStates.add(new LEDState());
        assertEquals(SequentialGenerator.deserialize("[1,[0,[0,0,0]]]"), new RandomGenerator(ledStates));
    }

    @Test
    public void random_red_one_sec_led_state_in_gen_serialization_test(){
        ArrayList<LEDState> ledStates = new ArrayList<>();
        ledStates.add(new LEDState(new Color(255,0,0),1000));
        assertEquals(SequentialGenerator.deserialize("[1,[1000,[255,0,0]]]"), new RandomGenerator(ledStates));
    }

    @Test
    public void random_two_red_one_sec_led_state_in_gen_serialization_test(){
        ArrayList<LEDState> ledStates = new ArrayList<>();
        ledStates.add(new LEDState(new Color(255,0,0),1000));
        ledStates.add(new LEDState(new Color(255,0,0),1000));
        assertEquals(SequentialGenerator.deserialize("[1,[1000,[255,0,0]],[1000,[255,0,0]]]"), new RandomGenerator(ledStates));
    }

    @Test
    public void random_red_one_sec_blue_two_sec_gen_serialization_test(){
        ArrayList<LEDState> ledStates = new ArrayList<>();
        ledStates.add(new LEDState(new Color(255,0,0),1000));
        ledStates.add(new LEDState(new Color(0,0,255),2000));
        assertEquals(SequentialGenerator.deserialize("[1,[1000,[255,0,0]],[2000,[0,0,255]]]"), new RandomGenerator(ledStates));
    }

    @Test
    public void random_red_one_sec_blue_two_sec_purple_three_sec_gen_serialization_test(){
        ArrayList<LEDState> ledStates = new ArrayList<>();
        ledStates.add(new LEDState(new Color(255,0,0),1000));
        ledStates.add(new LEDState(new Color(0,0,255),2000));
        ledStates.add(new LEDState(new Color(128,0,128),3000));
        assertEquals(SequentialGenerator.deserialize("[1,[1000,[255,0,0]],[2000,[0,0,255]],[3000,[128,0,128]]]"), new RandomGenerator(ledStates));
    }

    //TransitionLEDState Introduced

    @Test
    public void trans_red_one_sec_led_state_in_gen_serialization_test(){
        ArrayList<LEDState> ledStates = new ArrayList<>();
        ledStates.add(new TransitionLEDState(new Color(255,0,0),new Color(255,0,0),1000));
        assertEquals(SequentialGenerator.deserialize("[0,[1000,[255,0,0],[255,0,0]]]"), new SequentialGenerator(ledStates));
    }

    @Test
    public void trans_two_red_one_sec_led_state_in_gen_serialization_test(){
        ArrayList<LEDState> ledStates = new ArrayList<>();
        ledStates.add(new TransitionLEDState(new Color(255,0,0),new Color(255,0,0),1000));
        ledStates.add(new TransitionLEDState(new Color(255,0,0),new Color(255,0,0),1000));
        assertEquals(SequentialGenerator.deserialize("[0,[1000,[255,0,0],[255,0,0]],[1000,[255,0,0],[255,0,0]]]"), new SequentialGenerator(ledStates));
    }

    @Test
    public void trans_red_one_sec_blue_two_sec_gen_serialization_test(){
        ArrayList<LEDState> ledStates = new ArrayList<>();
        ledStates.add(new TransitionLEDState(new Color(255,0,0),new Color(255,0,0),1000));
        ledStates.add(new TransitionLEDState(new Color(0,0,255),new Color(0,0,255),2000));
        assertEquals(SequentialGenerator.deserialize("[0,[1000,[255,0,0],[255,0,0]],[2000,[0,0,255],[0,0,255]]]"), new SequentialGenerator(ledStates));
    }

    @Test
    public void trans_red_one_sec_blue_two_sec_purple_three_sec_gen_serialization_test(){
        ArrayList<LEDState> ledStates = new ArrayList<>();
        ledStates.add(new LEDState(new Color(255,0,0),1000));
        ledStates.add(new LEDState(new Color(0,0,255),2000));
        ledStates.add(new LEDState(new Color(128,0,128),3000));
        assertEquals(SequentialGenerator.deserialize("[0,[1000,[255,0,0]],[2000,[0,0,255]],[3000,[128,0,128]]]"), new SequentialGenerator(ledStates));
    }

    //TransitionLEDState Random Generator Testing
    @Test
    public void trans_random_red_one_sec_led_state_in_gen_serialization_test(){
        ArrayList<LEDState> ledStates = new ArrayList<>();
        ledStates.add(new TransitionLEDState(new Color(255,0,0),new Color(255,0,0),1000));
        assertEquals(SequentialGenerator.deserialize("[1,[1000,[255,0,0],[255,0,0]]]"), new RandomGenerator(ledStates));
    }

    @Test
    public void trans_random_two_red_one_sec_led_state_in_gen_serialization_test(){
        ArrayList<LEDState> ledStates = new ArrayList<>();
        ledStates.add(new TransitionLEDState(new Color(255,0,0),new Color(255,0,0),1000));
        ledStates.add(new TransitionLEDState(new Color(255,0,0),new Color(255,0,0),1000));
        assertEquals(SequentialGenerator.deserialize("[1,[1000,[255,0,0],[255,0,0]],[1000,[255,0,0],[255,0,0]]]"), new RandomGenerator(ledStates));
    }

    @Test
    public void trans_random_red_one_sec_blue_two_sec_gen_serialization_test(){
        ArrayList<LEDState> ledStates = new ArrayList<>();
        ledStates.add(new TransitionLEDState(new Color(255,0,0),new Color(255,0,0),1000));
        ledStates.add(new TransitionLEDState(new Color(0,0,255),new Color(0,0,255),2000));
        assertEquals(SequentialGenerator.deserialize("[1,[1000,[255,0,0],[255,0,0]],[2000,[0,0,255],[0,0,255]]]"), new RandomGenerator(ledStates));
    }

    @Test
    public void trans_random_red_one_sec_blue_two_sec_purple_three_sec_gen_serialization_test(){
        ArrayList<LEDState> ledStates = new ArrayList<>();
        ledStates.add(new LEDState(new Color(255,0,0),1000));
        ledStates.add(new LEDState(new Color(0,0,255),2000));
        ledStates.add(new LEDState(new Color(128,0,128),3000));
        assertEquals(SequentialGenerator.deserialize("[1,[1000,[255,0,0]],[2000,[0,0,255]],[3000,[128,0,128]]]"), new RandomGenerator(ledStates));
    }

}
