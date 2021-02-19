package ee.vahutordid.vahutordid.repository;

import java.util.HashSet;
import java.util.List;


import ee.vahutordid.vahutordid.domain.AbstractSale;
import ee.vahutordid.vahutordid.domain.CartItem;
import ee.vahutordid.vahutordid.domain.Product;
import ee.vahutordid.vahutordid.domain.ShoppingCart;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CartItemRepository extends CrudRepository<CartItem, Long> {

	CartItem findByShoppingCartAndProduct(ShoppingCart shoppingCart, Product product);
	
	HashSet<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
	@Transactional
	int deleteByIdAndShoppingCartId(Long id, Long shoppingCartId);
	
	
	int deleteByShoppingCartId(Long shoppingCartId);
	
	@Transactional
	@Modifying
	@Query("UPDATE Product p SET p.inStockNumber=p.inStockNumber-:qty WHERE p=:product AND p.inStockNumber>=:qty")
	int checkAvailabilityAndUpdate(@Param("product") Product product,@Param("qty") long qty);

	List<CartItem> findByAbstractSale(AbstractSale abstractSale);
	
}
