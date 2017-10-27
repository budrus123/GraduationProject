import org.chocosolver.solver.variables.IntVar;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


    public static IntVar[] twoOfThreeInsta(IntVar x,IntVar y, IntVar z){

        IntVar courses[] = new IntVar[3];
        if(x.isInstantiated() && y.isInstantiated()){
            courses[0]  = z;
            courses[1]  = x;
            courses[2]  = y;
            return courses;
        }
        else if(x.isInstantiated() && z.isInstantiated()){
            courses[0]  = y;
            courses[1]  = x;
            courses[2]  = z;
            return courses;
        }
        else if(y.isInstantiated() && z.isInstantiated()){
            courses[0]  = x;
            courses[1]  = y;
            courses[2]  = z;
            return courses;
        }

        return null;
    }


    public static boolean fourExamsInTwoDays(int timeSolt1,int timeSolt2){
        return (timeSolt2-timeSolt1)/6 == 0;
    }

    public static boolean fourExamsInTwoDays(int timeSolt1,int timeSolt2,int timeSolt3){
        return fourExamsInTwoDays(timeSolt1,timeSolt2) && fourExamsInTwoDays(timeSolt1,timeSolt3) && fourExamsInTwoDays(timeSolt2,timeSolt3);
    }


    public static int[] getInterval(int x) {
        int[] interval = new int[2];

        if (x % 3 == 1) {
            interval[0] = x;
            interval[1] = x+2;

        } else if (x % 3 == 2) {
            interval[0] = x - 1;
            interval[1] = x + 1;


        } else if (x % 3 == 0) {
            interval[0] = x - 2;
            interval[1] = x;

        }

        return interval;
    }

    public static int[] getInterval(int x,int y, int z) {
        int[] interval = new int[2];
        int arr[] = {x,y,z};
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

    public static boolean fourInTwo(int [] arr){
        Arrays.sort(arr);
        int min=arr[0];
        int max=arr[3];

        if((Math.abs((Math.ceil(max/3))-(Math.ceil(min/3)))) >= 2 )
            return false;

        return  true;

    }

    public static List<int[]> allCombination(int[] input, int k){

        List<int[]> subsets = new ArrayList<>();

        int[] s = new int[k];                  // here we'll keep indices
        // pointing to elements in input array

        if (k <= input.length) {
            // first index sequence: 0, 1, 2, ...
            for (int i = 0; (s[i] = i) < k - 1; i++);
            subsets.add(getSubset(input, s));
            for(;;) {
                int i;
                // find position of item that can be incremented
                for (i = k - 1; i >= 0 && s[i] == input.length - k + i; i--);
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


    public static String createInsertQuery(ArrayList<IntVar> variables,ArrayList<Course> courses,String tableName){

        // INSERT INTO TABLE_NAME (column1, column2, column3,...columnN) VALUES (value1, value2, value3,...valueN);
        //  (1, 2, 'ANTH335', 'A.Shaheen216', '2017-06-15', 14, 17),
        //  (2, 'ECON234', 'PALESTINIAN ECONOMICS', 'ECON'),


        System.out.println("WWEWWWWW");
        String query = "INSERT INTO " + tableName + "(STUDENT_NUMBER,COURSE_LABEL,ROOM,EXAM_DATE,START_TIME,END_TIME) VALUES ";
        int index = 0;
        String values = "";
        for (IntVar variable: variables) {
            int course_id = courses.get(index).getId();
            String course_label = courses.get(index).getLabel();
            String room = "x";
            int start_time = variable.getValue();
            int end_time = variable.getValue() + 3;
            String exam_date = "";
            ArrayList<Student> students = courses.get(index++).getAl();
            for (Student student:students){
                int student_number = student.getId();
                values = values+"("+student_number+","+formatName(course_label)+","+formatName(room)+","+formatName(exam_date)+","+start_time+","+end_time+"),\n";
            }
            variable.getValue();
        }
        query = query + values + ";";
        return query;
    }


    public static String formatName(String name){
        return "'"+name+"'";
    }


    public static String getSolutionQuery(int solution_id,ArrayList<IntVar> variables,ArrayList<Course> courses,String tableName){
        String query = "INSERT INTO " + tableName + "(id,solution_id,course_id,course_label,time_slot) VALUES ";
        int index = 0;
        String values = "";
        for (IntVar variable: variables) {
            int course_id = courses.get(index).getId();
            String course_label = courses.get(index++).getLabel();
            int time_slot = variable.getValue();
            if(index != courses.size())
                values = values+"("+index +"," + solution_id + "," +course_id+","+formatName(course_label)+","+time_slot+"),\n";
            else
                values = values+"("+index + ","+ solution_id + "," +course_id+","+formatName(course_label)+","+time_slot+");\n";

        }
        query = query + values;
        return query;
    }

}