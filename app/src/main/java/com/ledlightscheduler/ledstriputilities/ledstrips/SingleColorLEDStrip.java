package com.ledlightscheduler.ledstriputilities.ledstrips;

import android.os.Parcel;
import android.os.Parcelable;

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
}