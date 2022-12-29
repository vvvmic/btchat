package com.binarychat.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    //Implementierung ServerSocket
    private ServerSocket serverSocket;

    //Konstruktor
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    //Methode startServer
    public void startServer(){
        InetAddress ip;
        String hostname;

        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            System.out.println("Your current IP address : " + ip);
            System.out.println("Your current Hostname : " + hostname);
            while(!serverSocket.isClosed()){
                                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e){

        }
    }

    //avoiding nested try loops if an error occurs
    public void closeServerSocket(){
        try {
            if(serverSocket != null){
                serverSocket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //main
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1236);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
