

package RefactoredliftController.liftcontroller;

 
public enum LiftState {
 MOVINGUP(1),MOVINGDOWN(-1),REST(0);
 private final int State;
    private LiftState(int state){
        State=state;
    }
    
    public int value(){
        return State;
    }
 
}
