package ee.vahutordid.vahutordid.service;

import ee.vahutordid.vahutordid.domain.AbstractSale;
import ee.vahutordid.vahutordid.domain.Account;
import ee.vahutordid.vahutordid.domain.Transaction;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;



public interface TransactionService {

	List<Transaction> fetchAccountWithdrawsByPeriod(Account account, Timestamp from, Timestamp to);

	List<Transaction> fetchAccountDepositsByPeriod(Account account, Timestamp from, Timestamp to);
	
	Transaction oneWayTransaction(BigDecimal amount, AbstractSale abstractSale);
	
	Transaction twoWayTransaction(BigDecimal amount, Account fromAccount, Account toAccount, AbstractSale abstractSale);

}
