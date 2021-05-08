package pods.cabs;

import java.util.ArrayList;
import java.util.HashMap;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class Main {


    private final ActorRef<Cab.Command> cab;
    private final ActorRef<Wallet.Command> wallet;
    HashMap<String,CabData> CabDataMap = new HashMap<>();

    public static Behavior<Void> create() {
         return Behaviors.setup(
	        context -> {
                // Initialize other actors here


                 /*
    *   Initialize CabData HashMap
    */
      
    ArrayList<String> cabIds=new ArrayList<>();
    ArrayList<String> walletIds=new ArrayList<>();
    

    try {
        File inputFile = new File("IDs.txt");
        Scanner in = new Scanner(inputFile);

        int section = 0;
        while(in.hasNextLine()) {
            String line = in.nextLine();
            if(line.compareTo("****") == 0) {
                section++;
            }
            else if(section == 1) {
                cabIds.add(line);
            }
            else if(section== 2)
            {
                walletIds.add(line);
            }
        }

        in.close();
    }
    catch(Exception e) {
        System.out.println("ERROR: Could not read input file!");
    }

    for(String id : cabIds) {
        CabDataMap.put(id,new CabData(id));

     //#create cab-actors
     cab= context.spawn(Cab.create(id));
     //#create-actors

     Globals.cabs.put(id,cab.self);
    }

    for(String id:walletIds)
    {
        //create wallet-actors
        wallet=context.spawn(Wallet.create(id));
        Globals.wallets.put(id,wallet.self);
    }


	            return Behaviors.empty();
	        }
         );
    }



   

    


}
