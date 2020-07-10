package com.ledlightscheduler;

import com.ledlightscheduler.ledstriputilities.ledstates.Color;

import org.junit.Test;

import static org.junit.Assert.*;

public class ColorDeserializationUnitTest {
    @Test
    public void black_color__deserialization() {
        assertEquals(Color.deserialize("[0,0,0]"),new Color(0,0,0));
    }

    @Test
    public void white_color_deserialization(){
        assertEquals(Color.deserialize("[255,255,255]"),new Color(255,255,255));
    }

    @Test
    public void red_color_deserialization() {
        assertEquals(Color.deserialize("[255,0,0]"), new Color(255,0,0));
    }

    @Test
    public void green_color_deserialization(){
        assertEquals(Color.deserialize("[0,255,0]"), new Color(0,255,0));
    }

    @Test
    public void blue_color_deserialization(){
        assertEquals(Color.deserialize("[0,0,255]"), new Color(0,0,255));
    }

    @Test
    public void purple_color_deserialization(){
        assertEquals(Color.deserialize("[128,0,128]"), new Color(128,0,128));
    }

}