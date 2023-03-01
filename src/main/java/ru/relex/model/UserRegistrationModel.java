package ru.relex.model;

import ru.relex.dto.UserRegistrationDto;
import ru.relex.entity.Role;
import ru.relex.entity.UserEntity;
import lombok.Data;

import java.util.Set;

@Data
public class UserRegistrationModel {
    private String username;
    private String email;
    private String password;

    public static UserRegistrationModel fromDtoToModel(UserRegistrationDto dto) {
        UserRegistrationModel model = new UserRegistrationModel();
        model.setUsername(dto.getUsername());
        model.setEmail(dto.getEmail());

        return model;
    }

    public static UserEntity fromModelToEntity(UserRegistrationModel model) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(model.getUsername());
        userEntity.setEmail(model.getEmail());
        userEntity.setPassword(model.getPassword());
        userEntity.setRoles(Set.of(Role.USER));

        return userEntity;
    }
}
