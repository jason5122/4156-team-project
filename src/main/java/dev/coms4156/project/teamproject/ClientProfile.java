package dev.coms4156.project.teamproject;

import java.io.Serializable;
import java.util.Random;

/**
 * Represents a Client Profile (clients of the service)
 */
public class ClientProfile implements Serializable {

  private int clientId;
  /**
   * Constructs a new Client object with the given parameters.
   *
   */
  public ClientProfile() {
    Random rand = new Random();
    this.clientId = rand.nextInt(1000);
  }

  public int getClientId() {
    return this.clientId;
  }
}
