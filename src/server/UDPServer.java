package server;

import java.net.*;
import java.util.*;

public class UDPServer {
    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(9876);

            // me kete pjese te kodit arrijme ti ruajme klientet ne nje list
            HashSet<SocketAddress> clients = new HashSet<>();


            System.out.println("Server is running...");


            // pjesa e kodit me te cilen serveri qendron vazhdimisht i hapur dhe ne dispozicion 
            //per te pritur mesazhe te klienteve
            while(true){
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new

                DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                String message = new String(receivePacket.getData(),0, receivePacket.getLength());
                SocketAddress clientAddress = receivePacket.getSocketAddress();

                if(!clients.contains(clientAddress)){
                    clients.add(clientAddress);
                    System.out.println("New client connected: " + clientAddress);
                    System.out.println("Total connected clients: " + clients.size());
                }

                System.out.println("Received from " + clientAddress + ": " + message);

            }


            //pjesa e kodit qe mirret mesazhi nga nje klient dhe shperndahet te klientet e tjere
            
            String response = message;
            byte sendData = response.getBytes();

            for(SocketAddress addr : clients){
                if(!addr.equals(clientAddress)){

                    InetSocketAddress inetAddr = (InetSocketAddress) addr;

                    DatagramPacket sendPacket = new DatagramPacket(
                        sendData,
                        sendData.length,
                        inetAddr.getAddress(),
                        inetAddr.getPort()
                    );
                    serverSocket.send(sendPacket);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
