package com.ledlightscheduler.uimanager.configurationsaver;

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
import java.util.Arrays;
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

    public static ArrayList<String> getLEDStripFileNames(Context context){
        try {
            File directory = new File(context.getFilesDir() + "/" + saveFileDirectory);
            ArrayList<String> result = new ArrayList<>();
            Arrays.stream(directory.listFiles((File pathname) -> pathname.getName().endsWith(".txt")))
                    .forEach(file -> {
                        String name = file.getName().substring(0,file.getName().indexOf('.'));
                        if(!name.equals("Default")) {
                            result.add(name);
                        }
                    });
            return result;
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
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

    public static void removeLEDStrips (Context context, String name) {
        File possibleFile = new File(context.getFilesDir().getAbsolutePath() + "/" + saveFileDirectory + "/" + name + ".txt");
        try {
            possibleFile.delete();
        } catch (Exception e){
            e.printStackTrace();
        }
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
