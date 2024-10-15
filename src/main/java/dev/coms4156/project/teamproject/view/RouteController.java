package dev.coms4156.project.teamproject.view;

import dev.coms4156.project.teamproject.controller.FoodListingRepository;
import dev.coms4156.project.teamproject.model.FoodListing;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class contains all the API routes for the system.
 */
@RestController
public class RouteController {

    @Autowired
    private FoodListingRepository foodListingRepository;

    /**
     * API endpoint to create a new food listing.
     *
     * @param accountId ID of the account creating the listing
     * @param foodType Type of the food
     * @param quantityListed Quantity of food available
     * @param latitude Latitude of the food's location
     * @param longitude Longitude of the food's location
     * @return Confirmation message
     */
    @PostMapping("/createFoodListing")
    public @ResponseBody String createFoodListing(@RequestParam String accountId, @RequestParam String foodType,
                                    @RequestParam int quantityListed, @RequestParam float latitude,
                                    @RequestParam float longitude) {

        FoodListing foodListing = new FoodListing(accountId, foodType, quantityListed,
                                                  LocalDateTime.now(), latitude, longitude);

        foodListingRepository.save(foodListing);

        return "Food listing created successfully with ID: " + foodListing.getListingId();
    }

    @GetMapping("/getFoodListings")
    public @ResponseBody Iterable<FoodListing> getFoodListings() {
        return foodListingRepository.findAll();
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
