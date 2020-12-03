package Server;

import DB.DBConnection;
import Models.User;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    final Socket socket;
    final DataInputStream in;
    final DataOutputStream out;
    final ObjectInputStream inObj;
    final ObjectOutputStream outObj;

    public ClientHandler(Socket socket, DataInputStream in, DataOutputStream out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.inObj = null;
        this.outObj = null;
    }

    public ClientHandler(Socket socket, ObjectInputStream inObj, ObjectOutputStream outObj){
        this.socket = socket;
        this.inObj = inObj;
        this.outObj = outObj;
        this.in = null;
        this.out = null;
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
            connection.closeConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addUser(){
        User user;
        String password;
        try{
            user = (User) inObj.readObject(); //Get User to add in DB
            System.out.println("CLIENT >> User: " + user.toString());

            password = (String) inObj.readObject();//Get password to add in DB

            //Connect to the Db and add user
            DBConnection connection = new DBConnection();
            String res = connection.addUser(user, password);
            outObj.writeObject(res);
            outObj.flush();
            connection.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //checkPassword();
        addUser();
        try {
            this.in.close();
            this.out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
