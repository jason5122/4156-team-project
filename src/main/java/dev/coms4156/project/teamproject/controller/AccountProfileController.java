package dev.coms4156.project.teamproject.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.coms4156.project.teamproject.model.AccountProfile;
import dev.coms4156.project.teamproject.model.ClientProfile;
import dev.coms4156.project.teamproject.repository.AccountProfileRepository;
import dev.coms4156.project.teamproject.repository.ClientProfileRepository;

@RestController
@RequestMapping("/api/accountProfiles")
public class AccountProfileController {

    private final AccountProfileRepository accountProfileRepository;
	private final ClientProfileRepository clientProfileRepository;

    public AccountProfileController(AccountProfileRepository accountProfileRepository,ClientProfileRepository clientProfileRepository) {
        this.accountProfileRepository = accountProfileRepository;
		this.clientProfileRepository = clientProfileRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<AccountProfile> createAccountProfile(
			@RequestParam int clientId,
            @RequestParam AccountProfile.AccountType accountType,
            @RequestParam String phoneNumber,
            @RequestParam String name) {
		
		Optional<ClientProfile> clientOptional = clientProfileRepository.findById(clientId);
		if (!clientOptional.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		ClientProfile client = clientOptional.get();
        AccountProfile accountProfile = new AccountProfile(client, accountType, phoneNumber, name);
        accountProfileRepository.save(accountProfile);
        return new ResponseEntity<>(accountProfile, HttpStatus.CREATED);
    }
}