// Viktor Spudil
package com.binarychat.server;

import java.net.ServerSocket;
import java.util.Set;
import java.util.TreeSet;

public class MessengerServer {
    // === 0. NOTES ===
    // === 1. CLASS VARIABLES ===
    // === 2. OBJECT VARIABLES ===
    private int localPortNumber = 4999;
    private final Set<ClientHandlerDaemon> allClientHandlerDaemons = new TreeSet<>();


    // === 3. CONSTRUCTORS ===
    // --- 3.1 STATIC BLOCKS ---
    // --- 3.2 INSTANCE INITIALIZER ---
    // --- 3.3 REAL CONSTRUCTORS ---
    // === 4. STATIC METHODS ===
    // === 5. GETTER AND SETTER ===
    // === 6. MISCELLANEOUS OBJECT METHODS ===
    public void startServer() {
        System.out.println("---SERVER STARTED---");


        try {
            ServerSocket serverSocket = new ServerSocket(localPortNumber);
            ClientHandlerDaemon tempClientHandlerDaemon;

            /* accepting connections to clients and creating a handler-thread for each of them */
            while (!serverSocket.isClosed()) {
                tempClientHandlerDaemon = new ClientHandlerDaemon(serverSocket.accept(), allClientHandlerDaemons);
                tempClientHandlerDaemon.setDaemon(true);
                tempClientHandlerDaemon.start();
                allClientHandlerDaemons.add(tempClientHandlerDaemon);
                System.out.println("Client connected");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//end private void startServer()


    // === 7. MAIN ===
    public static void main(String[] args) {
        MessengerServer serverInstance = new MessengerServer();
        serverInstance.startServer();
    }//end public static void main(String[] args)
}//end public class MessengerServer


