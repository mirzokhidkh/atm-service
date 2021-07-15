package uz.mk.atmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import uz.mk.atmservice.entity.Bankomat;
import uz.mk.atmservice.entity.BankomatSet;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BankomatSetRepository extends JpaRepository<BankomatSet, Integer> {
    boolean existsByBanknoteId(Integer banknote_id);

    Set<BankomatSet> findByBankomatId(Integer bankomat_id);

    Optional<BankomatSet> findByBanknoteId(Integer banknote_id);

    List<BankomatSet> findAllByBankomatId(Integer bankomat_id);
}
