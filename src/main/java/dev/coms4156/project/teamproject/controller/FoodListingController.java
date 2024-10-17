package dev.coms4156.project.teamproject.controller;

import dev.coms4156.project.teamproject.model.ClientProfile;
import dev.coms4156.project.teamproject.model.FoodListing;
import dev.coms4156.project.teamproject.model.Location;
import dev.coms4156.project.teamproject.repository.ClientProfileRepository;
import dev.coms4156.project.teamproject.repository.FoodListingRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class contains all the API routes for the system.
 */
@RestController
public class FoodListingController {
    @Autowired private FoodListingRepository foodListingRepository;
    @Autowired private ClientProfileRepository clientProfileRepository;
    @Autowired private ClientProfileController clientProfileController;

    /**
     * API endpoint to create a new food listing.
     * Expects that the client for whom the food listing is to be created
     * has been registered in the database.
     *
     * @param clientId ID of the client that the account is under
     * @param accountId ID of the account creating the listing
     * @param foodType Type of the food
     * @param quantityListed Quantity of food available
     * @param latitude Latitude of the food's location
     * @param longitude Longitude of the food's location
     * @return A ResponseEntity with status code OK and
     *    a message with the food listing ID if it was successfully created.
     *    Otherwise, a ResponseEntity with status code INTERNAL_SERVER_ERROR
     *    and an error message.
     */
    @PostMapping("/createFoodListing")
    public ResponseEntity<?> createFoodListing(
        @RequestParam String accountId, @RequestParam String clientId,
        @RequestParam String foodType, @RequestParam int quantityListed,
        @RequestParam float latitude, @RequestParam float longitude) {

        // Get client under `clientId`
        ResponseEntity<?> queryResult = clientProfileController.getClientProfile(clientId);
        if (!(queryResult.getBody() instanceof ClientProfile client)) {
            return queryResult;
        }

        FoodListing foodListing = new FoodListing(accountId, client, foodType, quantityListed,
            LocalDateTime.now(), latitude, longitude);

        FoodListing savedFoodListing = foodListingRepository.save(foodListing);

        if (savedFoodListing.getAccountId().equals(accountId)
            && savedFoodListing.getFoodType().equals(foodType)
            && savedFoodListing.getQuantityListed() == quantityListed
            && savedFoodListing.getLatitude() == latitude
            && savedFoodListing.getLongitude() == longitude) {
            return ResponseEntity.ok().body("Food listing created successfully with ID: "
                + savedFoodListing.getListingId());
        } else {
            return ResponseEntity.internalServerError().body("Failed to create food listing");
        }
    }

    /**
     * API endpoint to get all food listings.
     * Returns a ResponseEntity with status code OK and
     * a message with the food listing ID if it was successfully created.
     * Otherwise, returns a ResponseEntity with status code
     * INTERNAL_SERVER_ERROR and an error message.
     *
     * @param clientId ID of client to filter the food listings for,
     *                 assumed to be an integer
     * @return A ResponseEntity with status code OK and
     *    a collection of food listings if there is at least one listing in the database.
     *    Otherwise, a ResponseEntity with status code NOT_FOUND.
     */
    @GetMapping("/getFoodListings")
    public ResponseEntity<?> getFoodListings(@RequestParam String clientId) {
        // Find all listings under client with `clientId`
        int clientId_ = Integer.parseInt(clientId);
        List<FoodListing> listings = foodListingRepository.findByClient_ClientId(clientId_);

        if (!listings.isEmpty()) {
            return ResponseEntity.ok().body(listings);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * API endpoint for getting food listings with pick-up locations
     * within `maxDistance` of (`latitude`, `longitude`).
     *
     * @param clientId ID of client to filter the food listings for
     * @param latitude Latitude of the query location
     * @param longitude Longitude of the query location
     * @param maxDistance Maximum distance from the specified location to consider
     *                    when searching for food listings, expected to be greater than 0
     * @return A ResponseEntity containing a list of FoodListing objects
     *           that are within the specified distance. The HTTP status code will be OK if
     *           listings are found, or 404 NOT_FOUND otherwise.
     */
    @GetMapping("/getNearbyListings")
    public ResponseEntity<List<FoodListing>> getNearbyListings(@RequestParam String clientId,
        @RequestParam float latitude, @RequestParam float longitude,
        @RequestParam(required = false, defaultValue = "5") int maxDistance) {

        // Find all listings under client with `clientId`
        int clientId_ = Integer.parseInt(clientId);
        List<FoodListing> allListings = foodListingRepository.findByClient_ClientId(clientId_);

        // Then find listings within `maxDistance` of query location
        List<FoodListing> nearbyListings = new ArrayList<>();
        Location queryLocation = new Location(latitude, longitude);

        for (FoodListing listing: allListings) {
            Location listingLocation = new Location(listing.getLatitude(), listing.getLongitude());
            if (queryLocation.distance(listingLocation) <= maxDistance) {
                nearbyListings.add(listing);
            }
        }

        if (!nearbyListings.isEmpty()) {
            return ResponseEntity.ok().body(nearbyListings);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * API endpoint to get all food listings under the account with `accountId`
     * for the client under `clientId`
     *
     * @param clientId ID of client to filter the food listings for,
     *                 assumed to be an integer
     * @param accountId ID of account to fetch food listings for
     * @return A ResponseEntity with status code OK and
     *          a collection of food listings if there is at least one listing under `accountId`
     *          for the client with `clientId`.
     *          Otherwise, a ResponseEntity with status code NOT_FOUND.
     */
    @GetMapping("/getFoodListingsUnderAccount")
    public ResponseEntity<?> getFoodListingsUnderAccount(@RequestParam String clientId,
                                                         @RequestParam String accountId) {
        // Find all listings under client with `clientId`
        int clientId_ = Integer.parseInt(clientId);
        List<FoodListing> accountListings = foodListingRepository.findByClient_ClientIdAndAccountId(
            clientId_, accountId
        );

        if (!(accountListings.isEmpty())) {
            return ResponseEntity.ok().body(accountListings);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Redirects to the homepage.
     *
     * @return A String containing the name of the html file to be loaded.
     */
    @GetMapping({"/", "/index", "/home"})
    public @ResponseBody String index() {
        return "Welcome, in order to make an API call direct your browser or Postman to an "
            + "endpoint "
            + "\n\n This can be done using the following format: \n\n http:127.0.0"
            + ".1:8080/endpoint?arg=value";
    }

}
