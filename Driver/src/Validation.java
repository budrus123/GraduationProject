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
}
