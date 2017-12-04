import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;

import java.util.ArrayList;
import java.util.Arrays;


// Propagator to apply X >= Y
public class ThreeInaRow extends Propagator<IntVar> {
    IntVar x, y, z;
    //IntVar vars[];

    public ThreeInaRow(IntVar[] varsz) {
        super(varsz, PropagatorPriority.CUBIC, true);
        //System.out.println(vars[0]);
        //this.vars = varsz;
        this.x = vars[0];
        this.y = vars[1];
        this.z = vars[2];
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
            //int[] interval = Helper_Functions.getInterval(x.getValue());
            if (y.isInstantiated() && ((y.getValue() == x.getValue() + 1) || (y.getValue() == x.getValue() - 1))) {
                if ((y.getValue() == x.getValue() + 1) && (y.getValue() != 36)) {
                    z.removeValue(x.getValue() + 2, this);
                }
                if ((y.getValue() == x.getValue() - 1) && (y.getValue() != 1)) {
                    z.removeValue(x.getValue() - 2, this);
                }
                //z.removeInterval(interval[0], interval[1], this);
            } else if (z.isInstantiated() && ((z.getValue() == x.getValue() + 1) || (z.getValue() == x.getValue() - 1))) {
                if ((z.getValue() == x.getValue() + 1) && (z.getValue() != 36)) {
                    y.removeValue(x.getValue() + 2, this);
                }
                if ((z.getValue() == x.getValue() - 1) && (z.getValue() != 1)) {
                    y.removeValue(x.getValue() - 2, this);
                }
            }
        } else if (vars[idxVarInProp] == y) {
            //int[] interval = Helper_Functions.getInterval(y.getValue());
            if (z.isInstantiated() && ((z.getValue() == y.getValue() + 1) || (z.getValue() == y.getValue() - 1))) {
                if ((z.getValue() == y.getValue() + 1) && (z.getValue() != 36)) {
                    x.removeValue(y.getValue() + 2, this);
                }
                if ((z.getValue() == y.getValue() - 1) && (z.getValue() != 1)) {
                    x.removeValue(y.getValue() - 2, this);
                }
                // x.removeInterval(interval[0], interval[1], this);
            } else if (x.isInstantiated() && ((x.getValue() == y.getValue() + 1) || (x.getValue() == y.getValue() - 1))) {
                //z.removeInterval(interval[0], interval[1], this);
                if ((x.getValue() == y.getValue() + 1) && (x.getValue() != 36)) {
                    z.removeValue(y.getValue() + 2, this);
                }
                if ((x.getValue() == y.getValue() - 1) && (x.getValue() != 1)) {
                    z.removeValue(y.getValue() - 2, this);
                }
            }
        } else if (vars[idxVarInProp] == z) {
            int[] interval = Helper_Functions.getInterval(z.getValue());
            if (y.isInstantiated() && ((y.getValue() == z.getValue() + 1) || (y.getValue() == z.getValue() - 1))) {
                if ((y.getValue() == z.getValue() + 1) && (y.getValue() != 36)) {
                    x.removeValue(z.getValue() + 2, this);
                }
                if ((y.getValue() == z.getValue() - 1) && (y.getValue() != 1)) {
                    x.removeValue(z.getValue() - 2, this);
                }
                //x.removeInterval(interval[0], interval[1], this);
            } else if (x.isInstantiated() && ((x.getValue() == z.getValue() + 1) || (x.getValue() == z.getValue() - 1))) {
                if ((x.getValue() == z.getValue() + 1) && (x.getValue() != 36)) {
                    y.removeValue(z.getValue() + 2, this);
                }
                if ((x.getValue() == z.getValue() - 1) && (x.getValue() != 1)) {
                    y.removeValue(z.getValue() - 2, this);
                }
                //y.removeInterval(interval[0], interval[1], this);
            }
        }

    }

    @Override
    public ESat isEntailed() {

        if (x.isInstantiated() && y.isInstantiated() && z.isInstantiated()) {
            int[] nums = {x.getValue(), y.getValue(), z.getValue()};
            Arrays.sort(nums);
            if ((nums[2] - nums[0]) == 2) {
                return ESat.FALSE;
            } else
                return ESat.TRUE;
        } else
            return ESat.UNDEFINED;


    }


}
