// Viktor Spudil beta
package com.binarychat.server;

import com.binarychat.server.datastructures.GroupContainer;
import com.binarychat.systemMessageTypes.*;
import com.binarychat.userMessageTypes.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;

import static com.binarychat.systemMessageTypes.ServiceRequestType.*;

public class ClientHandlerDaemon extends Thread {
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
    //TODO: groups chatGroups data structure must be synchronized
    //TODO: implementation of a proper exception handling


    // === 1. CLASS VARIABLES ===
    // === 2. OBJECT VARIABLES ===
    private final Socket clientSocket;
    private final List<ClientHandlerDaemon> allClientHandlerDaemons;
    private final List<GroupContainer> chatGroups;
    private ObjectOutputStream streamToClient = null;
    private ObjectInputStream streamFromClient = null;
    private boolean messengerServiceEnabled = false;


    // === 3. CONSTRUCTORS ===
    // --- 3.1 STATIC BLOCKS ---
    // --- 3.2 INSTANCE INITIALIZER ---
    // --- 3.3 REAL CONSTRUCTORS ---
    public ClientHandlerDaemon(Socket clientSocket, List<ClientHandlerDaemon> allClientHandlerDaemons, List<GroupContainer> chatGroups) {
        this.clientSocket = clientSocket;
        this.allClientHandlerDaemons = allClientHandlerDaemons;
        this.chatGroups = chatGroups;
    }//end public ClientHandlerDaemon(Socket clientSocket, List<Thread> allClientHandlerDaemons)


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

            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Client " + this.getName() + " disconnected");
        }
    }//end public void run()

    private void serverHandshake() throws Exception {
        Object message;
        boolean aliasAlreadyExists = false;

        while(!messengerServiceEnabled) {
            message = streamFromClient.readObject();

            if ((message instanceof ServiceRequestMessage) &&
                    (((ServiceRequestMessage) message).getServiceRequest() == HANDSHAKE)) {
                for (int i = 0; i < allClientHandlerDaemons.size(); i++) {
                    if (allClientHandlerDaemons.get(i).getName().contains(((ServiceRequestMessage)message).getName())) {
                        /* contains might be a problem if it contains the substring! (see String class */
                        aliasAlreadyExists = true;
                    }
                }

                if(!aliasAlreadyExists) {
                    this.setName(((ServiceRequestMessage) message).getName());
                    messengerServiceEnabled = true;
                    break;
                }
                else {
                    //TODO: maybe change to ServiceReplyMessage error
                    TextMessage errorMessage = new TextMessage("Messenger Daemon", this.getName(),
                            false, LocalDateTime.now(), "Error: Alias already exists!");
                    streamToClient.writeObject(errorMessage);
                }
            }
            else {
                //TODO: maybe change to ServiceReplyMessage error
                TextMessage errorMessage = new TextMessage("Messenger Daemon", this.getName(),
                        false, LocalDateTime.now(), "Error: server handshake needed!");
                streamToClient.writeObject(errorMessage);
            }
        }
    }//end private void serverHandshake() throws Exception

    private void setNewAlias(ServiceRequestMessage serviceRequestMessage) throws Exception {
        boolean aliasAlreadyExists = false;

        for (int i = 0; i < allClientHandlerDaemons.size(); i++) {
            if (allClientHandlerDaemons.get(i).getName().contains(serviceRequestMessage.getName())) {
                /* contains might be a problem if it contains the substring! (see String class */
                if (allClientHandlerDaemons.get(i) != this) {
                    aliasAlreadyExists = true;
                }
            }
        }

        if(!aliasAlreadyExists) {
            this.setName(serviceRequestMessage.getName());
        }
        else {
            //TODO: maybe change to ServiceReplyMessage error
            TextMessage errorMessage = new TextMessage("Messenger Daemon", this.getName(),
                    false, LocalDateTime.now(), "Error: Alias already exists!");
            streamToClient.writeObject(errorMessage);
        }
    }//end private void setNewAlias() throws Exception

    private void joinChatGroup(ServiceRequestMessage serviceRequestMessage) throws Exception {
        for (int i = 0; i < chatGroups.size(); i++) {
            if (chatGroups.get(i).getChatGroupName().contains(serviceRequestMessage.getName())) {
                chatGroups.get(i).getGroupMemberList().add(this);
                return;
            }
        }
        //TODO: maybe change to ServiceReplyMessage error
        TextMessage errorMessage = new TextMessage("Messenger Daemon", this.getName(),
                false, LocalDateTime.now(), "Error: Group not found!");
        streamToClient.writeObject(errorMessage);
    }//end private void joinChatGroup(ServiceRequestMessage serviceRequestMessage) throws Exception

    private void exitChatGroup(ServiceRequestMessage serviceRequestMessage) throws Exception {
        for (int i = 0; i < chatGroups.size(); i++) {
            if (chatGroups.get(i).getChatGroupName().contains(serviceRequestMessage.getName())) {
                chatGroups.get(i).getGroupMemberList().remove(i);
                return;
            }
        }
        //TODO: maybe change to ServiceReplyMessage error
        TextMessage errorMessage = new TextMessage("Messenger Daemon", this.getName(),
                false, LocalDateTime.now(), "Error: you are not in that group!");
        streamToClient.writeObject(errorMessage);
    }//end private void exitChatGroup(ServiceRequestMessage serviceRequestMessage) throws Exception

    private void createChatGroup(ServiceRequestMessage serviceRequestMessage) throws Exception {
        boolean groupAlreadyExists = false;

        for (int i = 0; i < chatGroups.size(); i++) {
            if (chatGroups.get(i).getChatGroupName().contains(serviceRequestMessage.getName())) {
                groupAlreadyExists = true;
                break;
            }
        }
        if (!groupAlreadyExists) {
            chatGroups.add(new GroupContainer(serviceRequestMessage.getName()));
            chatGroups.get(chatGroups.size() - 1).getGroupMemberList().add(this);
        }
        else {
            //TODO: maybe change to ServiceReplyMessage error
            TextMessage errorMessage = new TextMessage("Messenger Daemon", this.getName(),
                    false, LocalDateTime.now(), "Error: Group already exists!");
            streamToClient.writeObject(errorMessage);
        }
    }//end private void createChatGroup(ServiceRequestMessage serviceRequestMessage) throws Exception

    private void unicast(BasicMessage message) throws Exception {
        ClientHandlerDaemon messageTarget = null;

        /* searching fo the target client */
        for (int i = 0; i < allClientHandlerDaemons.size(); i++) {
            if (allClientHandlerDaemons.get(i).getName().contains(message.getRecipientAlias())) {
                /* contains might be a problem if it contains the substring! (see String class */
                messageTarget = allClientHandlerDaemons.get(i);
                break;
            }
        }

        /* forwarding the message if the targeted client has been found, otherwise sending back an error textmessage */
        if (messageTarget != null) {
            messageTarget.getStreamToClient().writeObject(message);
            System.out.println("Unicast Message forwarded");
        }
        else {
            //TODO: maybe change to ServiceReplyMessage error
            TextMessage errorMessage = new TextMessage("Messenger Daemon", this.getName(),
                    false, LocalDateTime.now(), "Error: recipient not found!");
            streamToClient.writeObject(errorMessage);
        }
    }//end private void unicast(BasicMessage message) throws Exception

    private void multicast(BasicMessage message) throws Exception {
        GroupContainer chatGroupContainer = null;


        /* searching for the target chat group */
        for (int i = 0; i < chatGroups.size(); i++) {
            if (chatGroups.get(i).getChatGroupName().contains(message.getRecipientAlias())) {
                /* contains might be a problem if it contains the substring! (see String class */
                chatGroupContainer = chatGroups.get(i);
            }
            break;
        }

        /* sending the message to all user daemons in the chat group */
        if (chatGroupContainer != null) {
            for (int i = 0; i < chatGroupContainer.getGroupMemberList().size(); i++) {
                chatGroupContainer.getGroupMemberList().get(i).getStreamToClient().writeObject(message);
            }
            System.out.println("Multicast Message forwarded");
        }
        else {
            //TODO: maybe change to ServiceReplyMessage error
            TextMessage errorMessage = new TextMessage("Messenger Daemon", this.getName(),
                    false, LocalDateTime.now(), "Error: group not found!");
            streamToClient.writeObject(errorMessage);
        }
    }//end private void multicast(BasicMessage message) throws Exception


    // === 7. MAIN ===
}//end public class ClientHandlerDaemon extends Thread

