package uz.mk.atmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.mk.atmservice.entity.CommissionSet;

@RepositoryRestResource(path = "commissionSet",collectionResourceRel = "list")
public interface CommissionSetRepository extends JpaRepository<CommissionSet, Integer> {
}
