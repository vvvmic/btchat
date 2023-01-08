package com.binarychat.client;

import javafx.stage.FileChooser;

import java.io.*;
import java.util.List;

public class printMessageList {
    /*
    Prints the passed string list to a text file at a location of choice
     */
    public static void printMessageList(List<String> messageList) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Textfiles", "*.txt"));
        File selectedDirectory = null;

        try {
            selectedDirectory = fileChooser.showSaveDialog(null);
            FileWriter fileWriter = new FileWriter(selectedDirectory);
            for (String string : messageList) {
                fileWriter.write(string);
                fileWriter.write("\r\n");
            }
            fileWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException exception) {
            System.out.println("An error occurred.");
            exception.printStackTrace();
        }
    }
}
