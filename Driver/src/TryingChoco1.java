/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

/**
 *
 * @author Mahmoud
 */
public class TryingChoco1 {

    /**
     * @param args the command line arguments
     */
    
    //model for the problem
    static Model model = new Model("my first problem");
    
    //arraylists for the courses, variabls (same as courses) and the students
    static ArrayList<Course> courseAL = new ArrayList<Course>();
    static ArrayList<IntVar> vars = new ArrayList<IntVar>();
    static ArrayList<Student> students = new ArrayList<Student>();
    
    //our connection
    static Connection connection;

    public static void main(String[] args) throws SQLException {
        // TODO code application logic here
       

        //creating students to test
        /* no need fo this
        Student s1 = new Student(1, "d");
        Student s2 = new Student(2, "d");
        Student s3 = new Student(3, "d");
        Student s4 = new Student(4, "d");
        Student s5 = new Student(5, "d");
        Student s6 = new Student(6, "d");

        Course[] c = new Course[4];
        c[0] = new Course("phys141");
        c[1] = new Course("math141");
        c[2] = new Course("comp141");
        c[3] = new Course("java");

        c[0].addStudent(s1);
        c[0].addStudent(s3);
        //c[0].addStudent(s4);
        //c[0].addStudent(s6);

        c[1].addStudent(s1);
        c[1].addStudent(s3);
        c[1].addStudent(s2);
        c[1].addStudent(s6);

        c[2].addStudent(s4);
        c[2].addStudent(s1);

        c[3].addStudent(s2);
        c[3].addStudent(s4);
        c[3].addStudent(s5);
        c[3].addStudent(s6);

        //adding courses to students
//        s1.addCourse("phys141");
//        s1.addCourse("math141");
//        
//        s2.addCourse("comp141");
//        s3.addCourse("math141");    
        // 2. Create variables 
        IntVar phys141 = model.intVar("phys141", new int[]{1, 2, 3, 4}); // phys141 in 1 2 3  
        IntVar math141 = model.intVar("math141", new int[]{1, 2, 3, 4}); // math141 in 1 2 3 
        IntVar comp141 = model.intVar("comp141", new int[]{1, 2, 3, 4}); // comp141 in 1 2 
        IntVar java = model.intVar("java", new int[]{1, 2, 3, 4}); // comp141 in 1 2 

        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 4; j++) {
                if (haveCommonStudents(c[i], c[j])) {
                    model.allDifferent(getCourseIntVar(c[i]), getCourseIntVar(c[j])).post();
                }
            }
        }
        
        */

      
        
        
        //connecting to the database
        String url = "jdbc:mysql://localhost:3306/exams";
        String username = "root";
        String password = "";

        System.out.println("Connecting database...");

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected!");

            
            /* this is to fill both
            the course and variables 
            arraylists from the course
            table from the database
            */
            Statement stmt = connection.createStatement();
            ResultSet getCourses = stmt.executeQuery("SELECT * FROM course_table");
            while (getCourses.next()) {
               
//                IntVar temp = model.intVar(getCourses.getString("COURSE_LABEL"), new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
//                    16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45}); // phys141 in 1 2 3  
                
                IntVar temp = model.intVar(getCourses.getString("COURSE_LABEL"), new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45}); // phys141 in 1 2 3  
                vars.add(temp);
                Course co = new Course(Integer.valueOf(getCourses.getString("id")), getCourses.getString("COURSE_LABEL"), getCourses.getString("COURSE TITLE"), getCourses.getString("DEPT"));
                courseAL.add(co);

               
            }
            // end filling up the course arraylist and vars as well
            
            
            
            /*
            this is to fill the student arraylist (all the students)
            */
            Statement stmt2 = connection.createStatement();
            ResultSet getStudents = stmt.executeQuery("SELECT * FROM student_table");
            while (getStudents.next()) {
                //String s = rs.getString("id")+" "+rs.getString("COURSE_LABEL");
                Student s = new Student(Integer.valueOf(getStudents.getString("id")), getStudents.getString("DEPARTMENT"));
                students.add(s);
                // System.out.println(s);
            }

        } catch (SQLException ex) {
            Logger.getLogger(TryingChoco1.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        
        /*
        first function is to fill the student array list 
        in each course so we know who takes each course
        course A now has an arraylist of students who take it
        */
        fillStudentTakesCourse();
        
        
        /*
        this is to create the hard constraints
        by checking if 2 courses have the same students
        we tell the MODEL that their values can't be the same
        */
        fillHardConst();
        
        
        /*
        if the model finds a solutin, print it
        else
        print can't find a solution
        */
        if(model.getSolver().solve()){
            for(int i=0;i<vars.size();i++){
            System.out.println("i equals "+i+" "+vars.get(i));
        }
        }
        
        else{
            System.out.println("no solution");
        }
        

    }

    //################################################################################################################
    //################################################################################################################
    //################################################################################################################
    //################################################################################################################
    //################################################################################################################
    public static void fillHardConst() throws SQLException {
        for (int i = 0; i < courseAL.size(); i++) {
            for (int j = i + 1; j < courseAL.size(); j++) {
                if (haveCommonStudents(courseAL.get(i), courseAL.get(j))) {
                    model.allDifferent(vars.get(i), vars.get(j)).post();
                }
            }
        }
    }

    //################################################################################################################
    //################################################################################################################
    //################################################################################################################
    //################################################################################################################
    //################################################################################################################
    public static void fillStudentTakesCourse() throws SQLException {

//    static ArrayList courseAL = new ArrayList<Course>();
//    static ArrayList vars = new ArrayList<IntVar>();
//    static ArrayList students = new ArrayList<Student>();
/*
-- Dumping structure for table exams.student_course_table
CREATE TABLE IF NOT EXISTS `student_course_table` (
  `STUDENT_NUMBER` int(10) unsigned NOT NULL,
  `COURSE_LABEL` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table exams.student_course_table: ~63,296 rows (approximately)
/*!40000 ALTER TABLE `student_course_table` DISABLE KEYS ;
INSERT INTO `student_course_table` (`STUDENT_NUMBER`, `COURSE_LABEL`) VALUES
	(1, 'JOUR412'),
         */
        for (int i = 0; i < courseAL.size(); i++) {
            Statement stmt = connection.createStatement();
            //String s="SELECT * FROM student_course_table where COURSE_LABEL='"+courseAL.get(i).getLabel()+"'";
            ResultSet course_student = stmt.executeQuery("SELECT * FROM student_course_table where COURSE_LABEL='" + courseAL.get(i).getLabel() + "'");

            while (course_student.next()) {
                courseAL.get(i).addStudent(students.get(Integer.valueOf(course_student.getString("STUDENT_NUMBER")) - 1));
                // System.out.println(course_student.getString("STUDENT_NUMBER"));
            }

            //System.out.println(s);
            //System.out.println(courseAL.get(0).getLabel());
        }

    }

//################################################################################################################
//################################################################################################################
//################################################################################################################
//################################################################################################################
//################################################################################################################
    public static IntVar getCourseIntVar(Course c) {

        for (int i = 0; i < model.getNbVars(); i++) {
            if (c.getLabel().equals(model.getVar(i).getName())) {
                return (IntVar) model.getVar(i);
            }
        }
        return null;
    }

    //################################################################################################################
    //################################################################################################################
    //################################################################################################################
    //################################################################################################################
    //################################################################################################################
    public static boolean haveCommonStudents(Course c1, Course c2) {
        ArrayList<Student> a1 = new ArrayList<Student>();;
        ArrayList<Student> a2 = new ArrayList<Student>();;

        a1 = c1.getAl();
        a2 = c2.getAl();

        for (int j = 0; j < a2.size(); j++) {
            if (a1.contains(a2.get(j))) {
                return true;
            }
        }
        return false;
    }
}
