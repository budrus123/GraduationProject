
import java.sql.*;


public class Driver {

    public static void main(String[] args) {

        System.out.println("Graduation Project");
//        try{
//
//            Connection connection = Database.connect();
//            ResultSet rs = Helper_Functions.executeQuery(connection,"SELECT DEPARTMENT FROM student_table");
//            while (rs.next()) {
//                String s = rs.getString("DEPARTMENT");
//                System.out.println(s);
//            }
//
//        } catch (Exception e) {
//            throw new IllegalStateException("Something error !!", e);
//        }

        String url = "jdbc:mysql://localhost:3306/exams";
        String username = "root";
        String password = "";

        System.out.println("Connecting database...");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Database connected!");


            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT DEPARTMENT FROM student_table");

            while (rs.next()) {
                String s = rs.getString("DEPARTMENT");
                System.out.println(s);
            }


        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }



    }

}
