package Server;

import DB.DBConnection;
import Models.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClientHandler extends Thread {
    final Socket socket;
    final ObjectInputStream inObj;
    final ObjectOutputStream outObj;

    public ClientHandler(Socket socket, ObjectInputStream inObj, ObjectOutputStream outObj){
        this.socket = socket;
        this.inObj = inObj;
        this.outObj = outObj;
    }

    private String checkPassword() {
        String nickname;
        String password;
        String access = null;
        try {
            nickname = (String) inObj.readObject(); //Get TNickname
            System.out.println("CLIENT >> NICKNAME:" + nickname);
            password = (String) inObj.readObject(); //Get TPassword
            System.out.println("CLIENT >> PASSWORD:" + password);

            //Connect to the DB and find the password
            DBConnection connection = new DBConnection();
            access = connection.getAccess(nickname, password);
            outObj.writeObject(access);
            outObj.flush();
            connection.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
            access = "ERROR";
        }
        return access;
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

    private void sendAnswerList(List<Question> questionList, int index){
        int questId = 0;
        try{
            //Connect to the DB and
            DBConnection connection = new DBConnection();
            questId = questionList.get(index).getQuestID();

            //Get answerList for the questID
            List<Answer> answerList = connection.answerList(questId);

            //Send our List of all Answers to the client
            outObj.writeObject(answerList);
            outObj.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendQuestList(){
        String errMSG;

        String theme;
        int level;
        int numberOfQuest;
        List<Question> questionList = null;
        try {
            //Get selected topic
            theme = (String) inObj.readObject();
            System.out.println("SERVER >> CLIENT THEME:" + theme);

            //Get selected level
            level = (int) inObj.readObject();
            System.out.println("SERVER >> CLIENT LEVEL:" + level);

            //Get selected number of question
            numberOfQuest = (int) inObj.readObject();
            System.out.println("SERVER >> NUMBER OF QUEST:" + numberOfQuest);

            //Get A list of question from DB
            DBConnection connection = new DBConnection();
            questionList = connection.questionList(theme, level, numberOfQuest);
            outObj.writeObject(questionList);
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

    private void sendUser(){
        String nickname;
        User user;
        try{
            //Get a nickname
            nickname = (String) inObj.readObject();

            //Retrieve data from DB
            DBConnection connection = new DBConnection();
            user = connection.findUser(nickname);

            //Send user to the Client
            outObj.writeObject(user);
            outObj.flush();
            connection.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendRating(){
        Rating rating;
        String res;
        try{
            //Get Rating
            rating = new Rating();
            rating = (Rating) inObj.readObject();

            //Retrieve data from DB
            DBConnection connection = new DBConnection();
            res = connection.setRating(rating);

            outObj.writeObject(res);
            outObj.flush();
            connection.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendPrivateData(){
        PrivateData data;
        int userID;
        try{
            //Get UserID
            userID = (int) inObj.readObject();

            //Retrieve from DB
            DBConnection connection = new DBConnection();
            data = connection.checkRole(userID);

            outObj.writeObject(data);
            outObj.flush();
            connection.closeConnection();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendRatingList(){
        int userID;
        List<Rating> ratingList;
        try{
            userID = (int) inObj.readObject();

            //Retrieve from DB
            DBConnection connection = new DBConnection();
            ratingList = connection.getRatingList(userID);

            outObj.writeObject(ratingList);
            outObj.flush();
            connection.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendUserList(){
        List<User>userList;
        try {
            DBConnection connection = new DBConnection();
            userList = connection.getUserList();

            outObj.writeObject(userList);
            outObj.flush();
            connection.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateAdmin(){
        try{
            //Retrieve userID
            int userID = (int) inObj.readObject();

            DBConnection connection = new DBConnection();
            String success = connection.updateAdmin(userID);

            outObj.writeObject(success);
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
                        String access = checkPassword();
                        if (access.equalsIgnoreCase("Access")){
                            sendUser();
                            sendPrivateData();
                            countRate();
                        }
                        break;
                    case "Signup":
                        addUser();
                        break;
                    case "StartTest":
                        sendQuestList();
                        break;
                    case "Answer":
                        List<Question> questionList = (List<Question>) inObj.readObject();
                        int index = (int) inObj.readObject();
                        sendAnswerList(questionList, index);
                        break;
                    case "Insert rate":
                        sendRating();
                        break;
                    case "Rating":
                        sendRatingList();
                        break;
                    case "User list":
                        sendUserList();
                        break;
                    case "Admin":
                        updateAdmin();
                        break;
                    case "Exit":
                        socket.close();
                        System.out.println("SERVER >> CLIENT IS DISCONNECTED");
                        return;
                }

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
