// Viktor Spudil beta
package com.binarychat.multiGroupVersion.multiGroupServer;

import com.binarychat.multiGroupVersion.multiGroupServer.datastructures.GroupContainer;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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


    // === 1. CLASS VARIABLES ===
    // === 2. OBJECT VARIABLES ===
    private int localPortNumber = 4999;
    private final List<ClientHandlerDaemon> allClientHandlerDaemons = Collections.synchronizedList(new LinkedList<ClientHandlerDaemon>());
    private final List<GroupContainer> allChatGroups = Collections.synchronizedList(new LinkedList<GroupContainer>());


    // === 3. CONSTRUCTORS ===
    // --- 3.1 STATIC BLOCKS ---
    // --- 3.2 INSTANCE INITIALIZER ---
    // --- 3.3 REAL CONSTRUCTORS ---
    // === 4. STATIC METHODS ===
    // === 5. GETTER AND SETTER ===
    // === 6. MISCELLANEOUS OBJECT METHODS ===
    public void startServer() {
        System.out.println("---SERVER STARTED---");

        /* (beta) create default chat group */
        GroupContainer defaultChatgroup = new GroupContainer("default");
        allChatGroups.add(defaultChatgroup);



        try {
            ServerSocket serverSocket = new ServerSocket(localPortNumber);
            ClientHandlerDaemon tempClientHandlerDaemon;

            /* print server details */
            InetAddress ip = InetAddress.getLocalHost();
            String hostname = ip.getHostName();
            System.out.println("Your current IP address : " + ip);
            System.out.println("Your current Hostname : " + hostname);


            /* accepting connections to clients and creating a handler-thread for each of them */
            while (!serverSocket.isClosed()) {
                tempClientHandlerDaemon = new ClientHandlerDaemon(serverSocket.accept(), allClientHandlerDaemons, allChatGroups);
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

