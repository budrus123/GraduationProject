import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;


// Propagator to apply X >= Y
public class ThreeInADay extends Propagator<IntVar> {
    IntVar x, y, z;
    //IntVar vars[];

    public ThreeInADay(IntVar[] varsz) {
        super(varsz, PropagatorPriority.CUBIC, true);
        //System.out.println(vars[0]);
        //this.vars = varsz;
        this.x = vars[0];
        this.y = vars[1];
        this.z = vars[2];
//        System.out.println(x);
//        System.out.println(y);
//        System.out.println(z);
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
       // System.out.println(" smth changed");
    }

    @Override
    public void propagate(int idxVarInProp, int evtmask) throws ContradictionException {
//        System.out.println(vars[idxVarInProp] + " smth changed");
//        System.out.println(Main.model4.getVar(idxVarInProp) + " changed");
//        System.out.println(Main.model4);

        if (vars[idxVarInProp] == x) {
            int[] interval = Helper_Functions.getInterval(x.getValue());
            if (y.isInstantiated() && (Helper_Functions.haveSameDay(x.getValue(), y.getValue()))) {
                z.removeInterval(interval[0], interval[1], this);
            } else if (z.isInstantiated() && (Helper_Functions.haveSameDay(z.getValue(), x.getValue()))) {
                y.removeInterval(interval[0], interval[1], this);
            }
        } else if (vars[idxVarInProp] == y) {
            int[] interval = Helper_Functions.getInterval(y.getValue());
            if (z.isInstantiated() && (Helper_Functions.haveSameDay(z.getValue(), y.getValue()))) {
                x.removeInterval(interval[0], interval[1], this);
            } else if (x.isInstantiated() && (Helper_Functions.haveSameDay(y.getValue(), x.getValue()))) {
                z.removeInterval(interval[0], interval[1], this);
            }
        } else if (vars[idxVarInProp] == z) {
            int[] interval = Helper_Functions.getInterval(z.getValue());
            if (y.isInstantiated() && (Helper_Functions.haveSameDay(y.getValue(), z.getValue()))) {
                x.removeInterval(interval[0], interval[1], this);
            } else if (x.isInstantiated() && (Helper_Functions.haveSameDay(x.getValue(), z.getValue()))) {
                y.removeInterval(interval[0], interval[1], this);
            }
        }

    }

    @Override
    public ESat isEntailed() {
        if(x.isInstantiated() && y.isInstantiated() && z.isInstantiated()){
            if(Helper_Functions.haveSameDay(x.getValue(),y.getValue()) && Helper_Functions.haveSameDay(x.getValue(),z.getValue())){
                return  ESat.FALSE;
            }
            else
                return ESat.TRUE;
        }
        else
            return ESat.UNDEFINED;


    }



}
