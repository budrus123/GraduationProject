import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;

import java.util.ArrayList;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;

import java.util.ArrayList;


// Propagator to apply X >= Y
public class twoInADay extends Propagator<IntVar> {
    IntVar x, y, z;
    //IntVar vars[];

    public twoInADay(IntVar[] varsz) {
        super(varsz, PropagatorPriority.CUBIC, true);
        //this.vars = varsz;
        this.x = vars[0];
        this.y = vars[1];
        System.out.println(x);
        System.out.println(y);
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
        if (x.isInstantiated()) {
            System.out.println("x ins");
            if (x.getValue() % 3 == 1) {
                y.removeInterval(x.getValue(), x.getValue() + 2, this);
            } else if (x.getValue() % 3 == 2) {
                y.removeInterval(x.getValue() - 1, x.getValue() + 1, this);
            } else if (x.getValue() % 3 == 0) {
                y.removeInterval(x.getValue() - 2, x.getValue(), this);
            }
            this.setPassive();
        } else if (y.isInstantiated()) {
            System.out.println("y ins");
            if (y.getValue() % 3 == 1) {
                x.removeInterval(y.getValue(), y.getValue() + 2, this);
            } else if (y.getValue() % 3 == 2) {
                x.removeInterval(y.getValue() - 1, y.getValue() + 1, this);
            } else if (y.getValue() % 3 == 0) {
                x.removeInterval(y.getValue() - 2, y.getValue(), this);
            }
//            if (x.removeValue(y.getValue(), this) || !x.contains(y.getValue())) {
//                this.setPassive();
//                System.out.println("x got ins");
//            }
//        } else if (x.getUB() < (y.getLB()) || (y.getUB()) < x.getLB()) {
//            setPassive();
//        }
            this.setPassive();
        }
//        else{
//            setPassive();
//        }
    }

    @Override
    public void propagate(int idxVarInProp, int evtmask) throws ContradictionException {
        System.out.println(vars[idxVarInProp]+" smth changed");
        System.out.println(Main.model3.getVar(idxVarInProp) + " changed");
        System.out.println(Main.model3);

        if(vars[idxVarInProp]==x){
            if (x.getValue() % 3 == 1) {
                y.removeInterval(x.getValue(), x.getValue() + 2, this);
            } else if (x.getValue() % 3 == 2) {
                y.removeInterval(x.getValue() - 1, x.getValue() + 1, this);
            } else if (x.getValue() % 3 == 0) {
                y.removeInterval(x.getValue() - 2, x.getValue(), this);
            }
        }
        else if(vars[idxVarInProp]==y){
            if (y.getValue() % 3 == 1) {
                x.removeInterval(y.getValue(), y.getValue() + 2, this);
            } else if (y.getValue() % 3 == 2) {
                x.removeInterval(y.getValue() - 1, y.getValue() + 1, this);
            } else if (y.getValue() % 3 == 0) {
                x.removeInterval(y.getValue() - 2, y.getValue(), this);
            }
        }


    }

    @Override
    public ESat isEntailed() {

//        if (vars[0].isInstantiated() && vars[1].isInstantiated()) {
//            if (((((vars[0].getValue() - 1) / 3))) == ((((vars[1].getValue() - 1) / 3)))) {
//                return ESat.FALSE;
//            } else {
//                return ESat.TRUE;
//            }
//        } else {
//            return ESat.UNDEFINED;
//        }

        if ((((x.getUB() - 1) / 3) < ((y.getLB() - 1) / 3) || (((y.getUB() - 1) / 3) < ((x.getLB() - 1) / 3))))
            return ESat.TRUE;
        else if (x.isInstantiated() && y.isInstantiated())
            return ESat.FALSE;
        else
            return ESat.UNDEFINED;


//        if (vars[0].isInstantiated() && vars[1].isInstantiated() && vars[2].isInstantiated()) {
//            if ((((((vars[0].getValue() - 1) / 3))) == ((vars[1].getValue() - 1) / 3)) && ((((vars[0].getValue() - 1) / 3) == (((vars[2].getValue() - 1) / 3)))))
//
//        } else
//            return ESat.TRUE;
//        return ESat.UNDEFINED;


    }
}
