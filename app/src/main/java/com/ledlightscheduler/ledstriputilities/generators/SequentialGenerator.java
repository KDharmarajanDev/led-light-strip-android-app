package com.ledlightscheduler.ledstriputilities.generators;

import android.os.Parcel;
import android.os.Parcelable;

import com.ledlightscheduler.ledstriputilities.ledstates.LEDState;

import java.util.ArrayList;

public class SequentialGenerator implements Parcelable {

    private ArrayList<LEDState> states;
    private int currentStateIndex;
    private long lastStateStartTime;

    public SequentialGenerator(ArrayList<LEDState> states){
        this(states, 0);
    }

    public SequentialGenerator(ArrayList<LEDState> states, int startTime){
        this.states = states;
        this.currentStateIndex = 0;
        this.lastStateStartTime = startTime;
    }

    public LEDState getCurrentLEDState(){
        if(states.size() == 0){
            return new LEDState();
        }
        return states.get(currentStateIndex);
    }

    public LEDState getNextState(){
        if(currentStateIndex+1 < states.size()){
            currentStateIndex++;
        } else {
            currentStateIndex = 0;
        }
        setStartTime(System.currentTimeMillis());
        return getCurrentLEDState();
    }

    public int getEndStateIndex(){
        return states.size();
    }

    public void setStartTime(long startTime){
        lastStateStartTime = startTime;
    }

    public void addState(LEDState state){
        states.add(state);
    }

    public LEDState getState(int index){
        if(index >= 0 && index < states.size()) {
            return states.get(index);
        }
        return states.get(0);
    }

    public void setCurrentStateIndex(int currentStateIndex){
        this.currentStateIndex = currentStateIndex;
    }

    public long getStartTime(){
        return lastStateStartTime;
    }

    public ArrayList<LEDState> getStates(){
        return states;
    }

    public RandomGenerator toRandomGenerator(){
        return new RandomGenerator(states);
    }

    protected SequentialGenerator(Parcel in) {
        if (in.readByte() == 0x01) {
            states = new ArrayList<LEDState>();
            in.readList(states, LEDState.class.getClassLoader());
        } else {
            states = null;
        }
        currentStateIndex = in.readInt();
        lastStateStartTime = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (states == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(states);
        }
        dest.writeInt(currentStateIndex);
        dest.writeLong(lastStateStartTime);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SequentialGenerator> CREATOR = new Parcelable.Creator<SequentialGenerator>() {
        @Override
        public SequentialGenerator createFromParcel(Parcel in) {
            return new SequentialGenerator(in);
        }

        @Override
        public SequentialGenerator[] newArray(int size) {
            return new SequentialGenerator[size];
        }
    };

    public String serialize(){
        StringBuilder builder = new StringBuilder();
        builder.append("[0");
        for(int i = 0; i < states.size(); i++){
            builder.append(",");
            builder.append(states.get(i).serialize());
        }
        builder.append("]");
        return builder.toString();
    }
}