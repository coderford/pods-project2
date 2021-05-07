package pods.cabs;

import akka.actor.typed.ActorSystem;

import java.io.IOException;
public class ApplicationMain {
  public static void main(String[] args) {
    //#actor-system
    final ActorSystem<Void> actorSystem = ActorSystem.create(Main.create(), "cabs");
    //#actor-system

    //#main-send-messages

    //#main-send-messages

    try {
      System.out.println(">>> Press ENTER to exit <<<");
      System.in.read();
    } catch (IOException ignored) {
    } finally {
      actorSystem.terminate();
    }
  }
}
