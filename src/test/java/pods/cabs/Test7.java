package pods.cabs;

import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;

import static org.junit.Assert.assertEquals;

import org.junit.ClassRule;
import org.junit.Test;
import java.util.Random;

//This test checks for consistency of wallet balance
public class Test7 {
  @ClassRule
  public static final TestKitJunitResource testKit = new TestKitJunitResource();
  public static final int arr[]=new int[4];

  @Test
  public void test() {
    TestProbe<Main.Started> startedProbe = testKit.createTestProbe();
    ActorRef<Void> underTest = testKit.spawn(Main.create(startedProbe.getRef()), "Main");

    startedProbe.expectMessageClass(Main.Started.class);

    System.out.println("-- RECEIVED STARTED");

    for(int i=0;i<4;i++)arr[i]=0;


        ActorRef<Cab.Command> cab = Globals.cabs.get("101");
        cab.tell(new Cab.SignIn(10));
        System.out.println("CAB 101 SIGNED IN");

        cab = Globals.cabs.get("102");
        cab.tell(new Cab.SignIn(10));
        System.out.println("CAB 102 SIGNED IN");

        cab = Globals.cabs.get("103");
        cab.tell(new Cab.SignIn(10));
        System.out.println("CAB 103 SIGNED IN");


         Random rand=new Random();


        
        TestProbe<RideService.RideResponse> probe1 = testKit.createTestProbe();
        TestProbe<RideService.RideResponse> probe2 = testKit.createTestProbe();
        TestProbe<RideService.RideResponse> probe3 = testKit.createTestProbe();


        Demo R1 = new  Demo(probe1, "201",arr);
    R1.start();

    Demo R2 = new  Demo(probe2, "202",arr);
    R2.start();

    Demo R3 = new Demo(probe3, "203",arr);
    R3.start();


    int count=0;
    for(int i=0;i<4;i++)
    {
        if(arr[i]==1)count++;
    }

    if(count==3)System.out.println("TEST 7 PASSED");
    
  }
}

class Demo extends Thread {
  private Thread t;
    private int arr[]=new int[4];
  private String threadid;
  private TestProbe<RideService.RideResponse> threadprobe;

  Demo(TestProbe<RideService.RideResponse> probe, String id,int [] array) {
    threadprobe = probe;
    threadid = id;
    arr=array;
    // System.out.println("Creating " + threadName );
  }

  public void run() {

      Random rand=new Random();
        ActorRef<RideService.Command> rideService = Globals.rideService.get(rand.nextInt(10));

        rideService.tell(new RideService.RequestRide(threadid, 10, 100, threadprobe.getRef()));
        RideService.RideResponse resp = threadprobe.receiveMessage();
        assert(resp.rideId != -1);
        arr[Integer.parseInt(resp.cabId)%101]=1;
        System.out.println("RIDE FOR CUSTOMER "+threadid+" STARTED WITH CAB "+resp.cabId);
  }

  public void start() {
    // System.out.println("Starting " + threadName );
    if (t == null) {
      t = new Thread(this, threadid);
      t.start();
    }
  }
}
