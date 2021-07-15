package uz.mk.atmservice.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ReplenishAtmHistory {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Banknote banknote;

    @Transient
    private Double summa;

    private Integer amount;

    @ManyToOne
    private User staff;

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
