package uz.mk.atmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.mk.atmservice.entity.Banknote;
import uz.mk.atmservice.entity.Bankomat;

@Repository
public interface BankomatRepository extends JpaRepository<Bankomat, Integer> {
}
