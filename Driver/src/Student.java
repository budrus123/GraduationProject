/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

/**
 *
 * @author Mahmoud
 */
public class Student {

    private int id;
    private String dept;
    private ArrayList<Course> studentCourses = new ArrayList<Course>();
    private ArrayList<Integer> slots; //has the time slots of exams 2,3,21,22
    private int [][] slots2D;
    private int examsLen;// to calculate the len of the students exam period
    private int firstSlot;
    private int lastSlot;
    private double avgDaysBetweenExams;
    private double varianceOfSpaces;

    ;
            
            
 public Student(int id, String dept) {
        this.id = id;
        this.dept = dept;

    }

    public String toString() {
        return "Student "+this.id + " " + this.dept;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the al
     */
    public ArrayList getCourses() {
        return studentCourses;
    }

    /**
     * @param al the al to set
     */
    public void setAl(ArrayList al) {
        this.studentCourses = al;
    }

    public void addCourse(Course course) {
        studentCourses.add(course);
    }

    public ArrayList<Integer> getSlots() {
        return slots;
    }

    public void setSlots(ArrayList<Integer> slots) {
        this.slots = slots;
        convert2D(slots);
    }

    public void convert2D (ArrayList<Integer> slots){
        int [][] s2D = new int[11][3];

        for(int i=0;i<slots.size();i++){
            s2D[(slots.get(i)-1)/3][(slots.get(i)-1)%3]=1;
        }

        /*
        below are just for printing
         */
        this.slots2D=s2D;

        System.out.println(slots);

        for(int i=0;i<11;i++){
            System.out.print("day "+(i+1)+" -> ");
            for(int k=0;k<3;k++){
                System.out.print(s2D[i][k]+" ");
            }
            System.out.println("");
        }


    }

    public int getExamsLen() {
        return examsLen;
    }

    public void setExamsLen(int examsLen) {
        this.examsLen = examsLen;
    }

    public int[][] getSlots2D() {
        return slots2D;
    }

    public void setSlots2D(int[][] slots2D) {
        this.slots2D = slots2D;
    }

    public int getFirstSlot() {
        return firstSlot;
    }

    public void setFirstSlot(int firstSlot) {
        this.firstSlot = firstSlot;
    }

    public int getLastSlot() {
        return lastSlot;
    }

    public void setLastSlot(int lastSlot) {
        this.lastSlot = lastSlot;
    }

    public double getAvgDaysBetweenExams() {
        return avgDaysBetweenExams;
    }

    public void setAvgDaysBetweenExams(double avgDaysBetweenExams) {
        this.avgDaysBetweenExams = avgDaysBetweenExams;
    }

    public double getVarianceOfSpaces() {
        return varianceOfSpaces;
    }

    public void setVarianceOfSpaces(double varianceOfSpaces) {
        this.varianceOfSpaces = varianceOfSpaces;
    }
}
