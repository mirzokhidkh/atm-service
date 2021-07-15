package uz.mk.atmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.mk.atmservice.entity.AccountType;
import uz.mk.atmservice.entity.enums.AccountTypeName;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, Integer> {
    AccountType findByName(AccountTypeName name);
}
