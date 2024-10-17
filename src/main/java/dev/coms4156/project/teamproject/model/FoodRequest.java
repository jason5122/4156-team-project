package dev.coms4156.project.teamproject.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Represents a Food Request made by an account (user) 
 * of a client (app) for a specific food listing.
 */
@Entity
public class FoodRequest implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "request_id", unique = true)
  private int requestId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)  
  @JoinColumn(name = "client_id", nullable = false)  
  private ClientProfile client;

  @Serial
  private static final long serialVersionUID = 345678L;
  private final int listingId;
  private final String accountId;
  private int quantityRequested;
  private final LocalDateTime requestTime;
  private LocalDateTime pickupTime;

  /**
   * Constructs a new FoodRequest object.
   *
   * @param listingId ID of the food listing being requested
   * @param accountId ID of the account (user) making the request
   * @param client client for whom this account is being created
   * @param quantityRequested Quantity of the food requested
   */
  public FoodRequest(int listingId, String accountId, ClientProfile client, int quantityRequested) {
    this.listingId = listingId;
    this.accountId = accountId;
    this.client = client;
    this.quantityRequested = quantityRequested;
    this.requestTime = LocalDateTime.now();  // Automatically set to current time
  }

  /**
   * Generates a unique request ID based on the client ID, account ID, and current timestamp.
   *
   * @param clientId The ID of the client (app) making the API call.
   * @param accountId The ID of the account (user) making the request.
   * @return A unique request ID.
   */
  public static String genRequestId(String clientId, String accountId) {
    LocalDateTime current = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    String timestamp = current.format(formatter);
    return clientId + "_" + accountId + "_" + timestamp;
  }

  public int getRequestId() {
    return requestId;
  }

  public int getListingId() {
    return listingId;
  }

  public String getAccountId() {
    return accountId;
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

  public LocalDateTime getPickupTime() {
    return pickupTime;
  }

  public void setPickupTime(LocalDateTime pickupTime) {
    this.pickupTime = pickupTime;
  }

  @Override
  public String toString() {
    return "FoodRequest{" 
           + "requestId='" + requestId + '\'' 
           + ", listingId=" + listingId 
           + ", accountId='" + accountId + '\'' 
           + ", quantityRequested=" + quantityRequested
           + ", requestTime=" + requestTime 
           + ", pickupTime=" + pickupTime 
           + '}';
  }
}
