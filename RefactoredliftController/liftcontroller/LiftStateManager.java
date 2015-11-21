package RefactoredliftController.liftcontroller;

import RefactoredliftController.liftcontrolerInterface.LiftStateInfo;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LiftStateManager implements LiftStateInfo {

    private Lift lift[];

    public LiftStateManager(Lift lift[]) {
        this.lift = lift;
    }

    @Override
    public void showLiftsStatus() {
        for (int i = 0; i < lift.length; ++i) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(LiftScheduler.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Lift " + lift[i].getLiftNumber() + " is at " + lift[i].getCurrentFloor());
        }
    }

    @Override
    public void stopLifts() {
        for (int i = 0; i < lift.length; ++i) {
            lift[i].stopLift();
        }
    }

}
