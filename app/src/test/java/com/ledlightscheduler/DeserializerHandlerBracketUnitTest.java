package com.ledlightscheduler;

import com.ledlightscheduler.arduinopackets.DeserializerHandler;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class DeserializerHandlerBracketUnitTest {

    @Test
    public void one_sets_brackets_test(){
        DeserializerHandler handler = new DeserializerHandler("[]");
        assertEquals("[]", handler.getNextItemInBrackets());
    }

    @Test
    public void two_sets_brackets_test(){
        DeserializerHandler handler = new DeserializerHandler("[[]]");
        assertEquals("[[]]", handler.getNextItemInBrackets());
    }

    @Test
    public void three_sets_brackets_test(){
        DeserializerHandler handler = new DeserializerHandler("[[[]]]");
        assertEquals("[[[]]]", handler.getNextItemInBrackets());
    }

    @Test
    public void three_sets_brackets_test_with_text_inside(){
        DeserializerHandler handler = new DeserializerHandler("[[[test]]]");
        assertEquals("[[[test]]]", handler.getNextItemInBrackets());
    }

    @Test
    public void multiple_interwoven_brackets_test(){
        DeserializerHandler handler = new DeserializerHandler("[[[test][]]]");
        assertEquals("[[[test][]]]", handler.getNextItemInBrackets());
    }

    @Test
    public void next_bracket_after_one_has_been_called(){
        DeserializerHandler handler = new DeserializerHandler("[[[test],[]]],[]");
        handler.getNextItemInBrackets();
        assertEquals("[]", handler.getNextItemInBrackets());
    }

}
