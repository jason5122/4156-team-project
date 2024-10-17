package dev.coms4156.project.teamproject.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Represents a Food Request made by an account (user) 
 * of a client (app) for a specific food listing.
 */
@Entity
public class FoodRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = 345678L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "request_id", unique = true)
  private int requestId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)  
  @JoinColumn(name = "client_id", nullable = false)  
  private ClientProfile client;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "account_id", nullable = false)
  private AccountProfile account;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "listing_id", nullable = false)
  private FoodListing foodListing;

  private int quantityRequested;
  private LocalDateTime requestTime;

  public FoodRequest() {}
  
  /**
   * Constructs a new FoodRequest object.
   *
   * @param client client for whom this account is being created
   * @param account the account (user) making the request
   * @param foodListing the food listing being requested
   * @param quantityRequested Quantity of the food requested
   */
  public FoodRequest(ClientProfile client, AccountProfile account, FoodListing foodListing, int quantityRequested) {
    this.client = client;
    this.account = account;
    this.foodListing = foodListing;
    this.quantityRequested = quantityRequested;
    this.requestTime = LocalDateTime.now();  // Automatically set to current time
  }

  public int getRequestId() {
    return requestId;
  }

  public FoodListing getListing() {
    return foodListing;
  }

  public AccountProfile getAccountId() {
    return account;
  }

  public ClientProfile getClient() {
    return client;
  }

  public int getQuantityRequested() {
    return quantityRequested;
  }

  public void setQuantityRequested(int quantityRequested) {
    this.quantityRequested = quantityRequested;
  }

  public LocalDateTime getRequestTime() {
    return requestTime;
  }
}
