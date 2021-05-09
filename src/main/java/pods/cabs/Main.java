package pods.cabs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class Main {

    public static class Started {

    }

    public static Behavior<Void> create(ActorRef<Main.Started> testProbe) {
        return Behaviors.setup(context -> {
            /*
             * Initialize CabData HashMap
             */
            ActorRef<Cab.Command> cab;
            ActorRef<Wallet.Command> wallet;
            HashMap<String, CabData> cabDataMap = new HashMap<>();

            int initBalance = 0;
            ArrayList<String> cabIds = new ArrayList<>();
            ArrayList<Integer> walletIds = new ArrayList<>();

            try {
                File inputFile = new File("IDs.txt");
                Scanner in = new Scanner(inputFile);

                int section = 0;
                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    if (line.compareTo("****") == 0) {
                        section++;
                    } else if (section == 1) {
                        cabIds.add(line);
                    } else if (section == 2) {
                        walletIds.add(Integer.parseInt(line));
                    } else if (section == 3) {
                        initBalance = Integer.parseInt(line);
                    }
                }

                in.close();
            } catch (Exception e) {
                System.out.println("ERROR: Could not read input file!");
            }

            // Create Cab actors
            for (String id : cabIds) {
                cabDataMap.put(id, new CabData(id));

                String name = "cab-actor-" + id;
                cab = context.spawn(Cab.create(id), name);
                Globals.cabs.put(id, cab);
            }

            // Create Wallet actors
            for (int id : walletIds) {
                String name = "wallet-actor-" + Integer.toString(id);
                wallet = context.spawn(Wallet.create(id, initBalance), name);
                Globals.wallets.put(id, wallet);
            }

            // Create 10 RideService actors
            for (int i = 0; i < 1; i++) {
                String name = "ride-actor-" + Integer.toString(i);
                ActorRef<RideService.Command> tmpRide = context.spawn(RideService.create(cabDataMap), name);
                Globals.rideService.add(tmpRide);
            }

            // Send a message to testprobe
            testProbe.tell(new Started());
            return Behaviors.empty();
        });
    }
}
