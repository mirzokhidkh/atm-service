package uz.mk.atmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.mk.atmservice.entity.Card;

import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(path = "card",collectionResourceRel = "list")
public interface CardRepository extends JpaRepository<Card, UUID> {
    Optional<Card> findByNumber(String number);
}
