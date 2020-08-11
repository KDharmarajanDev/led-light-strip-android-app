package com.ledlightscheduler;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.ledlightscheduler.ledstriputilities.generators.RandomGenerator;
import com.ledlightscheduler.ledstriputilities.generators.SequentialGenerator;
import com.ledlightscheduler.ledstriputilities.ledstates.Color;
import com.ledlightscheduler.ledstriputilities.ledstates.LEDState;
import com.ledlightscheduler.ledstriputilities.ledstates.TransitionLEDState;
import com.ledlightscheduler.ledstriputilities.ledstrips.SingleColorLEDStrip;
import com.ledlightscheduler.uimanager.configurationsaver.FileSaverAndLoader;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class FileLoaderAndSaverTest {

    @Test
    public void empty_led_strip_save_test(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ArrayList<SingleColorLEDStrip> ledStrips = new ArrayList<>();
        ledStrips.add(new SingleColorLEDStrip(0,0,0, new ArrayList<>()));
        FileSaverAndLoader.saveLEDStrips(ledStrips, appContext, "empty_led_strip_save_test");
        assertEquals(ledStrips, FileSaverAndLoader.getLEDStrips(appContext, "empty_led_strip_save_test"));
    }

    @Test
    public void empty_gen_led_strip_save_test(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ArrayList<SingleColorLEDStrip> ledStrips = new ArrayList<>();
        ArrayList<SequentialGenerator> generators = new ArrayList<>();
        generators.add(new SequentialGenerator(new ArrayList<>()));
        ledStrips.add(new SingleColorLEDStrip(0,0,0, generators));
        FileSaverAndLoader.saveLEDStrips(ledStrips, appContext, "empty_gen_led_strip_save_test");
        assertEquals(FileSaverAndLoader.getLEDStrips(appContext, "empty_gen_led_strip_save_test"), ledStrips);
    }

    @Test
    public void one_gen_led_strip_save_test(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ArrayList<SingleColorLEDStrip> ledStrips = new ArrayList<>();
        ArrayList<SequentialGenerator> generators = new ArrayList<>();
        ArrayList<LEDState> states = new ArrayList<>();
        states.add(new LEDState());
        generators.add(new SequentialGenerator(states));
        ledStrips.add(new SingleColorLEDStrip(0,0,0, generators));
        FileSaverAndLoader.saveLEDStrips(ledStrips, appContext, "one_gen_led_strip_save_test");
        assertEquals(FileSaverAndLoader.getLEDStrips(appContext, "one_gen_led_strip_save_test"), ledStrips);
    }

    @Test
    public void two_gen_led_strip_save_test(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ArrayList<SingleColorLEDStrip> ledStrips = new ArrayList<>();
        ArrayList<SequentialGenerator> generators = new ArrayList<>();
        ArrayList<LEDState> ledStates = new ArrayList<>();
        ledStates.add(new LEDState(new Color(255,0,0),1000));
        generators.add(new SequentialGenerator(ledStates));
        generators.add(new SequentialGenerator(ledStates));
        ledStrips.add(new SingleColorLEDStrip(0,0,0, generators));
        FileSaverAndLoader.saveLEDStrips(ledStrips, appContext, "two_gen_led_strip_save_test");
        assertEquals(FileSaverAndLoader.getLEDStrips(appContext, "two_gen_led_strip_save_test"), ledStrips);
    }

    @Test
    public void three_gen_led_strip_save_test_with_different_pins(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ArrayList<SingleColorLEDStrip> ledStrips = new ArrayList<>();
        ArrayList<SequentialGenerator> generators = new ArrayList<>();
        ArrayList<LEDState> ledStates = new ArrayList<>();
        ledStates.add(new LEDState(new Color(255,0,0),1000));
        generators.add(new SequentialGenerator(ledStates));
        generators.add(new SequentialGenerator(ledStates));
        generators.add(new SequentialGenerator(ledStates));
        ledStrips.add(new SingleColorLEDStrip(1,2,3, generators));
        FileSaverAndLoader.saveLEDStrips(ledStrips, appContext, "three_gen_led_strip_save_test_with_different_pins");
        assertEquals(FileSaverAndLoader.getLEDStrips(appContext, "three_gen_led_strip_save_test_with_different_pins"), ledStrips);
    }

    @Test
    public void random_seq_gen_led_strip_save_test(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ArrayList<SingleColorLEDStrip> ledStrips = new ArrayList<>();
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

        ledStrips.add(new SingleColorLEDStrip(3,7,9, generators));
        FileSaverAndLoader.saveLEDStrips(ledStrips, appContext, "random_seq_gen_led_strip_save_test");
        assertEquals(FileSaverAndLoader.getLEDStrips(appContext, "random_seq_gen_led_strip_save_test"), ledStrips);
    }

    @Test
    public void two_random_seq_gen_led_strip_save_test(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ArrayList<SingleColorLEDStrip> ledStrips = new ArrayList<>();
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

        ledStrips.add(new SingleColorLEDStrip(3,7,9, generators));
        ledStrips.add(new SingleColorLEDStrip(0,1,1, generators));

        FileSaverAndLoader.saveLEDStrips(ledStrips, appContext, "two_random_seq_gen_led_strip_save_test");
        assertEquals(FileSaverAndLoader.getLEDStrips(appContext, "two_random_seq_gen_led_strip_save_test"), ledStrips);
    }
}
