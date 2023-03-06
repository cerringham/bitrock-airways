package it.bitrock.bitrockairways.repository;

import it.bitrock.bitrockairways.model.FidelityPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FidelityPointsRepository extends JpaRepository<FidelityPoints, Long> {

    @Query("UPDATE FidelityPoints fp SET fp.points = :points WHERE fp.id = :id")
    void updateFidelityPoints(Long id, Integer points);

}