package uz.mk.atmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import uz.mk.atmservice.entity.Banknote;
import uz.mk.atmservice.entity.Bankomat;

@RepositoryRestResource(path = "bankomat",collectionResourceRel = "list")
public interface BankomatRepository extends JpaRepository<Bankomat, Integer> {
}
