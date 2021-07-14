package uz.mk.atmservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Bankomat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double maxMoney;

    private Double minMoney;

    private Double balance = 0.0;

    @ManyToOne(cascade = CascadeType.ALL)
    private CommissionSet commissionSet;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Address address;

    @ManyToOne
    private Bank bank;

    @ManyToMany
    private List<CardType> cardTypes;

    @ManyToMany
    private List<Banknote> banknotes;

    @CreatedBy
    private UUID createdBy;

    @LastModifiedBy
    private UUID updatedBy;


}
