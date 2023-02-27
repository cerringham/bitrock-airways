package it.bitrock.bitrockairways.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable=false, length=50)
    private String name;

    @Column(name = "surname", nullable=false, length=50)
    private String surname;

    @Column(name = "gender", nullable=false, length=10)
    private String gender;

    @Column(name = "email", nullable=false, length=320)
    private String email;

    @Column(name = "phone", nullable=false, length=50)
    private String phone;

    @Column(name = "birthday", nullable=false)
    private LocalDate birthday;

    @Column(name = "handicap", nullable=false)
    private Boolean handicap;

    @Column(name = "passport_number", length=15)
    private String passportNumber;

    @Column(name = "id_number", length=15)
    private String idNumber;

    @Column(name = "active", nullable=false)
    private Boolean active = true;

    @Column(name = "date_inactivated")
    private LocalDateTime dateInactivated;
}
