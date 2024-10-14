package dev.coms4156.project.teamproject;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * Represents a Food Listing.
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "food_listing")
public class FoodListing implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "listing_id", unique = true)
  private int listingId;

  private static final long serialVersionUID = 123456L;

  @Column(name = "account_id", nullable = false)
  private String accountId;

  @Column(name = "food_type")
  private String foodType;

  @Column(name = "quantity")
  private int quantityListed;

  @Column(name = "earliest_pickup")
  private LocalDateTime earliestPickUpTime;

  @Column(name = "latitude")
  private float latitude;
  
  @Column(name = "longitude")
  private float longitude;

  // No-argument constructor required by JPA
  public FoodListing() {}

  /**
   * Constructs a food listing object with the given parameters.
   *
   * @param accountId Account id of the provider who listed the food
   * @param foodType Type of food
   * @param quantityListed Quantity of food in bags
   * @param earliestPickUpTime Earliest pick up time for the food listing
   * @param latitude latitude of the pick up location
   * @param longitude longitude of the pick up location
   */
  public FoodListing(String accountId, String foodType, int quantityListed, 
        LocalDateTime earliestPickUpTime, float latitude, float longitude) {
    this.accountId = accountId;
    this.foodType = foodType;
    this.quantityListed = quantityListed;
    this.earliestPickUpTime = earliestPickUpTime;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public String getAccountId() {
    return this.accountId;
  }

  public String getFoodType() {
    return this.foodType;
  }

  public int getQuantityListed() {
    return this.quantityListed;
  }

  public LocalDateTime getEarliestPickUpTime() {
    return this.earliestPickUpTime;
  }

  public float getLatitude() {
    return this.latitude;
  }

  public float getLongitude() {
    return this.longitude;
  }

  public int getListingId() {
    return this.listingId;
  }

  public void setQuantityListed(int quantityListed) {
    this.quantityListed = quantityListed;
  }

  public void setEarliestPickUpTime(LocalDateTime earliestPickUpTime) {
    this.earliestPickUpTime = earliestPickUpTime;
  }

  public void setFoodType(String foodType) {
    this.foodType = foodType;
  }

  public void setLatitude(float latitude) {
    this.latitude = latitude;
  }

  public void setLongitude(float longitude) {
    this.longitude = longitude;
  }

}
