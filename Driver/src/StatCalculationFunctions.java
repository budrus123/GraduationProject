import sun.applet.Main;

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


    public static double calculateVarianceOfSpaceForOneStudent(Student student) {
        int[][] s2D = student.getSlots2D();
        ArrayList<Integer> samples = getSpacesSamples(student);
        double sum = 0;

        for (int space:samples){
            sum += Math.pow((double) space - student.getAvgDaysBetweenExams(), 2);
        }
        int m = Helper_Functions.getDay(student.getFirstSlot());
        int numofExamsFirstDay = s2D[m][0] + s2D[m][1] + s2D[m][2];
        double var = sum / ((double) student.getSlots().size() - numofExamsFirstDay);
        if (Double.isNaN(var)) {
            student.setVarianceOfSpaces(0);
        } else {
            student.setVarianceOfSpaces(var);
        }
        System.out.println(student.getVarianceOfSpaces());
        return student.getVarianceOfSpaces();
    }



    public static double calculateAvgSpacesBetweenExamsForOneStudent(Student student) {
        int[][] s2D = student.getSlots2D();
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println(Arrays.deepToString(s2D));

        ArrayList<Integer> samples = getSpacesSamples(student);
        int sumOfSpaces = 0;
        for (int space:samples){
            sumOfSpaces+=space;
        }
        int m = Helper_Functions.getDay(student.getFirstSlot());
        int numofExamsFirstDay = s2D[m][0] + s2D[m][1] + s2D[m][2];
        double avg = (sumOfSpaces) / ((double) student.getSlots().size() - numofExamsFirstDay);
        if (Double.isNaN(avg)) {
            student.setAvgDaysBetweenExams(0);
        } else {
            student.setAvgDaysBetweenExams(avg);
        }
        System.out.println(student.getAvgDaysBetweenExams());
        return student.getAvgDaysBetweenExams();
    }


    public static ArrayList<Integer> getSpacesSamples(Student student) {
        int[][] s2D = student.getSlots2D();
        ArrayList<Integer> samples = new ArrayList<Integer>();
        int count = 0;
        for (int i = Helper_Functions.getDay(student.getFirstSlot())+1; i <= Helper_Functions.getDay(student.getLastSlot()); i++) {
            if ((s2D[i][0] == 0) && (s2D[i][1] == 0) && (s2D[i][2] == 0)) {   // No exams in this day
                count++;
            }

            if ((s2D[i][0] == 1) || (s2D[i][1] == 1) || (s2D[i][2] == 1)) {
                samples.add(count);
                count = 0;
            }
        }
        return samples;
    }



    public static void calculateFullExamLengeth(Student stu, int k) {
        int firstSlot, LastSlot;
        //Integer [] stuSlots=Arrays.sort((Integer [])stu.getSlots().toArray());

        int[] stuSlots = stu.getSlots().stream().mapToInt(i -> i).toArray(); //array list to array
        Arrays.sort(stuSlots);
        firstSlot = stuSlots[0];
        LastSlot = stuSlots[stuSlots.length - 1];
        int leng = (((LastSlot - 1) / 3) - ((firstSlot - 1) / 3)) + 1;
        MainDriver.students.get(k).setExamsLen(leng);
        MainDriver.students.get(k).setFirstSlot(firstSlot);
        MainDriver.students.get(k).setLastSlot(LastSlot);


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


    public static double calculateStats() {
        double avgSum = 0;
        double varSum = 0;
        int countHasExams = 0;
        MainDriver.b2bTotal = 0;
        MainDriver.fourin2total = 0;
        MainDriver.end5start8sum=0;
        for (int i = 0; i < MainDriver.students.size(); i++) {
            MainDriver.fourin2total += StatCalculationFunctions.FourInTwo(MainDriver.students.get(i).getSlots());
            MainDriver.b2bTotal += StatCalculationFunctions.b2b(MainDriver.students.get(i).getSlots());
            MainDriver.end5start8sum+=StatCalculationFunctions.from5end8start(MainDriver.students.get(i).getSlots());
            if (MainDriver.students.get(i).getSlots().size() > 0) {
                //students.get(i).printSlots();
                StatCalculationFunctions.calculateFullExamLengeth(MainDriver.students.get(i), i);
                avgSum += StatCalculationFunctions.calculateAvgSpacesBetweenExamsForOneStudent(MainDriver.students.get(i));
                varSum += StatCalculationFunctions.calculateVarianceOfSpaceForOneStudent(MainDriver.students.get(i));

                //System.out.println(" ");
                countHasExams++;
            }

        }
        double max = avgSum / (double) countHasExams;
        MainDriver.variance = (varSum / countHasExams);

        if (MainDriver.fourin2total < 30) {
            System.out.println("4 in 2 count is: " + MainDriver.fourin2total);
            System.out.println("average mean of the solution = " + (avgSum / countHasExams));
            System.out.println("average variance of the solution = " + (varSum / countHasExams));
            System.out.println("back to back count is: " + MainDriver.b2bTotal);
        }
//        if (max > maxMean) {
//            maxMean = max;
//            System.out.println("New max : " + maxMean);
//            System.out.println("average mean of the solution = " + (avgSum / countHasExams));
//            System.out.println("average variance of the solution = " + (varSum / countHasExams));
//            System.out.println("back to back count is: " + b2bTotal);
//            System.out.println("4 in 2 count is: " + fourin2total);
//
////            for (int j = 0; j < variables.size(); j++) {
////                System.out.println(variables.get(j)+"\tvar index"+j);
////            }
//        }


        return max;
    }

    public static int from5end8start(ArrayList<Integer> slots) {
        Collections.sort(slots);
        int sum = 0;
        for (int i = 0; i < slots.size(); i++) {
            if (i == 0)
                continue;
            else {
                if ((slots.get(i) % 3 == 1) && Math.abs(slots.get(i - 1) - slots.get(i)) == 1) {
                    sum += 1;
                }
                //print(abs(list[index]-list[index-1]))

            }
        }
        return sum;


    }


}
