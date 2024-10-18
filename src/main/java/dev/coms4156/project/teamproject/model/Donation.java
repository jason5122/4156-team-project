package dev.coms4156.project.teamproject.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Represents a donation.
 */
@Entity
public class Donation implements Serializable {

  @Serial
  private static final long serialVersionUID = 234567L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "donation_id", unique = true)
  private int donationId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)  
  @JoinColumn(name = "client_id", nullable = false)  
  private ClientProfile client;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "account_id", nullable = false)
  private AccountProfile account;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "listing_id", nullable = false)
  private FoodListing foodListing;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "request_id", nullable = false)
  private FoodRequest foodRequest;

  private int quantityPickedUp;
  private LocalDateTime pickUpTime;

  /**
   * Creates a new donation object with the given params.
   *
   * @param account the account of the provider who put up the foodListing
   * @param client client for whom this account is being created
   * @param foodListing the food listing referenced by the donation
   * @param pickUpTime Pick up time of the donation
   * @param quantityPickedUp Quantity of the food listing pick up
   */
  public Donation(ClientProfile client, AccountProfile account, FoodListing foodListing, FoodRequest foodRequest, int quantityPickedUp, 
        LocalDateTime pickUpTime) {
    this.client = client;
    this.account = account;
    this.foodListing = foodListing;
    this.foodRequest = foodRequest;
    this.quantityPickedUp = quantityPickedUp;
    this.pickUpTime = pickUpTime;
  }

  public int getDonationId() {
    return donationId;
  }

  public FoodListing getFoodListing() {
    return foodListing;
  }

  public int getQuantityPickedUp() {
    return quantityPickedUp;
  }

  public LocalDateTime getPickUpTime() {
    return pickUpTime;
  }

  public void setPickUpTime(LocalDateTime pickUpTime) {
    this.pickUpTime = pickUpTime;
  }

  public void setQuantityPickedUp(int quantityPickedUp) {
    this.quantityPickedUp = quantityPickedUp;
  }
}
