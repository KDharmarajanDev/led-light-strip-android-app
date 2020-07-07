package com.ledlightscheduler.arduinopackets;

import com.ledlightscheduler.arduinopackets.packets.SerialPacket;
import com.ledlightscheduler.arduinopackets.packets.SerialPacketKeys;

import java.util.HashMap;

public class SerialMessageHandler {

    private HashMap<String, SerialPacketKeys> stringSerialPacketKeysHashMap;
    private HashMap<SerialPacketKeys, SerialPacket> keyToExample;

    private static SerialMessageHandler handlerInstance;

    public SerialMessageHandler(){
        stringSerialPacketKeysHashMap = new HashMap<>();
        keyToExample = new HashMap<>();
        for(SerialPacketKeys key : SerialPacketKeys.values()){
            try {
                stringSerialPacketKeysHashMap.put(key.getKey(), key);
                keyToExample.put(key, key.getPacketClass().getConstructor().newInstance());
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static SerialMessageHandler getHandlerInstance(){
        if(handlerInstance == null){
            handlerInstance = new SerialMessageHandler();
        }
        return handlerInstance;
    }

    public void handleMessage(String message) {
        new Thread() {
            @Override
            public void run() {
                String messagePrefix = message.split(" ")[0];
                if (stringSerialPacketKeysHashMap.containsKey(messagePrefix)) {
                    if (keyToExample.containsKey(stringSerialPacketKeysHashMap.get(messagePrefix))) {
                        keyToExample.get(stringSerialPacketKeysHashMap.get(messagePrefix)).deserialize(message);
                    }
                }
            }
        }.start();
    }

}
