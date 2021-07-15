package uz.mk.atmservice.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class BankomatSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Banknote banknote;

    @Column(nullable = false)
    private Integer amount = 0;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    private Bankomat bankomat;

    @Transient
    private Double summa;

    public Double getSumma() {
        summa = Double.valueOf(amount * banknote.getValue());
        return summa;
    }


}
