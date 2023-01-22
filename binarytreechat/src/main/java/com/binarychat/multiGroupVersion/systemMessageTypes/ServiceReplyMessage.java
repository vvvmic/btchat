package com.binarychat.multiGroupVersion.systemMessageTypes;

import java.io.Serializable;

public class ServiceReplyMessage implements Serializable {
    // === 0. NOTES ===
    // === 1. CLASS VARIABLES ===
    // === 2. OBJECT VARIABLES ===
    private ServiceReplyType serviceReply;


    // === 3. CONSTRUCTORS ===
    // --- 3.1 STATIC BLOCKS ---
    // --- 3.2 INSTANCE INITIALIZER ---
    // --- 3.3 REAL CONSTRUCTORS ---
    public ServiceReplyMessage(ServiceReplyType serviceReply) {
        this.serviceReply = serviceReply;
    }//end public ServiceReplyMessage(ServiceReplyType serviceReply)


    // === 4. STATIC METHODS ===
    // === 5. GETTER AND SETTER ===
    public ServiceReplyType getServiceReply(){
        return this.serviceReply;
    }//end public ServiceReplyType getServiceReply()


    // === 6. MISCELLANEOUS OBJECT METHODS ===
    // === 7. MAIN ===
}

