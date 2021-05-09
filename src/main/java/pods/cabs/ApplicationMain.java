package pods.cabs;

import akka.actor.typed.ActorSystem;

import java.io.IOException;
public class ApplicationMain {
  public static void main(String[] args) {
    final ActorSystem<Void> actorSystem = ActorSystem.create(Main.create(), "cabs");

    try {
      System.out.println(">>> Press ENTER to exit <<<");
      System.in.read();
    } catch (IOException ignored) {
    } finally {
      actorSystem.terminate();
    }
  }
}
