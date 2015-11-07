
package liftcontroller;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

class LiftScheduler {
    
    private Lift lift[];
    private Queue<Lift> Dispatcher;
    
    LiftScheduler(int NumberOfFloors, int NumberOfElevator) {
        lift=new Lift[NumberOfElevator];
        for(int i=0;i<NumberOfElevator;++i){
            lift[i]=new Lift(NumberOfFloors);
        }
        Dispatcher=new PriorityQueue<Lift>();
    }

      Lift getLift(Request req) {
        for(int i=0;i<lift.length;++i){
            if(lift[i].getState()==req.getDirection()){
                if(req.getDirection()==LiftState.MOVINGUP.value()&&req.getFloor()>=lift[i].getCurrentFloor()){
                    lift[i].setRelativeDistance(req.getFloor()-lift[i].getCurrentFloor()); 
                   // System.out.println("Lift "+lift[i].getLiftNumber() + " is added");
                    Dispatcher.add(lift[i]);
                }
                else if(req.getDirection()==LiftState.MOVINGDOWN.value()&&req.getFloor()<=lift[i].getCurrentFloor()){
                    lift[i].setRelativeDistance(lift[i].getCurrentFloor()-req.getFloor()); 
                    Dispatcher.add(lift[i]);
                }
            }
            else if(lift[i].getState()==LiftState.REST.value()){
                 lift[i].setRelativeDistance(Math.abs(lift[i].getCurrentFloor()-req.getFloor())); 
                 Dispatcher.add(lift[i]);
            }
           }
         Lift lift=Dispatcher.poll();
         Dispatcher.clear();         
         return lift;
        }

    void showLiftsStatus() {
         for(int i=0;i<lift.length;++i){
             try { 
                 Thread.sleep(1000);
             }catch (InterruptedException ex) {
                 Logger.getLogger(LiftScheduler.class.getName()).log(Level.SEVERE, null, ex);
             }
           System.out.println("Lift " + lift[i].getLiftNumber() + " is at " + lift[i].getCurrentFloor()); 
         }
         
    }

   public  void stopLifts() {
    for(int i=0;i<lift.length;++i){
        lift[i].stopLift();
    }
    }

    public Lift getLift(int i) {
        return lift[i];
    }
 }
    
