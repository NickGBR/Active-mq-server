package com.ngbr.server.service.impl;


import com.ngbr.server.consts.status.OperationStatus;
import com.ngbr.server.dao.Account;
import com.ngbr.server.dto.BankResponse;
import com.ngbr.server.dto.impl.BankResponseImpl;
import com.ngbr.server.repository.AccountDataRepository;
import com.ngbr.server.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private AccountDataRepository dataRepository;
    private JmsTemplate jmsTemplate;

    @Autowired

    public AccountServiceImpl(AccountDataRepository dataRepository, JmsTemplate jmsTemplate) {
        this.dataRepository = dataRepository;
        this.jmsTemplate = jmsTemplate;
    }

    @Transactional
    @Override
    public BankResponse withdraw(Long id, Double amount) {
        if (dataRepository.existsById(id)) {
            final Account account = dataRepository.getById(id);
            if ((account.getAmount() - amount) >= 0) {
                log.info(amount.toString());
                account.setAmount(account.getAmount() - amount);
                dataRepository.save(account);
                return BankResponseImpl.builder()
                        .deposit(account.getAmount())
                        .operationStatus(OperationStatus.WITHDRAW_SUCCESS)
                        .build();
            } else return BankResponseImpl.builder()
                    .operationStatus(OperationStatus.NOT_ENOUGH_MONEY)
                    .build();
        }
        List<Long> notValidIds = new ArrayList<>();
        notValidIds.add(id);
        return BankResponseImpl.builder()
                .notValidIds(notValidIds)
                .operationStatus(OperationStatus.NOT_EXITING_ID)
                .build();
    }

    @Transactional
    @Override
    public BankResponse deposit(Long id, Double amount) {
        if (dataRepository.existsById(id)) {
            final Account account = dataRepository.getById(id);
            log.info(amount.toString());
            account.setAmount(account.getAmount() + amount);
            dataRepository.save(account);
            return BankResponseImpl.builder()
                    .operationStatus(OperationStatus.DEPOSIT_SUCCESS)
                    .deposit(account.getAmount())
                    .build();
        }
        List<Long> notValidIds = new ArrayList<>();
        notValidIds.add(id);
        return BankResponseImpl.builder()
                .notValidIds(notValidIds)
                .operationStatus(OperationStatus.NOT_EXITING_ID)
                .build();
    }

    @Transactional
    @Override
    public BankResponse transferTo(Long idFrom, Long idTo, Double amount) {
        List<Long> notValidIds = new ArrayList<>();
        if(!dataRepository.existsById(idFrom)) notValidIds.add(idFrom);
        if(!dataRepository.existsById(idTo)) notValidIds.add(idTo);
        if (dataRepository.existsById(idFrom) && dataRepository.existsById(idTo)) {
                final BankResponse withdrawResult = withdraw(idFrom, amount);
                if (withdrawResult.getOperationStatus().equals(OperationStatus.WITHDRAW_SUCCESS)) {
                    deposit(idTo, amount);
                    return BankResponseImpl.builder()
                            .operationStatus(OperationStatus.TRANSFER_SUCCESS)
                            .deposit(dataRepository.getById(idFrom).getAmount())
                            .build();
                } else return withdrawResult;

        }
        return BankResponseImpl.builder()
                .notValidIds(notValidIds)
                .operationStatus(OperationStatus.NOT_EXITING_ID)
                .build();
    }


    @Transactional
    @Override
    public BankResponse createAccount(Long id) {
        if (!dataRepository.existsById(id)) {
            Account account = new Account();
            account.setId(id);
            account.setAmount(0.0);
            dataRepository.save(account);
            return BankResponseImpl.builder()
                    .operationStatus(OperationStatus.CREATE_SUCCESS)
                    .deposit(account.getAmount())
                    .build();
        }
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        return BankResponseImpl.builder()
                .notValidIds(ids)
                .operationStatus(OperationStatus.EXITING_ID)
                .build();
    }

    @Transactional
    @Override
    public BankResponse closeAccount(Long id) {
        if (dataRepository.existsById(id)) {
            Account account = dataRepository.getById(id);
            if (account.getAmount() == 0) {
                dataRepository.deleteById(id);
                return BankResponseImpl.builder()
                        .operationStatus(OperationStatus.CLOSE_SUCCESS)
                        .build();
            } else return BankResponseImpl.builder()
                    .operationStatus(OperationStatus.CLOSE_ERROR)
                    .deposit(account.getAmount())
                    .build();
        }
        List<Long> notValidIds = new ArrayList<>();
        notValidIds.add(id);
        return BankResponseImpl.builder()
                .operationStatus(OperationStatus.NOT_EXITING_ID)
                .notValidIds(notValidIds)
                .build();
    }
}
