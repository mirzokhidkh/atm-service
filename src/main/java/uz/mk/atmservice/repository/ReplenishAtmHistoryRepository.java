package uz.mk.atmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.mk.atmservice.entity.ReplenishAtmHistory;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReplenishAtmHistoryRepository extends JpaRepository<ReplenishAtmHistory, UUID> {
    List<ReplenishAtmHistory> findAllByBankomatId(Integer bankomat_id);
}
