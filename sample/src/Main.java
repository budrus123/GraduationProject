import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;

/**
 * @author Mahmoud
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    static Model model = new Model("my first problem");
    static Model model2 = new Model("my second problem");
    static Model model3 = new Model("my third problem");
    static Model model4 = new Model("my fourth problem");
    static Model model5 = new Model("my fifth problem");

    static ArrayList<Course> courseAL = new ArrayList<Course>();
    static ArrayList<IntVar> varsM = new ArrayList<IntVar>();
    static ArrayList<Student> students = new ArrayList<Student>();
    static Connection connection;
    static Course[] c = new Course[4];
    static IntVar[] variables = new IntVar[4];
    static IntVar[] sample = new IntVar[2];
    static IntVar[] threeCourses = new IntVar[3];
    static Student[] st = new Student[6];

    public static void main(String[] args) throws SQLException, ContradictionException {
        // TODO code application logic here
        // 1. Create a Model 

//        //creating students to test
//        Student s1 = new Student(1, "d");
//        Student s2 = new Student(2, "d");
//        Student s3 = new Student(3, "d");
//        Student s4 = new Student(4, "d");
//        Student s5 = new Student(5, "d");
//        Student s6 = new Student(6, "d");


        st[0] = new Student(1, "d");
        st[1] = new Student(2, "d");
        st[2] = new Student(3, "d");
        st[3] = new Student(4, "d");
        st[4] = new Student(5, "d");
        st[5] = new Student(6, "d");


        c[0] = new Course("phys141");
        c[1] = new Course("math141");
        c[2] = new Course("comp141");
        c[3] = new Course("java");

        c[0].addStudent(st[0]);
        c[0].addStudent(st[2]);
        //c[0].addStudent(s4);
        //c[0].addStudent(s6);

        c[1].addStudent(st[0]);
        c[1].addStudent(st[2]);
        c[1].addStudent(st[1]);
        c[1].addStudent(st[5]);

        c[2].addStudent(st[3]);
        c[2].addStudent(st[0]);

        c[3].addStudent(st[1]);
        c[3].addStudent(st[3]);
        c[3].addStudent(st[4]);
        c[3].addStudent(st[5]);
        //
        //new
        c[3].addStudent(st[0]);
////////////////////////////////////////////////////////
        st[0].addCourse(c[0]);
        st[0].addCourse(c[1]);
        st[0].addCourse(c[2]);
        //new
        st[0].addCourse(c[3]);

        st[1].addCourse(c[1]);
        st[1].addCourse(c[3]);

        st[2].addCourse(c[0]);
        st[2].addCourse(c[1]);

        st[3].addCourse(c[2]);
        st[3].addCourse(c[3]);

        st[4].addCourse(c[3]);

        st[5].addCourse(c[1]);
        st[5].addCourse(c[3]);


//        IntVar phys141 = model.intVar("phys141", 1, 5); // phys141 in 1 to 5
//        IntVar math141 = model.intVar("math141", 1, 5); // math141 in 1 to 5
//        IntVar comp141 = model.intVar("comp141", 1, 5); // comp141 in 1 to 5
//        IntVar java = model.intVar("java", 1, 5); // java in 1 to 5

        variables[0] = model5.intVar("phys141", 1, 6); // phys141 in 1 to 5;
        variables[1] = model5.intVar("math141", 1, 6); // math141 in 1 to 5
        variables[2] = model5.intVar("comp141", 1, 6); // comp141 in 1 to 5
        variables[3] = model5.intVar("java", 1, 6); // java in 1 to 5

        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 4; j++) {
                if (haveCommonStudents(c[i], c[j])) {
                    model5.allDifferent(variables[i], variables[j]).post();
                }
            }
        }
        for(int p=0;p<st.length;p++){
            if(st[p].getAl().size()>=3){
                studentHasMoreThanThreeCourses(st[p].getAl());
            }
        }
//        Constraint th = new Constraint("three in a day", new ThreeInADay(new IntVar[]{variables[0],variables[1],variables[2]}));
        model5.allDifferent(variables).post();;
        //model5.post(th);
        int counter=0;
        while(model5.getSolver().solve()){
            System.out.println("\nmain printing"); // Prints
            System.out.println(variables[0]); // Prints
            System.out.println(variables[1]); // Prints
            System.out.println(variables[2]); // Prints
            System.out.println(variables[3]); // Prints
            counter++;

        }

        System.out.println("number of solutions = "+counter);

        //Constraint cons = new Constraint("MyConstraint", new MySimplePropagator2(variables));

        /*
        //model.post(cons);
        for (int i = 0; i < st.length; i++) {
            ArrayList<Course> cAL = st[i].getAl();
            for (int j = 0; j < cAL.size(); j++) {
                for (int q = j+1; q < cAL.size(); q++) {
                    model.allDifferent(getCourseIntVar(cAL.get(j)),getCourseIntVar(cAL.get(q))).post();
                }
            }
        }
        */
        /*
        here starts the code for two courses not the same
        sample[0] = model2.intVar("phys141", 1, 3); // phys141 in 1 to 2
        sample[1] = model2.intVar("math141", 1, 3); // phys141 in 1 to 2
        MySimplePropagator m= new MySimplePropagator(sample[0],sample[1]);

        Constraint cons = new Constraint("MyConstraint", m);
        //Constraint c =new/ Constraint("s");

        model2.post(cons);
        //model2.postTemp(cons);
        //model2.allDifferent(sample).post();
        //System.out.println(model2.getCstrs().length);
        //System.out.println(m.getModel());
        //model2.not(sample).post();

        */
//        threeCourses[0] = model4.intVar("phys141", 1, 6); // phys141 in 1 to 6
//        threeCourses[1] = model4.intVar("comp141", 1, 6); // comp141 in 1 to 6
//        threeCourses[2] = model4.intVar("math141", 1, 6); // math141 in 1 to 6
        //model3.allDifferent(threeCourses).post();
//        ;
//
//        Constraint th = new Constraint("three in a day", new twoInADay(threeCourses));
//        model3.post(th);
//
//        model3.getSolver().solve();
//        Constraint th = new Constraint("three in a day", new ThreeInADay(threeCourses));
//        model4.allDifferent(threeCourses).post();;
//        model4.post(th);
//
//        while(model4.getSolver().solve()){
//            System.out.println("\nmain printing"); // Prints
//            System.out.println(threeCourses[0]); // Prints
//            System.out.println(threeCourses[1]); // Prints
//            System.out.println(threeCourses[2]); // Prints
//
//        }



//        while(model4.getSolver().solve()){
//            System.out.println("main printing"); // Prints
//            System.out.println(threeCourses[0]); // Prints
//            System.out.println(threeCourses[1]); // Prints
//            System.out.println(threeCourses[2]); // Prints
//        }



        System.out.println(model5); // Prints




        /*
        System.out.println(variables[0]); // Prints
        System.out.println(variables[1]); // Prints
        System.out.println(variables[2]); // Prints
        System.out.println(variables[3]); // Prints
        */
        //permute(java.util.Arrays.asList(3,4,6,2,1), 0);

    }


    static public void studentHasMoreThanThreeCourses(ArrayList<Course> courseAL){

        for(int i=0;i<courseAL.size();i++){
            for(int j=i+2;j<courseAL.size();j++){
                System.out.println(variables[i] +" "+variables[i+1]+"  "+variables[j]);
                Constraint th = new Constraint("three in a day "+i+" "+j+"",
                        new ThreeInADay(new IntVar[]{variables[i],variables[i+1],variables[j]}));
                //model5.allDifferent(variables).post();;
                model5.post(th);
            }
        }

    }


    //################################################################################################################
    //################################################################################################################
    //################################################################################################################
    //################################################################################################################
    //################################################################################################################
    public static void fillHardConst() throws SQLException {
        for (int i = 0; i < courseAL.size(); i++) {
            for (int j = i + 1; j < courseAL.size(); j++) {
                if (haveCommonStudents(courseAL.get(i), courseAL.get(j))) {
                    model.allDifferent(varsM.get(i), varsM.get(j)).post();
                }
            }
        }
    }

    //################################################################################################################
    //################################################################################################################
    //################################################################################################################
    //################################################################################################################
    //################################################################################################################
    public static void fillStudentTakesCourse() throws SQLException {

//    static ArrayList courseAL = new ArrayList<mahmoud.Course>();
//    static ArrayList varsM = new ArrayList<IntVar>();
//    static ArrayList students = new ArrayList<mahmoud.Student>();
/*
-- Dumping structure for table exams.student_course_table
CREATE TABLE IF NOT EXISTS `student_course_table` (
  `STUDENT_NUMBER` int(10) unsigned NOT NULL,
  `COURSE_LABEL` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table exams.student_course_table: ~63,296 rows (approximately)
/*!40000 ALTER TABLE `student_course_table` DISABLE KEYS ;
INSERT INTO `student_course_table` (`STUDENT_NUMBER`, `COURSE_LABEL`) VALUES
	(1, 'JOUR412'),
         */
        for (int i = 0; i < courseAL.size(); i++) {
            Statement stmt = connection.createStatement();
            //String s="SELECT * FROM student_course_table where COURSE_LABEL='"+courseAL.get(i).getLabel()+"'";
            ResultSet course_student = stmt.executeQuery("SELECT * FROM student_course_table where COURSE_LABEL='" + courseAL.get(i).getLabel() + "'");

            while (course_student.next()) {
                courseAL.get(i).addStudent(students.get(Integer.valueOf(course_student.getString("STUDENT_NUMBER")) - 1));
                // System.out.println(course_student.getString("STUDENT_NUMBER"));
            }

            //System.out.println(s);
            //System.out.println(courseAL.get(0).getLabel());
        }

    }

    //################################################################################################################
//################################################################################################################
//################################################################################################################
//################################################################################################################
//################################################################################################################
    public static IntVar getCourseIntVar(Course c) {

        for (int i = 0; i < model.getNbVars(); i++) {
            if (c.getLabel().equals(model.getVar(i).getName())) {
                return (IntVar) model.getVar(i);
            }
        }
        return null;
    }

    //################################################################################################################
    //################################################################################################################
    //################################################################################################################
    //################################################################################################################
    //################################################################################################################
    public static boolean haveCommonStudents(Course c1, Course c2) {
        ArrayList<Student> a1 = new ArrayList<Student>();
        ;
        ArrayList<Student> a2 = new ArrayList<Student>();
        ;

        a1 = c1.getAl();
        a2 = c2.getAl();

        for (int j = 0; j < a2.size(); j++) {
            if (a1.contains(a2.get(j))) {
                return true;
            }
        }
        return false;
    }
}
