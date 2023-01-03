package com.binarychat.client;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    // TODO: 26.12.2022 In and Output userMessageTypes


    private Socket socket; //listen for incoming connections
    private BufferedReader bufferedReader; //read data from the server
    private BufferedWriter bufferedWriter; //write data to the server
    private List<String> messageList = new ArrayList<String>();

    public Client(Socket socket) {
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println(LoginScreenController.getUsername()+ ": has joined chat-room.");
        }catch(IOException exception){
            System.out.println("Error creating Client!");
            exception.printStackTrace();
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
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
            bufferedWriter.write(LoginScreenController.getUsername()+ ": " + messageToServer);
            bufferedWriter.newLine(); //it is only sent when buffer is full
            bufferedWriter.flush(); //doing it manually
            messageList.add(LoginScreenController.getUsername()+ ": " + messageToServer);
        }catch(IOException exception){
            exception.printStackTrace();
            System.out.println("Error sending message to the Server!");
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void receiveMessageFromServer(VBox vBox) {
        new Thread(new Runnable() {
            @Override
            public void run() { //listen for userMessageTypes while the client is still connected
                while(socket.isConnected()){
                    try{
                        String messageFromServer = bufferedReader.readLine();
                        MessageScreenController.addLabel(messageFromServer, vBox);
                        messageList.add(messageFromServer);
                    }catch (IOException exception){
                        exception.printStackTrace();
                        System.out.println("Error receiving message from the Server!");
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }
            }
        }).start();
    }

    public void logoutfromServer(Client client) throws IOException {
        sendMessageToServer(LoginScreenController.getUsername() + " left the chat-room.");
        //client.getBufferedReader().close();
        //client.getWriterToClient().close();
        client.getSocket().close();

    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    public List<String> getMessageList(){ return messageList; }
}
