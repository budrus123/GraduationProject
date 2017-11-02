/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;

/**
 * @author Mahmoud
 */
public class TryingChoco1 {

    /**
     * @param args the command line arguments
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

    public static void main(String[] args) throws SQLException {
        // TODO code application logic here
        long startTime = System.currentTimeMillis();
        //connecting to the database
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
                IntVar temp = model.intVar(getCourses.getString("COURSE_LABEL"), 1, 36); // phys141 in 1 2 3
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
        for (int p = 0; p < students.size(); p++) {
            if (students.get(p).getCourses().size() >= 3) {
                studentHasThreeOrMore(students.get(p).getCourses());
            }
//            if(students.get(p).getCourses().size()>=4){
//                studentHasFourOrMore(students.get(p).getCourses());
//                //System.out.println(students.get(p).getId());
//            }
        }

        System.out.print("done with the combos " + constCounter);



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
//        Statement stmt = connection.createStatement();
//        String query = "DELETE FROM solution";
//        stmt.executeUpdate(query);  // delete all records in solution.
//        String insertQuery = Helper_Functions.getSolutionQuery(solution_id++, variables, courseAL, "solution");
//        Statement stmt2 = connection.createStatement();
//        stmt2.executeUpdate(insertQuery);
        /*
        done inserting the solution
         */

        new UniversityData(students);

        // model.getSolver().printStatistics();
        while( model.getSolver().solve()){
           // long stopTime = System.currentTimeMillis();
           // long elapsedTime = stopTime - startTime;
          //  System.out.println(elapsedTime / 1000.0);
            System.out.println("\n\nchecking if the solution found is valid!");
            fillStudentSlots();
            validateSolution();
            calculateStats();
            System.out.println("uni solution coming next");
            System.out.println("number of students who have 4 exams in 2 days is:" + fourInTwoCounter);

            System.out.println("Number of slots in our solution is equal to number of slots of University solution : " + Validation.numberTimeSlots(students));  // el mafrod awal wahde true mesh false !!


        }



        /*
        M A I N   E N D
        A
        I
        N

        E
        N
        D
         */
    }

    //################################################################################################################
    public static void calculateStats() {
        double avgSum=0;
        double varSum=0;
        int countHasExams=0;
        for(int i=0;i<students.size();i++){
            //System.out.println("\n***printing stats for student number "+ i+" ***");

            if(students.get(i).getSlots().size()>0){
                //students.get(i).printSlots();
                calculateFullExamLengeth(students.get(i),i);
                avgSum+=calculateAvgDaysBetweenExams(students.get(i),i);
                varSum+=calculateVarianceOfSpace(students.get(i),i);

                //System.out.println(" ");
                countHasExams++;
            }

        }
        System.out.println("average mean of the solution = "+(avgSum/countHasExams));
        System.out.println("average variance of the solution = "+(varSum/countHasExams));
    }
    //################################################################################################################


    //################################################################################################################
    public static double calculateVarianceOfSpace(Student stu,int q) {
        int [][] s2D = stu.getSlots2D();
        double sum=0;
        int count=0;
        for(int i=(stu.getFirstSlot()-1)/3;i<(((stu.getLastSlot()-1)/3)+1);i++){
            //System.out.println("i is -> "+i);
            if(i==(stu.getFirstSlot()-1)/3){
                continue;
            }
            if((s2D[i][0]==0)&&(s2D[i][1]==0)&&(s2D[i][2]==0)){
                count++;
            }
            if((s2D[i][0]==1)||(s2D[i][1]==1)||(s2D[i][2]==1)){
                if (s2D[i][0] == 1) {
                    sum+=Math.pow((double)count-stu.getAvgDaysBetweenExams(),2);
                    //System.out.println("added power -> "+Math.pow((double)count-stu.getAvgDaysBetweenExams(),2));
                    count=0;
                }
                 if (s2D[i][1] ==1) {
                    sum+=Math.pow((double)count-stu.getAvgDaysBetweenExams(),2);
                    //System.out.println("added power -> "+Math.pow((double)count-stu.getAvgDaysBetweenExams(),2));

                    count=0;
                }
                 if (s2D[i][2] == 1){
                    sum+=Math.pow((double)count-stu.getAvgDaysBetweenExams(),2);
                    //System.out.println("added power -> "+Math.pow((double)count-stu.getAvgDaysBetweenExams(),2));

                    count=0;
                }

            }


        }
        int m=(stu.getFirstSlot()-1)/3;
        int numofExamsFirstDay=s2D[m][0]+s2D[m][1]+s2D[m][2];
        //System.out.println("num in fDay -> "+numofExamsFirstDay);
        double var=sum/((double)stu.getSlots().size() - numofExamsFirstDay-1);
        if(Double.isNaN(var)){
            students.get(q).setVarianceOfSpaces(0);

        }
        else{
            students.get(q).setVarianceOfSpaces(sum/((double)stu.getSlots().size() - numofExamsFirstDay-1));
        }

        //System.out.println("Variance of number of days between exams is "+students.get(q).getVarianceOfSpaces()+" days");
        return students.get(q).getVarianceOfSpaces();
        //System.out.println("num is -> "+((double)stu.getSlots().size() - numofExamsFirstDay-1));
    }
    //################################################################################################################



    //################################################################################################################
    public static double calculateAvgDaysBetweenExams(Student stu,int q) {
       int [][] s2D = stu.getSlots2D();
       int count=0;
       for(int i=(stu.getFirstSlot()-1)/3;i<(stu.getLastSlot()-1)/3;i++){
           if((s2D[i][0]==0)&&(s2D[i][1]==0)&&(s2D[i][2]==0)){
               count++;
           }

       }
        int m=(stu.getFirstSlot()-1)/3;
        int numofExamsFirstDay=s2D[m][0]+s2D[m][1]+s2D[m][2];
        double avg=(count)/((double)stu.getSlots().size() - numofExamsFirstDay);
        if(Double.isNaN(avg)){
            students.get(q).setAvgDaysBetweenExams(0);
        }
        else{
            students.get(q).setAvgDaysBetweenExams(avg);
        }
       // System.out.println("Average number of days between exams is "+(count)/((double)stu.getSlots().size()-1)+" days");
        return students.get(q).getAvgDaysBetweenExams();
    }
    //################################################################################################################





    //################################################################################################################
    public static void calculateFullExamLengeth(Student stu,int k) {
        int firstSlot,LastSlot;
        //Integer [] stuSlots=Arrays.sort((Integer [])stu.getSlots().toArray());

            int[] stuSlots=stu.getSlots().stream().mapToInt(i->i).toArray(); //array list to array
            Arrays.sort(stuSlots);
            firstSlot=stuSlots[0];
            LastSlot=stuSlots[stuSlots.length-1];
            int leng= (((LastSlot -1)/3) - ((firstSlot -1)/3)) +1;
            students.get(k).setExamsLen(leng);
            students.get(k).setFirstSlot(firstSlot);
            students.get(k).setLastSlot(LastSlot);


    }
    //################################################################################################################




    //################################################################################################################
    public static int eval1(ArrayList<int[]> days) { //scoring same as s7s old function
        int score = 0;
        int size = days.size();
        int dayInc = 0;
        int flag = 0;
        for (int i = days.size() - 1; i >= 0; i--) {
            if (Arrays.asList(days.get(i)).contains(1)) { //check if we have exam that day
                dayInc = 1;
                if (IntStream.of(days.get(i)).sum() > 1) {
                    flag = 1;
                } else {
                    flag = 0;
                }
                //continue;


            } else {
                if (dayInc > 2) {
                    score += 3;
                } else if (flag == 0) {
                    score += 12;
                } else if (flag == 1) {
                    score += 6;
                }
            }
        }

        return score;
    }
    //################################################################################################################


    //################################################################################################################
    public static void fillStudentSlots() {
        int flag = 0;

        for (int i = 0; i < students.size(); i++) {
            ArrayList<Integer> studentExams = new ArrayList<Integer>();
            for (int k = 0; k < students.get(i).getCourses().size(); k++) {
                Course cid = (Course) students.get(i).getCourses().get(k);
                studentExams.add(variables.get(cid.getVariableIndex()).getValue());
                //student exams has the timeslots the student has exams in
            }
            students.get(i).setSlots(studentExams);

        }
    }
    //################################################################################################################


    //################################################################################################################
    public static void validateSolution() {
        int flag = 0;
        for (int i = 0; i < students.size(); i++) {
            //count the number of students who have 4 in 2
             FourInTwo(students.get(i).getSlots());
            if (!checkIfTwoSlotsSame(students.get(i).getSlots()) || !checkIfThreeSameDay(students.get(i).getSlots())) {
                System.out.println(students.get(i).getSlots() + "  " + students.get(i).getId());
                flag = 1;
//                System.out.println(studentExams+"\n"+students.get(i).getId());
            }
        }
        if (flag == 0)
            System.out.println("***********solution is valid***********");
        else
            System.out.println("solution is not valid");

    }
    //################################################################################################################


    //################################################################################################################
    public static void FourInTwo(ArrayList<Integer> slots) {
        int[] input = new int[slots.size()];    // input array
        for (int i = 0; i < slots.size(); i++) {
            input[i] = slots.get(i);
        }
        int k = 4;
        List<int[]> subsets = Helper_Functions.allCombination(input, k);
        /*combo of all the time slots for a student
        this is to see if any three exams or slots
        are in the same day*/

        for (int i = 0; i < subsets.size(); i++) {
            if (Helper_Functions.fourInTwo(subsets.get(i))) {
                fourInTwoCounter++;
            }

        }
    }
    //################################################################################################################


    //################################################################################################################
    public static boolean checkIfThreeSameDay(ArrayList<Integer> slots) {
        int[] input = new int[slots.size()];    // input array
        for (int i = 0; i < slots.size(); i++) {
            input[i] = slots.get(i);
        }
        int k = 3;
        List<int[]> subsets = Helper_Functions.allCombination(input, k);
        /*combo of all the time slots for a student
        this is to see if any three exams or slots
        are in the same day*/

        for (int i = 0; i < subsets.size(); i++) {
            if (Helper_Functions.haveSameDay(subsets.get(i)[0], subsets.get(i)[1]) &&
                    Helper_Functions.haveSameDay(subsets.get(i)[0], subsets.get(i)[2])) {
                return false;
            }

        }

        return true;
    }
    //################################################################################################################


    //################################################################################################################
    public static boolean checkIfTwoSlotsSame(ArrayList<Integer> slots) {

        for (int i = 0; i < slots.size(); i++) {
            for (int k = i + 1; k < slots.size(); k++) {
                if (slots.get(k) == slots.get(i)) {
                    System.out.println(slots);
                    return false;
                }
            }
        }

        return true;
    }
    //################################################################################################################


    //################################################################################################################
    static public void studentHasThreeOrMore(ArrayList<Course> courseAL) {

        int[] input = new int[courseAL.size()];    // input array

        for (int i = 0; i < courseAL.size(); i++) {
            input[i] = courseAL.get(i).getVariableIndex();
        }
        int k = 3;
        List<int[]> subsets = Helper_Functions.allCombination(input, k);
        //System.out.println(variables.get(courseAL.get(i).getVariableIndex()) +" "+variables[i+1]+"  "+variables[j]);

        for (int i = 0; i < subsets.size(); i++) {
            //System.out.println(subsets.get(i)[0]+" "+subsets.get(i)[1]+" "+subsets.get(i)[2]);
            Constraint th = new Constraint("Three in a day " + i,
                    new ThreeInADay(new IntVar[]{variables.get(subsets.get(i)[0]), variables.get(subsets.get(i)[1]),
                            variables.get(subsets.get(i)[2])}));
            model.post(th);

            //System.out.println(th);
            constCounter++;

        }

    }
    //################################################################################################################


    //################################################################################################################
    static public void studentHasFourOrMore(ArrayList<Course> courseAL) {

        int[] input = new int[courseAL.size()];    // input array

        for (int i = 0; i < courseAL.size(); i++) {
            input[i] = courseAL.get(i).getVariableIndex();
        }
        int k = 4;
        List<int[]> subsets = Helper_Functions.allCombination(input, k);
        //System.out.println(variables.get(courseAL.get(i).getVariableIndex()) +" "+variables[i+1]+"  "+variables[j]);

        for (int i = 0; i < subsets.size(); i++) {
            Constraint th = new Constraint("Four in two days " + i,
                    new FourExamsInTwoADays(new IntVar[]{variables.get(subsets.get(i)[0]), variables.get(subsets.get(i)[1]),
                            variables.get(subsets.get(i)[2]), variables.get(subsets.get(i)[3])}));
            //model5.allDifferent(variables).post();;
            model.post(th);
            constCounter++;
            //System.out.println(th);

        }


    }
    //################################################################################################################


    //################################################################################################################
    public static void oneMaxForEachStudent() throws SQLException {

        for (int i = 0; i < students.size(); i++) {
            Statement stmt = connection.createStatement();
            ResultSet getStudentCourses = stmt.executeQuery("SELECT * FROM student_course_table where STUDENT_NUMBER='" + (i + 1) + "'");
            Statement stmt2 = connection.createStatement();
            ResultSet getStudentCoursesCount = stmt2.executeQuery("SELECT count(*) FROM student_course_table where STUDENT_NUMBER='" + (i + 1) + "'");

            getStudentCoursesCount.next();
            int count = Integer.valueOf(getStudentCoursesCount.getString(1));
            //System.out.println(getStudentCoursesCount.getString(1));
            int q = 0;
            if (count > 1) {
                IntVar[] studentCourses = new IntVar[count];

                while (getStudentCourses.next()) {
                    studentCourses[q++] = variables.get(Integer.valueOf(getStudentCourses.getString(2)) - 1);
                }

                for (int m = 0; m < count; m++) {
                    for (int n = m; n < count; n++) {
                        model.arithm(studentCourses[m], "-", studentCourses[n], ">", 2).post();

                    }
                }
                //model.allDifferent(studentCourses);
            }


        }
    }
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
    public static void fillStudentTakesCourse() throws SQLException {

        Statement stmt2 = connection.createStatement();
        //String s="SELECT * FROM student_course_table where COURSE_LABEL='"+courseAL.get(i).getLabel()+"'";
        ResultSet course_student2 = stmt2.executeQuery("SELECT * FROM student_course_table");
        while (course_student2.next()) {
            courseAL.get(Integer.valueOf(course_student2.getString("course_id")) - 1).addStudent(students.get(Integer.valueOf(course_student2.getString("STUDENT_NUMBER")) - 1));
            students.get((Integer.valueOf(course_student2.getString("STUDENT_NUMBER")) - 1)).addCourse(courseAL.get(Integer.valueOf(course_student2.getString("course_id")) - 1));
        }


    }
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
    public static boolean haveCommonStudents(Course c1, Course c2) {
        ArrayList<Student> a1 = new ArrayList<Student>();
        ArrayList<Student> a2 = new ArrayList<Student>();


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
