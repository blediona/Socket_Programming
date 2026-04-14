package client;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;


public class UDPClient {
    public static void main(String[] args){
        try{
            DatagramSocket clientSocket = new DatagramSocket();

            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 9876;

            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter your name: ");
            String clientName = scanner.nextLine();

            System.out.println("You can start typing messages: ");

        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
