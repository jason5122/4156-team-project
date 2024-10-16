package dev.coms4156.project.teamproject.model;

import java.io.Serializable;

import jakarta.persistence.Column;
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
  @Column(name = "client_id", updatable = false, nullable = false)
  public int clientId;

  /**
   * Constructs a new Client object.
   *
   */
  public ClientProfile() {
  }

  public int getClientId() {
    return this.clientId;
  }
}
