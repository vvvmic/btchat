package com.binarychat.client;

import com.binarychat.multiGroupVersion.systemMessageTypes.ServiceRequestMessage;
import com.binarychat.multiGroupVersion.systemMessageTypes.ServiceRequestType;
import com.binarychat.multiGroupVersion.userMessageTypes.TextMessage;
import javafx.scene.layout.VBox;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Client {

    private Socket socket; //listen for incoming connections
    private ObjectInputStream streamFromServer; //read data from the server
    private ObjectOutputStream streamToServer; //write data to the server
    private List<String> messageList = new ArrayList<String>();

    public Client(String ipAddress) {
        try{
            socket = new Socket(ipAddress, 4999);
            streamToServer = new ObjectOutputStream(socket.getOutputStream());
            streamFromServer = new ObjectInputStream(socket.getInputStream());
        }catch(IOException exception){
            System.out.println("Error creating Client!");
            exception.printStackTrace();
            closeEverything(socket, streamFromServer, streamToServer);
        }
    }

    /**
     * if an error is triggered by the server, the connection is terminated
     */
    public void closeEverything(Socket socket, ObjectInputStream streamFromServer, ObjectOutputStream streamToServer){
        try{
            if (streamFromServer != null) {
                streamFromServer.close();
            }
            if (streamToServer != null) {
                streamToServer.close();
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
            TextMessage messagetoSend = new TextMessage(LoginScreenController.getUsername(), LoginScreenController.getChatWith(), LoginScreenController.getIsBroadcast(), LocalDateTime.now(), messageToServer); //Username, Target (Group or User), Mutlicast or Broadcast, Timestamp, Message
            streamToServer.writeObject(messagetoSend);
            LocalDateTime timestamp = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTimestamp = timestamp.format(dateTimeFormatter);
            messageList.add(formattedTimestamp + " " + LoginScreenController.getUsername()+ ": " + messageToServer);
        }catch(IOException exception){
            exception.printStackTrace();
            System.out.println("Error sending message to the Server!");
            closeEverything(socket, streamFromServer, streamToServer);
        }
    }

    public void receiveMessageFromServer(VBox vBox) {
        new Thread(new Runnable() {
            @Override
            public void run() { //listen for userMessageTypes while the client is still connected
                Object messageFromServer;
                ServiceRequestMessage serviceRequestMessage = new ServiceRequestMessage(LoginScreenController.getUsername(), ServiceRequestType.HANDSHAKE);
                try{
                    streamToServer.writeObject(serviceRequestMessage);
                    while(socket.isConnected()) {
                        messageFromServer = streamFromServer.readObject();
                        if (messageFromServer instanceof TextMessage) {
                            String message = ((TextMessage) messageFromServer).getTextMessage();
                            String recipientAlias = ((TextMessage) messageFromServer).getRecipientAlias();
                            Boolean isMulticastMessage = ((TextMessage) messageFromServer).getIsMulticastMessage();
                            String senderAlias = ((TextMessage) messageFromServer).getSenderAlias();
                            LocalDateTime timestamp = ((TextMessage) messageFromServer).getCreatedTimeStamp();

                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            String formattedTimestamp = timestamp.format(dateTimeFormatter);

                            if(Objects.equals(recipientAlias, LoginScreenController.getChatWith()) && isMulticastMessage) { //chatroom
                                MessageScreenController.addLabel(senderAlias , message, vBox);
                                messageList.add(formattedTimestamp + " " + senderAlias + ": " + message);
                            } else if (Objects.equals(senderAlias, LoginScreenController.getChatWith()) && !isMulticastMessage) {//singlechat
                                MessageScreenController.addLabel(senderAlias, message, vBox);
                                messageList.add(formattedTimestamp + " " + senderAlias + ": " + message);
                            }
                        }
                    }
                }catch (Exception exception){
                        exception.printStackTrace();
                        System.out.println("Error receiving message from the Server!");
                        closeEverything(socket, streamFromServer, streamToServer);
                }
            }
        }).start();
    }

    public void logoutFromServer(Client client) throws IOException { //sends a logout message and closes the connection to the server
        sendMessageToServer(LoginScreenController.getUsername() + " left the chat-room.");
        client.getSocket().close();
    }

    public Socket getSocket() {
        return socket;
    }

    public List<String> getMessageList(){ return messageList; }
}
