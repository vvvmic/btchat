package com.binarychat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class MessageScreen implements Runnable {
    // TODO: 26.12.2022 In and Output messages

    private Socket socket;
    private BufferedReader inputMessage;

    public MessageScreen(Socket socket) throws IOException {
        this.socket = socket;
        this.inputMessage = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = inputMessage.readLine();
                System.out.println(message);
            }
        } catch (SocketException exception) {
            System.out.println("You left the chat-room");
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            try {
                inputMessage.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
