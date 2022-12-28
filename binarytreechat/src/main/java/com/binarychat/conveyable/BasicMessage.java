package com.binarychat.conveyable;

import java.time.*;

public abstract class BasicMessage {
    // === 0. NOTES ===
    // === 1. CLASS VARIABLES ===
    // === 2. OBJECT VARIABLES ===
    private String sender;
    private String recipient;
    private final LocalDateTime createdTimeStamp;
    private LocalDateTime receivedTimeStamp;


    // === 3. CONSTRUCTORS ===
    // --- 3.1 STATIC BLOCKS ---
    // --- 3.2 INSTANCE INITIALIZER ---
    // --- 3.3 REAL CONSTRUCTORS ---
    /* constructor when a message is being created, all fields are mandatory */
    public BasicMessage(String sender, String recipient, LocalDateTime createdTimeStamp) {
        this.sender = sender;
        this.recipient = recipient;
        this.createdTimeStamp = createdTimeStamp;
    }//end public BasicMessage(String sender, String recipient, LocalDateTime createdTimeStamp)


    // === 4. STATIC METHODS ===
    // === 5. GETTER AND SETTER ===
    public String getSender() {
        return this.sender;
    }//end public String getSender()

    public String getRecipient() {
        return this.recipient;
    }//end  public String getRecipient()

    public LocalDateTime getCreatedTimeStamp() {
        return this.createdTimeStamp;
    }//end public LocalDateTime getCreatedTimeStamp()

    public LocalDateTime getReceivedTimeStamp() {
        return this.receivedTimeStamp;
    }//end  public LocalDateTime getReceivedTimeStamp()

    public void setReceivedTimeStamp(LocalDateTime receivedTimeStamp) {
        this.receivedTimeStamp = receivedTimeStamp;
    }//end public void setReceivedTimeStamp(LocalDateTime receivedTimeStamp)


    // === 6. MISCELLANEOUS OBJECT METHODS ===
    // === 7. MAIN ===
}//end public abstract class BasicMessage