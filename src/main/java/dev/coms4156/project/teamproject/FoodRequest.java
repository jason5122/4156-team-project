package dev.coms4156.project.teamproject;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a Food Request made by an account (user) 
 * of a client (app) for a specific food listing.
 */
public class FoodRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = 345678L;
  private final String requestId;
  private final int listingId;
  private final String accountId;
  private final String clientId;
  private int quantityRequested;
  private final LocalDateTime requestTime;
  private LocalDateTime pickupTime;

  /**
   * Constructs a new FoodRequest object.
   *
   * @param listingId ID of the food listing being requested
   * @param accountId ID of the account (user) making the request
   * @param clientId ID of the client (app) making the API call
   * @param quantityRequested Quantity of the food requested
   */
  public FoodRequest(int listingId, String accountId, String clientId, int quantityRequested) {
    this.listingId = listingId;
    this.accountId = accountId;
    this.clientId = clientId;
    this.quantityRequested = quantityRequested;
    this.requestTime = LocalDateTime.now();  // Automatically set to current time
    this.requestId = genRequestId(clientId, accountId);
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

  public String getRequestId() {
    return requestId;
  }

  public int getListingId() {
    return listingId;
  }

  public String getAccountId() {
    return accountId;
  }

  public String getClientId() {
    return clientId;
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
           + ", clientId='" + clientId + '\''
           + ", quantityRequested=" + quantityRequested
           + ", requestTime=" + requestTime 
           + ", pickupTime=" + pickupTime 
           + '}';
  }
}