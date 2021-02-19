package ee.vahutordid.vahutordid.utility;


import ee.vahutordid.vahutordid.domain.Address;

public class AddressCheckoutFormWrapper {
	
	
	
	private Address shippingAddress;
	private Address billingAddress;
	
	public AddressCheckoutFormWrapper() {
		shippingAddress = new Address();
		billingAddress = new Address();
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

}
