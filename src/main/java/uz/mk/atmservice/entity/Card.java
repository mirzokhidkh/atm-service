package uz.mk.atmservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Card {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String number;

    @Column(nullable = false)
    private String cvvCode;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private Date validityPeriod;

    @Column(nullable = false)
    private String code;

    @ManyToOne
    private CardType cardType;

    @ManyToOne
    private Bank bank;

    private boolean status;
}
