package uz.mk.atmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.mk.atmservice.entity.CardType;

import java.util.Collection;
import java.util.List;

@Repository
public interface CardTypeRepository extends JpaRepository<CardType, Integer> {
    List<CardType> findAllByIdIn(Collection<Integer> id);
}
