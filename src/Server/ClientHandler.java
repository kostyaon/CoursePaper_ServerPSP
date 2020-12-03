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
        String nickname;
        String password;
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

    private void countRate(){
        String nickname;
        try{
            //Get nickname
            nickname = (String)inObj.readObject();
            System.out.println("SERVER >> CLIENT Nickname: " + nickname);

            //Connect to the DB and return summary rating
            DBConnection connection = new DBConnection();
            float rating = connection.countSumRateDB(nickname);
            outObj.writeObject(rating);
            outObj.flush();
            connection.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addUser(){
        User user;
        String password;
        try{
            //Get User to add in DB
            user = (User) inObj.readObject();
            System.out.println("CLIENT >> User: " + user.toString());

            //Get password to add in DB
            password = (String) inObj.readObject();

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
                //Choose a functionality
                choose = (String) inObj.readObject();

                switch (choose){
                    case "Login":
                        checkPassword();
                        countRate();
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
