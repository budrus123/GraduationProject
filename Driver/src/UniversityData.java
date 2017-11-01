import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class UniversityData {


    ArrayList<Student> students;
    static Connection connection;

    public UniversityData(ArrayList<Student> students) {
        this.students = students;

        try {
            connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Password.password);
            fillStudentObjects();
            calculateStatsU();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public static void calculateFullExamLengethU(Student student) {
        int firstSlot,LastSlot;

        int[] stuSlots=student.getSlotsU().stream().mapToInt(i->i).toArray(); //array list to array
        Arrays.sort(stuSlots);
        firstSlot=stuSlots[0];
        LastSlot=stuSlots[stuSlots.length-1];
        int leng= (((LastSlot -1)/3) - ((firstSlot -1)/3)) +1;
        student.setExamsLenU(leng);
        student.setFirstSlotU(firstSlot);
        student.setLastSlotU(LastSlot);

    }

    public double calculateAvgDaysBetweenExams(Student stu) {
        int [][] s2D = stu.getSlots2DU();
        int count=0;
        for(int i=(stu.getFirstSlotU()-1)/3;i<(stu.getLastSlotU()-1)/3;i++){
            if((s2D[i][0]==0)&&(s2D[i][1]==0)&&(s2D[i][2]==0)){
                count++;
            }

        }
        int m=(stu.getFirstSlotU()-1)/3;
        int numofExamsFirstDay=s2D[m][0]+s2D[m][1]+s2D[m][2];
        double avg=(count)/((double)stu.getSlotsU().size() - numofExamsFirstDay);
        if(Double.isNaN(avg)){
            stu.setAvgDaysBetweenExamsU(0);
        }
        else{
            stu.setAvgDaysBetweenExamsU(avg);
        }
        return stu.getAvgDaysBetweenExamsU();
    }


    public double calculateVarianceOfSpace(Student stu) {
        int [][] s2D = stu.getSlots2DU();
        double sum=0;
        int count=0;
        for(int i=(stu.getFirstSlotU()-1)/3;i<(((stu.getLastSlotU()-1)/3)+1);i++){
            //System.out.println("i is -> "+i);
            if(i==(stu.getFirstSlotU()-1)/3){
                continue;
            }
            if((s2D[i][0]==0)&&(s2D[i][1]==0)&&(s2D[i][2]==0)){
                count++;
            }
            if((s2D[i][0]==1)||(s2D[i][1]==1)||(s2D[i][2]==1)){
                if (s2D[i][0] == 1) {
                    sum+=Math.pow((double)count-stu.getAvgDaysBetweenExamsU(),2);
                    count=0;
                }
                if (s2D[i][1] ==1) {
                    sum+=Math.pow((double)count-stu.getAvgDaysBetweenExamsU(),2);
                    count=0;
                }
                if (s2D[i][2] == 1){
                    sum+=Math.pow((double)count-stu.getAvgDaysBetweenExamsU(),2);
                    count=0;
                }

            }


        }
        int m=(stu.getFirstSlotU()-1)/3;
        int numofExamsFirstDay=s2D[m][0]+s2D[m][1]+s2D[m][2];
        double var=sum/((double)stu.getSlotsU().size() - numofExamsFirstDay-1);
        if(Double.isNaN(var)){
            stu.setVarianceOfSpacesU(0);

        }
        else{
            stu.setVarianceOfSpacesU(sum/((double)stu.getSlotsU().size() - numofExamsFirstDay-1));
        }
        return stu.getVarianceOfSpacesU();
    }





    public void calculateStatsU() {
        double avgSum=0;
        double varSum=0;
        int countHasExams=0;
        for(int i=0;i<students.size();i++){
            //System.out.println("\n***printing stats for student number "+ i+" ***");

            if(students.get(i).getSlotsU().size()>0){
                //students.get(i).printSlots();
                calculateFullExamLengethU(students.get(i));
                avgSum+=calculateAvgDaysBetweenExams(students.get(i));
                varSum+=calculateVarianceOfSpace(students.get(i));

                //System.out.println(" ");
                countHasExams++;
            }

        }
        System.out.println("average mean of the University solution = "+(avgSum/countHasExams));
        System.out.println("average variance of the University solution = "+(varSum/countHasExams));
    }


    public void fillStudentObjects() throws SQLException {

        Statement statement = connection.createStatement();

        ResultSet getStudents = statement.executeQuery("SELECT * FROM result_uni");
        while (getStudents.next()) {
            //System.out.println("S - " + getStudents.getInt("STUDENT_NUMBER"));
            students.get(getStudents.getInt("STUDENT_NUMBER")-1).addSlotU(getStudents.getInt("time_slot"));
        }

        for(Student student: students){
            student.setSlotsU(student.getSlotsU());
        }
    }


}
