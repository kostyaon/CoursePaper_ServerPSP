package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static int counter = 0;

    public static void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(3333);
        System.out.println("SERVER is starting...");

        while(true){
            Socket clientSocket = null;

            try{
                counter++;

                clientSocket = serverSocket.accept();
                System.out.println("Client №" + counter + "is connected!");

                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                ObjectInputStream inObj = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream outObj = new ObjectOutputStream(clientSocket.getOutputStream());

                System.out.println("Assigning new thread for this client");

                Thread thread = new ClientHandler(clientSocket, inObj, outObj);
                thread.start();

            }catch (Exception e){
                serverSocket.close();
                e.printStackTrace();
            }
        }
    }
}
