package uz.mk.atmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.mk.atmservice.entity.Role;
import uz.mk.atmservice.entity.enums.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(RoleName name);
}
