package fr.exteam.fmaouche.web;

import com.google.gson.Gson;
import fr.exteam.fmaouche.BankAccountKataApplicationTests;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by fmaouche on 20/12/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankAccountKataApplicationTests.class)
@WebAppConfiguration
@EnableWebMvc
@DataJpaTest
@Transactional
public class TestAccountController {

    private static final String ACCOUNT_NUMBER = "XXX-XXXX-XXXX";

    private MockMvc mockMvc;
    @Autowired protected WebApplicationContext webApplicationContext;

    @Autowired
    private TestEntityManager testEntityManager;

    private Account account;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        Customer custom = testEntityManager.persist(new Customer("first name", "last name"));
        account = testEntityManager.persist(new Account(ACCOUNT_NUMBER, 50L, custom));
    }

    @Test
    public void testDeposit() throws Exception {
        Long amount = 50L;
        mockMvc.perform(put("/accounts/"+ACCOUNT_NUMBER+"/"+OperationType.DEPOSIT.name())
                .content(toJson(50L))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", is(account.getNumber())))
                .andExpect(jsonPath("$.balance", is(account.getBalance().intValue())))
                .andExpect(jsonPath("$.operations[0].amount", is(amount.intValue())))
                .andExpect(jsonPath("$.operations[0].type", is(OperationType.DEPOSIT.name())));
    }

    @Test
    public void testWithdrawal() throws Exception {
        Long amount = 50L;
        mockMvc.perform(put("/accounts/"+ACCOUNT_NUMBER+"/"+OperationType.WITHDRAWAL.name())
                .content(toJson(50L))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", is(account.getNumber())))
                .andExpect(jsonPath("$.balance", is(account.getBalance().intValue())))
                .andExpect(jsonPath("$.operations[0].amount", is(amount.intValue())))
                .andExpect(jsonPath("$.operations[0].type", is(OperationType.WITHDRAWAL.name())));
    }

    @Test
    public void testGetOperationsHistory() throws Exception {
        Operation withdrawalOperation = testEntityManager.persist(new Operation(50L, OperationType.WITHDRAWAL, account));
        Thread.sleep(100L);
        Operation depositOperation = testEntityManager.persist(new Operation(50L, OperationType.DEPOSIT, account));

        mockMvc.perform(get("/accounts/operations/"+ACCOUNT_NUMBER)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(withdrawalOperation.getId().intValue())))
                .andExpect(jsonPath("$[0].amount", is(withdrawalOperation.getAmount().intValue())))
                .andExpect(jsonPath("$[0].type", is(withdrawalOperation.getType().name())))

                .andExpect(jsonPath("$[1].id", is(depositOperation.getId().intValue())))
                .andExpect(jsonPath("$[1].amount", is(depositOperation.getAmount().intValue())))
                .andExpect(jsonPath("$[1].type", is(depositOperation.getType().name())));
    }

    private String toJson(Object object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}
