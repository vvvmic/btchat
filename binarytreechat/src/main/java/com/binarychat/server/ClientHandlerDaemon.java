//Viktor Spudil
package com.binarychat.server;

import java.io.*;
import java.net.Socket;
import java.util.Set;


public class ClientHandlerDaemon extends Thread {
    // === 0. NOTES ===
    // === 1. CLASS VARIABLES ===
    // === 2. OBJECT VARIABLES ===
    private final Socket clientSocket;
    private final Set<ClientHandlerDaemon> allClientHandlerDaemons;
    private BufferedReader readerFromClient;
    private BufferedWriter writerToClient;


    // === 3. CONSTRUCTORS ===
    // --- 3.1 STATIC BLOCKS ---
    // --- 3.2 INSTANCE INITIALIZER ---
    // --- 3.3 REAL CONSTRUCTORS ---
    public ClientHandlerDaemon(Socket clientSocket, Set<ClientHandlerDaemon> allClientHandlerDaemons) {
        this.clientSocket = clientSocket;
        this.allClientHandlerDaemons = allClientHandlerDaemons;
        this.setName(clientSocket.getInetAddress().toString());
        try {
            writerToClient = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            readerFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (Exception e) {
            closeEverything(this.clientSocket, this.readerFromClient, this.writerToClient);
            allClientHandlerDaemons.remove(this);
            e.printStackTrace();
        }
    }//end public ClientHandlerDaemon(Socket clientSocket, List<ClientHandlerDaemon> allClientHandlerDaemons)


    // === 4. STATIC METHODS ===
    // === 5. GETTER AND SETTER ===
    public BufferedWriter getWriterToClient() {
        return this.writerToClient;
    }//end public BufferedWriter getWriterToClient()


    // === 6. MISCELLANEOUS OBJECT METHODS ===
    @Override
    public void run() {
        try {
            String message;


            /* entering normal server service */
            while (clientSocket.isConnected()) {
                /* read message from client */
                message = readerFromClient.readLine();
                broadcast(message);
            }

            closeEverything(this.clientSocket, this.readerFromClient, this.writerToClient);
            allClientHandlerDaemons.remove(this);
            System.out.println("Client " + this.getName() + " disconnected");
        } catch (Exception e) {
            closeEverything(this.clientSocket, this.readerFromClient, this.writerToClient);
            allClientHandlerDaemons.remove(this);
            e.printStackTrace();
        }
    }//end public void run()

    private void broadcast(String message) throws Exception {
        for (ClientHandlerDaemon recipient : allClientHandlerDaemons) {
            if (!recipient.getName().equals(this.getName()) && (recipient != null)) {
                recipient.getWriterToClient().write(message);
            }
        }
    }//end private void multicast(BasicMessage message) throws Exception

    public void closeEverything(Socket clientSocket, BufferedReader readerFromClient, BufferedWriter writerToClient) {
        try {
            if (readerFromClient != null) {
                readerFromClient.close();
            }
            if (writerToClient != null) {
                writerToClient.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
            }
    }//end public void closeEverything(Socket clientSocket, BufferedReader readerFromClient, BufferedWriter writerToClient)


    // === 7. MAIN ===
}//end public class ClientHandlerDaemon extends Thread

