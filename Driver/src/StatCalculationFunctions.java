import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StatCalculationFunctions {


    public static int b2b(ArrayList<Integer> slots) {
        Collections.sort(slots);
        int sum = 0;
        for (int i = 0; i < slots.size(); i++) {
            if (i == 0)
                continue;
            else {
                if (Math.abs(slots.get(i) - slots.get(i - 1)) <= 1) {
                    sum += 1;
                }

            }
        }
        return sum;
    }


    public static double calculateVarianceOfSpace(Student stu, int q) {
        int[][] s2D = stu.getSlots2D();
        double sum = 0;
        int count = 0;
        for (int i = (stu.getFirstSlot() - 1) / 3; i < (((stu.getLastSlot() - 1) / 3) + 1); i++) {
            if (i == (stu.getFirstSlot() - 1) / 3) {
                continue;
            }
            if ((s2D[i][0] == 0) && (s2D[i][1] == 0) && (s2D[i][2] == 0)) {
                count++;
            }
            if ((s2D[i][0] == 1) || (s2D[i][1] == 1) || (s2D[i][2] == 1)) {
                if (s2D[i][0] == 1) {
                    sum += Math.pow((double) count - stu.getAvgDaysBetweenExams(), 2);
                    count = 0;
                }
                if (s2D[i][1] == 1) {
                    sum += Math.pow((double) count - stu.getAvgDaysBetweenExams(), 2);

                    count = 0;
                }
                if (s2D[i][2] == 1) {
                    sum += Math.pow((double) count - stu.getAvgDaysBetweenExams(), 2);

                    count = 0;
                }

            }


        }
        int m = (stu.getFirstSlot() - 1) / 3;
        int numofExamsFirstDay = s2D[m][0] + s2D[m][1] + s2D[m][2];
        double var = sum / ((double) stu.getSlots().size() - numofExamsFirstDay - 1);
        if (Double.isNaN(var)) {
            TryingChoco1.students.get(q).setVarianceOfSpaces(0);

        } else {
            TryingChoco1.students.get(q).setVarianceOfSpaces(sum / ((double) stu.getSlots().size() - numofExamsFirstDay - 1));
        }

        //System.out.println("Variance of number of days between exams is "+students.get(q).getVarianceOfSpaces()+" days");
        return TryingChoco1.students.get(q).getVarianceOfSpaces();
        //System.out.println("num is -> "+((double)stu.getSlots().size() - numofExamsFirstDay-1));
    }


    public static double calculateAvgDaysBetweenExams(Student stu, int q) {
        int[][] s2D = stu.getSlots2D();
        int count = 0;
        for (int i = (stu.getFirstSlot() - 1) / 3; i < (stu.getLastSlot() - 1) / 3; i++) {
            if ((s2D[i][0] == 0) && (s2D[i][1] == 0) && (s2D[i][2] == 0)) {
                count++;
            }

        }
        int m = (stu.getFirstSlot() - 1) / 3;
        int numofExamsFirstDay = s2D[m][0] + s2D[m][1] + s2D[m][2];
        double avg = (count) / ((double) stu.getSlots().size() - numofExamsFirstDay);
        if (Double.isNaN(avg)) {
            TryingChoco1.students.get(q).setAvgDaysBetweenExams(0);
        } else {
            TryingChoco1.students.get(q).setAvgDaysBetweenExams(avg);
        }
        // System.out.println("Average number of days between exams is "+(count)/((double)stu.getSlots().size()-1)+" days");
        return TryingChoco1.students.get(q).getAvgDaysBetweenExams();
    }

    public static void calculateFullExamLengeth(Student stu, int k) {
        int firstSlot, LastSlot;
        //Integer [] stuSlots=Arrays.sort((Integer [])stu.getSlots().toArray());

        int[] stuSlots = stu.getSlots().stream().mapToInt(i -> i).toArray(); //array list to array
        Arrays.sort(stuSlots);
        firstSlot = stuSlots[0];
        LastSlot = stuSlots[stuSlots.length - 1];
        int leng = (((LastSlot - 1) / 3) - ((firstSlot - 1) / 3)) + 1;
        TryingChoco1.students.get(k).setExamsLen(leng);
        TryingChoco1.students.get(k).setFirstSlot(firstSlot);
        TryingChoco1.students.get(k).setLastSlot(LastSlot);


    }


    public static int FourInTwo(ArrayList<Integer> slots) {
        int[] input = new int[slots.size()];    // input array
        for (int i = 0; i < slots.size(); i++) {
            input[i] = slots.get(i);
        }
        int k = 4;
        List<int[]> subsets = Helper_Functions.allCombination(input, k);
        /*combo of all the time slots for a student
        this is to see if any three exams or slots
        are in the same day*/
        int sum = 0;

        for (int i = 0; i < subsets.size(); i++) {
            if (Helper_Functions.fourInTwo(subsets.get(i))) {
                sum++;
//                System.out.println(Arrays.toString(subsets.get(i)));
            }

        }

        return sum;
    }


}
