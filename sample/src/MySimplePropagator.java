import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;

import static org.chocosolver.solver.constraints.PropagatorPriority.QUADRATIC;

// Propagator to apply X > Y
public class MySimplePropagator extends Propagator<IntVar> {
    IntVar x, y;

    public MySimplePropagator(IntVar x, IntVar y) {
        super(new IntVar[]{x, y},QUADRATIC ,true,true);
        this.x = x;
        this.y = y;
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
//        x.updateLowerBound(y.getLB(), this);
//        y.updateUpperBound(x.getUB(), this);
        //int [] xvals=y.g
//        x.removeAllValuesBut(,this);
        //x.removeValue(3,this);
        //System.out.println(y.getRange());
       // x.removeValue(2,this);
        x.updateBounds(x.getUB()-1, x.getUB(), this);
        System.out.println(x+"  "+x.getLB()+" to "+x.getUB()+"  xval  "+x.getValue());
        //y.removeValue(x.getValue(),this);
        y.updateBounds(y.getLB(), y.getUB(), this);

        System.out.println(y+"  "+y.getLB()+" to "+y.getUB()+"  yval  "+y.getValue());
    }

    @Override
    public void propagate(int idxVarInProp,int evtmask) throws ContradictionException {
        System.out.println(Main.model2.getVar(idxVarInProp)+" changed");
        System.out.println(Main.model2);
        //Main.model2.getVar(idxVarInProp).


////        x.updateLowerBound(y.getLB(), this);
////        y.updateUpperBound(x.getUB(), this);
//        //int [] xvals=y.g
////        x.removeAllValuesBut(,this);
//        x.removeValue(3,this);
//        //System.out.println(y.getRange());
//        x.removeValue(2,this);
//        x.updateBounds(x.getLB(), x.getUB(), this);
//        System.out.println("xbounds  "+x.getLB()+"  "+x.getUB()+"  xval  "+x.getValue());
//        y.updateBounds(y.getLB(), y.getUB(), this);
//        y.removeValue(x.getValue(),this);
//        System.out.println("ybounds  "+y.getLB()+"  "+y.getUB()+"  yval  "+y.getValue());
    }

    @Override
    public ESat isEntailed() {

        if(x.getValue()!= y.getValue())
            return ESat.TRUE;
        else
            return ESat.FALSE;
//        if(x.contains(y.getValue())){
//            return ESat.FALSE;
//        }
//        else{
//            return ESat.FALSE;
//        }
        //x.get
//        if(y.getValue()==x.getValue()){
//            return ESat.FALSE;
//        }
//        else {
//            return ESat.TRUE;
//        }
//        if (x.getUB() < y.getLB())
//            return ESat.FALSE;
//        else if (x.getLB() > y.getUB() &&(x.getValue()!=y.getValue()))
//            return ESat.TRUE;
//        else
//            return ESat.UNDEFINED;
    }
}
