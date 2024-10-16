package dev.coms4156.project.teamproject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.coms4156.project.teamproject.controller.FoodListingRepository;
import dev.coms4156.project.teamproject.model.FoodListing;
import dev.coms4156.project.teamproject.view.RouteController;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class RouteControllerUnitTests {

  @Autowired
  private RouteController routeController;

  @Autowired
  private FoodListingRepository foodListingRepository;

  public void saveFoodListing1() {
    FoodListing foodListing1 = new FoodListing("acc1", "snack",
        25, LocalDateTime.of(2024, 10, 6, 11, 0),
        34.052f, -118.243f);

    foodListingRepository.save(foodListing1);
  }

  public void saveFoodListing2() {
    FoodListing foodListing2 = new FoodListing("acc2", "beverage",
        30, LocalDateTime.of(2024, 10, 7, 16, 30),
        78.122f, 120.281f);
    foodListingRepository.save(foodListing2);
  }

  public void saveFoodListing3() {
    FoodListing foodListing3 = new FoodListing("acc3", "rice",
        30, LocalDateTime.of(2024, 10, 8, 14, 00),
        33.989f, -118.243f);
    foodListingRepository.save(foodListing3);
  }

  @BeforeEach
  public void setup() {
    // Make sure repository is in a clean state
    foodListingRepository.deleteAll();
  }

  @Test
  public void createFoodListingOkTest() {
    ResponseEntity<?> response = routeController.createFoodListing(
        "testAccountId", "kiwi", 10,
        34.052f, -118.244f);

    List<FoodListing> listings = foodListingRepository.findAll();
    assertEquals(1, listings.size());
    FoodListing listing = listings.get(0);

    assert(((String) response.getBody()).contains("Food listing created successfully with ID: "));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("testAccountId", listing.getAccountId());
    assertEquals("kiwi", listing.getFoodType());
    assertEquals(34.052f, listing.getLatitude());
    assertEquals(-118.244f, listing.getLongitude());
  }

  @Test
  public void getFoodListingsOkTest() {
    // Test with non-empty repository
    saveFoodListing1();
    saveFoodListing2();

    ResponseEntity<List<FoodListing>> response = routeController.getFoodListings();
    String expected1 = "FoodListing{accountId='acc1', foodType=snack, "
        + "quantityListed=25, earliestPickUpTime=2024-10-06 11:00:00, "
        + "latitude=34.052, longitude=-118.243}";
    String expected2 = "FoodListing{accountId='acc2', foodType=beverage, "
        + "quantityListed=30, earliestPickUpTime=2024-10-07 16:30:00, "
        + "latitude=78.122, longitude=120.281}";
    Set<String> expected = Set.of(expected1, expected2);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    List<FoodListing> foodListings = response.getBody();
    assert(foodListings.size() == 2);
    for (FoodListing listing: foodListings) {
      assert(expected.contains(listing.toString()));
    }
  }

  @Test
  public void getFoodListingNotFoundTest() {
    // Test with empty repository
    ResponseEntity<?> response = routeController.getFoodListings();
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void getNearbyListingsZeroFoundTest() {
    saveFoodListing1();
    saveFoodListing2();

    ResponseEntity<?> response = routeController.getNearbyListings(
        30.000f, -100.000f, 10);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void getNearbyListingsOneFoundTest() {
    saveFoodListing1();
    saveFoodListing2();

    ResponseEntity<List<FoodListing>> response = routeController.getNearbyListings(
        34.060f, -118.250f, 10);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    List<FoodListing> listingsFound = response.getBody();
    assert(listingsFound.size() == 1);
    assertEquals("acc1", listingsFound.get(0).getAccountId());

    ResponseEntity<List<FoodListing>> response2 = routeController.getNearbyListings(
        78.121f, 120.282f, 10);
    assertEquals(HttpStatus.OK, response2.getStatusCode());
    List<FoodListing> listingsFound2 = response2.getBody();
    assert(listingsFound2.size() == 1);
    assertEquals("acc2", listingsFound2.get(0).getAccountId());
  }

  @Test
  public void getNearbyListingsTwoFoundTest() {
    saveFoodListing1();
    saveFoodListing3();

    ResponseEntity<List<FoodListing>> response = routeController.getNearbyListings(
        34.021f, -118.243f, 10);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    List<FoodListing> listingsFound = response.getBody();
    assertEquals(2, listingsFound.size());
    Set<String> expected = Set.of("acc1", "acc3");
    for (FoodListing listing: listingsFound) {
      assert(expected.contains(listing.getAccountId()));
    }
  }
}
