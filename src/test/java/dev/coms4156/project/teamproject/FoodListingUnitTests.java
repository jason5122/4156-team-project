package dev.coms4156.project.teamproject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.coms4156.project.teamproject.model.FoodListing;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes= FoodListingUnitTests.class)
@ContextConfiguration
public class FoodListingUnitTests {

  @Test
  public void testFoodListingToString() {
    // Set up fields for listing
    String accountId = "4156";
    String foodType = "samosa";
    int quantityListed = 5;
    float latitude = 40.7128f;
    float longitude = -74.0060f;
    LocalDateTime earliestPickUpTime = LocalDateTime.of(2024, 10, 15,
        16, 30);

    FoodListing listing = new FoodListing(accountId, foodType, quantityListed,
        earliestPickUpTime, latitude, longitude);
    String expected = "FoodListing{"
        + "accountId='4156'"
        + ", foodType=samosa"
        + ", quantityListed=5"
        + ", earliestPickUpTime=2024-10-15 16:30:00"
        + ", latitude=40.713"
        + ", longitude=-74.006"
        + '}';
    assertEquals(expected, listing.toString());
  }
}
