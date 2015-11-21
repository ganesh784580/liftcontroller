

package RefactoredliftController.liftcontrolerInterface;

import RefactoredliftController.liftcontroller.Lift;
import RefactoredliftController.liftcontroller.Request;


public interface LiftRequestRaised {

    public Lift moveRequest(Request req);
    
}
