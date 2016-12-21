package fr.exteam.fmaouche.services;

import fr.exteam.fmaouche.BankAccountKataApplicationTests;
import fr.exteam.fmaouche.domain.Account;
import fr.exteam.fmaouche.domain.Customer;
import fr.exteam.fmaouche.domain.OperationType;
import fr.exteam.fmaouche.exception.AccountNotFoundException;
import fr.exteam.fmaouche.exception.InsufficientBalanceException;
import fr.exteam.fmaouche.exception.NegativeAmountException;
import fr.exteam.fmaouche.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankAccountKataApplicationTests.class)
@WebAppConfiguration
@EnableWebMvc
@DataJpaTest
public class TestAccountService {

    private static final String ACCOUNT_NUMBER = "XXX-XXXX-XXXX";

    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    private Customer custom;

    @Before
    public void setUp(){
        testEntityManager.clear();
        custom = new Customer("first name", "last name");
        custom = testEntityManager.persist(custom);
    }

    @Test
    public void testDeposit() {
        Account account = createAndSave();
        assertEquals(account, accountRepository.findByNumber(ACCOUNT_NUMBER));

        Long amount = 100L;
        Account result = accountService.updateSolde(amount, ACCOUNT_NUMBER, OperationType.DEPOSIT);
        account = accountRepository.findByNumber(ACCOUNT_NUMBER);
        assertEquals(account, result);

        assertEquals((Long)1100L, result.getBalance());
    }

    @Test
    public void testWithdrawal() {
        Account account = createAndSave();
        assertEquals(account, accountRepository.findByNumber(ACCOUNT_NUMBER));

        Long amount = 100L;
        Account result = accountService.updateSolde(amount, ACCOUNT_NUMBER, OperationType.WITHDRAWAL);
        account = accountRepository.findByNumber(ACCOUNT_NUMBER);
        assertEquals(account, result);

        assertEquals((Long)900L, result.getBalance());
    }

    @Test
    public void testCaseAccountNotFoundException() {
        assertThatThrownBy(() -> accountService.updateSolde(500L, "BAD_ACCOUNT_NUMBER", OperationType.DEPOSIT))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    public void testCaseWhenInsufficientBalance() {
        Account account = createAndSave();
        assertEquals(account, accountRepository.findByNumber(ACCOUNT_NUMBER));
        assertThatThrownBy(() -> accountService.updateSolde(2000L, ACCOUNT_NUMBER, OperationType.WITHDRAWAL))
                .isInstanceOf(InsufficientBalanceException.class);
    }

    @Test
    public void testCaseWhenNegativeAmount() {
        Account account = createAndSave();
        assertEquals(account, accountRepository.findByNumber(ACCOUNT_NUMBER));
        assertThatThrownBy(() -> accountService.updateSolde(-10L, ACCOUNT_NUMBER, OperationType.WITHDRAWAL))
                .isInstanceOf(NegativeAmountException.class);
    }

    private Account createAndSave(){
        Account account = new Account(ACCOUNT_NUMBER, 1000L, custom);
        return accountRepository.save(account);
    }
}
