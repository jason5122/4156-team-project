package dev.coms4156.project.teamproject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.coms4156.project.teamproject.controller.ClientProfileController;
import dev.coms4156.project.teamproject.controller.FoodListingController;
import dev.coms4156.project.teamproject.model.ClientProfile;
import dev.coms4156.project.teamproject.model.FoodListing;
import dev.coms4156.project.teamproject.repository.FoodListingRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@DataJpaTest
@Import({FoodListingController.class, ClientProfileController.class})
public class FoodListingControllerTests {

  @Autowired
  private FoodListingRepository foodListingRepository;

  @Autowired
  private FoodListingController foodListingController;

  @Autowired
  private ClientProfileController clientProfileController;

  public void saveFoodListing1(ClientProfile client) {
    FoodListing foodListing1 = new FoodListing("acc1", client, "snack",
        25, LocalDateTime.of(2024, 10, 6, 11, 0),
        34.052f, -118.243f);

    foodListingRepository.save(foodListing1);
  }

  public void saveFoodListing2(ClientProfile client) {
    FoodListing foodListing2 = new FoodListing("acc2", client, "beverage",
        30, LocalDateTime.of(2024, 10, 7, 16, 30),
        78.122f, 120.281f);
    foodListingRepository.save(foodListing2);
  }

  public void saveFoodListing3(ClientProfile client) {
    FoodListing foodListing3 = new FoodListing("acc3", client, "rice",
        140, LocalDateTime.of(2024, 10, 8, 14, 00),
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
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();

    ResponseEntity<?> response = foodListingController.createFoodListing(
        "testAccountId", String.valueOf(clientProfile.getBody().getClientId()),
        "kiwi", 10,
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
  public void getFoodListingNoneFoundTest() {
    // Test with empty repository
    ResponseEntity<?> response = foodListingController.getFoodListings("1");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void getFoodListingsOneClientTest() {
    // Test with non-empty repository
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    saveFoodListing1(client);
    saveFoodListing2(client);
    String clientId = String.valueOf(client.getClientId());

    ResponseEntity<?> response = foodListingController.getFoodListings(clientId);
    String expected1 = "FoodListing{accountId='acc1', foodType=snack, "
        + "quantityListed=25, earliestPickUpTime=2024-10-06 11:00:00, "
        + "latitude=34.052, longitude=-118.243}";
    String expected2 = "FoodListing{accountId='acc2', foodType=beverage, "
        + "quantityListed=30, earliestPickUpTime=2024-10-07 16:30:00, "
        + "latitude=78.122, longitude=120.281}";
    Set<String> expected = Set.of(expected1, expected2);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    List<FoodListing> foodListings = (List<FoodListing>) response.getBody();
    assert(Objects.requireNonNull(foodListings).size() == 2);
    for (FoodListing listing: foodListings) {
      assert(expected.contains(listing.toString()));
    }
  }

  @Test
  public void getFoodListingMultipleClientsTest() {
    // Listing 1 and 2 have the same client
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    saveFoodListing1(client);
    saveFoodListing2(client);
    assert client != null;
    String clientId = String.valueOf(client.getClientId());

    // Listing 3 has a different client
    ResponseEntity<ClientProfile> clientProfile2 = clientProfileController.createClientProfile();
    ClientProfile client2 = clientProfile2.getBody();
    saveFoodListing3(client2);
    assert client2 != null;
    String client2Id = String.valueOf(client2.getClientId());

    // Test that we have the correct listings for the first client
    ResponseEntity<?> response = foodListingController.getFoodListings(clientId);
    String expected1 = "FoodListing{accountId='acc1', foodType=snack, "
        + "quantityListed=25, earliestPickUpTime=2024-10-06 11:00:00, "
        + "latitude=34.052, longitude=-118.243}";
    String expected2 = "FoodListing{accountId='acc2', foodType=beverage, "
        + "quantityListed=30, earliestPickUpTime=2024-10-07 16:30:00, "
        + "latitude=78.122, longitude=120.281}";
    Set<String> expectedSet = Set.of(expected1, expected2);

    assertEquals(HttpStatus.OK, response.getStatusCode()); // Check status code

    // Check body
    @SuppressWarnings("unchecked")
    List<FoodListing> foodListings = (List<FoodListing>) response.getBody();
    assert(Objects.requireNonNull(foodListings).size() == 2);
    for (FoodListing listing: foodListings) {
      assert(expectedSet.contains(listing.toString()));
    }

    // Test that we have the correct listings for the second client
    String expected3 = "FoodListing{accountId='acc3', foodType=rice, "
        + "quantityListed=140, earliestPickUpTime=2024-10-08 14:00:00, "
        + "latitude=33.989, longitude=-118.243}";
    ResponseEntity<?> response2 = foodListingController.getFoodListings(client2Id);
    assertEquals(HttpStatus.OK, response2.getStatusCode()); // Check status code

    // Check body
    @SuppressWarnings("unchecked")
    List<FoodListing> foodListings2 = (List<FoodListing>) response2.getBody();
    assert(Objects.requireNonNull(foodListings2).size() == 1);
    Set<String> expectedSet2 = Set.of(expected3);
    for (FoodListing listing: foodListings2) {
      assert(expectedSet2.contains(listing.toString()));
    }
  }

  @Test
  public void getFoodListingsMissingClientTest() {
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    saveFoodListing1(client);
    saveFoodListing2(client);
    assert client != null;

    // No client has this ID
    String badClientId = String.valueOf(client.getClientId() + 2);

    ResponseEntity<?> response = foodListingController.getFoodListings(badClientId);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void getNearbyListingsZeroFoundTest() {
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    saveFoodListing1(client);
    saveFoodListing2(client);
    assert client != null;
    String clientId = String.valueOf(client.getClientId());

    saveFoodListing1(client);
    saveFoodListing2(client);

    ResponseEntity<?> response = foodListingController.getNearbyListings(
        clientId, 30.000f, -100.000f, 10);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void getNearbyListingsMissingClientTest() {
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    saveFoodListing1(client);
    saveFoodListing2(client);
    assert client != null;
    // No client has this ID
    String badClientId = String.valueOf(client.getClientId() + 2);

    saveFoodListing1(client);
    saveFoodListing2(client);

    // This query location is within 10 (units) of listing 1, but
    // should still be NOT_FOUND since client doesn't exist
    ResponseEntity<?> response = foodListingController.getNearbyListings(
        badClientId, 78.121f, 120.282f, 10);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void getNearbyListingsOneFoundSingleClientTest() {
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    saveFoodListing1(client);
    saveFoodListing2(client);
    assert client != null;
    String clientId = String.valueOf(client.getClientId());

    ResponseEntity<List<FoodListing>> response = foodListingController.getNearbyListings(
        clientId, 34.060f, -118.250f, 10);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    List<FoodListing> listingsFound = response.getBody();
    assert(Objects.requireNonNull(listingsFound).size() == 1);
    assertEquals("acc1", listingsFound.get(0).getAccountId());

    ResponseEntity<List<FoodListing>> response2 = foodListingController.getNearbyListings(
        clientId,78.121f, 120.282f, 10);
    assertEquals(HttpStatus.OK, response2.getStatusCode());
    List<FoodListing> listingsFound2 = response2.getBody();
    assert(Objects.requireNonNull(listingsFound2).size() == 1);
    assertEquals("acc2", listingsFound2.get(0).getAccountId());
  }

  @Test
  public void getNearbyListingsOneFoundMultipleClientsTest() {
    ResponseEntity<ClientProfile> clientProfile1 = clientProfileController.createClientProfile();
    ResponseEntity<ClientProfile> clientProfile2 = clientProfileController.createClientProfile();

    ClientProfile client1 = clientProfile1.getBody();
    saveFoodListing1(client1); // Listings 1 and 2 are far apart;
    saveFoodListing2(client1); // should only find listing 1 under the same client
    ClientProfile client2 = clientProfile2.getBody();
    saveFoodListing2(client2); // Listings 2 and 3 are far apart;
    saveFoodListing3(client2); // should only find listing 2 under the same client

    assert client1 != null;
    String client1Id = String.valueOf(client1.getClientId());
    assert client2 != null;
    String client2Id = String.valueOf(client2.getClientId());

    ResponseEntity<List<FoodListing>> response = foodListingController.getNearbyListings(
        client1Id, 34.060f, -118.250f, 10);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    List<FoodListing> listingsFound = response.getBody();
    assert(Objects.requireNonNull(listingsFound).size() == 1);
    assertEquals("acc1", listingsFound.get(0).getAccountId());

    ResponseEntity<List<FoodListing>> response2 = foodListingController.getNearbyListings(
        client2Id,78.121f, 120.282f, 10);
    assertEquals(HttpStatus.OK, response2.getStatusCode());
    List<FoodListing> listingsFound2 = response2.getBody();
    assert(Objects.requireNonNull(listingsFound2).size() == 1);
    assertEquals("acc2", listingsFound2.get(0).getAccountId());
  }

  @Test
  public void getNearbyListingsTwoFoundTest() {
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    saveFoodListing1(client); // Only listings 1 and 3 are close to each other
    saveFoodListing2(client);
    saveFoodListing3(client);
    assert client != null;
    String clientId = String.valueOf(client.getClientId());

    ResponseEntity<List<FoodListing>> response = foodListingController.getNearbyListings(
        clientId, 34.021f, -118.243f, 10);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    List<FoodListing> listingsFound = response.getBody();
    assert listingsFound != null;
    assertEquals(2, listingsFound.size());
    Set<String> expected = Set.of("acc1", "acc3");
    for (FoodListing listing: listingsFound) {
      assert(expected.contains(listing.getAccountId()));
    }
  }
}
