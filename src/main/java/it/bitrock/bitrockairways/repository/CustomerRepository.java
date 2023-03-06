package it.bitrock.bitrockairways.repository;

import it.bitrock.bitrockairways.model.Customer;
import it.bitrock.bitrockairways.model.FidelityPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("from Customer c")
    List<Customer> getAllCustomers();

    @Query("from Customer c where c.birthday <= :birthDate")
    List<Customer> getCustomersWithBirthDateLessThanOrEqual(LocalDate birthDate);

    @Query("from Customer c where c.id in (select fp.customer.id from FidelityPoints fp)")
    List<Customer> getCustomersInFidelityProgram();

    @Query("FROM FidelityPoints fp WHERE fp.customer.id = :customerId")
    Optional<FidelityPoints> getCustomerFromFidelityProgram(long customerID);
}