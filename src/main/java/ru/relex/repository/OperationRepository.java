package ru.relex.repository;

import ru.relex.entity.OperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<OperationEntity, Long> {
    OperationEntity findByDescription(String description);
}
