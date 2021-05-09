package pods.cabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import akka.actor.typed.ActorRef;

public class Globals {
    public static HashMap<String, ActorRef<Cab.Command>> cabs = new HashMap<>();
    public static HashMap<Integer, ActorRef<Wallet.Command>> wallets = new HashMap<>();
    public static List<ActorRef<RideService.Command>> rideServices = new ArrayList<>();

    public static int nextRideId = 0;

    synchronized public static int getNextRideId() {
        nextRideId++;
        return nextRideId;
    }
}
