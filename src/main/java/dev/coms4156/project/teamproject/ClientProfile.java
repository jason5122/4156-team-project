package dev.coms4156.project.teamproject;

import java.io.Serializable;

/**
 * Represents a Client Profile (clients of the service).
 */
public class ClientProfile implements Serializable {

  public int clientId;

  /**
   * Constructs a new Client object with the given parameters.
   *
   */
  public ClientProfile(int clientId) {
    this.clientId = clientId;
  }

  public int getClientId() {
    return this.clientId;
  }
}
