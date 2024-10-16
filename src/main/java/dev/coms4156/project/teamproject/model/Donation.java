package dev.coms4156.project.teamproject.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

/**
 * Represents a donation.
 */
@Entity
public class Donation implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "donation_id", unique = true)
  private int donationId;
  
  @Serial
  private static final long serialVersionUID = 234567L;
  private final String accountId;
  private final int listingId;
  private int quantityPickedUp;
  private LocalDateTime pickUpTime;

  /**
   * Creates a new donation object with the given params.
   *
   * @param accountId Account id of the provider who put up the foodListing
   * @param listingId Id of the food listing referenced by the donation
   * @param pickUpTime Pick up time of the donation
   * @param quantityPickedUp Quantity of the food listing pick up
   */
  public Donation(String accountId, int listingId, int quantityPickedUp, 
        LocalDateTime pickUpTime) {
    this.accountId = accountId;
    this.listingId = listingId;
    this.quantityPickedUp = quantityPickedUp;
    this.pickUpTime = pickUpTime;
  }

  /**
   * Generates a unique donation ID to use. 
   *
   * @return String representing a unique donation from the account.
   */
  public static String genDonationId(String accountId) {
    LocalDateTime current = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    String timestamp = current.format(formatter);

    return accountId + timestamp;
  }

  public int getDonationId() {
    return donationId;
  }

  public int getListingId() {
    return listingId;
  }

  public int getQuantityPickedUp() {
    return quantityPickedUp;
  }

  public LocalDateTime getPickUpTime() {
    return pickUpTime;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setPickUpTime(LocalDateTime pickUpTime) {
    this.pickUpTime = pickUpTime;
  }

  public void setQuantityPickedUp(int quantityPickedUp) {
    this.quantityPickedUp = quantityPickedUp;
  }
}
