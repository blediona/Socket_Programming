package server;

import java.net.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

public class UDPServer {

    private static final int PORT = 9876;
    private static final int BUFFER_SIZE = 4096;

    private static final Map<String, Set<Permission>> allowedUsers = new HashMap<>();
    private static final Map<SocketAddress, ClientInfo> connectedClients = new HashMap<>();

    private static FileService fileService;


    public static void main(String[] args) {
        try {

            //pjesa e kodit per inicializimin e UDP serverit/ pergatitja e tij per komunikim
            setupUsers();
            fileService = new FileService("server_files");

            DatagramSocket serverSocket = new DatagramSocket(PORT);

            String serverIp = InetAddress.getLocalHost().getHostAddress();

            System.out.println("==================================");
            System.out.println("UDP Server eshte aktiv");
            System.out.println("IP: " + serverIp);
            System.out.println("PORT: " + PORT);
            System.out.println("==================================");



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
