package dev.coms4156.project.teamproject.repository;

import dev.coms4156.project.teamproject.FoodListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodListingRepository extends JpaRepository<FoodListing, Integer> {}
