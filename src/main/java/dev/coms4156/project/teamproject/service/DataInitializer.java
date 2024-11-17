package dev.coms4156.project.teamproject.service;

import dev.coms4156.project.teamproject.model.AccountProfile;
import dev.coms4156.project.teamproject.model.ClientProfile;
import dev.coms4156.project.teamproject.model.FoodListing;
import dev.coms4156.project.teamproject.repository.AccountProfileRepository;
import dev.coms4156.project.teamproject.repository.ClientProfileRepository;
import dev.coms4156.project.teamproject.repository.FoodListingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {
  private final ClientProfileRepository clientProfileRepository;
  private final AccountProfileRepository accountProfileRepository;
  private final FoodListingRepository foodListingRepository;

  public DataInitializer(ClientProfileRepository clientProfileRepository, AccountProfileRepository accountProfileRepository,
      FoodListingRepository foodListingRepository) {
    this.clientProfileRepository = clientProfileRepository;
    this.accountProfileRepository = accountProfileRepository;
    this.foodListingRepository = foodListingRepository;
  }

  @Override
  public void run(String... args) {
    // Clear existing data
    foodListingRepository.deleteAll();
    accountProfileRepository.deleteAll();
    clientProfileRepository.deleteAll();

    ClientProfile foodDonationClient = new ClientProfile();
    clientProfileRepository.save(foodDonationClient);

    // Create Account Profiles for Providers
    AccountProfile provider1 = new AccountProfile(foodDonationClient, AccountProfile.AccountType.PROVIDER, "1234567890", "ProviderA");
    AccountProfile provider2 = new AccountProfile(foodDonationClient, AccountProfile.AccountType.PROVIDER, "0987654321", "ProviderB");
    accountProfileRepository.save(provider1);
    accountProfileRepository.save(provider2);

    // Create Food Listings for each provider
    FoodListing listing1 = new FoodListing(null, provider1, "Sandwiches", 20,
        LocalDateTime.now().plusHours(5), 34.052f, -118.243f);
    FoodListing listing2 = new FoodListing(null, provider1, "Pasta", 15,
        LocalDateTime.now().plusHours(3), 34.052f, -118.243f);
    FoodListing listing3 = new FoodListing(null, provider1, "Salads", 10,
        LocalDateTime.now().plusHours(6), 34.052f, -118.243f);

    FoodListing listing4 = new FoodListing(null, provider2, "Fruit Basket", 8,
        LocalDateTime.now().plusHours(4), 40.7128f, -74.0060f);
    FoodListing listing5 = new FoodListing(null, provider2, "Cereal", 12,
        LocalDateTime.now().plusHours(2), 40.7128f, -74.0060f);

    foodListingRepository.save(listing1);
    foodListingRepository.save(listing2);
    foodListingRepository.save(listing3);
    foodListingRepository.save(listing4);
    foodListingRepository.save(listing5);

    System.out.println("Sample provider accounts and food listings initialized for testing.");
  }
}
