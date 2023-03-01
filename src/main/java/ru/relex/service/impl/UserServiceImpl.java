package ru.relex.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.relex.dto.LoginRequestDto;
import ru.relex.dto.UserRegistrationDto;
import ru.relex.entity.UserEntity;
import ru.relex.exception.ResourceAlreadyExistsException;
import ru.relex.exception.ResourceNotFoundException;
import ru.relex.jwt.JwtTokenProvider;
import ru.relex.model.UserRegistrationModel;
import ru.relex.repository.UserRepository;
import ru.relex.service.UserService;

@Service
@RequiredArgsConstructor
@Validated
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final Logger logger;

    @Transactional
    @Validated
    public String addUser(UserRegistrationDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new ResourceAlreadyExistsException(
                    String.format("User with name \"%s\" already exists", userDto.getUsername())
            );
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ResourceAlreadyExistsException(
                    String.format("User with email \"%s\" already exists", userDto.getEmail())
            );
        }

        UserRegistrationModel userRegistrationModel = UserRegistrationModel.fromDtoToModel(userDto);
        userRegistrationModel.setPassword(passwordEncoder.encode(userDto.getPassword()));
        UserEntity userEntity = UserRegistrationModel.fromModelToEntity(userRegistrationModel);

        logger.info("User with login \"{}\" is REGISTRATED", userDto.getUsername());
        userRepository.save(userEntity);

        return userEntity.getUsername();
    }

    public String getJwtToken(LoginRequestDto loginRequestDto) {
        logger.warn("Try to login the user \"{}\"", loginRequestDto.getUsername());
        UserEntity userEntity = userRepository.findByUsername(loginRequestDto.getUsername());
        if (userEntity == null) {
            throw new ResourceNotFoundException(String.format(
                    "User with name \"%s\" does not exist",
                    loginRequestDto.getUsername())
            );
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
        );

        logger.info("The user \"{}\" was authenticated", loginRequestDto.getUsername());

        return jwtTokenProvider.createToken(userEntity.getUsername(), userEntity.getRoles());
    }
}