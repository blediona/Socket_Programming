package client;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;


public class UDPClient {
    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        try{
            System.out.println("Shkruaj IP e serverit: ");
            String serverIp = scanner.nextLine().trim();

            System.out.println("Shkruaj portin e serverit: ");
            int serverPort = Integer.parseInt(scanner.nextLine().trim());

            InetAddress serverAddress = InetAddress.getByName(serverIp);
            DatagramSocket clientSocket = new DatagramSocket();

            System.out.println("Lidhja u pergatit me sukses.");
            System.out.println("Komanda e pare duhet te jete: REGISTER <username>");

            while(true){
                System.out.print("Klienti>");
                String message = scanner.nextLine().trim();

                if(message.isBlank()){
                    continue;
                }

                byte[] sendData = message.getBytes(StandardCharsets.UTF_8);
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);

                clientSocket.send(sendPacket);
            }

        } catch (Exception e){
            System.out.println("Gabim ne klient: " + e.getMessage());
            e.printStackTrace();
        } finally{
            scanner.close();
        }
    }

}
