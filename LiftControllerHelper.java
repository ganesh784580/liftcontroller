
package liftcontroller;

import java.util.logging.Level;
import java.util.logging.Logger;

class LiftControllerHelper {
   
    private LiftScheduler Scheduler;
    private int NumberofElevators;
    private int NumberofFloors;
    private boolean Exit;
    private WaitingRequestsHandler WReqhandler;
    private int WaitingRequest;
    
    LiftControllerHelper(int NumberOfFloors, int NumberOfElevator) {
        Scheduler=new LiftScheduler(NumberOfFloors,NumberOfElevator);
        NumberofElevators=NumberOfElevator;
        NumberofFloors=NumberOfFloors;
        WReqhandler=new WaitingRequestsHandler();
    }

    Lift moveUp(Request req) {
       Lift lift= Scheduler.getLift(req);
       if(lift!=null){
           System.out.println("Lift " + lift.getLiftNumber()+" is booked");
           
           if(!lift.isFloorBooked(req.getFloor())) 
            lift.setRequests();
           
            lift.requestFloor(req.getFloor());
            
            if(req.getFloor()<lift.getCurrentFloor())
               lift.setState((-1)*req.getDirection());
            else
               lift.setState(req.getDirection());    
            lift.requestRaised();
       }
       else{
           WReqhandler.handleRequest(req);
           ++WaitingRequest;
       }
       return lift;
    }

    Lift moveDown(Request req) {
       Lift lift= Scheduler.getLift(req);
       if(lift!=null){
            if(!lift.isFloorBooked(req.getFloor())) 
            lift.setRequests();
            lift.requestFloor(req.getFloor());
            if(req.getFloor()>lift.getCurrentFloor())
               lift.setState((-1)*req.getDirection());
            else
               lift.setState(req.getDirection());    
            lift.requestRaised();    
       }
       else{
           WReqhandler.handleRequest(req);
           ++WaitingRequest;
       }
       return lift;
    }

    void showLiftsStatus() {
        Scheduler.showLiftsStatus();
    }

    void stopLifts() {
        Scheduler.stopLifts();
        Exit=true;
    }
    
    private class WaitingRequestsHandler implements Events{
    private Request UpRequests[];
    private Request DownRequests[];
    private Thread Thrd;
    private int UpreqCount;
    private int DownReqCount;
    
    WaitingRequestsHandler() {
        UpRequests=new Request[NumberofFloors];
        DownRequests=new Request[NumberofFloors];
        Thrd=new Thread(this,this.getClass().getSimpleName());
        Thrd.start();
    }
    
    @Override
    public void run(){
        while(true){
            if(WaitingRequest>0){
            for(int i=0;i<NumberofElevators;++i){
                Lift lift=Scheduler.getLift(i);
                if(lift.getState()==LiftState.REST.value()){
                        for(int j=0;j<UpRequests.length;++j){
                            if(UpRequests[j]!=null){
                                moveUp(UpRequests[j]);
                                UpRequests[j]=null;
                                --WaitingRequest;
                            }
                            else if(DownRequests[j]!=null){
                                moveDown(DownRequests[j]);
                                DownRequests[j]=null;
                                --WaitingRequest;
                            }
                        }
                }
                else if(lift.getState()==LiftState.MOVINGUP.value()){
                    for(int j=lift.getCurrentFloor();j<NumberofFloors;++j){
                        if(UpRequests[j]!=null){
                            moveUp(UpRequests[j]);
                            UpRequests[j]=null;
                            --WaitingRequest;
                        }
                    }
                }
                else if(lift.getState()==LiftState.MOVINGDOWN.value()){
                    for(int j=lift.getCurrentFloor();j>=0;--j){
                        if(DownRequests[j]!=null){
                            moveDown(UpRequests[j]);
                            DownRequests[j]=null;
                            --WaitingRequest;
                        }
                    }
                }
            }
            
        }
            if(Exit){
                break;
            }
            else{
                try { 
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LiftScheduler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }
  }

    void handleRequest(Request req){
        ++WaitingRequest;
        if(req.getDirection()==LiftState.MOVINGUP.value()){
            UpRequests[req.getFloor()]=req;
        }
        else{
            DownRequests[req.getFloor()]=req;
        }
    }
}
}
