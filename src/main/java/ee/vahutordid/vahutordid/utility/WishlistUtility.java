package ee.vahutordid.vahutordid.utility;

import java.util.ArrayList;
import java.util.List;


import ee.vahutordid.vahutordid.domain.Product;
import ee.vahutordid.vahutordid.domain.Wishlist;
import ee.vahutordid.vahutordid.domain.WishlistProduct;
import org.springframework.stereotype.Component;

@Component
public class WishlistUtility {
	
	public static List<Product> getProductList(Wishlist wishlist){
		List<WishlistProduct> wishlistProductList = wishlist.getWishlistProductList();
		List<Product> productList = new ArrayList<>();
		for (WishlistProduct wp : wishlistProductList) {
			productList.add(wp.getProduct());
		}
		return productList;
	}

}
