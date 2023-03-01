package ru.relex.repository.dao;

import ru.relex.entity.HistoryOperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface HistoryOperationDao extends JpaRepository<HistoryOperationEntity, Long> {
    @Query(value = "select h_o.date from HistoryOperationEntity h_o")
    List<Timestamp> findDates();
}
