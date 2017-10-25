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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

/**
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
    static ArrayList<IntVar> variables = new ArrayList<IntVar>();
    static ArrayList<Student> students = new ArrayList<Student>();
     static int constCounter=0;
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


        long startTime = System.currentTimeMillis();


        //connecting to the database
        String url = "jdbc:mysql://localhost:3306/exams";
        String username = "root";
        String password = Password.password; // Sehweil password
        //se7weil i love you <3

        System.out.println("Connecting database...");
        int c=0;
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected!");

            /* this is to fill both
            the course and variables 
            arraylists from the course
            table from the database
            */
            Statement stmt = connection.createStatement();
            ResultSet getCourses = stmt.executeQuery("SELECT * FROM course_table Order By course_table.id");
            while (getCourses.next()) {

//                IntVar temp = model.intVar(getCourses.getString("COURSE_LABEL"), new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
//                    16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45}); // phys141 in 1 2 3  

                IntVar temp = model.intVar(getCourses.getString("COURSE_LABEL"),1,100); // phys141 in 1 2 3
                variables.add(temp);
                Course co = new Course(Integer.valueOf(getCourses.getString("id")), getCourses.getString("COURSE_LABEL"), getCourses.getString("COURSE TITLE"), getCourses.getString("DEPT"),c++);
                courseAL.add(co);


            }
            // end filling up the course arraylist and vars as well



            /*
            this is to fill the student arraylist (all the students)
            */
            Statement stmt2 = connection.createStatement();
            ResultSet getStudents = stmt2.executeQuery("SELECT * FROM student_table");
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
        this is to make sure that any student doesn't have more than 1 exam
        in the same day, by looking at the courses any student takes and
        making all their values different
         */
        //oneMaxForEachStudent();
        for(int p=0;p<students.size();p++){
            if(students.get(p).getAl().size()>=3){
                studentHasThreeOrMore(students.get(p).getAl());
            }
//            if(students.get(p).getAl().size()>=4){
//                studentHasFourOrMore(students.get(p).getAl());
//                //System.out.println(students.get(p).getId());
//            }
        }

        System.out.print("fucked the combos "+ constCounter);






//        Co
        /*
        if the model finds a solutin, print it
        else
        print can't find a solution
        */
        int countOfSolution=0;
//        while(model.getSolver().solve()){
//            for (int i = 0; i < variables.size(); i++) {
//                System.out.println(variables.get(i).getValue());
//            }
//            System.out.println("solution number "+countOfSolution++);
//            System.out.println(" ");
//            System.out.println(" ");
//            System.out.println(" ");
//        }

        model.getSolver().solve();

            System.out.println("### is " + variables.size());

            String insertQuery = Helper_Functions.createInsertQuery(variables,courseAL,"schedule_table");
            System.out.println(insertQuery);
            //
// for (int i = 0; i < variables.size(); i++) {
//                System.out.println(variables.get(i).getValue());
//            }
//            System.out.println("solution number "+countOfSolution++);
//            System.out.println(" ");
//            System.out.println(" ");
//            System.out.println(" ");
            model.getSolver().printStatistics();
            //System.out.println(model);
            //System.out.println();



//        if (model.getSolver().solve()) {
//            for (int i = 0; i < variables.size(); i++) {
//                System.out.println(variables.get(i).getValue());
//            }
//        }
////        else if(model.getSolver().){
////            System.out.println("reached a limit");
////        }
//        else {
//            System.out.println("no solution");
//        }


        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println(elapsedTime / 1000.0);
        //System.out.println(model);

    }


    //################################################################################################################
    //################################################################################################################
    //################################################################################################################
    //################################################################################################################
    //################################################################################################################
    static public void studentHasThreeOrMore(ArrayList<Course> courseAL){

        int[] input = new int[courseAL.size()];    // input array

        for(int i = 0; i < courseAL.size();i++){
            input[i] = courseAL.get(i).getVariableIndex();
        }
        int k = 3;
        List<int[]> subsets = Helper_Functions.allCombination(input,k);
        //System.out.println(variables.get(courseAL.get(i).getVariableIndex()) +" "+variables[i+1]+"  "+variables[j]);

        for (int i = 0; i < subsets.size(); i++)
        {
            //System.out.println(subsets.get(i)[0]+" "+subsets.get(i)[1]+" "+subsets.get(i)[2]);
            Constraint th = new Constraint("Three in a day "+i,
                    new ThreeInADay(new IntVar[]{variables.get(subsets.get(i)[0]),variables.get(subsets.get(i)[1]),
                            variables.get(subsets.get(i)[2])}));
            model.post(th);

            //System.out.println(th);
            constCounter++;

        }

    }


    static public void studentHasFourOrMore(ArrayList<Course> courseAL){

        int[] input = new int[courseAL.size()];    // input array

        for(int i = 0; i < courseAL.size();i++){
            input[i] = courseAL.get(i).getVariableIndex();
        }
        int k = 4;
        List<int[]> subsets = Helper_Functions.allCombination(input,k);
        //System.out.println(variables.get(courseAL.get(i).getVariableIndex()) +" "+variables[i+1]+"  "+variables[j]);

        for (int i = 0; i < subsets.size(); i++)
        {
            Constraint th = new Constraint("Four in two days "+i,
                    new FourExamsInTwoADays(new IntVar[]{variables.get(subsets.get(i)[0]),variables.get(subsets.get(i)[1]),
                            variables.get(subsets.get(i)[2]),variables.get(subsets.get(i)[3])}));
            //model5.allDifferent(variables).post();;
            model.post(th);
            constCounter++;
            //System.out.println(th);

        }



    }

    public static void oneMaxForEachStudent() throws SQLException {

        for (int i = 0; i < students.size(); i++) {
            Statement stmt = connection.createStatement();
            ResultSet getStudentCourses = stmt.executeQuery("SELECT * FROM student_course_table where STUDENT_NUMBER='"+(i+1)+"'");
            Statement stmt2 = connection.createStatement();
            ResultSet getStudentCoursesCount = stmt2.executeQuery("SELECT count(*) FROM student_course_table where STUDENT_NUMBER='" +(i+1)+"'");

            getStudentCoursesCount.next();
            int count=Integer.valueOf(getStudentCoursesCount.getString(1));
            //System.out.println(getStudentCoursesCount.getString(1));
            int q=0;
            if(count>1){
                IntVar [] studentCourses=new IntVar[count];

                while(getStudentCourses.next()){
                    studentCourses[q++]=variables.get(Integer.valueOf(getStudentCourses.getString(2))-1);
                }

                for(int m=0;m<count;m++){
                    for(int n=m;n<count;n++){
                        model.arithm(studentCourses[m], "-",studentCourses[n] , ">", 2).post();

                    }
                }
                //model.allDifferent(studentCourses);
            }


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
                    model.allDifferent(variables.get(i), variables.get(j)).post();


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

        Statement stmt2 = connection.createStatement();
        //String s="SELECT * FROM student_course_table where COURSE_LABEL='"+courseAL.get(i).getLabel()+"'";
        ResultSet course_student2 = stmt2.executeQuery("SELECT * FROM student_course_table");
        while (course_student2.next()){
            courseAL.get(Integer.valueOf(course_student2.getString("course_id")) - 1).addStudent(students.get(Integer.valueOf(course_student2.getString("STUDENT_NUMBER")) - 1));
            students.get((Integer.valueOf(course_student2.getString("STUDENT_NUMBER")) - 1)).addCourse(courseAL.get(Integer.valueOf(course_student2.getString("course_id")) - 1));
        }

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
//        for (int i = 0; i < courseAL.size(); i++) {
//            Statement stmt = connection.createStatement();
//            //String s="SELECT * FROM student_course_table where COURSE_LABEL='"+courseAL.get(i).getLabel()+"'";
//            ResultSet course_student = stmt.executeQuery("SELECT * FROM student_course_table where course_id='"+i+"'");
//
//            while (course_student.next()) {
//                courseAL.get(i).addStudent(students.get(Integer.valueOf(course_student.getString("STUDENT_NUMBER")) - 1));
//                // System.out.println(course_student.getString("STUDENT_NUMBER"));
//            }
//
//            //System.out.println(s);
//            //System.out.println(courseAL.get(0).getLabel());
//        }

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
        ArrayList<Student> a1 = new ArrayList<Student>();
        ;
        ArrayList<Student> a2 = new ArrayList<Student>();
        ;

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
