package com.ledlightscheduler.ledstriputilities.generators;

import android.os.Parcel;
import android.os.Parcelable;

import com.ledlightscheduler.ledstriputilities.ledstates.LEDState;

import java.util.ArrayList;

public class RandomGenerator extends SequentialGenerator {

    public RandomGenerator(ArrayList<LEDState> states){
        super(states);
    }

    public RandomGenerator(ArrayList<LEDState> states, int startTime){
        super(states, startTime);
    }

    @Override
    public LEDState getNextState(){
        int desiredIndex = (int) (Math.random() * (super.getEndStateIndex()));
        super.setStartTime(System.currentTimeMillis());
        super.setCurrentStateIndex(desiredIndex);
        return super.getState(desiredIndex);
    }

    public SequentialGenerator toSequentialGenerator(){
        return new SequentialGenerator(super.getStates());
    }

    protected RandomGenerator(Parcel in) {
        super(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RandomGenerator> CREATOR = new Parcelable.Creator<RandomGenerator>() {
        @Override
        public RandomGenerator createFromParcel(Parcel in) {
            return new RandomGenerator(in);
        }

        @Override
        public RandomGenerator[] newArray(int size) {
            return new RandomGenerator[size];
        }
    };

    @Override
    public String serialize(){
        String result = super.serialize();
        return "[1" + result.substring(2);
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof RandomGenerator){
            return super.equals(other);
        }
        return false;
    }
}

