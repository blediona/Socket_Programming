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


    private static void setupUsers() {
            allowedUsers.put("admin1", EnumSet.of(Permission.READ, Permission.WRITE, Permission.EXECUTE));
            allowedUsers.put("client2", EnumSet.of(Permission.READ));
            allowedUsers.put("client3", EnumSet.of(Permission.READ));
            allowedUsers.put("client4", EnumSet.of(Permission.READ));
        }

    //metoda qe kthen mesazh te klienti ne baze te komandes se dhene nga ana e tij
    private static String processRequest(String request, SocketAddress clientAddress) {
            try {
                if (request.isBlank()) {
                    return "Kerkese e zbrazet.";
                }

                String[] parts = request.split(" ", 3);
                String command = parts[0].toUpperCase();

                switch (command) {
                    case "REGISTER":
                        return registerClient(parts, clientAddress);

                    case "LIST":
                        return listFiles(clientAddress);

                    case "READ":
                        return readFile(parts, clientAddress);

                    case "WRITE":
                        return writeFile(parts, clientAddress);

                    case "EXECUTE":
                        return executeFile(parts, clientAddress);

                    case "EXIT":
                        connectedClients.remove(clientAddress);
                        return "Klienti u largua nga serveri.";

                    default:
                        return "Komande e panjohur.";
                }

            } catch (Exception e) {
                return "Gabim: " + e.getMessage();
            }
        }


    //metoda qe verifikon username dhe e regjistron klientin ne server nese eshte i vlefshem
    private static String registerClient(String[] parts, SocketAddress clientAddress) {
            if (parts.length < 2) {
                return "Perdorimi: REGISTER <username>";
            }

            String username = parts[1].trim();

            if (!allowedUsers.containsKey(username)) {
                return "Ky username nuk lejohet.";
            }

            ClientInfo clientInfo = new ClientInfo(username, clientAddress, allowedUsers.get(username));
            connectedClients.put(clientAddress, clientInfo);

            return "Regjistrimi u krye me sukses. Perdoruesi: " + username +
                    " | Privilegjet: " + clientInfo.getPermissions();
        }

    private static ClientInfo getClient(SocketAddress address) {
        return connectedClients.get(address);
    }

    //metoda qe kthen listen e fajllave ne server nese klienti eshte i regjistruar dhe ka read permission
    private static String listFiles(SocketAddress clientAddress) throws Exception {
        ClientInfo client = getClient(clientAddress);

        if (client == null) {
            return "Duhet te beni REGISTER fillimisht.";
        }

        if (!client.hasPermission(Permission.READ)) {
            return "Nuk keni READ permission.";
        }

        return "Files ne server:\n" + fileService.listFiles();
    }

    //metoda qe lexon permbajtjen e nje fajlli te dhene nese klienti ka read permission dhe jep filename korrekt
    private static String readFile(String[] parts, SocketAddress clientAddress) throws Exception {
        ClientInfo client = getClient(clientAddress);

        if (client == null) {
            return "Duhet te beni REGISTER fillimisht.";
        }

        if (!client.hasPermission(Permission.READ)) {
            return "Nuk keni READ permission.";
        }

        if (parts.length < 2) {
            return "Perdorimi: READ <filename>";
        }

        return fileService.readFile(parts[1].trim());
    }

    // metoda qe shkruan tekst ne nje fajll te caktuar nese klienti ka write permission dhe jep parametrat e duhur
    private static String writeFile(String[] parts, SocketAddress clientAddress) throws Exception {
        ClientInfo client = getClient(clientAddress);

        if (client == null) {
            return "Duhet te beni REGISTER fillimisht.";
        }

        if (!client.hasPermission(Permission.WRITE)) {
            return "Nuk keni WRITE permission.";
        }

        if (parts.length < 3) {
            return "Perdorimi: WRITE <filename> <text>";
        }

        String fileName = parts[1].trim();
        String content = parts[2].trim();

        return fileService.writeFile(fileName, content);
    }

    // metoda qe ekzekuton nje fajll te caktuar nese klienti ka execute permission dhe jep filename
    private static String executeFile(String[] parts, SocketAddress clientAddress) throws Exception {
        ClientInfo client = getClient(clientAddress);

        if (client == null) {
            return "Duhet te beni REGISTER fillimisht.";
        }

        if (!client.hasPermission(Permission.EXECUTE)) {
            return "Nuk keni EXECUTE permission.";
        }

        if (parts.length < 2) {
            return "Perdorimi: EXECUTE <filename>";
        }

        return fileService.executeFile(parts[1].trim());
    }
}



