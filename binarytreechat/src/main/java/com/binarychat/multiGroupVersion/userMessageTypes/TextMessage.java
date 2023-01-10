package com.binarychat.multiGroupVersion.userMessageTypes;

import java.time.*;

public class TextMessage extends BasicMessage {
    // === 0. NOTES ===
    // === 1. CLASS VARIABLES ===
    // === 2. OBJECT VARIABLES ===
    private final String textMessage;


    // === 3. CONSTRUCTORS ===
    // --- 3.1 STATIC BLOCKS ---
    // --- 3.2 INSTANCE INITIALIZER ---
    // --- 3.3 REAL CONSTRUCTORS ---
    public TextMessage(String senderAlias, String recipientAlias, boolean isMulticastMessage,
                       LocalDateTime createdTimeStamp, String textMessage) {
        super(senderAlias, recipientAlias, isMulticastMessage, createdTimeStamp);
        this.textMessage = textMessage;
    }//end public TextMessage(String senderAlias, String recipientAlias, boolean isMulticastMessage, LocalDateTime createdTimeStamp, String textMessage)


    // === 4. STATIC METHODS ===
    // === 5. GETTER AND SETTER ===
    public String getTextMessage() {
        return this.textMessage;
    }//end public String getTextMessage()


    // === 6. MISCELLANEOUS OBJECT METHODS ===
    // === 7. MAIN ===
}//end public class TextMessage extends BasicMessage

