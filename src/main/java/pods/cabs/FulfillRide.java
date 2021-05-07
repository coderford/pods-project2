package pods.cabs;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class FulfillRide extends AbstractBehavior<FulfillRide.Command> {
    public interface Command {}
    public interface Response {}

    /*
     * COMMAND DEFINITIONS
     */
    public static final class RequestRide implements Command {
        // final int cabId;
        final int rideId;
        final int sourceLoc;
        final int destinationLoc;
        final ActorRef<FulfillRide.Command> replyTo;

        public RequestRide(int rideId, int sourceLoc, int destinationLoc,
                           ActorRef<FulfillRide.Command> replyTo) {
            this.rideId = rideId;
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
    public static Behavior<Command> create() {
        return Behaviors.setup(
	        context -> {
                return new FulfillRide(context);
	        }
        );
    }

    private FulfillRide(ActorContext<Command> context) {
        super(context);
    }

    /*
     * MESSAGE HANDLING
     */
    @Override
    public Receive<Command> createReceive() {
        ReceiveBuilder<Command> builder = newReceiveBuilder();

        builder.onMessage(RequestRide.class,   this::onRequestRide);

        return builder.build();
    }

    private Behavior<Command> onRequestRide(RequestRide message) {
        return this;
    }
}

