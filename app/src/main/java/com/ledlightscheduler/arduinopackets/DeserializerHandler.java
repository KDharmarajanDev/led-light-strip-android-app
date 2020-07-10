package com.ledlightscheduler.arduinopackets;

import java.util.Stack;

public class DeserializerHandler {

    private String serializedRepresentation;
    private int currentIndex;

    public DeserializerHandler(String input){
        this.serializedRepresentation = input;
        currentIndex = 0;
    }

    public int getNextInteger(){
        boolean hasStarted = false;
        StringBuilder stringRepresentation = new StringBuilder();
        while(currentIndex < serializedRepresentation.length()){
            char currentChar = serializedRepresentation.charAt(currentIndex);
            if(currentChar != '[' && currentChar != ']' && currentChar != ','){
                if(!hasStarted){
                    hasStarted=true;
                }
                stringRepresentation.append(currentChar);
            } else if(hasStarted){
                break;
            }
            currentIndex++;
        }
        return stringRepresentation.length() != 0 ? Integer.parseInt(stringRepresentation.toString()) : 0;
    }

    public String getNextItemInBrackets(){
        StringBuilder builder = new StringBuilder();
        Stack<Character> brackets = new Stack<>();
        while(currentIndex < serializedRepresentation.length()){
            if(serializedRepresentation.charAt(currentIndex) == '['){
                builder.append('[');
                brackets.push('[');
            } else if(serializedRepresentation.charAt(currentIndex) == ']' && brackets.size() > 0 && brackets.peek() == '['){
                brackets.pop();
                builder.append(']');
                if(brackets.size() == 0){
                    currentIndex++;
                    break;
                }
            } else if (brackets.size() > 0){
                builder.append(serializedRepresentation.charAt(currentIndex));
            }
            currentIndex++;
        }
        return builder.toString();
    }
}
