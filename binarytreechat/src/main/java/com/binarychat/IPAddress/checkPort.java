package com.binarychat.IPAddress;

public class checkPort {
    public static boolean validatePort(int port) {
        return port > 1023 && port < 49152; //user ports
    }
}
