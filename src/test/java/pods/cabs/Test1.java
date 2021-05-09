package pods.cabs;

import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;

import org.junit.ClassRule;
import org.junit.Test;

public class Test1 {
    @ClassRule
    public static final TestKitJunitResource testKit = new TestKitJunitResource();

    @Test
    public void test1() {
        TestProbe<Main.Started> startedProbe = testKit.createTestProbe();
        ActorRef<Void> underTest = testKit.spawn(Main.create(startedProbe.getRef()), "Main");

        startedProbe.expectMessageClass(Main.Started.class);

        System.out.println("-- RECEIVED STARTED");
    
        TestProbe<Cab.NumRidesResponse> cabResetProbe = testKit.createTestProbe();
        Globals.cabs.values().forEach(
            cab -> {
                cab.tell(new Cab.Reset(cabResetProbe.getRef()));
                cabResetProbe.expectMessageClass(Cab.NumRidesResponse.class);
            }
        );

        System.out.println("-- CABS RESET SUCCESSFUL");

        TestProbe<Wallet.ResponseBalance> walletTestProbe = testKit.createTestProbe();
        Globals.wallets.values().forEach(
            wallet -> {
                wallet.tell(new Wallet.Reset(walletTestProbe.getRef()));
                walletTestProbe.expectMessageClass(Wallet.ResponseBalance.class);
            }
        );

        System.out.println("-- WALLETS RESET SUCCESSFUL");

        ActorRef<Cab.Command> cab101 = Globals.cabs.get("101");
        cab101.tell(new Cab.SignIn(10));

        ActorRef<RideService.Command> rideService = Globals.rideService.get(0);
        TestProbe<RideService.RideResponse> probe = testKit.createTestProbe();
        rideService.tell(new RideService.RequestRide(201, 10, 100, probe.getRef()));

        RideService.RideResponse resp = probe.receiveMessage();
        assert(resp.rideId != -1);
        System.out.println("Ride for cab started");
        cab101.tell(new Cab.RideEnded(resp.rideId));
        
        System.out.println("---- TEST 1 SUCCESSFUL");


       
    }
}