package com.binarychat.client;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class LoginScreen {

    public LoginScreen(String username, String ipAddress, int port){
        try {
            Socket socket = new Socket(ipAddress, port);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println(username + ": has joined chat-room.");
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
