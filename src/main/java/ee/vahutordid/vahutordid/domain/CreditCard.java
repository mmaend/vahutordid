package ee.vahutordid.vahutordid.domain;

import ee.vahutordid.vahutordid.domain.security.UserRole;

import javax.persistence.*;
import java.util.List;



@Entity
public class CreditCard {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(nullable=false)
	private String type;
	
	@Column(nullable=false)
	private String cardNumber;
	
	@Column(nullable=false)
	private int expiryMonth;
	
	@Column(nullable=false)
	private int expiryYear;
	
	@Column(nullable=false)
	private int cvc;
	
	@Column(nullable=false)
	private String holderName;
	
	@Column(nullable=false)
	private boolean defaultCreditCard;
	
	@OneToMany(mappedBy="creditCard", fetch=FetchType.LAZY)
	private List<AbstractSale> abstractSaleList;
	
	@OneToOne(fetch=FetchType.EAGER)
	private Address billingAddress;
	
	@ManyToOne( fetch=FetchType.EAGER)
	@JoinColumn(name="user_role_id")
	private UserRole userRole;

	public boolean isDefaultCreditCard() {
		return defaultCreditCard;
	}
	public void setDefaultCreditCard(boolean defaultCreditCard) {
		this.defaultCreditCard = defaultCreditCard;
	}
	public Address getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}
	public UserRole getUserRole() {
		return userRole;
	}
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
	public List<AbstractSale> getAbstractSaleList() {
		return abstractSaleList;
	}
	public void setAbstractSaleList(List<AbstractSale> abstractSaleList) {
		this.abstractSaleList = abstractSaleList;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public int getExpiryMonth() {
		return expiryMonth;
	}
	public void setExpiryMonth(int expiryMonth) {
		this.expiryMonth = expiryMonth;
	}
	public int getExpiryYear() {
		return expiryYear;
	}
	public void setExpiryYear(int expiryYear) {
		this.expiryYear = expiryYear;
	}
	public int getCvc() {
		return cvc;
	}
	public void setCvc(int cvc) {
		this.cvc = cvc;
	}
	public String getHolderName() {
		return holderName;
	}
	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}
}
