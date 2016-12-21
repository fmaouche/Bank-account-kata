package fr.exteam.fmaouche.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Operation  implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private Date date;

    private Long amount;

    @Enumerated(value=EnumType.STRING)
    private OperationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Account account;

    public Operation(){
        super();
    }

    public Operation(Long amount, OperationType type, Account account) {
        this.amount = amount;
        this.type = type;
        this.account = account;
        this.date = new Date();
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Long getAmount() {
        return amount;
    }

    public OperationType getType() {
        return type;
    }

    public Account getAccount() {
        return account;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Operation operation = (Operation) o;

        if (id != null ? !id.equals(operation.id) : operation.id != null) return false;
        if (date != null ? !date.equals(operation.date) : operation.date != null) return false;
        if (amount != null ? !amount.equals(operation.amount) : operation.amount != null) return false;
        if (type != operation.type) return false;
        return account != null ? account.equals(operation.account) : operation.account == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (account != null ? account.hashCode() : 0);
        return result;
    }
}
