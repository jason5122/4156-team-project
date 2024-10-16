package dev.coms4156.project.teamproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.coms4156.project.teamproject.model.ClientProfile;

@Repository
public interface ClientProfileRepository extends JpaRepository<ClientProfile, Integer> {}