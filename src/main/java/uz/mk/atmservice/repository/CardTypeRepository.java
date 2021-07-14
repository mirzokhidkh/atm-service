package uz.mk.atmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.mk.atmservice.entity.CardType;

@Repository
public interface CardTypeRepository extends JpaRepository<CardType, Integer> {
}
