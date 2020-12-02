import java.sql.*;

public class DBConnection {
    public static void start() {
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
    }
}
