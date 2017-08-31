import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Helper_Functions {

    public static ResultSet executeQuery(Connection connection,String query){

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return rs;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }


}