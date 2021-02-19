package ee.vahutordid.vahutordid.service;

import ee.vahutordid.vahutordid.domain.Product;
import ee.vahutordid.vahutordid.domain.Wishlist;
import ee.vahutordid.vahutordid.domain.WishlistProduct;

import java.util.List;



public interface WishlistProductService {
	
	boolean addToWishlist(Wishlist wishlist, Product product);

	boolean removeFromWishlist(WishlistProduct wishlistProduct);
	
	List<WishlistProduct> fetchAllProductsInWishlist(Wishlist wishlist);

	WishlistProduct findOne(Long wishlistProductId);
	
}
