package com.ledlightscheduler.ledstriputilities.ledstates;

import android.os.Parcel;
import android.os.Parcelable;

public class Color implements Parcelable {

    private int red;
    private int green;
    private int blue;

    public Color(int red, int green, int blue){
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRed(){
        return red;
    }

    public int getGreen(){
        return green;
    }

    public int getBlue(){
        return blue;
    }

    public void setGreen(int green){
        this.green = green;
    }

    public void setBlue(int blue){
        this.blue = blue;
    }

    public void setRed(int red){
        this.red = red;
    }

    protected Color(Parcel in) {
        red = in.readInt();
        green = in.readInt();
        blue = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(red);
        dest.writeInt(green);
        dest.writeInt(blue);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Color> CREATOR = new Parcelable.Creator<Color>() {
        @Override
        public Color createFromParcel(Parcel in) {
            return new Color(in);
        }

        @Override
        public Color[] newArray(int size) {
            return new Color[size];
        }
    };

    public int toAndroidColor(){
        return android.graphics.Color.rgb(red, green, blue);
    }
}