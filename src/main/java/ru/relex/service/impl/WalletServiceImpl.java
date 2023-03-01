package ru.relex.service.impl;

import org.slf4j.Logger;
import ru.relex.entity.UserEntity;
import ru.relex.entity.WalletEntity;
import ru.relex.exception.ResourceAlreadyExistsException;
import ru.relex.repository.UserRepository;
import ru.relex.repository.WalletRepository;
import ru.relex.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final Logger logger;

    @Transactional
    public UUID createWallet(Principal principal) {
        UserEntity userEntity = userRepository.findByUsername(principal.getName());
        if (userEntity.getWallet() == null) {
            WalletEntity walletEntity = new WalletEntity();
            walletEntity.setUser(userEntity);
            walletRepository.save(walletEntity);
            logger.info("The wallet {} was cretaed", walletEntity.getId());

            return walletEntity.getId();
        } else {
            throw new ResourceAlreadyExistsException(
                    String.format("The wallet for user \"%s\" is already exists", userEntity.getUsername())
            );
        }
    }
}
