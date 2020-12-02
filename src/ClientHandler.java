import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread{
    final Socket socket;
    final DataInputStream in;
    final DataOutputStream out;

    public ClientHandler(Socket socket, DataInputStream in, DataOutputStream out){
        this.socket = socket;
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        String received;
        String toSend;

        while(true){
            try {
                out.writeUTF("Choose the number from 1 to 3!");

                received = in.readUTF();

                if (received.equalsIgnoreCase("exit")){
                    System.out.println("Client want to exit...");
                    this.socket.close();
                    System.out.println("Connection is closed!");
                    break;
                }

                switch (received) {
                    case "1" -> {
                        toSend = "You send to server 1";
                        out.writeUTF(toSend);
                    }
                    case "2" -> {
                        toSend = "You send to server 2";
                        out.writeUTF(toSend);
                    }
                    case "3" -> {
                        toSend = "You send to server 3";
                        out.writeUTF(toSend);
                    }
                    default -> toSend = "You send INVALID number";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            this.in.close();
            this.out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
