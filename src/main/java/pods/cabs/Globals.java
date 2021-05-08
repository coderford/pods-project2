package pods.cabs;

import java.util.HashMap;

import akka.actor.typed.ActorRef;

public class Globals {
    public static HashMap<String, ActorRef<Cab.Command>> cabs;
    public static HashMap<Integer, ActorRef<Wallet.Command>> wallets;

    public static int nextRideId = 0;

    synchronized public static int getNextRideId() {
        nextRideId++;
        return nextRideId;
    }
}
