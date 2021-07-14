package uz.mk.atmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.mk.atmservice.entity.ReplenishAtmHistory;

import java.util.UUID;

@Repository
public interface ReplenishAtmHistoryRepository extends JpaRepository<ReplenishAtmHistory, UUID> {
}
