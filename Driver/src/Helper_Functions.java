import org.chocosolver.solver.variables.IntVar;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Helper_Functions {

    public static ResultSet executeQuery(Connection connection, String query) {

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    public static IntVar[] twoOfThreeInsta(IntVar x, IntVar y, IntVar z) {

        IntVar courses[] = new IntVar[3];
        if (x.isInstantiated() && y.isInstantiated()) {
            courses[0] = z;
            courses[1] = x;
            courses[2] = y;
            return courses;
        } else if (x.isInstantiated() && z.isInstantiated()) {
            courses[0] = y;
            courses[1] = x;
            courses[2] = z;
            return courses;
        } else if (y.isInstantiated() && z.isInstantiated()) {
            courses[0] = x;
            courses[1] = y;
            courses[2] = z;
            return courses;
        }

        return null;
    }


    public static boolean fourExamsInTwoDays(int timeSolt1, int timeSolt2) {
        return (timeSolt2 - timeSolt1) / 6 == 0;
    }

    public static boolean fourExamsInTwoDays(int timeSolt1, int timeSolt2, int timeSolt3) {
        return fourExamsInTwoDays(timeSolt1, timeSolt2) && fourExamsInTwoDays(timeSolt1, timeSolt3) && fourExamsInTwoDays(timeSolt2, timeSolt3);
    }


    public static int[] getInterval(int x) {
        int[] interval = new int[2];

        if (x % 3 == 1) {
            interval[0] = x;
            interval[1] = x + 2;

        } else if (x % 3 == 2) {
            interval[0] = x - 1;
            interval[1] = x + 1;


        } else if (x % 3 == 0) {
            interval[0] = x - 2;
            interval[1] = x;

        }

        return interval;
    }

    public static int[] getInterval(int x, int y, int z) {
        int[] interval = new int[2];
        int arr[] = {x, y, z};
        Arrays.sort(arr);
        int min = arr[0];
        int max = arr[2];

        interval[0] = getInterval(min)[0];
        interval[1] = getInterval(max)[1];

        return interval;
    }

    public static boolean haveSameDay(int x, int y) {
        if (((x - 1) / 3) == (((y - 1) / 3))) {
            return true;

        }
        return false;
    }

    public static int[] getSubset(int[] input, int[] subset) {
        int[] result = new int[subset.length];
        for (int i = 0; i < subset.length; i++)
            result[i] = input[subset[i]];
        return result;
    }

    public static boolean fourInTwo(int[] arr) {
        Arrays.sort(arr);
        int min = arr[0];
        int max = arr[3];

        if ((Math.abs((Math.ceil(max / 3.0)) - (Math.ceil(min / 3.0)))) >= 2) {

            return false;
        }

//        System.out.println("min is "+min+" max is "+max);
//        System.out.println((Math.ceil(max/3)));
//        System.out.println((Math.ceil(min/3)));
        return true;

    }

    public static List<int[]> allCombination(int[] input, int k) {

        List<int[]> subsets = new ArrayList<>();

        int[] s = new int[k];                  // here we'll keep indices
        // pointing to elements in input array

        if (k <= input.length) {
            // first index sequence: 0, 1, 2, ...
            for (int i = 0; (s[i] = i) < k - 1; i++) ;
            subsets.add(getSubset(input, s));
            for (; ; ) {
                int i;
                // find position of item that can be incremented
                for (i = k - 1; i >= 0 && s[i] == input.length - k + i; i--) ;
                if (i < 0) {
                    break;
                }
                s[i]++;                    // increment this item
                for (++i; i < k; i++) {    // fill up remaining items
                    s[i] = s[i - 1] + 1;
                }
                subsets.add(getSubset(input, s));
            }
        }

        return subsets;

    }


    public static String createInsertQuery(ArrayList<IntVar> variables, ArrayList<Course> courses, String tableName) {

        // INSERT INTO TABLE_NAME (column1, column2, column3,...columnN) VALUES (value1, value2, value3,...valueN);
        //  (1, 2, 'ANTH335', 'A.Shaheen216', '2017-06-15', 14, 17),
        //  (2, 'ECON234', 'PALESTINIAN ECONOMICS', 'ECON'),


        System.out.println("WWEWWWWW");
        String query = "INSERT INTO " + tableName + "(STUDENT_NUMBER,COURSE_LABEL,ROOM,EXAM_DATE,START_TIME,END_TIME) VALUES ";
        int index = 0;
        String values = "";
        for (IntVar variable : variables) {
            int course_id = courses.get(index).getId();
            String course_label = courses.get(index).getLabel();
            String room = "x";
            int start_time = variable.getValue();
            int end_time = variable.getValue() + 3;
            String exam_date = "";
            ArrayList<Student> students = courses.get(index++).getAl();
            for (Student student : students) {
                int student_number = student.getId();
                values = values + "(" + student_number + "," + formatName(course_label) + "," + formatName(room) + "," + formatName(exam_date) + "," + start_time + "," + end_time + "),\n";
            }
            variable.getValue();
        }
        query = query + values + ";";
        return query;
    }


    public static String formatName(String name) {
        return "'" + name + "'";
    }


    public static String getSolutionQuery(int solution_id, ArrayList<IntVar> variables, ArrayList<Course> courses, String tableName) {
        String query = "INSERT INTO " + tableName + "(id,solution_id,course_id,course_label,time_slot,roomslots) VALUES ";

        int index = 0;
        String values = "";
        for (Course course : courses) {
            index++;
            int course_id = course.getId();
            String course_label = course.getLabel();
            int time_slot = variables.get(course.getVariableIndex()).getValue();
            String rooms="";
            for (int i = 0; i <course.getRooms().size() ; i++) {
                if(i!=course.getRooms().size()-1)
                    rooms=rooms+course.getRooms().get(i).getLabel()+"+";
                else
                    rooms=rooms+course.getRooms().get(i).getLabel();
                System.out.println("fuck s7s");

            }
            if (index != courses.size())
                values = values + "(" + index + "," + solution_id + "," + course_id + "," + formatName(course_label) + "," + time_slot + "," + formatName(rooms) + "),\n";
            else
                values = values + "(" + index + "," + solution_id + "," + course_id + "," + formatName(course_label) + "," + time_slot + "," + formatName(rooms) + ");\n";

        }
        query = query + values;
        return query;
    }


    public static void fillInterSectFactor() {
        int fact = 0;
        for (int i = 0; i < MainDriver.courseAL.size(); i++) {
            for (int j = 0; j < MainDriver.courseAL.size(); j++) {
                if (getCommonCount(MainDriver.courseAL.get(i).getAl(), MainDriver.courseAL.get(j).getAl()) > 0) {
                    fact++;
                }

            }
            MainDriver.courseAL.get(i).setIntersectFactor(fact);
            //System.out.println(courseAL.get(i).getLabel()+" factor of "+fact);
            fact = 0;

        }
    }

    public static int getCommonCount(ArrayList<Student> st1, ArrayList<Student> st2) {
        int sum = 0;
        for (int i = 0; i < st1.size(); i++) {
            for (int j = 0; j < st2.size(); j++) {
                if (st1.get(i).getId() == st2.get(j).getId()) {
                    sum++;
                }

            }
        }
        return sum;
    }

    public static void mostCommonCourses() {

        for (int i = 0; i < MainDriver.courseAL.size(); i++) {
            for (int j = i + 1; j < MainDriver.courseAL.size(); j++) {
                ArrayList<Student> st1 = MainDriver.courseAL.get(i).getAl();
                ArrayList<Student> st2 = MainDriver.courseAL.get(j).getAl();
                int commonCount = getCommonCount(st1, st2);
                if (commonCount > 300) {
                    System.out.println(MainDriver.courseAL.get(i).getLabel() + ", " + MainDriver.courseAL.get(j).getLabel() + " count is " + commonCount);

//                    model.setObjective(Model.MAXIMIZE, variables.get(courseAL.get(i).getVariableIndex()));
//                    model.setObjective(Model.MINIMIZE, variables.get(courseAL.get(j).getVariableIndex()));


                }
//                if (commonCount == 0) {
//                    model.setObjective(Model.MINIMIZE, variables.get(courseAL.get(i).getVariableIndex()));
//                    model.setObjective(Model.MAXIMIZE, variables.get(courseAL.get(j).getVariableIndex()));
//                }
            }

        }

    }


    public static void fillStudentSlots() {
        int flag = 0;

        for (int i = 0; i < MainDriver.students.size(); i++) {
            ArrayList<Integer> studentExams = new ArrayList<Integer>();
            for (int k = 0; k < MainDriver.students.get(i).getCourses().size(); k++) {
                Course cid = (Course) MainDriver.students.get(i).getCourses().get(k);
                studentExams.add(MainDriver.variables.get(cid.getVariableIndex()).getValue());
                //student exams has the timeslots the student has exams in
            }
            MainDriver.students.get(i).setSlots(studentExams);

        }
    }

    public static boolean checkIfTwoSlotsSame(ArrayList<Integer> slots) {

        for (int i = 0; i < slots.size(); i++) {
            for (int k = i + 1; k < slots.size(); k++) {
                if (slots.get(k) == slots.get(i)) {
                    //System.out.println(slots);
                    return false;
                }
            }
        }

        return true;
    }

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
    public static void fillStudentTakesCourse() throws SQLException {

        Statement stmt2 = MainDriver.connection.createStatement();
        //String s="SELECT * FROM student_course_table where COURSE_LABEL='"+courseAL.get(i).getLabel()+"'";
        ResultSet course_student2 = stmt2.executeQuery("SELECT * FROM student_course_table");
        while (course_student2.next()) {
            MainDriver.courseAL.get(Integer.valueOf(course_student2.getString("course_id")) - 1).addStudent(MainDriver.students.get(Integer.valueOf(course_student2.getString("STUDENT_NUMBER")) - 1));
            MainDriver.students.get((Integer.valueOf(course_student2.getString("STUDENT_NUMBER")) - 1)).addCourse(MainDriver.courseAL.get(Integer.valueOf(course_student2.getString("course_id")) - 1));
        }


    }
    //################################################################################################################


    //################################################################################################################
    public static IntVar getCourseIntVar(Course c) {

        for (int i = 0; i < MainDriver.model.getNbVars(); i++) {
            if (c.getLabel().equals(MainDriver.model.getVar(i).getName())) {
                return (IntVar) MainDriver.model.getVar(i);
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

    public static int getDay(int slot) {
        return (slot - 1) / 3;
    }

    public static void printOneTimeSlotInfo(ArrayList<Course> courses) {

        System.out.println("--------------------------------------------------------------");

        for (Course course : courses) {
            System.out.println("Course Name: " + course.getLabel());
            System.out.println("Number of students in this course: " + course.getAl().size());
            System.out.println("Number of rooms used: " + course.getRooms().size());
            System.out.println("The rooms used are: ");
            for (Room room : course.getRooms()) {
                System.out.println(room.getLabel() + " " + room.getCapacity());
            }
            System.out.println("--------------------------------------------------------------");
        }
    }

}