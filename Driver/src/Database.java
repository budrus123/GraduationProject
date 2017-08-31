import java.sql.*;

public class Database {



    public static Connection connect(){

        String url = "jdbc:mysql://"+Constants.HOST_NAME+":"+Constants.PORT_NUMBER+"/"+Constants.DATABASE_NAME;

        try (Connection connection = DriverManager.getConnection(url, Constants.USERNAME, Constants.PASSWORD)) {
            System.out.println("Database connected!");
            return connection;
        } catch (Exception e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

}
