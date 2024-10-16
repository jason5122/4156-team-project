package dev.coms4156.project.teamproject.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Represents a Client Profile (clients of the service).
 */
@Entity
public class ClientProfile implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)

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
