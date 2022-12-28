package com.binarychat.client;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class LoginScreen {
    String message;
    Scanner scanner = new Scanner(System.in);

    public LoginScreen(String username, String ipAddress, int port){
        try {
            Socket client = new Socket(ipAddress, port);
            PrintWriter printWriter = new PrintWriter(client.getOutputStream(), true);
            MessageScreen messageScreen = new MessageScreen(client);
            new Thread(messageScreen).start();
            printWriter.println(username + ": has joined chat-room.");
            do {
                String message = (username + " : ");
                this.message = scanner.nextLine();
                printWriter.println(message + this.message);
            } while (true); //logout button implementieren
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }
}
