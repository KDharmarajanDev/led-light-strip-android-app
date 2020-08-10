package com.ledlightscheduler;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.ledlightscheduler.arduinopackets.packets.LEDStripsInformationPacket;
import com.ledlightscheduler.ledstriputilities.ledstrips.SingleColorLEDStrip;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class FileSaverAndLoader {

    private static final String saveFileDirectory = "LEDStripSequences";

    public static void saveLEDStrips(ArrayList<SingleColorLEDStrip> ledStrips, Context context, String name){
        checkIfSaveDirExisted(context);
        try {
            String pathName = context.getFilesDir().getAbsolutePath() + "/" + saveFileDirectory + "/" + name + ".txt";
            File newFile = new File(pathName);
            if (!newFile.createNewFile()) {
                Log.w("[LEDLightStripScheduler]", "Couldn't create file " + name + ".txt.");
            }
            FileWriter writer = new FileWriter(pathName);
            writer.write(new LEDStripsInformationPacket(ledStrips).serialize());
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static File[] getLEDStripFiles(Context context){
        try {
            File directory = new File(context.getFilesDir() + "/" + saveFileDirectory);
            return directory.listFiles((File pathname) -> pathname.getName().endsWith(".txt"));
        } catch (Exception e){
            e.printStackTrace();
        }
        return new File[0];
    }

    public static ArrayList<SingleColorLEDStrip> getLEDStrips (Context context, String name){
        if(checkIfSaveDirExisted(context)){
            File possibleFile = new File(context.getFilesDir().getAbsolutePath() + "/" + saveFileDirectory + "/" + name + ".txt");
            if(possibleFile.exists()){
                try {
                    Scanner scanner = new Scanner(possibleFile);
                    StringBuilder stringBuilder = new StringBuilder();
                    while(scanner.hasNextLine()) {
                        stringBuilder.append(scanner.nextLine());
                    }
                    return new LEDStripsInformationPacket().deserialize(stringBuilder.toString()).getLEDStrips();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return new ArrayList<>();
    }
    

    private static boolean checkIfSaveDirExisted(Context context) {
        Path path = Paths.get(context.getFilesDir().getAbsolutePath() + "/" + saveFileDirectory);
        if (Files.notExists(path)){
            File directory = path.toFile();
            if (!directory.mkdir()) {
                Log.w("[LEDLightStripScheduler]","Unable to create the save directory!");
            }
            return false;
        }
        return true;
    }

}
