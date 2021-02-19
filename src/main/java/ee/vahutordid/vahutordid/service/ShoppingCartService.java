package ee.vahutordid.vahutordid.service;

import ee.vahutordid.vahutordid.domain.ShoppingCart;
import ee.vahutordid.vahutordid.domain.security.UserRole;

import java.math.BigDecimal;



public interface ShoppingCartService {

	boolean createShoppingCart(UserRole userRole);

	void setGrandTotal(ShoppingCart shoppingCart);
	
	
	
}
