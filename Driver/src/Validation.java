import java.util.ArrayList;

public class Validation {


    public static boolean numberTimeSlots(ArrayList<Student> students){

        int numberOfSlots = 0;
        int numberOfUniversitySlots = 0;
        for(Student student: students){
            numberOfSlots+=student.getSlots().size();
            numberOfUniversitySlots+=student.getSlotsU().size();
        }
        return numberOfSlots == numberOfUniversitySlots;
    }



    public static boolean validateUniversitySolution(ArrayList<Student> students){

        boolean flag = true;
        for (int i = 0; i < students.size(); i++) {
            //count the number of students who have 4 in 2
            if (!Helper_Functions.checkIfTwoSlotsSame(students.get(i).getSlotsU()) || !Helper_Functions.checkIfThreeSameDay(students.get(i).getSlotsU())) {
                System.out.println(students.get(i).getId());
                flag = false;
            }
        }

        return flag;
    }



}
