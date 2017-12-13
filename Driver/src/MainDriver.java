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
    public static int lenOfExamPeriod = 36;

    static int fourin2total, b2bTotal, end5start8sum;

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

        for (int i = 0; i < courseAL.size(); i++) {
            System.out.println(courseAL.get(i).getAl().size() + "  " + courseAL.get(i).getLabel());
        }

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
            int num2, num3, num4, num5, num6, num7, num8, num9;
            double score2 = 0, score3, score4, score5, score6, score7, score8, score9;
            num2 = num3 = num4 = num5 = num6 = num7 = num8 = num9 = 0;
            score2 = score3 = score4 = score5 = score6 = score7 = score8 = score9 = 0;

            for (int i = 0; i < MainDriver.students.size(); i++) {
                if (MainDriver.students.get(i).getSlots().size() > 1) {
                    num[MainDriver.students.get(i).getSlots().size() - 1]++;
                    score[MainDriver.students.get(i).getSlots().size() - 1] += ScoringSystem.score(MainDriver.students.get(i).getSlots2D());
                }
                //SumScore+= getScore(students.get(i).getSlots(),i);
            }

            double sumScore = 0;
            for (int i = 1; i < num.length; i++) {
                sumScore += num[i] * score[i];
            }

            System.out.println("our score = " + sumScore);
            ArrayList<ArrayList<Course>> dailyExams = new ArrayList<ArrayList<Course>>();

//            outer.add(inner);
//            Course cc=courseAL.get(18);
//            dailyExams.get(0).add(cc);
            for (int j = 1; j < lenOfExamPeriod+1; j++) {
                ArrayList<Course> inner = new ArrayList<Course>();
                for (int i = 0; i < courseAL.size(); i++) {
                    //dailyExams.get(i) = new ArrayList<Course>();
                    if (variables.get(courseAL.get(i).getVariableIndex()).getValue() == j) {
                        inner.add(courseAL.get(i));
                    }
                }
                dailyExams.add(inner);
            }


            //
//            for (int i = 0; i < courseAL.size(); i++) {
//                dailyExams.get(variables.get(courseAL.get(i).getVariableIndex()+1).getValue()).add(courseAL.get(i));
//            }
            ///start from here abuzaid
            for (int i = 0; i < lenOfExamPeriod; i++) {
                int sumStu=0;
                for (int j = 0; j < dailyExams.get(i).size(); j++) {
                    System.out.println(dailyExams.get(i).get(j));
                    sumStu+=dailyExams.get(i).get(j).getAl().size();
                }
                System.out.println("exams in slot " + (i+1)+" the number of them is "+ dailyExams.get(i).size());
                System.out.println("num of bitches who take this "+sumStu);
                System.out.println("\n\n");


            }
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

        //score=0;
//            if (slots.size() > 0) {
//                System.out.println("Student has [" + students.get(index).getSlots().size() + "] exams, slots are:");
//                students.get(index).printSlots();
//            }
        int fInt = StatCalculationFunctions.FourInTwo(slots);
        score -= fInt * 20;

        int b2b = StatCalculationFunctions.b2b(slots);
        score -= b2b * 5;

        int firstDay = (students.get(index).getFirstSlot() - 1) / 3;
        int lastDay = (students.get(index).getLastSlot() - 1) / 3;
        int examLen = students.get(index).getExamsLen();

//            for (int j = 0; j < students.get(i).getSlots2D().length; j++) {
//
//                if ((students.get(i).getSlots2D()[i][0] == 0) &&
//                        (students.get(i).getSlots2D()[i][1] == 0) &&
//                        (students.get(i).getSlots2D()[i][2] == 0)) {
//                    continue;
//                }
//
//
//            }
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
        // System.out.println("Score is: "+score);


    }


}
