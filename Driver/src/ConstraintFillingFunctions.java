import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ConstraintFillingFunctions {

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
                    new ThreeInADay(new IntVar[]{MainDriver.variables.get(subsets.get(i)[0]), MainDriver.variables.get(subsets.get(i)[1]),
                            MainDriver.variables.get(subsets.get(i)[2])}));
            MainDriver.model.post(th);

            //System.out.println(th);
            MainDriver.constCounter++;

        }

    }
    static public void studentHassThreeOrMoreNumber2(ArrayList<Course> courseAL) {

        int[] input = new int[courseAL.size()];    // input array

        for (int i = 0; i < courseAL.size(); i++) {
            input[i] = courseAL.get(i).getVariableIndex();
        }
        int k = 3;
        List<int[]> subsets = Helper_Functions.allCombination(input, k);
        //System.out.println(variables.get(courseAL.get(i).getVariableIndex()) +" "+variables[i+1]+"  "+variables[j]);

        for (int i = 0; i < subsets.size(); i++) {
            //System.out.println(subsets.get(i)[0]+" "+subsets.get(i)[1]+" "+subsets.get(i)[2]);
            Constraint th = new Constraint("Three in a row " + i,
                    new ThreeInaRow(new IntVar[]{MainDriver.variables.get(subsets.get(i)[0]), MainDriver.variables.get(subsets.get(i)[1]),
                            MainDriver.variables.get(subsets.get(i)[2])}));
            MainDriver.model.post(th);

            //System.out.println(th);
            MainDriver.constCounter++;

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
                    new FourExamsInTwoADays(new IntVar[]{MainDriver.variables.get(subsets.get(i)[0]), MainDriver.variables.get(subsets.get(i)[1]),
                            MainDriver.variables.get(subsets.get(i)[2]), MainDriver.variables.get(subsets.get(i)[3])}));
            //model5.allDifferent(variables).post();;
            MainDriver.model.post(th);
            MainDriver.constCounter++;
            //System.out.println(th);

        }


    }
    //################################################################################################################


    //################################################################################################################
    public static void oneMaxForEachStudent() throws SQLException {

        for (int i = 0; i < MainDriver.students.size(); i++) {
            Statement stmt = MainDriver.connection.createStatement();
            ResultSet getStudentCourses = stmt.executeQuery("SELECT * FROM student_course_table where STUDENT_NUMBER='" + (i + 1) + "'");
            Statement stmt2 = MainDriver.connection.createStatement();
            ResultSet getStudentCoursesCount = stmt2.executeQuery("SELECT count(*) FROM student_course_table where STUDENT_NUMBER='" + (i + 1) + "'");

            getStudentCoursesCount.next();
            int count = Integer.valueOf(getStudentCoursesCount.getString(1));
            //System.out.println(getStudentCoursesCount.getString(1));
            int q = 0;
            if (count > 1) {
                IntVar[] studentCourses = new IntVar[count];

                while (getStudentCourses.next()) {
                    studentCourses[q++] = MainDriver.variables.get(Integer.valueOf(getStudentCourses.getString(2)) - 1);
                }

                for (int m = 0; m < count; m++) {
                    for (int n = m; n < count; n++) {
                        MainDriver.model.arithm(studentCourses[m], "-", studentCourses[n], ">", 2).post();

                    }
                }
                //model.allDifferent(studentCourses);
            }


        }
    }
    //################################################################################################################


    //################################################################################################################
    public static void fillHardConst() throws SQLException {
        for (int i = 0; i < MainDriver.courseAL.size(); i++) {
            for (int j = i + 1; j < MainDriver.courseAL.size(); j++) {
                if (Helper_Functions.haveCommonStudents(MainDriver.courseAL.get(i), MainDriver.courseAL.get(j))) {
                    MainDriver.model.allDifferent(MainDriver.variables.get(i), MainDriver.variables.get(j)).post();


                }
            }
        }
    }


}
