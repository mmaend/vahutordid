package ee.vahutordid.vahutordid.domain;

import ee.vahutordid.vahutordid.domain.security.UserRole;

import javax.persistence.*;
import java.util.List;



@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Address {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	
	private String receiverName;
	
	
	private String street1;
	private String street2;
	
	
	private String city;
	
	private String state;
	
	
	private String country="Greece";
	
	
	private String zipcode;
	
	
	private boolean userShippingDefault=false;
	
	@OneToMany(mappedBy="billingAddress", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<AbstractSale> billingSaleList;
	
	@OneToMany(mappedBy="shippingAddress", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<AbstractSale> shippingSaleList;
	
	@OneToOne(mappedBy="billingAddress", fetch=FetchType.LAZY)
	private CreditCard creditCard;
	
	@ManyToOne
	@JoinColumn(name="user_role_id")
	private UserRole userRole;
	
	
	
	public boolean isUserShippingDefault() {
		return userShippingDefault;
	}
	public void setUserShippingDefault(boolean userShippingDefault) {
		this.userShippingDefault = userShippingDefault;
	}
	public UserRole getUserRole() {
		return userRole;
	}
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
	
	public CreditCard getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}
	public List<AbstractSale> getBillingSaleList() {
		return billingSaleList;
	}
	public List<AbstractSale> getShippingSaleList() {
		return shippingSaleList;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getStreet1() {
		return street1;
	}
	public void setStreet1(String street1) {
		this.street1 = street1;
	}
	public String getStreet2() {
		return street2;
	}
	public void setStreet2(String street2) {
		this.street2 = street2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	

}
