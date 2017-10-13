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

    }

    @Override
    public void propagate(int idxVarInProp, int evtmask) throws ContradictionException {
//        System.out.println(vars[idxVarInProp] + " smth changed");
//        System.out.println(Main.model4.getVar(idxVarInProp) + " changed");
//        System.out.println(Main.model4);

        if (vars[idxVarInProp] == x) {
            int[] interval = getInterval(x.getValue());
            if (y.isInstantiated() && (haveSameDay(x.getValue(), y.getValue()))) {
                z.removeInterval(interval[0], interval[1], this);
            } else if (z.isInstantiated() && (haveSameDay(z.getValue(), x.getValue()))) {
                y.removeInterval(interval[0], interval[1], this);
            }
        } else if (vars[idxVarInProp] == y) {
            int[] interval = getInterval(y.getValue());
            if (z.isInstantiated() && (haveSameDay(z.getValue(), y.getValue()))) {
                x.removeInterval(interval[0], interval[1], this);
            } else if (x.isInstantiated() && (haveSameDay(y.getValue(), x.getValue()))) {
                z.removeInterval(interval[0], interval[1], this);
            }
        } else if (vars[idxVarInProp] == z) {
            int[] interval = getInterval(z.getValue());
            if (y.isInstantiated() && (haveSameDay(y.getValue(), z.getValue()))) {
                x.removeInterval(interval[0], interval[1], this);
            } else if (x.isInstantiated() && (haveSameDay(x.getValue(), z.getValue()))) {
                y.removeInterval(interval[0], interval[1], this);
            }
        }

    }

    @Override
    public ESat isEntailed() {
        if(x.isInstantiated() && y.isInstantiated() && z.isInstantiated()){
            if(((((x.getValue()-1)/3))==(((y.getValue()-1)/3))) && (((((x.getValue()-1)/3))==(((z.getValue()-1)/3))))){
                return  ESat.FALSE;
            }
            else
                return ESat.TRUE;
        }
        else
            return ESat.UNDEFINED;


    }

    public int[] getInterval(int x) {
        int[] interval = new int[2];
        if (x == 1) {
            interval[0] = 2;
            interval[1] = 3;

        } else {
            if (x % 3 == 1) {

            } else if (x % 3 == 2) {
                interval[0] = x - 1;
                interval[1] = x + 1;


            } else if (x % 3 == 0) {
                interval[0] = x - 2;
                interval[1] = x - 1;

            }
        }


        return interval;
    }

    public boolean haveSameDay(int x, int y) {
        if (((x - 1) / 3) == (((y - 1) / 3))) {
            return true;

        }
        return false;
    }

}
