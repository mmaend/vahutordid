package ee.vahutordid.vahutordid.service;

import ee.vahutordid.vahutordid.domain.Address;
import ee.vahutordid.vahutordid.domain.CreditCard;
import ee.vahutordid.vahutordid.domain.User;
import ee.vahutordid.vahutordid.domain.security.UserRole;

import java.util.List;



public interface UserRoleService {

	boolean hasThisRole(String roleName, User user);

	List<User> fetchUsersOfRole(String roleName);
	
	boolean createUserRole(UserRole userRole);

	UserRole findByUserAndRole(User user, String roleType);

	void updateBillingAddress(Address billingAddress, CreditCard creditCard, UserRole userRole);

	void updateShippingAddress(Address shippingAddress, UserRole userRole);
	
}
