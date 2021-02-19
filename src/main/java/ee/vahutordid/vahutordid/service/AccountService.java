package ee.vahutordid.vahutordid.service;

import ee.vahutordid.vahutordid.domain.Account;
import ee.vahutordid.vahutordid.domain.security.UserRole;

import java.math.BigDecimal;
import java.util.List;



public interface AccountService {
	
	Account findAdminAccount();
	
	Account createAccount(UserRole userRole, double initialBalance);

	List<Account> findAll();

	Account findOne(Long id);
	
	boolean deposit(Account account, BigDecimal amount);
	
	boolean withdraw(Account account, BigDecimal amount);

	boolean hasEnoughBalance(BigDecimal setScale);

}
