package fr.exteam.fmaouche.repository;

import fr.exteam.fmaouche.domain.Account;
import fr.exteam.fmaouche.domain.Customer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TestAccountRepository {

    public static final String ACCOUNT_NUMBER = "XXX-XXXX-XXXX";
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TestEntityManager testEntityManager;
    private Account expectedAccount;
    private Customer customer;

    @Before
    public void setUp() {
        customer = new Customer("first name", "last name");
        expectedAccount = new Account(ACCOUNT_NUMBER,200L, customer);
        testEntityManager.clear();
        testEntityManager.persist(customer);
        testEntityManager.persist(expectedAccount);
    }

    @Test
    public void testFindAccountByNumber() {
        Account resulting = accountRepository.findByNumber(ACCOUNT_NUMBER);
        assertEquals(expectedAccount, resulting);
    }

    @Test
    public void testBalanceUpdated() throws Exception {
        String query = "SELECT OBJECT(account) FROM Account account WHERE number = '"+ACCOUNT_NUMBER+"' ";
        Account actual = (Account) testEntityManager.getEntityManager().createQuery(query).getSingleResult();
        assertEquals(expectedAccount.getBalance(), actual.getBalance());

        Long newBalance = 60L;
        accountRepository.saveAndFlush(new Account(ACCOUNT_NUMBER, newBalance, customer));

        actual = (Account) testEntityManager.getEntityManager()
                .createQuery(query)
                .getSingleResult();
        assertEquals(newBalance, actual.getBalance());
    }
}
