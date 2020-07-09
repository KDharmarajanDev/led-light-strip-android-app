package com.ledlightscheduler;

import com.ledlightscheduler.ledstriputilities.ledstates.Color;

import org.junit.Test;

import static org.junit.Assert.*;

public class ColorSerializationUnitTest {
    @Test
    public void black_color__serialization() {
        assertEquals("[0,0,0]", new Color(0,0,0).serialize());
    }

    @Test
    public void white_color_serialization(){
        assertEquals("[255,255,255]", new Color(255,255,255).serialize());
    }

    @Test
    public void red_color_serialization() {
        assertEquals("[255,0,0]", new Color(255,0,0).serialize());
    }

    @Test
    public void green_color_serialization(){
        assertEquals("[0,255,0]", new Color(0,255,0).serialize());
    }

    @Test
    public void blue_color_serialization(){
        assertEquals("[0,0,255]", new Color(0,0,255).serialize());
    }

    @Test
    public void purple_color_serialization(){
        assertEquals("[128,0,128]", new Color(128,0,128).serialize());
    }

}