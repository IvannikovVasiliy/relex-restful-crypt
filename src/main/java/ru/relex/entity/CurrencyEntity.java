package ru.relex.entity;

import ru.relex.valid.constraint.AmountConstraint;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "currencies")
public class CurrencyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @AmountConstraint
    private Double value;
}
