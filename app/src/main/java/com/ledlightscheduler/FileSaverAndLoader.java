package com.ledlightscheduler;

import android.app.Activity;
import android.content.Context;

import com.ledlightscheduler.arduinopackets.packets.LEDStripsInformationPacket;
import com.ledlightscheduler.ledstriputilities.ledstrips.SingleColorLEDStrip;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileSaverAndLoader {

    private static final String saveFileDirectory = "LEDStripSequences.txt";

    public static void saveLEDStrips(ArrayList<SingleColorLEDStrip> ledStrips, Activity activity){
        try {
            FileOutputStream outputStream = activity.openFileOutput(saveFileDirectory, Context.MODE_PRIVATE);
            outputStream.write(new LEDStripsInformationPacket(ledStrips).serialize().getBytes());
            outputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ArrayList<SingleColorLEDStrip> getLEDStrips(Activity activity){
        try {
            FileInputStream inputStream = activity.openFileInput(saveFileDirectory);
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder output = new StringBuilder();
            String currText;
            while ((currText = bufferedReader.readLine()) != null) {
                output.append(currText).append("\n");
            }
            LEDStripsInformationPacket packet = new LEDStripsInformationPacket().deserialize(output.toString());
            bufferedReader.close();
            reader.close();
            inputStream.close();
            return packet.getLEDStrips();
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
