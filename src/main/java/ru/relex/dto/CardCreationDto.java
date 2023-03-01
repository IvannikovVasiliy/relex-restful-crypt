package ru.relex.dto;

import ru.relex.entity.CardEntity;

public class CardCreationDto {
    public Long id;
    public String username;

    public static CardCreationDto toDto(CardEntity cardEntity) {
        CardCreationDto dto = new CardCreationDto();
        dto.id = cardEntity.getId();
        dto.username = cardEntity.getUser().getUsername();
        return dto;
    }
}