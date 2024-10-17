package dev.coms4156.project.teamproject.controller;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/getClientProfile")
    public ResponseEntity<?> getClientProfile(@RequestParam String clientId) {
      int clientId_;
      try {
        clientId_ = Integer.parseInt(clientId);
      } catch (Exception e) {
        return ResponseEntity.badRequest().body("Expected clientId to be in integer form, got "
            + clientId + " instead.");
      }

      Optional<ClientProfile> clientOptional = clientProfileRepository.findById(clientId_);

      if (clientOptional.isEmpty()) {
        return ResponseEntity.notFound().build();
      } else {
        return ResponseEntity.ok().body(clientOptional.get());
      }
    }
}
