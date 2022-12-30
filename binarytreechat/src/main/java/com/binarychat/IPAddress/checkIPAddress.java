package com.binarychat.IPAddress;

import java.util.regex.Pattern;

public class checkIPAddress {
    private static final Pattern PATTERN = Pattern.compile("^([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\." +
            "([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\." +
            "([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\." +
            "([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$");

    public static boolean validateIP(final String ipAddress) {
        return PATTERN.matcher(ipAddress).matches();
    }
}
