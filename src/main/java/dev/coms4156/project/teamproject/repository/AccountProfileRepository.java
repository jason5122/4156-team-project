package dev.coms4156.project.teamproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.coms4156.project.teamproject.model.AccountProfile;

@Repository
public interface AccountProfileRepository extends JpaRepository<AccountProfile, String> {}