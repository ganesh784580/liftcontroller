package RefactoredliftController.liftcontroller;

import RefactoredliftController.liftcontrolerInterface.LiftRequestRaised;
import RefactoredliftController.liftcontrolerInterface.LiftStateInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LiftController {

    private LiftRequestRaised Helper;
    private LiftScheduler scheduler;
    private LiftStateInfo StateInfo;
    private WaitingRequestsHandler RequestsHandler;
    private Lift lifts[];

    public LiftController(int NumberOfFloors, int NumberOfElevator) {
        lifts = new Lift[NumberOfElevator];
        for (int i = 0; i < NumberOfElevator; ++i) {
            lifts[i] = new Lift(NumberOfFloors);
        }
        scheduler = new LiftScheduler(NumberOfFloors, NumberOfElevator, lifts);
        StateInfo = new LiftStateManager(lifts);

        RequestsHandler = new WaitingRequestsHandler(NumberOfFloors, NumberOfElevator,
                lifts);
        Helper = new LiftControllerHelper(NumberOfFloors, NumberOfElevator,
                scheduler, StateInfo, RequestsHandler);
        RequestsHandler.setLiftRequest(Helper); 

    }

    public static void main(String[] args) throws IOException {
        BufferedReader Input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the number of floors");
        int FloorNum = Integer.parseInt(Input.readLine());
        System.out.println("Enter the number of Elevators");
        int ElevatorNum = Integer.parseInt(Input.readLine());
        LiftController Controller = new LiftController(FloorNum, ElevatorNum);

        boolean Exit = false;
        while (!Exit) {
            System.out.println("Enter...\n 1.) for moving Up\n 2.) for moving down\n 3.) for AllLiftStatus\n 4.) forExit ");
            int choice = Integer.parseInt(Input.readLine());
            int FloorNumber;
            Lift lift;
            Request req;
            switch (choice) {
                case 1:
                    System.out.println("Enter your current floor number");
                    FloorNumber = Integer.parseInt(Input.readLine());
                    req = new Request();
                    req.setDirection(LiftState.MOVINGUP.value());
                    req.setFloor(FloorNumber);
                    lift = Controller.Helper.moveRequest(req);
                    if (lift != null) {
                        System.out.println("Lift " + lift.getLiftNumber() + " is on the way for floor" + req.getFloor());
                    } else {
                        System.out.println("right now all the lifts are busiy!! your request is submitted and soon will be serviced ");
                    }
                    break;
                case 2:
                    System.out.println("Enter your current floor number");
                    FloorNumber = Integer.parseInt(Input.readLine());
                    req = new Request();
                    req.setDirection(LiftState.MOVINGDOWN.value());
                    req.setFloor(FloorNumber);
                    lift = Controller.Helper.moveRequest(req);
                    if (lift != null) {
                        System.out.println("Lift " + lift.getLiftNumber() + " is on the way for floor" + req.getFloor());
                    } else {
                        System.out.println("Sorry right now all the lifts are busiy!!");
                    }

                    break;
                case 3:
                    //System.out.println(Thread.currentThread().getState());
                    Controller.StateInfo.showLiftsStatus();
                    break;

                case 4:
                    Controller.StateInfo.stopLifts();
                    Controller.RequestsHandler.stopHandler();

                    /*try { 
                     Thread.sleep(4000);
                     } catch (InterruptedException ex) {
                     Logger.getLogger(LiftController.class.getName()).log(Level.SEVERE, null, ex);
                     }*/
                    Exit = true;
                    System.out.println("Thanks for Using Elevator");
                    break;

                default:
                    System.out.println("Oops!!! that was a Invalid input try again " + choice);
                    break;
            }

        }
    }

}
