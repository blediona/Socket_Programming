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



            // loop kryesor i UDP serverit qe pranon kerkesat nga klientet, i perpunon ato dhe dergon pergjigje
           while (true) {
                byte[] buffer = new byte[BUFFER_SIZE];
                DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(requestPacket);

                String request = new String(
                        requestPacket.getData(),
                        0,
                        requestPacket.getLength(),
                        StandardCharsets.UTF_8
                ).trim();

                SocketAddress clientAddress = requestPacket.getSocketAddress();

                System.out.println("Kerkese nga " + clientAddress + ": " + request);

                String response = processRequest(request, clientAddress);

                byte[] responseData = response.getBytes(StandardCharsets.UTF_8);
                DatagramPacket responsePacket = new DatagramPacket(
                        responseData,
                        responseData.length,
                        requestPacket.getAddress(),
                        requestPacket.getPort()
                );

                serverSocket.send(responsePacket);
            }

            } catch (Exception e) {
                System.out.println("Gabim ne server: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }

 private static void setupUsers() {
        allowedUsers.put("admin1", EnumSet.of(Permission.READ, Permission.WRITE, Permission.EXECUTE));
        allowedUsers.put("client2", EnumSet.of(Permission.READ));
        allowedUsers.put("client3", EnumSet.of(Permission.READ));
        allowedUsers.put("client4", EnumSet.of(Permission.READ));
    }
