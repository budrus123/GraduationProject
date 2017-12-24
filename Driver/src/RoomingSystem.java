import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;


public class RoomingSystem {

    int lenOfExamPeriod;
    ArrayList<Course> courseAL;
    ArrayList<IntVar> variables;
    ArrayList<Room> Rooms;
    ArrayList<ArrayList<Course>> dailyExams;
    public RoomingSystem(int lenOfExamPeriod,ArrayList<Course> courseAL,ArrayList<IntVar> variables,ArrayList<Room> Rooms){
        this.lenOfExamPeriod = lenOfExamPeriod;
        this.courseAL = courseAL;
        this.variables = variables;
        this.Rooms = Rooms;
    }
    public boolean roomingSystem(){

        dailyExams = new ArrayList<ArrayList<Course>>();

        for (int j = 1; j < lenOfExamPeriod + 1; j++) {
            ArrayList<Course> inner = new ArrayList<Course>();
            for (int i = 0; i < courseAL.size(); i++) {
                //dailyExams.get(i) = new ArrayList<Course>();
                if (variables.get(courseAL.get(i).getVariableIndex()).getValue() == j) {
                    inner.add(courseAL.get(i));
                }
            }

            for (int i = 0; i < inner.size(); i++) {
                for (int k = i; k < inner.size(); k++) {
                    if (inner.get(k).getAl().size() > inner.get(i).getAl().size()) {
                        Collections.swap(inner, i, k);
                    }
                }
            }

//                for (Course ce : inner)
//                    System.out.println(ce.getAl().size());
            dailyExams.add(inner);
        }


        ArrayList<Room> AvaliableRoom;
        boolean enoughRooms = true;
        for (int i = 0; i < lenOfExamPeriod; i++) {
            AvaliableRoom = new ArrayList<Room>(Rooms);
            for (int j = 0; j < dailyExams.get(i).size(); j++) {
                ArrayList<Room> bestAvalibleRooms = FindBestRooms(AvaliableRoom, dailyExams.get(i).get(j));

                if (bestAvalibleRooms == null) {
                    enoughRooms = false;
                    break;
                }
                dailyExams.get(i).get(j).setRooms(bestAvalibleRooms);
            }

            if (!enoughRooms) {
                break;
            }
        }
        if (!enoughRooms) {
            System.out.println("Rooms are not enough , so this solution is not valid ! ");
            return false;
        }
        return true;
    }

    public static ArrayList<Room> FindBestRooms(ArrayList<Room> Avaliabe, Course course) {
        ArrayList<Room> ExamRoom = new ArrayList<Room>();

        if (Avaliabe.size() == 0) {
            return null;
        }

        int numberofstudent = course.getAl().size() * 2;

        while (numberofstudent > 0) {
            //Single Room Exam
            if (Avaliabe.size() == 0) {
                return null;
            }

            if (Avaliabe.get(Avaliabe.size() - 1).getCapacity() >= numberofstudent) {
                for (Room r : Avaliabe) {

                    if (r.getCapacity() >= numberofstudent) {
                        ExamRoom.add(r);
                        Avaliabe.remove(r);

                        return ExamRoom;
                    }

                }
            }
            //multiple room
            else {
                // After first loop
                ExamRoom.add(Avaliabe.get(Avaliabe.size() - 1));
                numberofstudent -= Avaliabe.get(Avaliabe.size() - 1).getCapacity();
                Avaliabe.remove(Avaliabe.get(Avaliabe.size() - 1));
            }
        }


        return ExamRoom;
    }


    public boolean validateRoomingSystem(){

        boolean confRooms = false;
        for (int i = 1; i < lenOfExamPeriod + 1; i++) {
            ArrayList<Room> slotRooms = new ArrayList<Room>();
            for (int j = 0; j < courseAL.size(); j++) {
                if (variables.get(courseAL.get(j).getVariableIndex()).getValue() == i) {
                    for (int k = 0; k < courseAL.get(j).getRooms().size(); k++) {
                        slotRooms.add(courseAL.get(j).getRooms().get(k));
                    }
                }
            }

            //List<Room> list = slotRooms;
            Set<Room> set = new HashSet<Room>(slotRooms);

            if (set.size() < slotRooms.size()) {
                //if true this means that the hashset has two or more of the same room
                //meaning 2 or more courses in the same room
                System.out.println("Rooms are conflicting");
                confRooms = true;
            }
        }


        boolean roomsvalid = true;
        for (int i = 0; i < courseAL.size(); i++) {
            roomsvalid = validateRoomsCap(courseAL.get(i));
            if (!roomsvalid) {
                System.out.println("Rooms are not of enough SIZE");
                break;
            }
        }
        if (!confRooms) {
//            System.out.println("None of the Rooms are conflicting");
        }


        return !confRooms && roomsvalid;
    }

    public static boolean validateRoomsCap(Course c) {

        int numOfStudents = c.getAl().size() * 2;

        int reservedSeats = 0;
        for (int i = 0; i < c.getRooms().size(); i++) {
            reservedSeats += c.getRooms().get(i).getCapacity();
        }

        if (reservedSeats < numOfStudents) {
            return false;
        }

        return true;

    }


    public void printAllRooms(){
        for (int i = 0; i < lenOfExamPeriod; i++) {
            System.out.println("\n\nRooms for slot #" + i);
            Helper_Functions.printOneTimeSlotInfo(this.dailyExams.get(i));
        }
    }

}
