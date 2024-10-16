package dev.coms4156.project.teamproject.repository;

import dev.coms4156.project.teamproject.model.ClientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientProfileRepository extends JpaRepository<ClientProfile, Integer> {}
