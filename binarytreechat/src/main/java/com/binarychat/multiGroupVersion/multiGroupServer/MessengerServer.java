// Viktor Spudil beta
package com.binarychat.multiGroupVersion.multiGroupServer;

import com.binarychat.multiGroupVersion.multiGroupServer.datastructures.GroupContainer;

import java.net.ServerSocket;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class MessengerServer {
    // === 0. NOTES ===
    /* this is a messaging server which forwards unicast and multicast messages */
    /* each user can get a member of several group (multicast domains) */
    /* user are identified over their String alias name */
    /* groups (multicast domains) are identified over their String alias name */
    /* When connecting to the server, a handshake like the following example has to be done first!
    Example:
    ServiceRequestMessage handshake = new ServiceRequestMessage(this.clientAlias, HANDSHAKE);
    streamToServer.writeObject(handshake);
    This is needed, to register the client with his alias at the server */

    //TODO: bugfix in server crashing after a client disconnect!!!
    //sockets maybe need to be closed?
    //TODO: implementation of a proper exception handling
    //TODO: removing closed threads out of allClientHandlerDaemons list


    // === 1. CLASS VARIABLES ===
    // === 2. OBJECT VARIABLES ===
    private int localPortNumber = 4999;
    private final List<ClientHandlerDaemon> allClientHandlerDaemons = Collections.synchronizedList(new ArrayList<ClientHandlerDaemon>());
    private final List<GroupContainer> chatGroups = Collections.synchronizedList(new ArrayList<GroupContainer>());


    // === 3. CONSTRUCTORS ===
    // --- 3.1 STATIC BLOCKS ---
    // --- 3.2 INSTANCE INITIALIZER ---
    // --- 3.3 REAL CONSTRUCTORS ---
    // === 4. STATIC METHODS ===
    // === 5. GETTER AND SETTER ===
    // === 6. MISCELLANEOUS OBJECT METHODS ===
    public void startServer() {
        System.out.println("---SERVER STARTED---");

        GroupContainer defaultChatgroup = new GroupContainer("default");
        chatGroups.add(defaultChatgroup);


        try {
            ServerSocket serverSocket = new ServerSocket(localPortNumber);
            ClientHandlerDaemon tempClientHandlerDaemon;

            System.out.println(serverSocket.getInetAddress().toString());

            /* accepting connections to clients and creating a handler-thread for each of them */
            while (!serverSocket.isClosed()) {
                tempClientHandlerDaemon = new ClientHandlerDaemon(serverSocket.accept(), allClientHandlerDaemons, chatGroups);
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

