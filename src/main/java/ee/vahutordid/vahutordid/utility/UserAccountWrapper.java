package ee.vahutordid.vahutordid.utility;

import ee.vahutordid.vahutordid.domain.Account;
import ee.vahutordid.vahutordid.domain.User;

import java.math.BigDecimal;


public class UserAccountWrapper {

	private String username;
	
	private String firstName;
	private String lastName;
	
	private Long accountId;
	
	private Long userId;

	private BigDecimal balance;

	public UserAccountWrapper(User user, Account account) {
		this.username = user.getUsername();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.accountId = account.getId();
		this.balance = account.getBalance();
		this.userId = user.getId();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
}
