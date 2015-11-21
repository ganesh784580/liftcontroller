package RefactoredliftController.liftcontroller;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Lift implements Events,Comparable<Lift>{
     
    private Thread thrd;
    private int CurrentFloor;
    private int State;
    private int RelativeDistance;
    private int NumberOfFloors;
    private int Requests;
    private static int LiftCount=0;
    private final int LiftNumber=++LiftCount;
    private boolean IsRequestRaised;
    private boolean Exit=false;
    private boolean Floors[];
    
    public Lift(int NumberOfFloors) {
        Floors=new boolean[NumberOfFloors];
        thrd=new Thread(this,"lift "+ LiftNumber);
        this.NumberOfFloors=NumberOfFloors;
        thrd.start();
    }

    public int getLiftNumber() {
        return LiftNumber;
    }
    
    public boolean isFloorBooked(int FloorNumber){
        return Floors[FloorNumber];
    }
    
    public int getRelativeDistance() {
        return RelativeDistance;
    }

    public void setRelativeDistance(int RelativeDistance) {
        this.RelativeDistance = RelativeDistance;
    }
    
    /*public void setLiftWaitingTime(int LiftWaitingTime) {
        this.LiftWaitingTime = LiftWaitingTime;
    }*/

    public int getCurrentFloor() {
        return CurrentFloor;
    }

    public void setState(int State) {
        this.State = State;
    }
    
    public int getState() {
        return State;
    }

    public int getRequests() {
        return Requests;
    }

    public void setRequests() {
        ++Requests;
    }

     
    @Override
    public void run() {
     System.out.println(Thread.currentThread().getName() + " is Started");
        while(true){
    if(IsRequestRaised){
         System.out.print("Current floor ");
         while(State!=LiftState.REST.value()&&Requests>0){
            if(Floors[CurrentFloor]){
                try {
                    --Requests;
                    Floors[CurrentFloor]=false;
                    Thread.sleep(4000);
                    System.out.println("Lift" + getLiftNumber() + " is opening at floor " + getCurrentFloor());
                } catch (InterruptedException ex) {
                    Logger.getLogger(Lift.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(Requests==0)
                    break;
            }
            if(State==LiftState.MOVINGUP.value())
            {
                System.out.print(this.CurrentFloor=(CurrentFloor+1)%NumberOfFloors);
                try { 
                    Thread.sleep(4000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Lift.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.print("\rCurrent floor ");
            }
            else{
                System.out.print(this.CurrentFloor--);
            try { 
                    Thread.sleep(4000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Lift.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.print("\rCurrent floor ");
                
            }
            try {
                Thread.sleep(300);
            } 
            catch (InterruptedException ex) {
                Logger.getLogger(Lift.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        State=LiftState.REST.value();
        IsRequestRaised=false;
        System.out.println(CurrentFloor + "\n");
    }
      
         else if(Exit){
//        System.out.println(Thread.currentThread().getName()+ " is gong ");
        break;
    }
         else{
             try { 
             Thread.sleep(1);
         } catch (InterruptedException ex) {
             Logger.getLogger(Lift.class.getName()).log(Level.SEVERE, null, ex);
         }
         }
    }
    }
    @Override
    public int compareTo(Lift o) { 
        return this.RelativeDistance-o.RelativeDistance;
    }

    void requestFloor(int floor) {
        Floors[floor]=true;
    }
    
    void requestRaised(){
        IsRequestRaised=true;
    }
    void stopLift(){
        Exit=true;
    //System.out.println(Thread.currentThread().getName()+" is "+thrd.getState());
        try {
            thrd.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Lift.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(thrd.getName() + " is Stopped");
    }    
}
