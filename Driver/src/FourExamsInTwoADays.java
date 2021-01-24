import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;


// Propagator to apply X >= Y
public class FourExamsInTwoADays extends Propagator<IntVar> {
    IntVar course1, course2, course3,course4;
    //IntVar vars[];

    public FourExamsInTwoADays(IntVar[] varsz) {
        super(varsz, PropagatorPriority.CUBIC, true);
        //this.vars = varsz;
        this.course1 = vars[0];
        this.course2 = vars[1];
        this.course3 = vars[2];
        this.course4 = vars[2];
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {

    }

    @Override
    public void propagate(int idxVarInProp, int evtmask) throws ContradictionException {
        if (vars[idxVarInProp] == course1) {
            IntVar[] courses = Helper_Functions.twoOfThreeInsta(course2,course3,course4);
            if ((courses != null) && (Helper_Functions.fourExamsInTwoDays(course1.getValue(),courses[1].getValue(),courses[2].getValue()))) {
                int[] interval = Helper_Functions.getInterval(course1.getValue(),courses[1].getValue(),courses[2].getValue());
                courses[0].removeInterval(interval[0], interval[1], this);
            }
        } else if (vars[idxVarInProp] == course2) {
            IntVar[] courses = Helper_Functions.twoOfThreeInsta(course1,course3,course4);
            if ((courses != null) && (Helper_Functions.fourExamsInTwoDays(course2.getValue(),courses[1].getValue(),courses[2].getValue()))) {
                int[] interval = Helper_Functions.getInterval(course2.getValue(),courses[1].getValue(),courses[2].getValue());
                courses[0].removeInterval(interval[0], interval[1], this);
            }
        } else if (vars[idxVarInProp] == course3) {
            IntVar[] courses = Helper_Functions.twoOfThreeInsta(course2,course1,course4);
            if ((courses != null) && (Helper_Functions.fourExamsInTwoDays(course3.getValue(),courses[1].getValue(),courses[2].getValue()))) {
                int[] interval = Helper_Functions.getInterval(course3.getValue(),courses[1].getValue(),courses[2].getValue());

                courses[0].removeInterval(interval[0], interval[1], this);
            }
        }
        else if (vars[idxVarInProp] == course4){
            IntVar[] courses = Helper_Functions.twoOfThreeInsta(course2,course3,course1);
            if ((courses != null) && (Helper_Functions.fourExamsInTwoDays(course4.getValue(),courses[1].getValue(),courses[2].getValue()))) {
                int[] interval = Helper_Functions.getInterval(course4.getValue(),courses[1].getValue(),courses[2].getValue());
                courses[0].removeInterval(interval[0], interval[1], this);
            }
        }

    }

    @Override
    public ESat isEntailed() {
        if(course1.isInstantiated() && course2.isInstantiated() && course3.isInstantiated() && course4.isInstantiated()){
            if(Helper_Functions.fourExamsInTwoDays(course1.getValue(),course2.getValue(),course3.getValue()) && Helper_Functions.fourExamsInTwoDays(course1.getValue(),course2.getValue(),course4.getValue())){
                return  ESat.FALSE;
            }
            else
                return ESat.TRUE;
        }
        else
            return ESat.UNDEFINED;


    }

}
