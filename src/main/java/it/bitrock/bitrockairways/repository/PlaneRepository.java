package it.bitrock.bitrockairways.repository;

import it.bitrock.bitrockairways.model.Plane;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaneRepository extends JpaRepository<Plane, Long> {
    Optional<Plane> findByModel(String model);
}
