package RefactoredliftController.liftcontroller;

import RefactoredliftController.liftcontrolerInterface.AvailableLiftInfo;
import RefactoredliftController.liftcontrolerInterface.LiftRequestRaised;
import RefactoredliftController.liftcontrolerInterface.LiftStateInfo;
import RefactoredliftController.liftcontrolerInterface.WaitingRequetListener;

class LiftControllerHelper implements LiftRequestRaised {

    private AvailableLiftInfo Scheduler;
    private int NumberofElevators;
    private int NumberofFloors;
    private WaitingRequetListener WReqhandler;
    private int WaitingRequest;
    private LiftStateInfo LiftstateInfo;

    LiftControllerHelper(int NumberOfFloors, int NumberOfElevator,
            LiftScheduler Scheduler, LiftStateInfo liftstateInfo,
            WaitingRequetListener WReqhandler) {
        this.Scheduler = Scheduler;
        NumberofElevators = NumberOfElevator;
        NumberofFloors = NumberOfFloors;
        this.LiftstateInfo = liftstateInfo;
        this.WReqhandler = WReqhandler;

    }

    @Override
    public Lift moveRequest(Request req) {
        Lift lift = Scheduler.getLift(req);
        if (lift != null) {
            if (!lift.isFloorBooked(req.getFloor())) {
                lift.setRequests();
            }

            lift.requestFloor(req.getFloor());
            if (req.getDirection() == LiftState.MOVINGUP.value()) {
                if (req.getFloor() < lift.getCurrentFloor()) {
                    lift.setState((-1) * req.getDirection());
                } else {
                    lift.setState(req.getDirection());
                }
            } else {
                if (req.getFloor() > lift.getCurrentFloor()) {
                    lift.setState((-1) * req.getDirection());
                } else {
                    lift.setState(req.getDirection());
                }
            }
            lift.requestRaised();
        } else {
            WReqhandler.handleRequest(req);
        }
        return lift;
    }

}
