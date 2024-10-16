package dev.coms4156.project.teamproject.controller;

import dev.coms4156.project.teamproject.model.FoodListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodListingRepository extends JpaRepository<FoodListing, Integer> {}
