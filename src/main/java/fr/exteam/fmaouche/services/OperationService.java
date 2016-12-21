package fr.exteam.fmaouche.services;

import fr.exteam.fmaouche.domain.Account;
import fr.exteam.fmaouche.domain.Operation;
import fr.exteam.fmaouche.repository.AccountRepository;
import fr.exteam.fmaouche.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationService {

    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private AccountRepository accountRepository;

    public List<Operation> getOperations(String accountNumber) {
        Account account = accountRepository.findByNumber(accountNumber);
        return operationRepository.findByAccount(account);
    }
}
