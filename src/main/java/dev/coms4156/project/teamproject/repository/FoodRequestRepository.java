package dev.coms4156.project.teamproject.repository;

import dev.coms4156.project.teamproject.model.FoodRequest;
import dev.coms4156.project.teamproject.model.FoodListing;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRequestRepository extends JpaRepository<FoodRequest, Integer> {
  public List<FoodListing> findByClient_ClientId(int clientId);
}
