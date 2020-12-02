import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {
    final Socket socket;
    final DataInputStream in;
    final DataOutputStream out;

    public ClientHandler(Socket socket, DataInputStream in, DataOutputStream out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
    }

    private void checkPassword() {
        String nickname = null;
        String password = null;
        try {
            nickname = in.readUTF(); //Get TNickname
            System.out.println("CLIENT >> NICKNAME:" + nickname);
            password = in.readUTF(); //Get TPassword
            System.out.println("CLIENT >> PASSWORD:" + password);

            //Connect to the DB and find the password
            DBConnection connection = new DBConnection();
            String access = connection.getAccess(nickname, password);
            out.writeUTF(access);
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        checkPassword();
        try {
            this.in.close();
            this.out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
