// Viktor Spudil beta
package com.binarychat.systemMessageTypes;

import java.io.Serializable;

public class ServiceRequestMessage implements Serializable {
    // === 0. NOTES ===
    // === 1. CLASS VARIABLES ===
    // === 2. OBJECT VARIABLES ===
    private String name;
    private ServiceRequestType serviceRequest;


    // === 3. CONSTRUCTORS ===
    // --- 3.1 STATIC BLOCKS ---
    // --- 3.2 INSTANCE INITIALIZER ---
    // --- 3.3 REAL CONSTRUCTORS ---
    public ServiceRequestMessage(String name, ServiceRequestType serviceRequest) {
        this.name = name;
        this.serviceRequest = serviceRequest;
    }//end public ServiceRequestMessage(String name, ServiceRequestType serviceRequest)


    // === 4. STATIC METHODS ===
    // === 5. GETTER AND SETTER ===
    public String getName() {
        return name;
    }//end public String getName()

    public ServiceRequestType getServiceRequest() {
        return serviceRequest;
    }//end ServiceRequestType getServiceRequest()


    // === 6. MISCELLANEOUS OBJECT METHODS ===
    // === 7. MAIN ===
}

