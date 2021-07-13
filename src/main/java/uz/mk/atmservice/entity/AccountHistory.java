package uz.mk.atmservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import uz.mk.atmservice.entity.enums.AccountTypeName;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AccountHistory {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Banknote banknote;

    @Transient
    private Double summa;

    private Integer amount;

    @ManyToOne
    private Card card;

    @ManyToOne
    private AccountType accountType;

    @ManyToOne
    private Bankomat bankomat;

    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private Timestamp date;

    public Double getSumma() {
        Integer value = banknote.getValue();
        summa = Double.valueOf(amount * value);
        return summa;
    }
}
