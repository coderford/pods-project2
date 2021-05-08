package pods.cabs;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class FulfillRide extends AbstractBehavior<FulfillRide.Command> {
    final HashMap<Integer, CabData> cabDataMap;
    final HashMap<Integer, WalletData> walletDataMap;

    public interface Command {}
    public interface Response {}

    /*
     * COMMAND DEFINITIONS
     */
    public static final class FulfillRideRequest implements Command {
        final int custId;
        final int sourceLoc;
        final int destinationLoc;
        final ActorRef<RideService.Command> replyTo;

        public FulfillRideRequest(int custId, int sourceLoc, int destinationLoc,
                           ActorRef<RideService.Command> replyTo) {
            this.custId = custId;
            this.sourceLoc = sourceLoc;
            this.destinationLoc = destinationLoc;
            this.replyTo = replyTo;
        }
    }

    public static final class RequestRideResponse implements Command {
        final boolean accepted;

        public RequestRideResponse(boolean accepted) {
            this.accepted = accepted;
        }
    }

    public static final class RideStartedResponse implements Command {
        final boolean accepted;

        public RideStartedResponse(boolean accepted) {
            this.accepted = accepted;
        }
    }

    public static final class RideCancelledResponse implements Command {
        final boolean accepted;

        public RideCancelledResponse(boolean accepted) {
            this.accepted = accepted;
        }
    }

    /*
     * INITIALIZATION
     */
    public static Behavior<Command> create(
        HashMap<Integer, CabData> cabDataMap,
        HashMap<Integer, WalletData> walletDataMap
    ) {
        return Behaviors.setup(
	        context -> {
                return new FulfillRide(context, cabDataMap, walletDataMap);
	        }
        );
    }

    private FulfillRide(
        ActorContext<Command> context, 
        HashMap<Integer, CabData> cabDataMap,
        HashMap<Integer, WalletData> walletDataMap
    ) {
        super(context);
        this.cabDataMap = cabDataMap;
        this.walletDataMap = walletDataMap;
    }

    /*
     * MESSAGE HANDLING
     */
    @Override
    public Receive<Command> createReceive() {
        ReceiveBuilder<Command> builder = newReceiveBuilder();

        builder.onMessage(FulfillRideRequest.class, this::onFulfillRideRequest);

        return builder.build();
    }

    private Behavior<Command> onFulfillRideRequest(FulfillRideRequest message) {
        // Will try to find an available cab and start a ride
        
        // - first, make a list of cabs and sort them by distance from source
        class CabComparer implements Comparator<CabData>{
            int sourceLoc;
            public CabComparer(int sourceLoc) {
                this.sourceLoc = sourceLoc;
            }

            public int compare(CabData a, CabData b) {
                return Math.abs(a.location - sourceLoc) - Math.abs(b.location - sourceLoc);
            
        }

        List<CabData> cabList = cabDataMap.values()
                                          .stream()
                                          .sorted(new CabComparer(message.sourceLoc))
                                          .collect(Collectors.toList());

        // - in the sorted list, send ride request to each cab

        return this;
    }
}

