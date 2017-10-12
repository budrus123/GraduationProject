import java.util.ArrayList;

/**
 *
 * @author Mahmoud
 */

/* 
CREATE TABLE IF NOT EXISTS `course_table` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `COURSE_LABEL` text NOT NULL,
  `COURSE TITLE` text NOT NULL,
  `DEPT` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1005 DEFAULT CHARSET=latin1;\
*/
public class Course {
    
    private String label,title,dept;
    private int id;
    private ArrayList al = new ArrayList<Student>();;
    
    
    public Course(int id,String label,String title,String dept){
        this.id=id;
        this.label=label;
        this.title=title;
        this.dept=dept;
        
    }
     public Course(String label){
       
        this.label=label;
    
        
    }
    @Override
     public String toString(){
         return "mahmoud.Course "+String.valueOf(id)+" "+this.label+" "+this.title+" "+this.dept;
     }

 
    public ArrayList getAl() {
        return al;
    }

    /**
     * @param al the al to set
     */
    public void setAl(ArrayList al) {
        this.al = al;
    }
    
    public void addStudent(Student s){
        al.add(s);
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the dept
     */
    public String getDept() {
        return dept;
    }

    /**
     * @param dept the dept to set
     */
    public void setDept(String dept) {
        this.dept = dept;
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
    
}
