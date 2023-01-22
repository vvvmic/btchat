package com.binarychat.ipAddress;

import java.util.regex.Pattern;

public class CheckIPAddress {
    /**
     * checks if the passed IP Address is correct by comparing it with the pattern
     */
    private static final Pattern PATTERN = Pattern.compile("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$");

    public static boolean validateIP(final String ipAddress) {
        return PATTERN.matcher(ipAddress).matches();
    }
}
