package ru.relex.service;

import java.security.Principal;
import java.util.UUID;

public interface WalletService {
    UUID createWallet(Principal principal);
}
