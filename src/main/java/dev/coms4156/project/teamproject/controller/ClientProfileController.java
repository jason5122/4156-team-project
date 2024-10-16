package dev.coms4156.project.teamproject.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.coms4156.project.teamproject.model.ClientProfile;
import dev.coms4156.project.teamproject.repository.ClientProfileRepository;

@RestController
@RequestMapping("/api/clientProfiles")
public class ClientProfileController {

	private final ClientProfileRepository clientProfileRepository;

    public ClientProfileController(ClientProfileRepository clientProfileRepository) {
		this.clientProfileRepository = clientProfileRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<ClientProfile> createClientProfile() {		
		ClientProfile client = new ClientProfile();
        clientProfileRepository.save(client);
        return new ResponseEntity<>(client, HttpStatus.CREATED);
    }
}