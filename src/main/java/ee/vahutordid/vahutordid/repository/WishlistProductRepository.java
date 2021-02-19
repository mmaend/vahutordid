package ee.vahutordid.vahutordid.repository;

import ee.vahutordid.vahutordid.domain.Product;
import ee.vahutordid.vahutordid.domain.Wishlist;
import ee.vahutordid.vahutordid.domain.WishlistProduct;
import org.springframework.data.repository.CrudRepository;

import java.util.List;



public interface WishlistProductRepository extends CrudRepository<WishlistProduct, Long> {

	WishlistProduct findByWishlistAndProduct(Wishlist wishlist, Product product);
	
	List<WishlistProduct> findByWishlist(Wishlist wishlist);
	
}
