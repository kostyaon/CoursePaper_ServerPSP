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

    public String getAccess(String nickname, String password){
        String pswrd= findPassword(nickname);
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


    /*public static void start() {
        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=CP_DB;integratedSecurity=true";

        try (Connection connection = DriverManager.getConnection(connectionUrl)){
            System.out.println("Successfully connected to the SQL SERVER!");
            String sql = "SELECT * FROM Users;";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)){
                while(resultSet.next()) {
                    System.out.println(
                            "\nUSER ID:" + resultSet.getInt(1) + "\nNickName:" + resultSet.getString(2)
                                    + "\nSpecialization:" + resultSet.getString(3) + "\nCountry:" + resultSet.getString(4));
                    System.out.println("///////////////////////////////////////////////////////////////////////////////////////////");
                }
                connection.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }*/
}
