package pods.cabs;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import java.util.HashMap;

public class RideService extends AbstractBehavior<Ride.Command>
{
  /*  private String cabId;
   private int numRides;
    private String state;

    private int rideId;
    private int location;
    private int sourceLoc;
    private int destinationLoc;
    private int custId = 0;
*/
    /*CabData HashMap
    */
    private HashMap<String , CabData> CabDataMap;


    public interface Command{}


      /*
     * COMMAND DEFINITIONS
     */

     public static final class CabSignsIn implements Command{
         final String cabID;
         final  int initialPos;
         public CabSignsIn(String cabId, int initialPos)
         {

            this.cabID=cabId;
            this.initialPos=initialPos;
         }
     }

     public static final class CabSignsOut implements Command{
        final  String cabId;
         public CabSignsOut(String cabId){
             this.cabId=cabId;
         }
     }

     public static final class RequestRide implements Command{
        final ActorRef<RideService.RideResponse> replyTo;
        final String custId;
        final int sourceLoc;
        final  int destinationLoc;
        public RequestRide(String custId, int sourceLoc, int destinationLoc, ActorRef<RideService.RideResponse> replyTo){
            this.custId=custId;
            this.sourceLoc=sourceLoc;
            this.destinationLoc=destinationLoc;
            this.replyTo=replyTo;
        }
    }

    

    public static final class Reset implements Command
    {
        public Reset()
        {
        


        }
    } 



    /*
     * RESPONSE 
     */


     public static final class RideResponse 
     {
        final   int rideId ;
        final   String cabId;
        final  int fare;
        final  ActorRef<FulfillRide.Command> fRide; 

         public  RideResponse(int rideId, String cabId, int fare, ActorRef<FulfillRide.Command> fRide)
         {

            this.rideId=rideId;
            this.cabId=cabId;
            this.fare=fare;
            this.fRide=fRide;
         }
     }

      /*
     * INITIALIZATION
     */

    public static Behavior<Command> create(int id) {
        return Behaviors.setup(
	        context -> {
                return new Ride(context, cabId);
	        }
        );
    }

    private RideService(ActorContext<Command> context, HashMap<String , CabData> CabDataMap) {
        super(context);
       this.CabDataMap=CabDataMap;
    }


     /*
     * MESSAGE HANDLING
     */

    @Override
    public Receive<Command> createReceive() {
        ReceiveBuilder<Command> builder = newReceiveBuilder();

        builder.onMessage(RequestRide.class,   this::onRequestRide);
        builder.onMessage(CabSignsIn.class,        this::onCabSignsIn);
        builder.onMessage(CabSignsOut.class,       this::onCabSignsOut);
        builder.onMessage(RideResponse.class,       this::onRideResponse);
        builder.onMessage(Reset.class,         this::onReset);

        return builder.build();
    }


    private Behavior<Command> onRequestRide(RequestRide message)
    {

        message.replyTo.tell(new FulfillRide.RequestRide(this.custId,this.sourceLoc, this.destinationLoc,context.self));

        return this; 
    }


    private Behavior<Command> onCabSignsIn(CabSignsIn message)
    {

        CabDataMap.get(message.cabID).state=CabState.AVAILABLE;

        return this;
    }


    private Behavior<Command> onCabSignsOut(CabSignsOut message)
    {
  
        CabDataMap.get(message.cabID).state=CabState.SIGNED_OUT;
        return this;
    }


    private Behavior<Command> onRideResponse(RideResponse message)
    {

        CabDataMap.get(this.cabId).rideId=message.rideId;
        CabDataMap.get(this.cabId).custId=this.custId;
        CabDataMap.get(this.cabId).sourceLoc=this.sourceLoc;
        CabDataMap.get(this.cabId).destinationLoc=this.destinationLoc;
        return this;
    }

    private Behavior<Command> onReset(Reset message)
    {

        for(String i:CabDataMap.keySet())
        {

           CabDataMap.get(i).numRides = 0;
           CabDataMap.get(i).state = CabState.SIGNED_OUT;
           CabDataMap.get(i).rideId = -1;
           CabDataMap.get(i).location = 0;
           CabDataMap.get(i).sourceLoc = -1;
           CabDataMap.get(i).destinationLoc = -1;

        }

        
        return this;
    }


  
   
}