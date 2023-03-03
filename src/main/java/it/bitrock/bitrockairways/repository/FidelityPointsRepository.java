package it.bitrock.bitrockairways.repository;

import it.bitrock.bitrockairways.model.FidelityPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FidelityPointsRepository extends JpaRepository<FidelityPoints, Long> {

    void updateFidelityPoints(FidelityPoints fidelityPoints);

}