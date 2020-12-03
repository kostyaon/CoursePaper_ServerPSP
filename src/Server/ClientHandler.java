package Server;

import DB.DBConnection;
import Models.User;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    final Socket socket;
    final ObjectInputStream inObj;
    final ObjectOutputStream outObj;

    public ClientHandler(Socket socket, ObjectInputStream inObj, ObjectOutputStream outObj){
        this.socket = socket;
        this.inObj = inObj;
        this.outObj = outObj;
    }

    private void checkPassword() {
        String nickname = null;
        String password = null;
        try {
            nickname = (String) inObj.readObject(); //Get TNickname
            System.out.println("CLIENT >> NICKNAME:" + nickname);
            password = (String) inObj.readObject(); //Get TPassword
            System.out.println("CLIENT >> PASSWORD:" + password);

            //Connect to the DB and find the password
            DBConnection connection = new DBConnection();
            String access = connection.getAccess(nickname, password);
            outObj.writeObject(access);
            outObj.flush();
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
            String res = connection.addUserDB(user, password);
            outObj.writeObject(res);
            outObj.flush();
            connection.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String choose;
        try {
            while(true){
                //Choose a functionallity
                choose = (String) inObj.readObject();

                switch (choose){
                    case "Login":
                        checkPassword();
                        break;
                    case "Signup":
                        addUser();
                        break;
                    case "Exit":
                        socket.close();
                        System.out.println("SERVER >> CLIENT IS DISCONNECTED");
                        break;
                }

                //TODO: Add extra functionallity

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            inObj.close();
            outObj.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
