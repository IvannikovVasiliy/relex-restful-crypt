package ru.relex.repository;

import ru.relex.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<CardEntity, Long> {

}
