package fr.exteam.fmaouche.web;

import fr.exteam.fmaouche.domain.Account;
import fr.exteam.fmaouche.domain.Operation;
import fr.exteam.fmaouche.domain.OperationType;
import fr.exteam.fmaouche.services.AccountService;
import fr.exteam.fmaouche.services.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/accounts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private OperationService operationService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(method = RequestMethod.PUT, value="/{accountNumber}/{operationType}")
    public Account update(@PathVariable String accountNumber,
                          @PathVariable OperationType operationType,
                          @RequestBody Long amount) {
        return accountService.updateSolde(amount, accountNumber, operationType);
    }

    @RequestMapping(method = RequestMethod.GET, value="/operations/{accountNumber}")
    public List<Operation> getAccountHistory(@PathVariable String accountNumber) {
        return operationService.getOperations(accountNumber);
    }
}
