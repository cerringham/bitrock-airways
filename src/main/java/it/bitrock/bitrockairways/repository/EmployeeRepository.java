package it.bitrock.bitrockairways.repository;

import it.bitrock.bitrockairways.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("FROM Employee e WHERE e.role = 2")
    public List<Employee> findBoardOfDirectors();

    @Query("FROM Employee e JOIN e.role role WHERE role.name = :role")
    public List<Employee> findByRole(String role);

    @Query("FROM Employee e WHERE e.name LIKE %?1% AND e.surname LIKE %?2%")
    public List<Employee> findByNameSurnameContaining(String name, String surname);

    @Query("FROM Employee e WHERE e.name LIKE %?1%")
    public List<Employee> findByNameContaining(String name);

    @Query("FROM Employee e WHERE e.surname LIKE %?1%")
    public List<Employee> findBySurnameContaining(String surname);

}
