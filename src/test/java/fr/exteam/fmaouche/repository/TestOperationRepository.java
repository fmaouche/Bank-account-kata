package fr.exteam.fmaouche.repository;

import fr.exteam.fmaouche.domain.Account;
import fr.exteam.fmaouche.domain.Customer;
import fr.exteam.fmaouche.domain.Operation;
import fr.exteam.fmaouche.domain.OperationType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TestOperationRepository {

    public static final String ACCOUNT_NUMBER = "XXX-XXXX-XXXX";

    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    private Customer customer;
    private Operation depositOperation;
    private Operation withdrawalOperation;
    private Account account;

    @Before
    public void setUp() {
        customer = new Customer("first name", "last name");
        account = new Account(ACCOUNT_NUMBER, 500L, customer);
        depositOperation = new Operation(50L, OperationType.DEPOSIT, account);
        withdrawalOperation = new Operation(300L, OperationType.WITHDRAWAL, account);

        testEntityManager.persist(account);
        testEntityManager.persist(customer);
        testEntityManager.persist(depositOperation);
        testEntityManager.persist(withdrawalOperation);
    }

    @Test
    public void testFindOperationsByAccount() {
        List<Operation> result = operationRepository.findByAccount(account);
        assertThat(result).isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .containsExactly(depositOperation, withdrawalOperation);
    }

}