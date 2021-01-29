package com.ngbr.server.service;

import com.ngbr.server.dto.BankResponse;

public interface AccountService {
    BankResponse withdraw(Long id, Double amount);

    BankResponse deposit(Long id, Double amount);

    BankResponse transferTo(Long idFrom, Long idTo, Double amount);

    BankResponse createAccount(Long id);

    BankResponse closeAccount(Long id);
}
