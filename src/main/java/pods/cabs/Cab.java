package pods.cabs;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class Cab extends AbstractBehavior<Cab.Command> {
    public interface Command {}

    public interface Response {}

    /*
     * COMMAND DEFINITIONS
     */
    public static final class RequestRide implements Command {

    }

    public static final class RideStarted implements Command {

    }

    public static final class RideCancelled implements Command {

    }

    public static final class RideEnded implements Command {

    }

    public static final class SignIn implements Command {

    }

    public static final class SignOut implements Command {

    }

    public static final class NumRides implements Command {
        final ActorRef<Cab.NumRidesResponse> replyTo;

        public NumRides(ActorRef<Cab.NumRidesResponse> replyTo) {
            this.replyTo = replyTo;
        }
    }

    public static final class Reset implements Command {
        final ActorRef<Cab.NumRidesResponse> replyTo;

        public Reset(ActorRef<Cab.NumRidesResponse> replyTo) {
            this.replyTo = replyTo;
        }
    }

    /*
     * RESPONSE DEFINITIONS
     */
    public static final class NumRidesResponse implements Response {
        final int ridesGiven;

        public NumRidesResponse(int ridesGiven) {
            this.ridesGiven = ridesGiven;
        }
    }

    /*
     * INITIALIZATION
     */
    public static Behavior<Command> create() {
        return Behaviors.setup(Cab::new);
    }

    private Cab(ActorContext<Command> context) {
        super(context);
    }

    /*
     * MESSAGE HANDLING
     */
    @Override
    public Receive<Command> createReceive() {
        ReceiveBuilder<Command> builder = newReceiveBuilder();

        builder.onMessage(RequestRide.class,   this::onRequestRide);
        builder.onMessage(RideStarted.class,   this::onRideStarted);
        builder.onMessage(RideCancelled.class, this::onRideCancelled);
        builder.onMessage(RideEnded.class,     this::onRideEnded);
        builder.onMessage(SignIn.class,        this::onSignIn);
        builder.onMessage(SignOut.class,       this::onSignOut);
        builder.onMessage(NumRides.class,      this::onNumRides);
        builder.onMessage(Reset.class,         this::onReset);

        return builder.build();
    }

    private Behavior<Command> onRequestRide(RequestRide message) {
        return this;
    }
    private Behavior<Command> onRideStarted(RideStarted message) {
        return this;
    }
    private Behavior<Command> onRideCancelled(RideCancelled message) {
        return this;
    }
    private Behavior<Command> onRideEnded(RideEnded message) {
        return this;
    }
    private Behavior<Command> onSignIn(SignIn message) {
        return this;
    }
    private Behavior<Command> onSignOut(SignOut message) {
        return this;
    }
    private Behavior<Command> onNumRides(NumRides message) {
        return this;
    }
    private Behavior<Command> onReset(Reset message) {
        return this;
    }
}
