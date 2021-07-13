package uz.mk.atmservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Bankomat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double maxMoney;

    private Double minMoney;

    @ManyToOne
    private CommissionSet commissionSet;

    @OneToOne
    private Address address;

    @ManyToOne
    private Bank bank;

    @ManyToMany
    private List<CardType> cardTypes;

    @ManyToMany
    private List<Banknote> banknotes;


}
