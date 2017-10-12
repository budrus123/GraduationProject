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
        return "mahmoud.Student "+this.id + " " + this.dept;
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
    public ArrayList getAl() {
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
