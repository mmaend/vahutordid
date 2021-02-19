package ee.vahutordid.vahutordid.service;


import ee.vahutordid.vahutordid.domain.Address;
import ee.vahutordid.vahutordid.domain.security.UserRole;

public interface AddressService {
	
	Address createAddress(Address address);

	Address findById(Long shippingAddressId);

	void setDefaultShippingAddress(Long defaultShippingAddressId, UserRole userRole);

	void removeFromUserRole(Long shippingAddressId, UserRole userRole);

	Address deepCopyAddress(Address ad, Address currentShippingAddress);

	void save(Address address);
	

}
