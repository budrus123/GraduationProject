import java.util.ArrayList;

public class Validation {


    public static boolean numberTimeSlots(ArrayList<Student> students){

        int numberOfSlots = 0;
        int numberOfUniversitySlots = 0;
        for(Student student: students){
            numberOfSlots+=student.getSlots().size();
            numberOfUniversitySlots+=student.getSlotsU().size();
            if(student.getSlots().size()!=student.getSlotsU().size()){
                System.out.println(student.getSlots());
                System.out.println(student.getSlotsU());

            }
        }
        System.out.println(numberOfSlots - numberOfUniversitySlots);
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

    public static void validateSolution() {
        int flag = 0;
        for (int i = 0; i < TryingChoco1.students.size(); i++) {
            //count the number of students who have 4 in 2

            if (!Helper_Functions.checkIfTwoSlotsSame(TryingChoco1.students.get(i).getSlots()) ||
                    !Helper_Functions.checkIfThreeSameDay(TryingChoco1.students.get(i).getSlots())) {
                //System.out.println(students.get(i).getSlots() + "  " + students.get(i).getId());
                flag = 1;
//                System.out.println(studentExams+"\n"+students.get(i).getId());
            }
        }
        if (flag == 0)
            System.out.println("***********solution is valid***********");
        else
            System.out.println("solution is not valid");

    }



}
