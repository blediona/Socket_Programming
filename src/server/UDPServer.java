package server;

import java.net.*;
import java.util.*;

public class UDPServer {
    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(9876);
            byte[] receiveData = new byte[1024];

            // me kete pjese te kodit arrijme ti ruajme klientet ne nje list
            HashSet<SocketAddress> clients = new HashSet<>();


            System.out.println("Server is running...");






        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
