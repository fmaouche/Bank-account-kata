package fr.exteam.fmaouche.domain;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Account implements Serializable{

    @NotNull
    @NotBlank
    @Size(min=1)
    @Id
    private String number;

    @NotNull
    @Min(0)
    private Long balance;

    @OneToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Operation> operations = new ArrayList<>();

    public Account() {
        super();
    }

    public Account(String number, Long balance, Customer customer) {
        this.number = number;
        this.balance = balance;
        this.customer = customer;
    }

    public String getNumber() {
        return number;
    }

    public Long getBalance() {
        return balance;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (number != null ? !number.equals(account.number) : account.number != null) return false;
        if (balance != null ? !balance.equals(account.balance) : account.balance != null) return false;
        if (customer != null ? !customer.equals(account.customer) : account.customer != null) return false;
        return operations != null ? operations.equals(account.operations) : account.operations == null;

    }

    @Override
    public int hashCode() {
        int result = number != null ? number.hashCode() : 0;
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (operations != null ? operations.hashCode() : 0);
        return result;
    }
}
