package com.binarychat.conveyable;

import java.time.LocalDateTime;

public class Textmessage extends BasicMessage {
    // === 0. NOTES ===
    // === 1. CLASS VARIABLES ===
    // === 2. OBJECT VARIABLES ===
    private final String textMessage;


    // === 3. CONSTRUCTORS ===
    // --- 3.1 STATIC BLOCKS ---
    // --- 3.2 INSTANCE INITIALIZER ---
    // --- 3.3 REAL CONSTRUCTORS ---
    public Textmessage(String sender, String recipient, LocalDateTime createdTimeStamp, String textMessage) {
        super(sender, recipient, createdTimeStamp);
        this.textMessage = textMessage;
    }//end public Textmessage(String sender, String recipient, LocalDateTime createdTimeStamp, String textMessage)


    // === 4. STATIC METHODS ===
    // === 5. GETTER AND SETTER ===
    public String getTextMessage() {
        return this.textMessage;
    }//end public String getTextMessage()


    // === 6. MISCELLANEOUS OBJECT METHODS ===
    // === 7. MAIN ===
}//end public class Textmessage extends BasicMessage
