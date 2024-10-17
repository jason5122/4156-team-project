package dev.coms4156.project.teamproject.controller;

import dev.coms4156.project.teamproject.model.ClientProfile;
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
@RequestMapping("/api/clientProfiles")
public class ClientProfileController {

    @Autowired private ClientProfileRepository clientProfileRepository;

    @PostMapping("/create")
    public ResponseEntity<ClientProfile> createClientProfile() {
        ClientProfile client = new ClientProfile();
        clientProfileRepository.save(client);
        return new ResponseEntity<>(client, HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getClientProfile(@RequestParam int clientId) {
        Optional<ClientProfile> clientOptional = clientProfileRepository.findById(clientId);
        if (!clientOptional.isPresent()) {
            Map<String, Object> body = new HashMap<>();
            body.put("error", "Client ID not found.");
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        }

        ClientProfile client = clientOptional.get();
        Map<String, Object> body = new HashMap<>();
        body.put("client_id", client.getClientId());
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}
