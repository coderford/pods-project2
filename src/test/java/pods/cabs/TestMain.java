package pods.cabs;


import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import org.junit.ClassRule;
import org.junit.Test;

public class TestMain {


    @ClassRule
    public static final TestKitJunitResource testKit = new TestKitJunitResource();
//#definition

    //#test
    @Test
    public void testMainActor() {
        TestProbe<Main.create> testProbe = testKit.createTestProbe();
        ActorRef<Main.Started> underTest = testKit.spawn(Main.create(), "Main");
        underTest.tell(new Main.Started("Main.started", testProbe.getRef()));
        testProbe.expectMessage(new Main.create("Main.started", underTest));
    
    //#test

    for(Cab i:Globals.cabs)
    {
        ActorRef<Cab.Command> cab = Globals.cabs.get(i);
        cab.tell(new Cab.Reset());
    }
    for(Wallet i:Globals.cabs)
    {
        ActorRef<Wallet.Command> wallet = Globals.cabs.get(i);

        wallet.tell(new Wallet.Reset());
    }

    ActorRef<Cab.Command> cab101 = Globals.cabs.get(“101”);
	cab101.tell(new Cab.SignIn(10));
	ActorRef<RideService.Command> rideService = Globals.rideService[0];
	TestProbe<RideService.RideResponse> probe = testKit.createTestProbe();
	rideService.tell(new RideService.RequestRide(“201”, 10, 100, probe.ref()));
	RideService.RideResponse resp = probe.receiveMessage();
	assertEquals(resp.rideId != -1);
	cab101.tell(new Cab.RideEnded(resp.rideId));
    
    }
    
}
