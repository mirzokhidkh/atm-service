package uz.mk.atmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.mk.atmservice.entity.Address;
import uz.mk.atmservice.entity.Role;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
}
