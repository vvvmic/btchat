package com.binarychat.multiGroupVersion.userMessageTypes;

import java.io.Serializable;
import java.time.*;

public class BasicMessage implements Serializable {
    // === 0. NOTES ===
    // === 1. CLASS VARIABLES ===
    // === 2. OBJECT VARIABLES ===
    private String senderAlias;
    private String recipientAlias;
    private boolean isMulticastMessage;
    private final LocalDateTime createdTimeStamp;


    // === 3. CONSTRUCTORS ===
    // --- 3.1 STATIC BLOCKS ---
    // --- 3.2 INSTANCE INITIALIZER ---
    // --- 3.3 REAL CONSTRUCTORS ---
    /* constructor when a message is being created, all fields are mandatory */
    public BasicMessage(String senderAlias, String recipientAlias, boolean isMulticastMessage, LocalDateTime createdTimeStamp) {
        this.senderAlias = senderAlias;
        this.recipientAlias = recipientAlias;
        this.isMulticastMessage = isMulticastMessage;
        this.createdTimeStamp = createdTimeStamp;
    }//end public BasicMessage(String senderAlias, String recipientAlias, boolean isMulticastMessage, LocalDateTime createdTimeStamp)


    // === 4. STATIC METHODS ===
    // === 5. GETTER AND SETTER ===
    public String getSenderAlias() {
        return senderAlias;
    }//end public String getName()

    public String getRecipientAlias() {
        return this.recipientAlias;
    }//end  public String getRecipientName()

    public LocalDateTime getCreatedTimeStamp() {
        return this.createdTimeStamp;
    }//end public LocalDateTime getCreatedTimeStamp()

    public boolean getIsMulticastMessage() {
        return this.isMulticastMessage;
    }//end public boolean isMulticastMessage()


    // === 6. MISCELLANEOUS OBJECT METHODS ===
    // === 7. MAIN ===
}//end public abstract class BasicMessage

