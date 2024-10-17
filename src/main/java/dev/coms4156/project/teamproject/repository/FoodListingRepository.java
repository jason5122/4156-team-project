package dev.coms4156.project.teamproject.repository;

import dev.coms4156.project.teamproject.model.AccountProfile;
import dev.coms4156.project.teamproject.model.ClientProfile;
import dev.coms4156.project.teamproject.model.FoodListing;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodListingRepository extends JpaRepository<FoodListing, Integer> {
    public List<FoodListing> findByClient(ClientProfile client);
    public List<FoodListing> findByClientAndAccount(ClientProfile client, AccountProfile account);
    public Optional<FoodListing> findByClientAndAccountAndListingId(ClientProfile client,
                                                                    AccountProfile account,
                                                                    int listingId);
}
