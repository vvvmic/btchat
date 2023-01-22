package com.binarychat.multiGroupVersion.multiGroupServer;

import com.binarychat.multiGroupVersion.multiGroupServer.datastructures.GroupContainer;
import com.binarychat.multiGroupVersion.systemMessageTypes.*;
import com.binarychat.multiGroupVersion.userMessageTypes.*;

import java.io.*;
import java.net.Socket;
import java.util.List;

import static com.binarychat.multiGroupVersion.systemMessageTypes.ServiceRequestType.*;

public class ClientHandlerDaemon extends Thread {
    // === 0. NOTES ===
    /* this is a messaging server which forwards unicast and multicast messages */
    /* each user can get a member of several groups (multicast domains) */
    /* user are identified over their String alias names */
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
    private final Socket clientSocket;
    private final List<ClientHandlerDaemon> allClientHandlerDaemons;
    private final List<GroupContainer> allChatGroups;
    private ObjectOutputStream streamToClient = null;
    private ObjectInputStream streamFromClient = null;
    private boolean messengerServiceEnabled = false;


    // === 3. CONSTRUCTORS ===
    // --- 3.1 STATIC BLOCKS ---
    // --- 3.2 INSTANCE INITIALIZER ---
    // --- 3.3 REAL CONSTRUCTORS ---
    public ClientHandlerDaemon(Socket clientSocket, List<ClientHandlerDaemon> allClientHandlerDaemons, List<GroupContainer> allChatGroups) {
        this.clientSocket = clientSocket;
        this.allClientHandlerDaemons = allClientHandlerDaemons;
        this.allChatGroups = allChatGroups;
    }//end public ClientHandlerDaemon(Socket clientSocket, List<ClientHandlerDaemon> allClientHandlerDaemons, List<GroupContainer> allChatGroups)


    // === 4. STATIC METHODS ===
    // === 5. GETTER AND SETTER ===
    public ObjectOutputStream getStreamToClient() {
        return this.streamToClient;
    }//end public ObjectOutputStream getStreamToClient()


    // === 6. MISCELLANEOUS OBJECT METHODS ===
    @Override
    public void run() {
        try {
            streamToClient = new ObjectOutputStream(clientSocket.getOutputStream());
            streamFromClient = new ObjectInputStream(clientSocket.getInputStream());


            /* doing server handshake before entering into normal server service mode */
            serverHandshake();


            Object message;
            /* entering normal server service */
            while (clientSocket.isConnected()) {
                /* read message from client */
                message = streamFromClient.readObject();


                if (message instanceof BasicMessage) {
                    if (((BasicMessage) message).getIsMulticastMessage()) {
                        multicast((BasicMessage) message);
                    }
                    else {
                        unicast((BasicMessage) message);
                    }
                }
                else {
                    if (message instanceof ServiceRequestMessage) {
                        switch (((ServiceRequestMessage) message).getServiceRequest()) {
                            case HANDSHAKE:
                                break;
                            case SETNEWALIAS:
                                setNewAlias((ServiceRequestMessage) message);
                                break;
                            case CREATEGROUP:
                                createChatGroup((ServiceRequestMessage) message);
                                break;
                            case DELETEGROUP:
                                deleteChatGroup((ServiceRequestMessage) message);
                                break;
                            case JOINGROUP:
                                joinChatGroup((ServiceRequestMessage) message);
                                break;
                            case EXITGROUP:
                                exitChatGroup((ServiceRequestMessage) message);
                                break;
                        }
                    }
                }
            }

            closeEverything();
            System.out.println("Client " + this.getName() + " disconnected");
        } catch (Exception e) {
            e.printStackTrace();
            closeEverything();
        }
    }//end public void run()

    private void serverHandshake() throws Exception {
        Object message;
        boolean aliasAlreadyExists = false;

        while(!this.messengerServiceEnabled) {
            message = streamFromClient.readObject();

            if ((message instanceof ServiceRequestMessage) &&
                    (((ServiceRequestMessage) message).getServiceRequest() == HANDSHAKE)) {

                for (ClientHandlerDaemon clientHandlerDaemon : allClientHandlerDaemons) {
                    if (clientHandlerDaemon.getName().equals(((ServiceRequestMessage) message).getName())) {
                        aliasAlreadyExists = true;
                    }
                }

                if(!aliasAlreadyExists) {
                    this.setName(((ServiceRequestMessage) message).getName());
                    this.messengerServiceEnabled = true;
                    break;
                }
                else {
                    ServiceReplyMessage errorMessage = new ServiceReplyMessage(ServiceReplyType.ALIASNOTAVIABLE);
                    streamToClient.writeObject(errorMessage);
                }
            }
            else {
                ServiceReplyMessage errorMessage = new ServiceReplyMessage(ServiceReplyType.SERVERHANDSHAKENEEDED);
                streamToClient.writeObject(errorMessage);
            }
        }
    }//end private void serverHandshake() throws Exception

    private void setNewAlias(ServiceRequestMessage serviceRequestMessage) throws Exception {
        boolean aliasAlreadyExists = false;

        for (ClientHandlerDaemon clientHandlerDaemon : allClientHandlerDaemons) {
            if (clientHandlerDaemon.getName().equals(serviceRequestMessage.getName())) {
                if (clientHandlerDaemon != this) {
                    aliasAlreadyExists = true;
                }
            }
        }

        if(!aliasAlreadyExists) {
            this.setName(serviceRequestMessage.getName());
        }
        else {
            ServiceReplyMessage errorMessage = new ServiceReplyMessage(ServiceReplyType.ALIASNOTAVIABLE);
            streamToClient.writeObject(errorMessage);
        }
    }//end private void setNewAlias() throws Exception

    private void joinChatGroup(ServiceRequestMessage serviceRequestMessage) throws Exception {
        for (GroupContainer groupContainer : allChatGroups) {
            if (groupContainer.getChatGroupName().equals(serviceRequestMessage.getName())) {
                groupContainer.getGroupMemberList().add(this);
                return;
            }
        }

        GroupContainer newGroupContainer = new GroupContainer(serviceRequestMessage.getName());
        newGroupContainer.getGroupMemberList().add(this);
        allChatGroups.add(newGroupContainer);
    }//end private void joinChatGroup(ServiceRequestMessage serviceRequestMessage) throws Exception

    private void exitChatGroup(ServiceRequestMessage serviceRequestMessage) throws Exception {
        for (GroupContainer groupContainer : allChatGroups) {
            if (groupContainer.getChatGroupName().equals(serviceRequestMessage.getName())) {
                allChatGroups.remove(groupContainer);
                return;
            }
        }

        ServiceReplyMessage errorMessage = new ServiceReplyMessage(ServiceReplyType.INVALIDREQUEST);
        streamToClient.writeObject(errorMessage);
    }//end private void exitChatGroup(ServiceRequestMessage serviceRequestMessage) throws Exception

    private void createChatGroup(ServiceRequestMessage serviceRequestMessage) throws Exception {
        boolean groupAlreadyExists = false;

        for (GroupContainer groupContainer : allChatGroups) {
            if (groupContainer.getChatGroupName().equals(serviceRequestMessage.getName())) {
                groupAlreadyExists = true;
                break;
            }
        }

        if (!groupAlreadyExists) {
            allChatGroups.add(new GroupContainer(serviceRequestMessage.getName()));
        }
        else {
            ServiceReplyMessage errorMessage = new ServiceReplyMessage(ServiceReplyType.ALIASNOTAVIABLE);
            streamToClient.writeObject(errorMessage);
        }
    }//end private void createChatGroup(ServiceRequestMessage serviceRequestMessage) throws Exception

    private void deleteChatGroup(ServiceRequestMessage serviceRequestMessage) throws Exception {
        for (GroupContainer groupContainer : allChatGroups) {
            if (groupContainer.getChatGroupName().equals(serviceRequestMessage.getName())) {
                allChatGroups.remove(groupContainer);
                break;
            }
        }
    }//end private void deleteChatGroup(ServiceRequestMessage serviceRequestMessage) throws Exception

    private void unicast(BasicMessage message) throws Exception {
        ClientHandlerDaemon messageTarget = null;

        /* searching fo the target client */
        for (ClientHandlerDaemon clientHandlerDaemon : allClientHandlerDaemons) {
            if (clientHandlerDaemon.getName().equals((message.getRecipientAlias()))) {
                messageTarget = clientHandlerDaemon;
                break;
            }
        }

        /* forwarding the message if the targeted client has been found, otherwise sending back an error textmessage */
        if (messageTarget != null) {
            messageTarget.getStreamToClient().writeObject(message);
            System.out.println("Unicast Message forwarded");
        }
        else {
            ServiceReplyMessage errorMessage = new ServiceReplyMessage(ServiceReplyType.RECIPIENTNOTFOUND);
            streamToClient.writeObject(errorMessage);
        }
    }//end private void unicast(BasicMessage message) throws Exception

    private void multicast(BasicMessage message) throws Exception {
        GroupContainer targetChatGroup = null;


        /* searching for the target chat group */
        for (GroupContainer groupContainer : allChatGroups) {
            if (groupContainer.getChatGroupName().equals(message.getRecipientAlias())) {
                targetChatGroup = groupContainer;
                break;
            }
        }

        /* sending the message to all user daemons in the chat group */
        if (targetChatGroup != null) {
            for (ClientHandlerDaemon clientHandlerDaemon : targetChatGroup.getGroupMemberList()) {
                if (clientHandlerDaemon != this) {
                    clientHandlerDaemon.getStreamToClient().writeObject(message);
                }
            }

            System.out.println("Multicast Message forwarded");
        }
        else {
            ServiceReplyMessage errorMessage = new ServiceReplyMessage(ServiceReplyType.RECIPIENTNOTFOUND);
            streamToClient.writeObject(errorMessage);
        }
    }//end private void multicast(BasicMessage message) throws Exception

    public void closeEverything() {
        try {
            if (this.streamToClient != null) {
                this.streamToClient.close();
            }
            if (this.streamFromClient != null) {
                this.streamFromClient.close();
            }
            if (this.clientSocket != null) {
                this.clientSocket.close();
            }

            this.allClientHandlerDaemons.remove(this);

            for (GroupContainer groupContainer : allChatGroups) {
                groupContainer.getGroupMemberList().remove(this);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }//end public void closeEverything(Socket clientSocket, ObjectOutputStream streamToClient, ObjectOutputStream streamFromClient)


    // === 7. MAIN ===
}//end public class ClientHandlerDaemon extends Thread

