package pods.cabs;

public class Ride extends AbstractBehavior<Ride.Command>
{
    private int cabId;
   private int numRides;
    private String state;

    private int rideId;
    private int location;
    private int sourceLoc;
    private int destinationLoc;
    private int custId = 0;


    public interface Command{}


      /*
     * COMMAND DEFINITIONS
     */

     public static final class CabSignsIn implements Command{
         public CabSignsIn(String cabId, int initialPos)
         {

         }
     }

     public static final class CabSignsOut implements Command{
         public CabSignsOut(String cabId){
             
         }
     }

     public static final class RequestRide implements Command{
        ActorRef<RideService.RideResponse> replyTo;
        public RequestRide(String custId, int sourceLoc, int destinationLoc, ActorRef<RideService.RideResponse> replyTo){
            
        }
    }

    public static final class RideEnded implements Command{
        public RideEnded(int cabId , int rideId)
        {

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

         public  RideResponse(int rideId, String cabId, int fare, ActorRef<FulfillRide.Command> fRide)
         {

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

    private Ride(ActorContext<Command> context, int id) {
        super(context);
        this.cabId = id;
        this.numRides = 0;
        this.state = CabState.SIGNED_OUT;
        this.rideId = -1;
        this.location = 0;
        this.sourceLoc = -1;
        this.destinationLoc = -1;
        this.custId=-1;
    }


     /*
     * MESSAGE HANDLING
     */

    @Override
    public Receive<Command> createReceive() {
        ReceiveBuilder<Command> builder = newReceiveBuilder();

        builder.onMessage(RequestRide.class,   this::onRequestRide);
        builder.onMessage(RideEnded.class,     this::onRideEnded);
        builder.onMessage(CabSignsIn.class,        this::onCabSignsIn);
        builder.onMessage(CabSignsOut.class,       this::onCabSignsOut);
        builder.onMessage(RideResponse.class,       this::onRideResponse);
        builder.onMessage(Reset.class,         this::onReset);

        return builder.build();
    }


    private Behavior<Command> onRequestRide(RequestRide message)
    {

        return this;
    }

    private Behavior<Command> onRideEnded(RideEnded message)
    {
        
        return this;
    }


    private Behavior<Command> onCabSignsIn(CabSignsIn message)
    {
  
        return this;
    }


    private Behavior<Command> onCabSignsOut(CabSignsOut message)
    {
  
        return this;
    }


    private Behavior<Command> onRideResponse(RideResponse message)
    {

        return this;
    }

    private Behavior<Command> onReset(Reset message)
    {
        return this;
    }


  
   
}