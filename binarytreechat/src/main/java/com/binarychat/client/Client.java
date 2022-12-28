package com.binarychat.client;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;

public class Client {
    // TODO: 26.12.2022 In and Output messages


    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Client(Socket socket) {
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("Test: has joined chat-room.");
        }catch(IOException exception){
            System.out.println("Error creating Client!");
            exception.printStackTrace();
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        }catch(IOException exception){
            exception.printStackTrace();
        }
    }


    public void sendMessageToServer(String messageToServer) {
        try{
            bufferedWriter.write(messageToServer);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch(IOException exception){
            exception.printStackTrace();
            System.out.println("Error sending message to the Server!");
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void receiveMessageFromServer(VBox vbox_messages) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()){
                    try{
                        String messageFromServer = bufferedReader.readLine();
                        MessageScreenController.addLabel(messageFromServer, vbox_messages);
                    }catch (IOException e){
                        e.printStackTrace();
                        System.out.println("Error receiving message from the Server!");
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }
            }
        }).start();
    }


}
