package ee.vahutordid.vahutordid.repository;

import ee.vahutordid.vahutordid.domain.Account;
import ee.vahutordid.vahutordid.domain.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;



public interface TransactionRepository extends CrudRepository<Transaction, Long> {

	List<Transaction> findByWithdrawAccountAndDateTimeBetween(Account withdrawAccount, Timestamp from, Timestamp to);
	
	List<Transaction> findByDepositAccountAndDateTimeBetween(Account depositAccount, Timestamp from, Timestamp to);
	
}
