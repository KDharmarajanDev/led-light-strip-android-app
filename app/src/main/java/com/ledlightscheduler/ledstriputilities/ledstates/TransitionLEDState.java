package com.ledlightscheduler.ledstriputilities.ledstates;

import android.os.Parcel;
import android.os.Parcelable;

public class TransitionLEDState extends LEDState implements Parcelable {

    private Color endColor;
    private Color changeColor;
    private Color currentColor;

    public TransitionLEDState(Color startColor, Color endColor, long duration){
        super(startColor, duration);
        currentColor = startColor;
        this.endColor = endColor;
        computeChangeColor();
    }

    @Override
    public Color getColor(long currentTime){
        if(currentTime < 0){
            currentTime = 0;
        }
        double multiplierRatio = ((double) currentTime)/super.getDuration();
        currentColor = new Color((int) (super.getColor().getRed() + multiplierRatio * changeColor.getRed())
                , (int) (super.getColor().getGreen() + multiplierRatio * changeColor.getGreen())
                , (int) (super.getColor().getBlue() + multiplierRatio * changeColor.getBlue()));
        return currentColor;
    }

    @Override
    public void setColor(Color color){
        super.setColor(color);
        computeChangeColor();
    }

    public void setEndColor(Color color){
        this.endColor = color;
        computeChangeColor();
    }

    public Color getEndColor(){
        return endColor;
    }

    public void computeChangeColor(){
        changeColor = new Color(endColor.getRed() - super.getColor().getRed()
                , endColor.getGreen() - super.getColor().getGreen()
                , endColor.getBlue() - super.getColor().getBlue());
    }

    public LEDState toLEDState(){
        return new LEDState(super.getColor(), super.getDuration());
    }

    protected TransitionLEDState(Parcel in) {
        super(in);
        endColor = (Color) in.readValue(Color.class.getClassLoader());
        changeColor = (Color) in.readValue(Color.class.getClassLoader());
        currentColor = (Color) in.readValue(Color.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
        dest.writeValue(endColor);
        dest.writeValue(changeColor);
        dest.writeValue(currentColor);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TransitionLEDState> CREATOR = new Parcelable.Creator<TransitionLEDState>() {
        @Override
        public TransitionLEDState createFromParcel(Parcel in) {
            return new TransitionLEDState(in);
        }

        @Override
        public TransitionLEDState[] newArray(int size) {
            return new TransitionLEDState[size];
        }
    };

    @Override
    public String serialize(){
        return "[" + super.getDuration() + "," + super.getColor().serialize() + "," + endColor.serialize() + "]";
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof TransitionLEDState){
            TransitionLEDState ledState = (TransitionLEDState) other;
            return super.equals(ledState) && ledState.endColor.equals(endColor);
        }
        return false;
    }
}