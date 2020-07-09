package com.ledlightscheduler.arduinopackets;

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

}
