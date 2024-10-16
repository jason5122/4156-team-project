package dev.coms4156.project.teamproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.coms4156.project.teamproject.repository.FoodListingRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class RouteControllerIntegrationTests {

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

    ResponseEntity<?> response = routeController.getFoodListings();
    String expected1 = "FoodListing{accountId='acc1', foodType=snack, "
        + "quantityListed=25, earliestPickUpTime=2024-10-06 11:00:00, "
        + "latitude=34.052, longitude=-118.243}";
    String expected2 = "FoodListing{accountId='acc2', foodType=beverage, "
        + "quantityListed=30, earliestPickUpTime=2024-10-07 16:30:00, "
        + "latitude=78.122, longitude=120.281}";
    Set<String> expected = Set.of(expected1, expected2);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    List<FoodListing> foodListings = (List<FoodListing>) response.getBody();
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
}
