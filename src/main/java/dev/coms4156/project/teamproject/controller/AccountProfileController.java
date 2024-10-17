package dev.coms4156.project.teamproject.controller;

import dev.coms4156.project.teamproject.model.AccountProfile;
import dev.coms4156.project.teamproject.model.ClientProfile;
import dev.coms4156.project.teamproject.repository.AccountProfileRepository;
import dev.coms4156.project.teamproject.repository.ClientProfileRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accountProfiles")
public class AccountProfileController {

    @Autowired private ClientProfileRepository clientProfileRepository;
    @Autowired private AccountProfileRepository accountProfileRepository;

    @PostMapping("/create")
    public ResponseEntity<AccountProfile>
    createAccountProfile(@RequestParam int clientId,
                         @RequestParam AccountProfile.AccountType accountType,
                         @RequestParam String phoneNumber, @RequestParam String name) {
        Optional<ClientProfile> clientOptional = clientProfileRepository.findById(clientId);
        if (!clientOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ClientProfile client = clientOptional.get();
        AccountProfile accountProfile = new AccountProfile(client, accountType, phoneNumber, name);
        accountProfileRepository.save(accountProfile);
        return new ResponseEntity<>(accountProfile, HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAccountProfile(@RequestParam int accountId) {
        Optional<AccountProfile> accountOptional = accountProfileRepository.findById(accountId);
        if (!accountOptional.isPresent()) {
            Map<String, Object> body = new HashMap<>();
            body.put("error", "Account ID not found.");
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        }

        AccountProfile account = accountOptional.get();
        Map<String, Object> body = new HashMap<>();
        body.put("account_id", account.getAccountId());
        body.put("name", account.getName());
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}
