package uz.mk.atmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.mk.atmservice.entity.Banknote;

@Repository
public interface BanknoteRepository extends JpaRepository<Banknote, Integer> {
}
