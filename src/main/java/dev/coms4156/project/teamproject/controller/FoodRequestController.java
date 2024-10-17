package dev.coms4156.project.teamproject.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.coms4156.project.teamproject.model.AccountProfile;
import dev.coms4156.project.teamproject.model.ClientProfile;
import dev.coms4156.project.teamproject.model.FoodListing;
import dev.coms4156.project.teamproject.model.FoodRequest;
import dev.coms4156.project.teamproject.repository.AccountProfileRepository;
import dev.coms4156.project.teamproject.repository.ClientProfileRepository;
import dev.coms4156.project.teamproject.repository.FoodListingRepository;
import dev.coms4156.project.teamproject.repository.FoodRequestRepository;

@RestController
@RequestMapping("/api/foodRequests")
public class FoodRequestController {

    private final FoodRequestRepository foodRequestRepository;
    private final ClientProfileRepository clientProfileRepository;
    private final AccountProfileRepository accountProfileRepository;
    private final FoodListingRepository foodListingRepository;

    public FoodRequestController(FoodRequestRepository foodRequestRepository, 
                                 ClientProfileRepository clientProfileRepository,
                                 AccountProfileRepository accountProfileRepository,
                                 FoodListingRepository foodListingRepository) {
        this.foodRequestRepository = foodRequestRepository;
        this.clientProfileRepository = clientProfileRepository;
        this.accountProfileRepository = accountProfileRepository;
        this.foodListingRepository = foodListingRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<FoodRequest> createFoodRequest(
            @RequestParam int clientId,
            @RequestParam int accountId,
            @RequestParam int listingId,
            @RequestParam int quantityRequested) {
        
        Optional<ClientProfile> clientOptional = clientProfileRepository.findById(clientId);
        Optional<AccountProfile> accountOptional = accountProfileRepository.findById(accountId);
        Optional<FoodListing> listingOptional = foodListingRepository.findById(listingId);

        if (!clientOptional.isPresent() || !accountOptional.isPresent() || !listingOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ClientProfile client = clientOptional.get();
        AccountProfile account = accountOptional.get();
        FoodListing foodListing = listingOptional.get();

        FoodRequest foodRequest = new FoodRequest(client, account, foodListing, quantityRequested);
        foodRequestRepository.save(foodRequest);

        return new ResponseEntity<>(foodRequest, HttpStatus.CREATED);
    }

	@GetMapping("/get")
	public ResponseEntity<?> getFoodRequest(@RequestParam int requestId) {
		try {
			Optional<FoodRequest> foodRequestOptional = foodRequestRepository.findById(requestId); 
			if (!foodRequestOptional.isPresent()) {
				Map<String, Object> body = new HashMap<>();
				body.put("error", "Food request ID not found.");
				return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
			}
			FoodRequest foodRequest = foodRequestOptional.get();
			Map<String, Object> body = new HashMap<>();
			body.put("request_id", foodRequest.getRequestId());
			body.put("client_id", foodRequest.getClient().getClientId());
			body.put("account_id", foodRequest.getAccountId().getAccountId());
			body.put("listing_id", foodRequest.getListing().getListingId());
			body.put("quantity_requested", foodRequest.getQuantityRequested());
			body.put("request_time", foodRequest.getRequestTime());
			return new ResponseEntity<>(body, HttpStatus.OK);
		} catch (Exception e) {
			Map<String, Object> errorBody = new HashMap<>();
			errorBody.put("error", "Error while processing the request.");
			errorBody.put("details", e.getMessage());
			return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
		}
	}

    @PutMapping("/update")
    public ResponseEntity<?> updateFoodRequest(
            @RequestParam int requestId,
            @RequestParam int quantityRequested) {
			        
        Optional<FoodRequest> foodRequestOptional = foodRequestRepository.findById(requestId);
        if (!foodRequestOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        FoodRequest foodRequest = foodRequestOptional.get();
        foodRequest.setQuantityRequested(quantityRequested);
        foodRequestRepository.save(foodRequest);
		Map<String, Object> body = new HashMap<>();
        body.put("message", "Updated Successfully.");
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}
