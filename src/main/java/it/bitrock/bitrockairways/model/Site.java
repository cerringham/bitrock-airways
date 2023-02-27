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

import java.time.LocalDateTime;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "site")
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable=false, length=50)
    private String name;

    @Column(name = "street", nullable=false, length=50)
    private String street;

    @Column(name = "city", nullable=false, length=50)
    private String city;

    @Column(name = "county", nullable=false, length=50)
    private String county;

    @Column(name = "nation", nullable=false, length=50)
    private String nation;

    @Column(name = "is_headquarter", nullable=false)
    private Boolean isHeadquarter;

    @Column(name = "active", nullable=false)
    private Boolean active = true;

    @Column(name = "date_inactivated")
    private LocalDateTime dateInactivated;

    @ManyToOne
    @JoinColumn(name="company_id", nullable=false)
    private Company company;
}
