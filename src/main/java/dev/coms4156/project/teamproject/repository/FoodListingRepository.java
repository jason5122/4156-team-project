package dev.coms4156.project.teamproject.repository;

import dev.coms4156.project.teamproject.FoodListing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodListingRepository extends CrudRepository<FoodListing, Integer> {
  // Custom queries here
}