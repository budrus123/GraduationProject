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

}
