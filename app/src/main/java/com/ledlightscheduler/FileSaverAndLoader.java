package com.ledlightscheduler;

import android.app.Activity;
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

    public static void saveLEDStrips(ArrayList<SingleColorLEDStrip> ledStrips, Activity activity, String name){
        checkIfSaveDirExisted(activity);
        try {
            String pathName = activity.getFilesDir().getAbsolutePath() + "/" + saveFileDirectory + "/" + name + ".txt";
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

    public static File[] getLEDStripFiles(Activity activity){
        try {
            File directory = new File(activity.getFilesDir() + "/" + saveFileDirectory);
            return directory.listFiles((File pathname) -> pathname.getName().endsWith(".txt"));
        } catch (Exception e){
            e.printStackTrace();
        }
        return new File[0];
    }

    public static ArrayList<SingleColorLEDStrip> getLEDStrips (Activity activity, String name){
        if(checkIfSaveDirExisted(activity)){
            File possibleFile = new File(activity.getFilesDir().getAbsolutePath() + "/" + saveFileDirectory + "/" + name + ".txt");
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

    private static boolean checkIfSaveDirExisted(Activity activity) {
        Path path = Paths.get(activity.getFilesDir().getAbsolutePath() + "/" + saveFileDirectory);
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
