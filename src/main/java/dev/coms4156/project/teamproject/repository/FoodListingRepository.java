package dev.coms4156.project.teamproject.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dev.coms4156.project.teamproject.FoodListing;

@Repository
public interface FoodListingRepository extends CrudRepository<FoodListing, Integer> {
  // Custom queries here
}