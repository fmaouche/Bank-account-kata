package fr.exteam.fmaouche.repository;

import fr.exteam.fmaouche.domain.Account;
import fr.exteam.fmaouche.domain.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//FIXME USE PAGINATIONS
public interface OperationRepository extends JpaRepository<Operation, Long> {

    List<Operation> findByAccount(Account account);
}
