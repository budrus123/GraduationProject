import org.chocosolver.solver.variables.IntVar;

import java.sql.*;
import java.util.ArrayList;

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

    public static void insertSol(Connection connection,  ArrayList<IntVar> variables,ArrayList<Course> courseAL) throws SQLException {
        int solution_id = 1;
        Statement stmt = connection.createStatement();

        String query = "DELETE FROM solution";
        stmt.executeUpdate(query);  // delete all records in solution.
        String insertQuery = Helper_Functions.getSolutionQuery(solution_id, variables, courseAL, "solution");
        Statement stmt2 = connection.createStatement();
        stmt2.executeUpdate(insertQuery);
    }

}
