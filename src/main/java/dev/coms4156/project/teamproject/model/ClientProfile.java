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
   * Default constructor for the FoodListing class.
   * This constructor is required by JPA for object instantiation.
   * We suppress the warning since the PMD default ruleset does not comply with this.
   */
  @SuppressWarnings({"PMD.UncommentedEmptyConstructor", "PMD.UnnecessaryConstructor"})
  public ClientProfile() {
  }

  public int getClientId() {
    return this.clientId;
  }
}
