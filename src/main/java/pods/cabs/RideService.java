package pods.cabs;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import java.util.HashMap;

public class RideService extends AbstractBehavior<RideService.Command> {
    // CabData HashMap
    private HashMap<String, CabData> cabDataMap;
    int fulfillSpawnCount = 0;

    public interface Command {
    }

    /*
     * COMMAND DEFINITIONS
     */

    public static final class CabSignsIn implements Command {
        final String cabID;
        final int initialPos;

        public CabSignsIn(String cabId, int initialPos) {

            this.cabID = cabId;
            this.initialPos = initialPos;
        }
    }

    public static final class CabSignsOut implements Command {
        final String cabId;

        public CabSignsOut(String cabId) {
            this.cabId = cabId;
        }
    }

    public static final class RequestRide implements Command {
        final int custId;
        final int sourceLoc;
        final int destinationLoc;
        final ActorRef<RideService.RideResponse> replyTo;

        public RequestRide(int custId, int sourceLoc, int destinationLoc, ActorRef<RideService.RideResponse> replyTo) {
            this.custId = custId;
            this.sourceLoc = sourceLoc;
            this.destinationLoc = destinationLoc;
            this.replyTo = replyTo;
        }
    }

    public static final class RideEnded implements Command {
        final String cabId;

        public RideEnded(String cabId) {
            this.cabId = cabId;
        }
    }

    public static final class Reset implements Command {

    }

    /*
     * RESPONSE
     */

    public static final class RideResponse implements Command {
        final int rideId;
        final String cabId;
        final int fare;
        final ActorRef<FulfillRide.Command> fRide;

        public RideResponse(int rideId, String cabId, int fare, ActorRef<FulfillRide.Command> fRide) {

            this.rideId = rideId;
            this.cabId = cabId;
            this.fare = fare;
            this.fRide = fRide;
        }
    }

    /*
     * INITIALIZATION
     */
    public static Behavior<Command> create(HashMap<String, CabData> cabDataMap) {
        return Behaviors.setup(context -> {
            return new RideService(context, cabDataMap);
        });
    }

    private RideService(ActorContext<Command> context, HashMap<String, CabData> CabDataMap) {
        super(context);
        this.cabDataMap = CabDataMap;
        this.fulfillSpawnCount = 0;
    }

    /*
     * MESSAGE HANDLING
     */
    @Override
    public Receive<Command> createReceive() {
        ReceiveBuilder<Command> builder = newReceiveBuilder();

        builder.onMessage(RequestRide.class, this::onRequestRide);
        builder.onMessage(CabSignsIn.class, this::onCabSignsIn);
        builder.onMessage(CabSignsOut.class, this::onCabSignsOut);
        builder.onMessage(RideResponse.class, this::onRideResponse);
        builder.onMessage(RideEnded.class, this::onRideEnded);
        builder.onMessage(Reset.class, this::onReset);

        return builder.build();
    }

    private Behavior<Command> onRequestRide(RequestRide message) {
        fulfillSpawnCount++;
        String name = this.toString() + "-ff" + fulfillSpawnCount;
        ActorRef<FulfillRide.Command> fulfillActor = getContext().spawn(FulfillRide.create(cabDataMap), name);

        fulfillActor.tell(new FulfillRide.FulfillRideRequest(message.custId, message.sourceLoc, message.destinationLoc,
                getContext().getSelf()));

        return this;
    }

    private Behavior<Command> onCabSignsIn(CabSignsIn message) {

        cabDataMap.get(message.cabID).state = CabState.AVAILABLE;

        return this;
    }

    private Behavior<Command> onCabSignsOut(CabSignsOut message) {
        cabDataMap.get(message.cabId).state = CabState.SIGNED_OUT;
        return this;
    }

    private Behavior<Command> onRideResponse(RideResponse message) {
        cabDataMap.get(message.cabId).rideId = message.rideId;
        return this;
    }

    private Behavior<Command> onRideEnded(RideEnded message) {
        cabDataMap.get(message.cabId).state = CabState.AVAILABLE;
        cabDataMap.get(message.cabId).rideId = -1;
        return this;
    }

    private Behavior<Command> onReset(Reset message) {

        for (String i : cabDataMap.keySet()) {
            cabDataMap.get(i).numRides = 0;
            cabDataMap.get(i).state = CabState.SIGNED_OUT;
            cabDataMap.get(i).rideId = -1;
            cabDataMap.get(i).location = 0;
            cabDataMap.get(i).sourceLoc = -1;
            cabDataMap.get(i).destinationLoc = -1;
        }

        return this;
    }

}