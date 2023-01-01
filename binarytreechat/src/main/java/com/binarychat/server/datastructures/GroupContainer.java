// Viktor Spudil beta
package com.binarychat.server.datastructures;

import com.binarychat.server.ClientHandlerDaemon;

import java.util.ArrayList;
import java.util.List;

public class GroupContainer {
    // === 0. NOTES ===
    /* this should be a synchronized datastructure */
    //TODO: make this datastructure synchronized


    // === 1. CLASS VARIABLES ===
    // === 2. OBJECT VARIABLES ===
    private String chatGroupName;
    private List<ClientHandlerDaemon> groupMemberList = new ArrayList<>();


    // === 3. CONSTRUCTORS ===
    // --- 3.1 STATIC BLOCKS ---
    // --- 3.2 INSTANCE INITIALIZER ---
    // --- 3.3 REAL CONSTRUCTORS ---
    public GroupContainer(String chatGroupName) {
        this.chatGroupName = chatGroupName;
    }//end public GroupContainer(String chatGroupName)


    // === 4. STATIC METHODS ===
    // === 5. GETTER AND SETTER ===

    public String getChatGroupName() {
        return chatGroupName;
    }//end public String getChatGroupName()

    public List<ClientHandlerDaemon> getGroupMemberList() {
        return groupMemberList;
    }//end public List<ClientHandlerDaemon> getGroupMemberList()

    /*public synchronized void addSync(ClientHandlerDaemon userDaemon) {
        this.groupMemberList.add(userDaemon);
    }//end public synchronized void addSync(ClientHandlerDaemon userDaemon)*/


    // === 6. MISCELLANEOUS OBJECT METHODS ===
    // === 7. MAIN ===
}//end public class GroupContainer

