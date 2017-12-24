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
import sun.security.x509.AVA;


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
    static ArrayList<Room> Rooms = new ArrayList<Room>();

    static int constCounter = 0;
    //our connection
    static Connection connection;
    static double maxMean = 0;
    static double variance = 0;
    static double ourScore = 0;
    public static int lenOfExamPeriod = 36;

    static int fourin2total, b2bTotal, end5start8sum;
    static boolean VARBOUS = false;
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/exams";
        String username = "root";
        System.out.println("Connecting database...");
        int c = 0;
        try {

            connection = DriverManager.getConnection(url, username, Password.password);
            System.out.println("Database connected!");

            /* this is to fill both
            the course and variables 
            arraylists from the course
            table from the database
            */
            Statement stmt = connection.createStatement();
            ResultSet getCourses = stmt.executeQuery("SELECT * FROM course_table Order By course_table.id");

            while (getCourses.next()) {

                IntVar temp = model.intVar(getCourses.getString("COURSE_LABEL"), 1, 36);
                variables.add(temp);

                Course co = new Course(Integer.valueOf(getCourses.getString("id")),
                        getCourses.getString("COURSE_LABEL"), getCourses.getString("COURSE TITLE"),
                        getCourses.getString("DEPT"), c++);
                courseAL.add(co);

            }
            // end filling up the course arraylist and vars as well



            /*
            this is to fill the student arraylist (all the students)
            */
            Statement stmt2 = connection.createStatement();
            ResultSet getStudents = stmt2.executeQuery("SELECT * FROM student_table");
            while (getStudents.next()) {
                Student s = new Student(Integer.valueOf(getStudents.getString("id")), getStudents.getString("DEPARTMENT"));
                students.add(s);
            }


               /*
            this is to fill the student arraylist (all the students)
            */
            Statement stmt3 = connection.createStatement();
            ResultSet getRoom = stmt3.executeQuery("SELECT * FROM room_table");
            while (getRoom.next()) {
                //String s = rs.getString("id")+" "+rs.getString("COURSE_LABEL");
                Room r = new Room(Integer.valueOf(getRoom.getString("id")), Integer.valueOf(getRoom.getString("CAPACITY")), getRoom.getString("ROOM"));
                Rooms.add(r);
//                System.out.println(r.getId() + " " + r.getLabel() + " " + r.getCapacity());
//                System.out.println("\n\n");
                // System.out.println(s);
            }

        } catch (SQLException ex) {
            Logger.getLogger(MainDriver.class.getName()).log(Level.SEVERE, null, ex);
        }


// Sort Room depinding on Capacity
        Collections.sort(Rooms);

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
                ConstraintFillingFunctions.studentHasThreeOrMore(students.get(p).getCourses());
                //ConstraintFillingFunctions.studentHassThreeOrMoreNumber2(students.get(p).getCourses());

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
//        for (int i = 0; i < courseAL.size(); i++) {
////            System.out.println(courseAL.get(i).getIntersectFactor() + " " + courseAL.get(i).getLabel() + " var index is:" + courseAL.get(i).getVariableIndex());
//        }


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

        // Student number for each exam
//        for (int i = 0; i < courseAL.size(); i++) {
//            System.out.println(courseAL.get(i).getAl().size() + "  " + courseAL.get(i).getLabel());
//        }

        //ArrayList<Course> [] dailyExams = new ArrayList<Course>()[lenOfExamPeriod];


        while (s.solve()) {
//            ArrayList<ArrayList<Course>> dailyExams = new ArrayList<ArrayList<Course>>(lenOfExamPeriod);
            Helper_Functions.fillStudentSlots();
            Validation.validateSolution();
            double current = StatCalculationFunctions.calculateStats();
            double currScore = MainDriver.score(current, variance, b2bTotal, fourInTwoCounter);
            if (currScore > ourScore) {
                maxMean = current;
                ourScore = currScore;
                System.out.println("score is: " + ourScore);
                System.out.println("average mean of the solution = " + maxMean);
                System.out.println("average variance of the solution = " + variance);
                System.out.println("back to back count is: " + b2bTotal);
                System.out.println("4 in 2 count is: " + fourin2total);
                System.out.println("ends 5 starts 8 total is: " + end5start8sum);

            }
            int[] num = new int[9];
            double[] score = new double[9];

            for (int i = 0; i < num.length; i++) {
                num[i] = 0;
                score[i] = 0;
            }


            for (int i = 0; i < MainDriver.students.size(); i++) {
                if (MainDriver.students.get(i).getSlots().size() > 1) {
                    num[MainDriver.students.get(i).getSlots().size() - 1]++;
                    score[MainDriver.students.get(i).getSlots().size() - 1] += ScoringSystem.score(MainDriver.students.get(i).getSlots2D());
                }
            }

            double sumScore = 0;
            for (int i = 1; i < num.length; i++) {
                sumScore += num[i] * score[i];
            }

            System.out.println("Our final score = " + sumScore);

            RoomingSystem roomingSystem = new RoomingSystem(lenOfExamPeriod,courseAL,variables,Rooms);
            boolean validRooms = roomingSystem.roomingSystem();
            if (!validRooms)
                continue;

            if (roomingSystem.validateRoomingSystem())
                System.out.println("Rooms are VALID and they are");

            if (VARBOUS)
                roomingSystem.printAllRooms();

            model.getSolver().setRestartOnSolutions();

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





    public static double score(double mean, double var, int b2b, int fourInTow) {
        double score = (mean / (Math.sqrt(var)) - (1.57 * b2b / 55000) - (36.6 * fourInTow / 55000));
        return score;
    }


    public static int getScore(ArrayList<Integer> slots, int index) {
        int scoreSum = 0;
        int score = 0;

        int fInt = StatCalculationFunctions.FourInTwo(slots);
        score -= fInt * 20;

        int b2b = StatCalculationFunctions.b2b(slots);
        score -= b2b * 5;

        int firstDay = (students.get(index).getFirstSlot() - 1) / 3;
        int lastDay = (students.get(index).getLastSlot() - 1) / 3;
        int examLen = students.get(index).getExamsLen();

        if (slots.size() > 0) {

            Collections.sort(slots);
            for (int q = 1; q < slots.size(); q++) {

                if (slots.get(q) - slots.get(q - 1) >= 7) {
                    score += 10;
                } else if (slots.get(q) - slots.get(q - 1) >= 4) {
                    score += 5;
                }
            }
        }
        return score;


    }


}
