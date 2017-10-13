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
        if(x.isInstantiated() && z.isInstantiated()){
            courses[0]  = y;
            courses[1]  = x;
            courses[2]  = z;
            return courses;
        }
        if(y.isInstantiated() && z.isInstantiated()){
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


}