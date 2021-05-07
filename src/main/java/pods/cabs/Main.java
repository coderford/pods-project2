package pods.cabs;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class Main {

    public static Behavior<Void> create() {
         return Behaviors.setup(
	        context -> {
                // Initialize other actors here

	            return Behaviors.empty();
	        }
         );
    }

}
