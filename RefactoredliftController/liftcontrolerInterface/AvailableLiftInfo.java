

package RefactoredliftController.liftcontrolerInterface;

import RefactoredliftController.liftcontroller.Lift;
import RefactoredliftController.liftcontroller.Request;



public interface AvailableLiftInfo {

    public Lift getLift(Request req);
    
}
