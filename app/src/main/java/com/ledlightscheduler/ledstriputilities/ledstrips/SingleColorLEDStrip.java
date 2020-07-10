package com.ledlightscheduler.ledstriputilities.ledstrips;

import android.os.Parcel;
import android.os.Parcelable;

import com.ledlightscheduler.arduinopackets.DeserializerHandler;
import com.ledlightscheduler.ledstriputilities.generators.SequentialGenerator;

import java.util.ArrayList;

public class SingleColorLEDStrip implements Parcelable {

    private int redPin;
    private int greenPin;
    private int bluePin;
    private ArrayList<SequentialGenerator> generators;
    private int currentGeneratorIndex;

    public SingleColorLEDStrip(int redPin, int greenPin, int bluePin, ArrayList<SequentialGenerator> generators){
        this.redPin = redPin;
        this.greenPin = greenPin;
        this.bluePin = bluePin;
        this.generators = generators;
        currentGeneratorIndex = 0;
    }

    public SequentialGenerator getSequentialGenerator(){
        if(generators.size() == 0){
            return new SequentialGenerator(new ArrayList<>());
        }
        return generators.get(currentGeneratorIndex);
    }

    public void setCurrentGeneratorIndex(int index){
        this.currentGeneratorIndex = index;
    }

    public int getActivatedGeneratorIndex(){
        return currentGeneratorIndex;
    }

    public ArrayList<SequentialGenerator> getGenerators(){
        return generators;
    }

    public void setRedPin(int redPin){
        this.redPin = redPin;
    }

    public void setGreenPin(int greenPin){
        this.greenPin = greenPin;
    }

    public void setBluePin(int bluePin){
        this.bluePin = bluePin;
    }

    public int getRedPin(){
        return redPin;
    }

    public int getGreenPin(){
        return greenPin;
    }

    public int getBluePin(){
        return bluePin;
    }

    protected SingleColorLEDStrip(Parcel in) {
        redPin = in.readInt();
        greenPin = in.readInt();
        bluePin = in.readInt();
        if (in.readByte() == 0x01) {
            generators = new ArrayList<SequentialGenerator>();
            in.readList(generators, SequentialGenerator.class.getClassLoader());
        } else {
            generators = null;
        }
        currentGeneratorIndex = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(redPin);
        dest.writeInt(greenPin);
        dest.writeInt(bluePin);
        if (generators == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(generators);
        }
        dest.writeInt(currentGeneratorIndex);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SingleColorLEDStrip> CREATOR = new Parcelable.Creator<SingleColorLEDStrip>() {
        @Override
        public SingleColorLEDStrip createFromParcel(Parcel in) {
            return new SingleColorLEDStrip(in);
        }

        @Override
        public SingleColorLEDStrip[] newArray(int size) {
            return new SingleColorLEDStrip[size];
        }
    };

    public String serialize(){
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("[%s,%s,%s",redPin, greenPin, bluePin));
        for(SequentialGenerator generator : generators){
            builder.append(",");
            builder.append(generator.serialize());
        }
        builder.append("]");
        return builder.toString();
    }

    public static SingleColorLEDStrip deserialize(String input){
        if(input.length() >= 7){
            DeserializerHandler handler = new DeserializerHandler(input);
            int redPin = handler.getNextInteger();
            int greenPin = handler.getNextInteger();
            int bluePin = handler.getNextInteger();
            ArrayList<SequentialGenerator> generators = new ArrayList<>();
            String generatorString = handler.getNextItemInBrackets();
            while(!generatorString.equals("")){
                generators.add(SequentialGenerator.deserialize(generatorString));
                generatorString = handler.getNextItemInBrackets();
            }
            return new SingleColorLEDStrip(redPin, greenPin, bluePin, generators);
        }
        return new SingleColorLEDStrip(0,0,0,new ArrayList<>());
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof SingleColorLEDStrip){
            SingleColorLEDStrip singleColorLEDStrip = (SingleColorLEDStrip) other;
            if(singleColorLEDStrip.generators.size() == generators.size()){
                for(int i = 0; i < generators.size(); i++){
                    if(!singleColorLEDStrip.getGenerators().get(i).equals(generators.get(i))){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}