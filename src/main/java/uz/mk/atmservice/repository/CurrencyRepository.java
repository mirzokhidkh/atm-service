package uz.mk.atmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.mk.atmservice.entity.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
}
