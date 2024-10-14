package dev.coms4156.project.teamproject;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a donation.
 */
public class Donation implements Serializable {
  @Serial
  private static final long serialVersionUID = 234567L;
  private final String accountId;
  private final int listingId;
  private int quantityPickedUp;
  private LocalDateTime pickUpTime;
  private final String donationId;

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
    this.donationId = genDonationId(accountId);
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

  public String getDonationId() {
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
