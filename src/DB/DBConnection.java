package DB;

import Models.Answer;
import Models.Question;
import Models.Rating;
import Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {
    private String connectionUrl;
    private Connection connection;
    private Statement statement;
    private String sql;

    public DBConnection(){
        try{
            this.connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=CP_DB;integratedSecurity=true";
            this.connection = DriverManager.getConnection(connectionUrl);
            this.statement = connection.createStatement();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public String addUserDB(User user, String password){
        sql = "INSERT INTO Users (Nickname, Specialization, Country)" +
                " VALUES ('"+ user.getNickname() + "', '" + user.getSpecialization() +
                "', '" + user.getCountry() + "');";
        try{
            //Add User
            int rows = statement.executeUpdate(sql);
            System.out.println("SERVER >> " + rows + " row has been added!");

            //Find the UserID
            sql = "SELECT UserID FROM USERS WHERE Nickname='" + user.getNickname() + "';";
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            int id = resultSet.getInt("UserID");

            //Add password
            sql = "INSERT INTO PrivateData (UserPassword, UserID)" +
                    " VALUES ('"+ password + "', " + id + ");";
            rows = statement.executeUpdate(sql);
            System.out.println("SERVER >> " + rows + " password has been added!");

            return "Success";
        }catch (SQLException e){
            e.printStackTrace();
            return "Error: You are not unique! Come up with a new nickname!";
        }
    }

    public String getAccess(String nickname, String password){
        String pswrd = findPassword(nickname);
        if (pswrd.equals(password)){
            return "Access";
        }else{
            return "Denied";
        }
    }

    public float countSumRateDB(String nickname){
        int counter = 0;
        float sumRate = 0;
        sql = "SELECT UserID FROM Users WHERE Nickname='" + nickname + "';";
        try{
            //Find UserID
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            int id = resultSet.getInt("UserID");

            //Count summary Rating
            sql = "SELECT Rating FROM Rating WHERE UserID=" + id + ";";
            resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                counter++;
                sumRate += resultSet.getInt("Rating");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        sumRate /= counter;
        return sumRate;
    }

    public List<Question> questionList(String questTheme, int questLevel, int numberOfQuest){
        //TODO: We can use a fixed size of Array
        List<Question> questList = new ArrayList<>();

        //TODO: CHANGE TOP 3 on TOP 10
        sql = "SELECT Question, QuestID FROM Question WHERE Question IN" + "(SELECT TOP " + numberOfQuest + " Question FROM Question WHERE QuestTheme='"
                + questTheme + "' AND QuestLevel=" + questLevel + " ORDER BY NEWID());";
        try{
            //Get 10 random Question
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                Question question = new Question(resultSet.getString("Question"), resultSet.getInt("QuestID"));
                questList.add(question);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        //Return 10 question for the WHOLE TEST
        return questList;
    }

    public List<Answer> answerList(int questID){
        //TODO: We can use a fixed size of Array
        List<Answer> answerList= new ArrayList<>();
        sql = "SELECT Answer, Correctness FROM Answer"
        + " INNER JOIN QuestAnswer ON Answer.AnswerID = QuestAnswer.AnswerID"
       + " WHERE QuestID=" + questID + ";";
        try{
            //Get 3 Answers
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                //Fill Answers for our List
                Answer answer = new Answer(resultSet.getString("Answer"), resultSet.getBoolean("Correctness"));
                answerList.add(answer);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        //Return a list of answers to the ONE question
        return answerList;
    }

    private String findPassword(String nickname){
        String password = null;
        sql = "SELECT UserID FROM Users WHERE Nickname='" + nickname + "';";
        try{
            //Get an ID to find a password
            int id;
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            id = resultSet.getInt("UserID");
            System.out.println("CLIENT >> ID = " + id);

            //Find the password by id
            sql = "SELECT UserPassword FROM PrivateData WHERE UserID=" + id + ";";
            resultSet = statement.executeQuery(sql);
            resultSet.next();
            password = resultSet.getString("UserPassword");
        }catch (Exception e){
            e.printStackTrace();
            password = "Error";
        }
        return password;
    }

    public User findUser(String nickname){
        User user = null;
        sql = "SELECT * FROM Users WHERE Nickname='" + nickname + "';";
        try{
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();

            user = new User(resultSet.getString("Nickname"), resultSet.getString("Specialization"), resultSet.getString("Country"));
            user.setUserID(resultSet.getInt("UserID"));
        }catch (SQLException e){
            e.printStackTrace();
        }
        return user;
    }

    public String setRating(Rating rating){
        sql = "INSERT INTO Rating (TestTheme, TestLevel, Rating, UserID)" +
                " VALUES ('" + rating.getTestTheme() + "', '" + rating.getTestLevel() + "', " + rating.getRating() +
                ", " + rating.getUserID() + ");";
        int rows;
        String res;
        try{
            rows = statement.executeUpdate(sql);
            System.out.println("SERVER >> " + rows + " Rating has been added!");
            res = "Success";
        }catch (SQLException e){
            e.printStackTrace();
            res = "Error";
        }
        return res;
    }
}
