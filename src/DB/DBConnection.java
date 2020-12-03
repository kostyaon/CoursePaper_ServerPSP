package DB;

import Models.User;

import java.sql.*;

public class DBConnection {
    private String connectionUrl;
    private Connection connection;
    private Statement statement;
    private String sql;
    private ResultSet resultSet;

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

    public String addUser(User user, String password){
        sql = "INSERT INTO Users (Nickname, Specialization, Country)" +
                " VALUES ('"+ user.getNickname() + "', '" + user.getSpecialization() +
                "', '" + user.getCountry() + "');";
        try{
            //Add User
            int rows = statement.executeUpdate(sql);
            System.out.println("SERVER >> " + rows + " row has been added!");

            //Find the UserID
            sql = "SELECT UserID FROM USERS WHERE Nickname='" + user.getNickname() + "';";
            resultSet = statement.executeQuery(sql);
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
            return "Error";
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

    private String findPassword(String nickname){
        String password = null;
        sql = "SELECT UserID FROM Users WHERE Nickname='" + nickname + "';";
        try{
            //Get an ID to find a password
            int id;
            resultSet = statement.executeQuery(sql);
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
        }
        return password;
    }

}
