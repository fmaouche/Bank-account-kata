package fr.exteam.fmaouche.services;

import fr.exteam.fmaouche.BankAccountKataApplicationTests;
import fr.exteam.fmaouche.domain.Account;
import fr.exteam.fmaouche.domain.Customer;
import fr.exteam.fmaouche.domain.Operation;
import fr.exteam.fmaouche.domain.OperationType;
import fr.exteam.fmaouche.repository.OperationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankAccountKataApplicationTests.class)
@WebAppConfiguration
@EnableWebMvc
@DataJpaTest
public class TestOperationService {

    public static final String ACCOUNT_NUMBER = "XXX-XXXX-XXXX";
    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private OperationService operationService;

    private Operation depositOperation;
    private Operation withdrawalOperation;

    @Before
    public void setUp() {
        Customer custom = testEntityManager.persist(new Customer("first name", "last name"));
        Account account = testEntityManager.persist(new Account(ACCOUNT_NUMBER, 500L, custom));
        depositOperation = operationRepository.save(new Operation(50L, OperationType.DEPOSIT, account));
        withdrawalOperation = operationRepository.save(new Operation(300L, OperationType.WITHDRAWAL, account));
    }

    @Test
    public void testGetOperations() throws Exception {
        List<Operation> operations = operationService.getOperations(ACCOUNT_NUMBER);
        assertThat(operations).isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .containsExactly(depositOperation, withdrawalOperation);
    }
}