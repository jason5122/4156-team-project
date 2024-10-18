package dev.coms4156.project.teamproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import dev.coms4156.project.teamproject.controller.AccountProfileController;
import dev.coms4156.project.teamproject.controller.ClientProfileController;
import dev.coms4156.project.teamproject.controller.FoodListingController;
import dev.coms4156.project.teamproject.model.AccountProfile;
import dev.coms4156.project.teamproject.model.ClientProfile;
import dev.coms4156.project.teamproject.model.FoodListing;
import dev.coms4156.project.teamproject.repository.FoodListingRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Unit tests for the FoodListingController class.
 *
 * <p>These tests validate the behavior of FoodListing-related API operations
 * such as creating and retrieving food listings.
 */
@DataJpaTest
@Import({FoodListingController.class,
    ClientProfileController.class,
    AccountProfileController.class})
public class FoodListingControllerUnitTests {

  @Autowired
  private FoodListingRepository foodListingRepository;

  @Autowired
  private FoodListingController foodListingController;

  @Autowired
  private ClientProfileController clientProfileController;

  @Autowired
  private AccountProfileController accountProfileController;

  private void saveFoodListing1(ClientProfile client, AccountProfile account) {
    FoodListing foodListing1 = new FoodListing(client, account, "snack",
        25, LocalDateTime.of(2024, 10, 6, 11, 0),
        34.052f, -118.243f);

    foodListingRepository.save(foodListing1);
  }

  private FoodListing makeFoodListing1(ClientProfile client, AccountProfile account) {
    return new FoodListing(client, account, "snack",
        25, LocalDateTime.of(2024, 10, 6, 11, 0),
        34.052f, -118.243f);
  }

  private void saveFoodListing2(ClientProfile client, AccountProfile account) {
    FoodListing foodListing2 = new FoodListing(client, account, "beverage",
        30, LocalDateTime.of(2024, 10, 7, 16, 30),
        78.122f, 120.281f);
    foodListingRepository.save(foodListing2);
  }

  private FoodListing makeFoodListing2(ClientProfile client, AccountProfile account) {
    return new FoodListing(client, account, "beverage",
        30, LocalDateTime.of(2024, 10, 7, 16, 30),
        78.122f, 120.281f);
  }

  private void saveFoodListing3(ClientProfile client, AccountProfile account) {
    FoodListing foodListing3 = new FoodListing(client, account, "rice",
        140, LocalDateTime.of(2024, 10, 8, 14, 00),
        33.989f, -118.243f);
    foodListingRepository.save(foodListing3);
  }

  private FoodListing makeFoodListing3(ClientProfile client, AccountProfile account) {
    return new FoodListing(client, account, "rice",
        140, LocalDateTime.of(2024, 10, 8, 14, 00),
        33.989f, -118.243f);
  }

  @BeforeEach
  public void setup() {
    // Make sure repository is in a clean state
    foodListingRepository.deleteAll();
  }

  @Test
  public void createFoodListingOkTest() {
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    ResponseEntity<AccountProfile> accountProfile = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.PROVIDER, "1234567890", "x");
    AccountProfile account = accountProfile.getBody();
    assert account != null;
    int accountId = account.getAccountId();

    ResponseEntity<?> response = foodListingController.createFoodListing(
        clientId, accountId,
        "kiwi", 10,
        34.052f, -118.244f);

    List<FoodListing> listings = foodListingRepository.findAll();
    assertEquals(1, listings.size());
    FoodListing listing = listings.get(0);

    assert (((String) Objects.requireNonNull(response.getBody()))
        .contains("Food listing created successfully with ID: "));
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals("kiwi", listing.getFoodType());
    assertEquals(10, listing.getQuantityListed());
    assertEquals(34.052f, listing.getLatitude());
    assertEquals(-118.244f, listing.getLongitude());
  }

  @Test
  public void createFoodListingMissingClientTest() {
    ResponseEntity<?> response = foodListingController.createFoodListing(
        111, 222,
        "kiwi", 10,
        34.052f, -118.244f);

    // Check status code
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void createFoodListingMissingAccountTest() {
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    ResponseEntity<?> response = foodListingController.createFoodListing(
        clientId, 222,
        "kiwi", 10,
        34.052f, -118.244f);

    // Check status code
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void getFoodListingNoneFoundTest() {
    // Test with empty repository
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();
    ResponseEntity<?> response = foodListingController.getFoodListings(clientId);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void getFoodListingsOneClientTest() {
    // Test with non-empty repository
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    ResponseEntity<AccountProfile> accountProfile = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.PROVIDER, "1234567890", "x");
    AccountProfile account = accountProfile.getBody();

    saveFoodListing1(client, account);
    saveFoodListing2(client, account);

    ResponseEntity<?> response = foodListingController.getFoodListings(clientId);
    FoodListing expected1 = makeFoodListing1(client, account);
    FoodListing expected2 = makeFoodListing2(client, account);
    Set<FoodListing> expected = Set.of(expected1, expected2);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    @SuppressWarnings("unchecked") // suppress warning for unchecked cast...
    List<FoodListing> foodListings = (List<FoodListing>) response.getBody();
    assert (Objects.requireNonNull(foodListings).size() == 2);

    for (FoodListing listing : foodListings) {
      assert (expected.contains(listing));
    }
  }

  @Test
  public void getFoodListingMultipleClientsTest() {
    // Listing 1 and 2 have the same client
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();
    // Create an account under this client
    ResponseEntity<AccountProfile> accountProfile = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.PROVIDER, "1234567890", "x");
    AccountProfile account = accountProfile.getBody();
    // Listings 1 and 2 are under the same client and account
    saveFoodListing1(client, account);
    saveFoodListing2(client, account);

    // Listing 3 has a different client
    ResponseEntity<ClientProfile> clientProfile2 = clientProfileController.createClientProfile();
    ClientProfile client2 = clientProfile2.getBody();
    assert client2 != null;
    int client2Id = client2.getClientId();
    // Create an account under this client
    ResponseEntity<AccountProfile> accountProfile2 = accountProfileController.createAccountProfile(
        client2Id, AccountProfile.AccountType.PROVIDER, "1001111000", "a");
    AccountProfile account2 = accountProfile2.getBody();
    saveFoodListing3(client2, account2);

    // Test that we have the correct listings for the first client
    ResponseEntity<?> response = foodListingController.getFoodListings(clientId);
    FoodListing expected1 = makeFoodListing1(client, account);
    FoodListing expected2 = makeFoodListing2(client, account);
    Set<FoodListing> expectedSet = Set.of(expected1, expected2);

    assertEquals(HttpStatus.OK, response.getStatusCode()); // Check status code

    // Check body
    @SuppressWarnings("unchecked") // suppress warning for unchecked cast...
    List<FoodListing> foodListings = (List<FoodListing>) response.getBody();
    assert (Objects.requireNonNull(foodListings).size() == 2);
    for (FoodListing listing : foodListings) {
      assert (expectedSet.contains(listing));
    }

    // Test that we have the correct listings for the second client
    FoodListing expected3 = makeFoodListing3(client2, account2);
    ResponseEntity<?> response2 = foodListingController.getFoodListings(client2Id);
    assertEquals(HttpStatus.OK, response2.getStatusCode()); // Check status code

    // Check body
    @SuppressWarnings("unchecked") // suppress warning for unchecked cast...
    List<FoodListing> foodListings2 = (List<FoodListing>) response2.getBody();
    assert (Objects.requireNonNull(foodListings2).size() == 1);
    assert (foodListings2.get(0).equals(expected3));
  }

  @Test
  public void getFoodListingsMissingClientTest() {
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    ResponseEntity<AccountProfile> accountProfile = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.PROVIDER, "1234567890", "x");
    AccountProfile account = accountProfile.getBody();

    saveFoodListing1(client, account);
    saveFoodListing2(client, account);

    // No client has this ID
    int badClientId = client.getClientId() + 2;

    ResponseEntity<?> response = foodListingController.getFoodListings(badClientId);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void getNearbyListingsZeroFoundTest() {
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    ResponseEntity<AccountProfile> accountProfile = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.PROVIDER, "1234567890", "x");
    AccountProfile account = accountProfile.getBody();

    saveFoodListing1(client, account);
    saveFoodListing2(client, account);

    ResponseEntity<?> response = foodListingController.getNearbyListings(
        clientId, 30.000f, -100.000f, 10);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void getNearbyListingsMissingClientTest() {
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    ResponseEntity<AccountProfile> accountProfile = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.PROVIDER, "1234567890", "x");
    AccountProfile account = accountProfile.getBody();

    saveFoodListing1(client, account);
    saveFoodListing2(client, account);

    // No client has this ID
    int badClientId = client.getClientId() + 2;

    // This query location is within 10 (units) of listing 1, but
    // response should still be NOT_FOUND since client doesn't exist
    ResponseEntity<?> response = foodListingController.getNearbyListings(
        badClientId, 78.121f, 120.282f, 10);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void getNearbyListingsOneFoundSingleClientTest() {
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    ResponseEntity<AccountProfile> accountProfile = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.PROVIDER, "1234567890", "x");
    AccountProfile account = accountProfile.getBody();

    saveFoodListing1(client, account);
    saveFoodListing2(client, account);

    // Query from a location near listing 1
    ResponseEntity<?> response = foodListingController.getNearbyListings(
        clientId, 34.060f, -118.250f, 10);
    // Check status code
    assertEquals(HttpStatus.OK, response.getStatusCode());
    // Check body
    @SuppressWarnings("unchecked") // suppress warning for unchecked cast...
    List<FoodListing> listingsFound = (List<FoodListing>) response.getBody();
    assert (Objects.requireNonNull(listingsFound).size() == 1);
    FoodListing expected1 = makeFoodListing1(client, account);
    assert (expected1.equals(listingsFound.get(0)));

    // Query form a location near listing 2
    ResponseEntity<?> response2 = foodListingController.getNearbyListings(
        clientId, 78.121f, 120.282f, 10);
    // Check status code
    assertEquals(HttpStatus.OK, response2.getStatusCode());
    // Check body
    @SuppressWarnings("unchecked")
    List<FoodListing> listingsFound2 = (List<FoodListing>) response2.getBody();
    assert (Objects.requireNonNull(listingsFound2).size() == 1);
    FoodListing expected2 = makeFoodListing2(client, account);
    assert (expected2.equals(listingsFound2.get(0)));
  }

  @Test
  public void getNearbyListingsOneFoundMultipleClientsTest() {
    // Create client profile 1
    ResponseEntity<ClientProfile> clientProfile1 = clientProfileController.createClientProfile();
    ClientProfile client1 = clientProfile1.getBody();
    assert client1 != null;
    int client1Id = client1.getClientId();
    // Create account under client 1
    ResponseEntity<AccountProfile> accountProfile = accountProfileController.createAccountProfile(
        client1Id, AccountProfile.AccountType.PROVIDER, "1234567890", "x");
    AccountProfile account1 = accountProfile.getBody();

    // Create client profile 2
    ResponseEntity<ClientProfile> clientProfile2 = clientProfileController.createClientProfile();
    ClientProfile client2 = clientProfile2.getBody();
    assert client2 != null;
    int client2Id = client2.getClientId();
    // Create account under client 2
    ResponseEntity<AccountProfile> accountProfile2 = accountProfileController.createAccountProfile(
        client2Id, AccountProfile.AccountType.PROVIDER, "0987654321", "a");
    AccountProfile account2 = accountProfile2.getBody();

    // Save listings to repository
    saveFoodListing1(client1, account1); // Listings 1 and 2 are far apart, under same client;
    saveFoodListing2(client1, account1); // should only find listing 1

    saveFoodListing2(client2, account2); // Listings 2 and 3 are far apart, under same client;
    saveFoodListing3(client2, account2); // should only find listing 2

    // Test endpoint
    ResponseEntity<?> response = foodListingController.getNearbyListings(
        client1Id, 34.060f, -118.250f, 10);
    // Check status code
    assertEquals(HttpStatus.OK, response.getStatusCode());
    // Check body
    @SuppressWarnings("unchecked") // suppress warning for unchecked cast...
    List<FoodListing> listingsFound = (List<FoodListing>) response.getBody();
    assert (Objects.requireNonNull(listingsFound).size() == 1);
    FoodListing expected1 = makeFoodListing1(client1, account1);
    assert (expected1.equals(listingsFound.get(0)));

    ResponseEntity<?> response2 = foodListingController.getNearbyListings(
        client2Id, 78.121f, 120.282f, 10);
    // Check status code
    assertEquals(HttpStatus.OK, response2.getStatusCode());
    // Check body
    @SuppressWarnings("unchecked") // suppress warning for unchecked cast...
    List<FoodListing> listingsFound2 = (List<FoodListing>) response2.getBody();
    assert (Objects.requireNonNull(listingsFound2).size() == 1);
    FoodListing expected2 = makeFoodListing1(client2, account2);
    assert (expected2.equals(listingsFound.get(0)));
  }

  @Test
  public void getNearbyListingsTwoFoundTest() {
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    ResponseEntity<AccountProfile> accountProfile = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.PROVIDER, "1234567890", "x");
    AccountProfile account = accountProfile.getBody();

    saveFoodListing1(client, account); // Only listings 1 and 3 are close to each other
    saveFoodListing2(client, account);
    saveFoodListing3(client, account);

    // Test endpoint
    ResponseEntity<?> response = foodListingController.getNearbyListings(
        clientId, 34.021f, -118.243f, 10);
    // Check status code
    assertEquals(HttpStatus.OK, response.getStatusCode());
    // Check body
    @SuppressWarnings("unchecked")
    List<FoodListing> listingsFound = (List<FoodListing>) response.getBody();
    assert listingsFound != null;
    assertEquals(2, listingsFound.size());

    FoodListing expected1 = makeFoodListing1(client, account);
    FoodListing expected3 = makeFoodListing3(client, account);
    Set<FoodListing> expectedSet = Set.of(expected1, expected3);
    for (FoodListing listing : listingsFound) {
      assert (expectedSet.contains(listing));
    }
  }

  @Test
  public void getFoodListingsUnderAccountMissingClientTest() {
    ResponseEntity<?> response = foodListingController.getFoodListingsUnderAccount(
        111, 222);

    // Check status code
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void getFoodListingsUnderAccountMissingAccountTest() {
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    ResponseEntity<?> response = foodListingController.getFoodListingsUnderAccount(
        clientId, 222);

    // Check status code
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void getFoodListingsUnderAccountNoneFoundTest() {
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    ResponseEntity<AccountProfile> accountProfile = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.PROVIDER, "1234567890", "x");
    AccountProfile account = accountProfile.getBody();
    assert account != null;

    ResponseEntity<AccountProfile> accountProfile2 = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.PROVIDER, "1234567890", "x");
    AccountProfile account2 = accountProfile2.getBody();

    // Save listings under account 2, then should find no listings when querying account 1
    saveFoodListing1(client, account2);
    saveFoodListing2(client, account2);
    saveFoodListing3(client, account2);

    // Check status code
    int accountId = account.getAccountId();
    ResponseEntity<?> response = foodListingController.getFoodListingsUnderAccount(
        clientId, accountId);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void getFoodListingsUnderAccountSomeFoundTest() {
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    ResponseEntity<AccountProfile> accountProfile = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.PROVIDER, "1234567890", "x");
    AccountProfile account = accountProfile.getBody();
    assert account != null;

    // Save listings under account 2, then should find no listings when querying account 1
    saveFoodListing1(client, account);
    saveFoodListing2(client, account);
    saveFoodListing3(client, account);

    // Check status code
    int accountId = account.getAccountId();
    ResponseEntity<?> response = foodListingController.getFoodListingsUnderAccount(
        clientId, accountId);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    // Check body
    @SuppressWarnings("unchecked") // suppress warning for unchecked cast...
    List<FoodListing> foodListings = (List<FoodListing>) response.getBody();
    assert (Objects.requireNonNull(foodListings).size() == 3);

    FoodListing expected1 = makeFoodListing1(client, account);
    FoodListing expected2 = makeFoodListing2(client, account);
    FoodListing expected3 = makeFoodListing3(client, account);
    Set<FoodListing> expectedSet = Set.of(expected1, expected2, expected3);

    for (FoodListing listing : foodListings) {
      assert (expectedSet.contains(listing));
    }
  }

  @Test
  public void fulfillRequestMissingClientTest() {
    ResponseEntity<?> response = foodListingController.fulfillRequest(
        111, 1, 1, 1);

    // Check status code
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void fulfillRequestMissingAccountTest() {
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    ResponseEntity<?> response = foodListingController.fulfillRequest(
        clientId, 1, 1, 1);

    // Check status code
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void fulfillRequestMissingListingIdTest() {
    // Create client
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    // Create provider account
    ResponseEntity<AccountProfile> providerProfile = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.PROVIDER, "1234567890", "p");
    AccountProfile providerAccount = providerProfile.getBody();
    assert providerAccount != null;
    int providerId = providerAccount.getAccountId();

    // Create listing
    FoodListing listing = new FoodListing(client, providerAccount, "beverage",
        30, LocalDateTime.of(2024, 10, 7, 16, 30),
        78.122f, 120.281f);
    foodListingRepository.save(listing);
    int badListingId = listing.getListingId() + 2; // No listing has this ID

    ResponseEntity<?> response = foodListingController.fulfillRequest(
        clientId, providerId, badListingId, 1);

    // Check status code
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void fulfillRequestOkTest() {
    // Create client
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    // Create provider account
    ResponseEntity<AccountProfile> providerProfile = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.PROVIDER, "1234567890", "p");
    AccountProfile providerAccount = providerProfile.getBody();
    assert providerAccount != null;
    int providerId = providerAccount.getAccountId();

    // Create listing
    FoodListing listing = new FoodListing(client, providerAccount, "beverage",
        30, LocalDateTime.of(2024, 10, 7, 16, 30),
        78.122f, 120.281f);
    FoodListing savedListing = foodListingRepository.save(listing);
    int listingId = savedListing.getListingId();

    ResponseEntity<?> response = foodListingController.fulfillRequest(
        clientId, providerId, listingId, 30);

    // Check status code
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void fulfillRequestBadRequestTest() {
    // Create client
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    // Create provider account
    ResponseEntity<AccountProfile> providerProfile = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.PROVIDER, "1234567890", "p");
    AccountProfile providerAccount = providerProfile.getBody();
    assert providerAccount != null;
    int providerId = providerAccount.getAccountId();

    // Create listing
    FoodListing listing = new FoodListing(client, providerAccount, "beverage",
        30, LocalDateTime.of(2024, 10, 7, 16, 30),
        78.122f, 120.281f);
    FoodListing savedListing = foodListingRepository.save(listing);
    int listingId = savedListing.getListingId();

    ResponseEntity<?> response = foodListingController.fulfillRequest(
        clientId, providerId, listingId, 31);

    // Check status code
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void fulfillRequestUnauthorizedAccountTest() {
    // Create client
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    // Create provider account
    ResponseEntity<AccountProfile> providerProfile = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.PROVIDER, "1234567890", "p");
    AccountProfile providerAccount = providerProfile.getBody();

    // Create recipient account to make unauthorized call to fulfillRequest
    ResponseEntity<AccountProfile> recipientProfile = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.RECIPIENT, "1234567890", "r");
    AccountProfile recipientAccount = recipientProfile.getBody();
    assert recipientAccount != null;
    int recipientAccountId = recipientAccount.getAccountId();

    // Create listing
    FoodListing listing = new FoodListing(client, providerAccount, "beverage",
        30, LocalDateTime.of(2024, 10, 7, 16, 30),
        78.122f, 120.281f);
    FoodListing savedListing = foodListingRepository.save(listing);
    int listingId = savedListing.getListingId();

    ResponseEntity<?> response = foodListingController.fulfillRequest(
        clientId, recipientAccountId, listingId, 12);

    // Check status code
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }

  @Test
  public void updateFoodListingMissingClientTest() {
    ResponseEntity<?> response = foodListingController.updateFoodListing(
        111, 1, 1,
        null, null, null, null);

    // Check status code
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void updateFoodListingMissingAccountTest() {
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    ResponseEntity<?> response = foodListingController.updateFoodListing(
        clientId, 1, 1,
        null, null, null, null);

    // Check status code
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void updateFoodListingMissingAccountIdTest() {
    // Create client
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    // Create provider account
    ResponseEntity<AccountProfile> providerProfile = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.PROVIDER, "1234567890", "p");
    AccountProfile providerAccount = providerProfile.getBody();
    assert providerAccount != null;
    int providerId = providerAccount.getAccountId();

    // Create listing
    FoodListing listing = new FoodListing(client, providerAccount, "beverage",
        30, LocalDateTime.of(2024, 10, 7, 16, 30),
        78.122f, 120.281f);
    foodListingRepository.save(listing);
    int badListingId = listing.getListingId() + 2; // No listing has this ID

    ResponseEntity<?> response = foodListingController.updateFoodListing(
        clientId, providerId, badListingId,
        null, null, null, null);

    // Check status code
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void updateFoodListingUnauthorizedAccountTest() {
    // Create client
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    // Create provider account
    ResponseEntity<AccountProfile> providerProfile = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.PROVIDER, "1234567890", "p");
    AccountProfile providerAccount = providerProfile.getBody();

    // Create recipient account to make unauthorized call to updateFoodListing
    ResponseEntity<AccountProfile> recipientProfile = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.RECIPIENT, "1234567890", "r");
    AccountProfile recipientAccount = recipientProfile.getBody();
    assert recipientAccount != null;
    int recipientAccountId = recipientAccount.getAccountId();

    // Create listing
    FoodListing listing = new FoodListing(client, providerAccount, "beverage",
        30, LocalDateTime.of(2024, 10, 7, 16, 30),
        78.122f, 120.281f);
    FoodListing savedListing = foodListingRepository.save(listing);
    int listingId = savedListing.getListingId();

    ResponseEntity<?> response = foodListingController.updateFoodListing(
        clientId, recipientAccountId, listingId,
        "OJ", 112.2f, 359.3f, 10);

    // Check status code
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }

  @Test
  public void updateFoodListingNewFoodTypeTest() {
    // Create client
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    // Create provider account
    ResponseEntity<AccountProfile> providerProfile = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.PROVIDER, "1234567890", "p");
    AccountProfile providerAccount = providerProfile.getBody();
    assert providerAccount != null;
    int providerAccountId = providerAccount.getAccountId();

    // Create listing
    FoodListing listing = new FoodListing(client, providerAccount, "beverage",
        30, LocalDateTime.of(2024, 10, 7, 16, 30),
        78.122f, 120.281f);
    FoodListing savedListing = foodListingRepository.save(listing);
    int listingId = savedListing.getListingId();

    ResponseEntity<?> response = foodListingController.updateFoodListing(
        clientId, providerAccountId, listingId,
        "hot cocoa", null, null, null);
    // Check status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    // Check that the food type of the saved listing has been correctly updated
    Optional<FoodListing> listingOptional = foodListingRepository.findById(listingId);
    if (listingOptional.isPresent()) {
      FoodListing updatedListing = listingOptional.get();
      assert (updatedListing.getFoodType().equals("hot cocoa")); // the only field that was changed
      assert (updatedListing.getLatitude() == 78.122f);
      assert (updatedListing.getLongitude() == 120.281f);
      assert (updatedListing.getQuantityListed() == 30);
    } else {
      fail();
    }
  }

  @Test
  public void updateFoodListingNewLatitudeTest() {
    // Create client
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    // Create provider account
    ResponseEntity<AccountProfile> providerProfile = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.PROVIDER, "1234567890", "p");
    AccountProfile providerAccount = providerProfile.getBody();
    assert providerAccount != null;
    int providerAccountId = providerAccount.getAccountId();

    // Create listing
    FoodListing listing = new FoodListing(client, providerAccount, "beverage",
        30, LocalDateTime.of(2024, 10, 7, 16, 30),
        78.122f, 120.281f);
    FoodListing savedListing = foodListingRepository.save(listing);
    int listingId = savedListing.getListingId();

    ResponseEntity<?> response = foodListingController.updateFoodListing(
        clientId, providerAccountId, listingId,
        null, 120f, null, null);
    // Check status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    // Check that the food type of the saved listing has been correctly updated
    Optional<FoodListing> listingOptional = foodListingRepository.findById(listingId);
    if (listingOptional.isPresent()) {
      FoodListing updatedListing = listingOptional.get();
      assert (updatedListing.getFoodType().equals("beverage"));
      assert (updatedListing.getLatitude() == 120f);
      assert (updatedListing.getLongitude() == 120.281f);
      assert (updatedListing.getQuantityListed() == 30);
    } else {
      fail();
    }
  }

  @Test
  public void updateFoodListingNewLongitudeTest() {
    // Create client
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    // Create provider account
    ResponseEntity<AccountProfile> providerProfile = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.PROVIDER, "1234567890", "p");
    AccountProfile providerAccount = providerProfile.getBody();
    assert providerAccount != null;
    int providerAccountId = providerAccount.getAccountId();

    // Create listing
    FoodListing listing = new FoodListing(client, providerAccount, "beverage",
        30, LocalDateTime.of(2024, 10, 7, 16, 30),
        78.122f, 120.281f);
    FoodListing savedListing = foodListingRepository.save(listing);
    int listingId = savedListing.getListingId();

    ResponseEntity<?> response = foodListingController.updateFoodListing(
        clientId, providerAccountId, listingId,
        null, null, 320.94f, null);
    // Check status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    // Check that the food type of the saved listing has been correctly updated
    Optional<FoodListing> listingOptional = foodListingRepository.findById(listingId);
    if (listingOptional.isPresent()) {
      FoodListing updatedListing = listingOptional.get();
      assert (updatedListing.getFoodType().equals("beverage"));
      assert (updatedListing.getLatitude() == 78.122f);
      assert (updatedListing.getLongitude() == 320.94f);
      assert (updatedListing.getQuantityListed() == 30);
    } else {
      fail();
    }
  }

  @Test
  public void updateFoodListingNewQuantityTest() {
    // Create client
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    // Create provider account
    ResponseEntity<AccountProfile> providerProfile = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.PROVIDER, "1234567890", "p");
    AccountProfile providerAccount = providerProfile.getBody();
    assert providerAccount != null;
    int providerAccountId = providerAccount.getAccountId();

    // Create listing
    FoodListing listing = new FoodListing(client, providerAccount, "beverage",
        30, LocalDateTime.of(2024, 10, 7, 16, 30),
        78.122f, 120.281f);
    FoodListing savedListing = foodListingRepository.save(listing);
    int listingId = savedListing.getListingId();

    ResponseEntity<?> response = foodListingController.updateFoodListing(
        clientId, providerAccountId, listingId,
        null, null, null, 1);
    // Check status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    // Check that the food type of the saved listing has been correctly updated
    Optional<FoodListing> listingOptional = foodListingRepository.findById(listingId);
    if (listingOptional.isPresent()) {
      FoodListing updatedListing = listingOptional.get();
      assert (updatedListing.getFoodType().equals("beverage"));
      assert (updatedListing.getLatitude() == 78.122f);
      assert (updatedListing.getLongitude() == 120.281f);
      assert (updatedListing.getQuantityListed() == 1);
    } else {
      fail();
    }
  }

  @Test
  public void updateFoodListingNewComboTest() {
    // Create client
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    // Create provider account
    ResponseEntity<AccountProfile> providerProfile = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.PROVIDER, "1234567890", "p");
    AccountProfile providerAccount = providerProfile.getBody();
    assert providerAccount != null;
    int providerAccountId = providerAccount.getAccountId();

    // Create listing
    FoodListing listing = new FoodListing(client, providerAccount, "beverage",
        30, LocalDateTime.of(2024, 10, 7, 16, 30),
        78.122f, 120.281f);
    FoodListing savedListing = foodListingRepository.save(listing);
    int listingId = savedListing.getListingId();

    ResponseEntity<?> response = foodListingController.updateFoodListing(
        clientId, providerAccountId, listingId,
        "espresso beans", 120f, null, 1000);
    // Check status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    // Check that the food type of the saved listing has been correctly updated
    Optional<FoodListing> listingOptional = foodListingRepository.findById(listingId);
    if (listingOptional.isPresent()) {
      FoodListing updatedListing = listingOptional.get();
      assert (updatedListing.getFoodType().equals("espresso beans"));
      assert (updatedListing.getLatitude() == 120f);
      assert (updatedListing.getLongitude() == 120.281f);
      assert (updatedListing.getQuantityListed() == 1000);
    } else {
      fail();
    }
  }

  @Test
  public void updateFoodListingNewEveryTest() {
    // Create client
    ResponseEntity<ClientProfile> clientProfile = clientProfileController.createClientProfile();
    ClientProfile client = clientProfile.getBody();
    assert client != null;
    int clientId = client.getClientId();

    // Create provider account
    ResponseEntity<AccountProfile> providerProfile = accountProfileController.createAccountProfile(
        clientId, AccountProfile.AccountType.PROVIDER, "1234567890", "p");
    AccountProfile providerAccount = providerProfile.getBody();
    assert providerAccount != null;
    int providerAccountId = providerAccount.getAccountId();

    // Create listing
    FoodListing listing = new FoodListing(client, providerAccount, "beverage",
        30, LocalDateTime.of(2024, 10, 7, 16, 30),
        78.122f, 120.281f);
    FoodListing savedListing = foodListingRepository.save(listing);
    int listingId = savedListing.getListingId();

    ResponseEntity<?> response = foodListingController.updateFoodListing(
        clientId, providerAccountId, listingId,
        "BLT sandwich", 119.275f, 67.982f, 21);
    // Check status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    // Check that the food type of the saved listing has been correctly updated
    Optional<FoodListing> listingOptional = foodListingRepository.findById(listingId);
    if (listingOptional.isPresent()) {
      FoodListing updatedListing = listingOptional.get();
      assert (updatedListing.getFoodType().equals("BLT sandwich"));
      assert (updatedListing.getLatitude() == 119.275f);
      assert (updatedListing.getLongitude() == 67.982f);
      assert (updatedListing.getQuantityListed() == 21);
    } else {
      fail();
    }
  }
}
