package ee.vahutordid.vahutordid.service.impl;

import java.math.BigDecimal;


import ee.vahutordid.vahutordid.domain.ShoppingCart;
import ee.vahutordid.vahutordid.domain.security.UserRole;
import ee.vahutordid.vahutordid.repository.CartItemRepository;
import ee.vahutordid.vahutordid.repository.ShoppingCartRepository;
import ee.vahutordid.vahutordid.service.CartItemService;
import ee.vahutordid.vahutordid.service.ShoppingCartService;
import ee.vahutordid.vahutordid.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService{
	
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private ShoppingCartRepository shoppingCartRepository;
	
	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Override
	public boolean createShoppingCart(UserRole userRole)
	{
	
		ShoppingCart shoppingCart=shoppingCartRepository.findByUserRole(userRole);
		
			if(userRole!=null && userRole.getUser()!=null)
			{
				shoppingCart=new ShoppingCart();
				shoppingCart.setUserRole(userRole);
				shoppingCart.setGrandTotal(BigDecimal.valueOf(0));
				shoppingCart=shoppingCartRepository.save(shoppingCart);
				
				LOG.info("\n\n\nSUCCESS: Shopping Cart for user {} succesfully created.\n\n",userRole.getUser().getUsername());
				return true;
			}
			else
			{
				LOG.info("\n\n\nFAILURE:Invalid Argument passed\n\n");
				return false;
			}
	}

	@Override
	public void setGrandTotal(ShoppingCart shoppingCart) {
		shoppingCart.setGrandTotal(cartItemService.getGrandTotal(shoppingCart));
		shoppingCartRepository.save(shoppingCart);
	}
	
	
}
