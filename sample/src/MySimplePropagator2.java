import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;

import java.util.ArrayList;

import static mahmoud.Main.*;


// Propagator to apply X >= Y
public class MySimplePropagator2 extends Propagator<IntVar> {
    IntVar x, y;
    IntVar vars[];

    public MySimplePropagator2(IntVar[] vars) {
        super(vars);
        this.vars = vars;
//        this.x = x;
//        this.y = y;

    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
        for (int i = 0; i < vars.length; i++) {
            vars[i].updateBounds(vars[i].getLB(), vars[i].getUB(), this);

        }
//        x.updateLowerBound(y.getLB(), this);
//        y.updateUpperBound(x.getUB(), this);

    }

    @Override
    public ESat isEntailed() {

        for (int i=0;i<st.length;i++){
            ArrayList<Course> cAL=st[i].getAl();
        }
//
//        for (int i = 0; i < vars.length; i++) {
//            for (int j = i + 1; j < vars.length; j++) {
//
//            }
//        }
//        if (vars[i].getUB() < vars[j].getLB())
//            return ESat.FALSE;
//        else if (vars[i].getLB() >= vars[j].getUB())
//            return ESat.TRUE;
//        else
            return ESat.UNDEFINED;
    }
}
