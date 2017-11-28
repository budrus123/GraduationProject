/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;


import static org.chocosolver.solver.search.strategy.Search.activityBasedSearch;

/**
 * @author Mahmoud
 */
public class MainDriver {
        /*
        M A I N     S T A R T   M
        A                       A
        I                       I
        N                       N

        *EXAMINATION TIMETABLING*
           MAHMOUD ABDELKAREEM
             MOHAMMAD SEHWEIL
               AHMAD ZEID
        S                       S
        T                       T
        A                       A
        R                       R
        T                       T

        M A I N     S T A R T   *
        */


    //model for the problem
    static Model model = new Model("my first problem");
    static int fourInTwoCounter = 0;
    //arraylists for the courses, variabls (same as courses) and the students
    static ArrayList<Course> courseAL = new ArrayList<Course>();
    static ArrayList<IntVar> variables = new ArrayList<IntVar>();
    static ArrayList<Student> students = new ArrayList<Student>();
    static int constCounter = 0;
    //our connection
    static Connection connection;
    static double maxMean = 0;
    static double variance = 0;
    static double ourScore = 0;
    static int fourin2total, b2bTotal,end5start8sum;

    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/exams";
        String username = "root";
        String password = Password.password;
        System.out.println("Connecting database...");
        int c = 0;
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
                IntVar temp = model.intVar(getCourses.getString("COURSE_LABEL"), 1, 60); // phys141 in 1 2 3
                variables.add(temp);
                Course co = new Course(Integer.valueOf(getCourses.getString("id")), getCourses.getString("COURSE_LABEL"), getCourses.getString("COURSE TITLE"), getCourses.getString("DEPT"), c++);
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
            Logger.getLogger(MainDriver.class.getName()).log(Level.SEVERE, null, ex);
        }



        /*
        first function is to fill the student array list 
        in each course so we know who takes each course
        course A now has an arraylist of students who take it
        */
        Helper_Functions.fillStudentTakesCourse();

        
        /*
        this is to create the hard constraints
        by checking if 2 courses have the same students
        we tell the MODEL that their values can't be the same
        */
        ConstraintFillingFunctions.fillHardConst();

        /*
        this is to make sure that any student doesn't have more than 1 exam
        in the same day, by looking at the courses any student takes and
        making all their values different
         */
        //oneMaxForEachStudent();
        for (int p = 0; p < students.size(); p++) {
            if (students.get(p).getCourses().size() >= 3) {
                //ConstraintFillingFunctions.studentHasThreeOrMore(students.get(p).getCourses());
                ConstraintFillingFunctions.studentHassThreeOrMoreNumber2(students.get(p).getCourses());

            }
//            if(students.get(p).getCourses().size()>=4){
//                ConstraintFillingFunctions.studentHasFourOrMore(students.get(p).getCourses());
//                //System.out.println(students.get(p).getId());
//            }
        }

        System.out.println("done with the combos " + constCounter);

        /*
        to fill the interect factor
         */
        Helper_Functions.fillInterSectFactor();

        Collections.sort(courseAL);

        //System.out.println(courseAL.size());
        for (int i = 0; i < courseAL.size(); i++) {
            System.out.println(courseAL.get(i).getIntersectFactor() + " " + courseAL.get(i).getLabel() + " var index is:" + courseAL.get(i).getVariableIndex());
        }


        /*
        if the model finds a solutin, print it
        else
        print can't find a solution
        */
        int countOfSolution = 0;

        int solution_id = 1;

        /*inserting the solution
        into the database
         */
        /*
        Statement stmt = connection.createStatement();
        String query = "DELETE FROM solution";
        stmt.executeUpdate(query);  // delete all records in solution.
        String insertQuery = Helper_Functions.getSolutionQuery(solution_id++, variables, courseAL, "solution");
        Statement stmt2 = connection.createStatement();
        stmt2.executeUpdate(insertQuery);
        /*
        done inserting the solution
         */

        new UniversityData(students);
        IntVar[] varArr = variables.toArray(new IntVar[variables.size()]);
        Collections.sort(courseAL, new Comparator<Course>() {
            @Override
            public int compare(Course o1, Course o2) {
                if (o1.getAl().size() < o2.getAl().size()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        Solver s = model.getSolver();
        s.setSearch(activityBasedSearch(varArr));
        while (s.solve()) {
            Helper_Functions.fillStudentSlots();
            Validation.validateSolution();
            double current = StatCalculationFunctions.calculateStats();

            if (current > maxMean) {
                maxMean = current;
                System.out.println("New max : " + maxMean);
                System.out.println("average mean of the solution = " + maxMean);
                System.out.println("average variance of the solution = " + variance);
                System.out.println("back to back count is: " + b2bTotal);
                System.out.println("4 in 2 count is: " + fourin2total);
                System.out.println("ends 5 starts 8 total is: " + end5start8sum);

            }

            model.getSolver().setRestartOnSolutions();
            //System.out.println("Number of slots in our solution is equal to number of slots of University solution : " + Validation.numberTimeSlots(students));  // el mafrod awal wahde true mesh false !!

        }


         /*
        M A I N        E N D    M
        A                       A
        I                       I
        N                       N

         *EXAMINATION TIMETABLING*
           MAHMOUD ABDELKAREEM
             MOHAMMAD SEHWEIL
               AHMAD ZEID
        E                       E
        N                       N
        D                       D
        M A I N        E N D    *
        */
    }





}
