package uz.mk.atmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.mk.atmservice.entity.CommissionSet;
import uz.mk.atmservice.entity.Role;

@Repository
public interface CommissionSetRepository extends JpaRepository<CommissionSet, Integer> {
}
