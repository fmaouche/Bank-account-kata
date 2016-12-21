package fr.exteam.fmaouche.services;

import fr.exteam.fmaouche.domain.Account;
import fr.exteam.fmaouche.domain.Operation;
import fr.exteam.fmaouche.domain.OperationType;
import fr.exteam.fmaouche.exception.AccountNotFoundException;
import fr.exteam.fmaouche.exception.NegativeAmountException;
import fr.exteam.fmaouche.exception.InsufficientBalanceException;
import fr.exteam.fmaouche.repository.AccountRepository;
import fr.exteam.fmaouche.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OperationRepository operationRepository;

    private Optional<Account> findByNumber(String accountNumber) {
        return Optional.ofNullable(accountRepository.findByNumber(accountNumber));
    }

    @Transactional
    public Account updateSolde(Long amount, String accountNum, OperationType operationType) {
        Account account = findByNumber(accountNum)
                .orElseThrow(() -> new AccountNotFoundException("Account not found, please check your account numero"));
        if(amount < 0){
            throw new NegativeAmountException("Amount can't be negative");
        }
        Operation operation = new Operation(amount, operationType, account);
        operation = operationRepository.save(operation);
        account.getOperations().add(operation);
        switch (operationType) {
            case DEPOSIT:
                account.setBalance(account.getBalance() + amount);
                return accountRepository.save(account);
            case WITHDRAWAL:
                if(amount > account.getBalance()) {
                    throw new InsufficientBalanceException("Insufficient balance");
                }
                account.setBalance(account.getBalance() - amount);
                return accountRepository.save(account);
            default:
                throw new IllegalArgumentException("Unknown operation: "+operationType);
        }
    }
}
