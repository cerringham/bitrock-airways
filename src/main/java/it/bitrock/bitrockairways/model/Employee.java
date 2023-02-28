package it.bitrock.bitrockairways.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable=false, length=50)
    private String name;

    @Column(name = "surname", nullable=false, length=50)
    private String surname;

    @Column(name = "email", nullable=false, length=320)
    private String email;

    @Column(name = "phone", nullable=false, length=50)
    private String phone;

    @Column(name = "hiring_date", nullable=false)
    private LocalDate hiringDate;

    @Builder.Default
    @Column(name = "active", nullable=false)
    private Boolean active = true;

    @Column(name = "date_inactivated")
    private ZonedDateTime dateInactivated;

    @ManyToOne
    @JoinColumn(name="site_id", nullable=false)
    private Site site;

    @ManyToOne
    @JoinColumn(name="role_id", nullable=false)
    private Role role;
}
