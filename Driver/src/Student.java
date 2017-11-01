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

    // For University

    private ArrayList<Integer> slotsU=new ArrayList<Integer>(); //has the time slots of exams 2,3,21,22
    private int [][] slots2DU;
    private int examsLenU;// to calculate the len of the students exam period U
    private int firstSlotU;
    private int lastSlotU;
    private double avgDaysBetweenExamsU;
    private double varianceOfSpacesU;

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public ArrayList<Course> getStudentCourses() {
        return studentCourses;
    }

    public void setStudentCourses(ArrayList<Course> studentCourses) {
        this.studentCourses = studentCourses;
    }

    public ArrayList<Integer> getSlotsU() {
        return slotsU;
    }

    public void setSlotsU(ArrayList<Integer> slotsU) {
        this.slotsU = slotsU;
        this.slots2DU= convert2D(slotsU);
    }

    public int[][] getSlots2DU() {
        return slots2DU;
    }

    public void setSlots2DU(int[][] slots2DU) {
        this.slots2DU = slots2DU;
    }

    public int getExamsLenU() {
        return examsLenU;
    }

    public void setExamsLenU(int examsLenU) {
        this.examsLenU = examsLenU;
    }

    public int getFirstSlotU() {
        return firstSlotU;
    }

    public void setFirstSlotU(int firstSlotU) {
        this.firstSlotU = firstSlotU;
    }

    public int getLastSlotU() {
        return lastSlotU;
    }

    public void setLastSlotU(int lastSlotU) {
        this.lastSlotU = lastSlotU;
    }

    public double getAvgDaysBetweenExamsU() {
        return avgDaysBetweenExamsU;
    }

    public void setAvgDaysBetweenExamsU(double avgDaysBetweenExamsU) {
        this.avgDaysBetweenExamsU = avgDaysBetweenExamsU;
    }

    public double getVarianceOfSpacesU() {
        return varianceOfSpacesU;
    }

    public void setVarianceOfSpacesU(double varianceOfSpacesU) {
        this.varianceOfSpacesU = varianceOfSpacesU;
    }

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
        this.slots2D= convert2D(slots);
    }

    public int [][] convert2D (ArrayList<Integer> slots){
        int [][] s2D = new int[12][3];

        for(int i=0;i<slots.size();i++){
            s2D[(slots.get(i)-1)/3][(slots.get(i)-1)%3]=1;
        }
        return s2D;
    }


    public void printSlots() {
        //System.out.println(slots2D);

        for (int i = 0; i < 12; i++) {
            System.out.print("day " + (i + 1) + " -> ");
            for (int k = 0; k < 3; k++) {
                System.out.print(slots2D[i][k] + " ");
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


    public void addSlotU (int slotU) {
        this.slotsU.add(slotU);
    }

}
