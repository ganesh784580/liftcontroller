package RefactoredliftController.liftcontroller;

import RefactoredliftController.liftcontrolerInterface.LiftRequestRaised;
import RefactoredliftController.liftcontrolerInterface.WaitingRequetListener;
import java.util.logging.Level;
import java.util.logging.Logger;

class WaitingRequestsHandler extends Thread implements WaitingRequetListener {

    private Request UpRequests[];
    private Request DownRequests[];
    private Thread Thrd;
    private int UpreqCount;
    private int DownReqCount;
    private int NumberofFloors;
    private int NumberofElevators;
    private LiftRequestRaised LiftRequest;
    private boolean Exit;
    private int WaitingRequest;
    private Lift[] lifts;

    WaitingRequestsHandler(int NumberofFloors, int NumberofElevators,
            Lift[] lifts) {

        UpRequests = new Request[NumberofFloors];
        DownRequests = new Request[NumberofFloors];
        this.NumberofElevators = NumberofElevators;
        this.LiftRequest = LiftRequest;
        this.lifts = lifts;
        Thrd = new Thread(this, this.getClass().getSimpleName());
        Thrd.start();
    }

    @Override
    public void run() {
        while (true) {
            if (WaitingRequest > 0) {
                for (int i = 0; i < NumberofElevators; ++i) {
                    Lift lift = lifts[i];
                    if (lift.getState() == LiftState.REST.value()) {
                        for (int j = 0; j < UpRequests.length; ++j) {
                            if (UpRequests[j] != null) {
                                LiftRequest.moveRequest(UpRequests[j]);
                                UpRequests[j] = null;
                                --WaitingRequest;
                            } else if (DownRequests[j] != null) {
                                LiftRequest.moveRequest(DownRequests[j]);
                                DownRequests[j] = null;
                                --WaitingRequest;
                            }
                        }
                    } else if (lift.getState() == LiftState.MOVINGUP.value()) {
                        for (int j = lift.getCurrentFloor(); j < NumberofFloors; ++j) {
                            if (UpRequests[j] != null) {
                                LiftRequest.moveRequest(UpRequests[j]);
                                UpRequests[j] = null;
                                --WaitingRequest;
                            }
                        }
                    } else if (lift.getState() == LiftState.MOVINGDOWN.value()) {
                        for (int j = lift.getCurrentFloor(); j >= 0; --j) {
                            if (DownRequests[j] != null) {
                                LiftRequest.moveRequest(UpRequests[j]);
                                DownRequests[j] = null;
                                --WaitingRequest;
                            }
                        }
                    }
                }

            }
            if (Exit) {
                break;
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LiftScheduler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void setLiftRequest(LiftRequestRaised LiftRequest) {
        this.LiftRequest = LiftRequest;
    }

    @Override
    public void handleRequest(Request req) {
        ++WaitingRequest;
        if (req.getDirection() == LiftState.MOVINGUP.value()) {
            UpRequests[req.getFloor()] = req;
        } else {
            DownRequests[req.getFloor()] = req;
        }
    }

    public void stopHandler() {
        Exit = true;
    }
}
