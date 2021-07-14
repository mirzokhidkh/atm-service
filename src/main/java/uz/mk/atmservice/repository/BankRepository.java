package uz.mk.atmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.mk.atmservice.entity.Bank;

@Repository
public interface BankRepository extends JpaRepository<Bank, Integer> {
    Bank findByName(String name);
}
