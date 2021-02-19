package ee.vahutordid.vahutordid.repository;


import ee.vahutordid.vahutordid.domain.AbstractSale;
import ee.vahutordid.vahutordid.domain.MemberCartItem;
import ee.vahutordid.vahutordid.domain.Product;
import ee.vahutordid.vahutordid.domain.ShoppingCart;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

public interface MemberCartItemRepository extends CrudRepository<MemberCartItem, Long>{
	
	MemberCartItem findByShoppingCartAndProduct(ShoppingCart shoppingCart, Product product);
	
	HashSet<MemberCartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
	MemberCartItem findOne(Long memberCartItemId);

	@Transactional
	int deleteByIdAndShoppingCartId(Long id, Long shoppingCartId);
	
	int deleteByShoppingCartId(Long shoppingCartId);
	
	@Transactional
	@Modifying
	@Query("UPDATE MemberCartItem mci SET mci.qty=mci.qty-:qty WHERE mci.product=:product AND mci.qty>:qty AND mci.shoppingCart=:shoppingCart")
	int partialPurchase(@Param("product") Product product,@Param("qty") int qty, @Param("shoppingCart") ShoppingCart shoppingCart);
	
	@Transactional
	@Modifying
	@Query("UPDATE MemberCartItem mci SET mci.abstractSale=:sale,mci.shoppingCart=null,mci.isVisible=false WHERE mci.product=:product AND mci.shoppingCart=:shoppingCart")
	int fullPurchase(@Param("product") Product product, @Param("shoppingCart") ShoppingCart shoppingCart,@Param("sale") AbstractSale abstractSale);
	
	List<MemberCartItem> findByProductIdAndIsVisibleTrue(Long id);
	
	

	
}
