package ee.vahutordid.vahutordid.service;

import ee.vahutordid.vahutordid.domain.MemberCartItem;
import ee.vahutordid.vahutordid.domain.Product;
import ee.vahutordid.vahutordid.domain.ShoppingCart;

import java.util.HashSet;
import java.util.List;



public interface MemberCartItemService {
	
	boolean emptyMemberCart(Long shoppingCartId);
	
	boolean removeMemberCartItem(Long id,Long shoppingCartId);
	
	MemberCartItem findById(Long memberCartItemId);
	
	boolean putUpForSale(Product product, int qty, ShoppingCart shoppingCart);
	
	boolean fullPurchaseFromMember(MemberCartItem memberCartItem);

	boolean partialPurchaseFromMember(MemberCartItem memberCartItem, int qty);
	
	List<MemberCartItem> findAllAvailableItems(Long productId);
	
	boolean toggleVisible(MemberCartItem memberCartItem);
	
	void activate(Long id);
	
	void deactivate(Long id);

	List<MemberCartItem> findAll();
	
	HashSet<MemberCartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
}
