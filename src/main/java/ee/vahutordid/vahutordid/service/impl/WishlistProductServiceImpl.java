package ee.vahutordid.vahutordid.service.impl;

import java.util.List;


import ee.vahutordid.vahutordid.domain.Product;
import ee.vahutordid.vahutordid.domain.Wishlist;
import ee.vahutordid.vahutordid.domain.WishlistProduct;
import ee.vahutordid.vahutordid.repository.WishlistProductRepository;
import ee.vahutordid.vahutordid.service.WishlistProductService;
import ee.vahutordid.vahutordid.service.WishlistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishlistProductServiceImpl implements WishlistProductService {
		
	private static final Logger LOG = LoggerFactory.getLogger(WishlistService.class);
	
	@Autowired
	private WishlistProductRepository wishlistProductRepository;

	@Override
	public boolean addToWishlist(Wishlist wishlist, Product product) {
		
		WishlistProduct wishlistProduct=wishlistProductRepository.findByWishlistAndProduct(wishlist, product);
		if(wishlistProduct==null)
		{
			wishlistProduct=new WishlistProduct();
			wishlistProduct.setProduct(product);
			wishlistProduct.setWishlist(wishlist);
			if(wishlistProductRepository.save(wishlistProduct)!=null)
			{
				LOG.info("\n\nSUCCESS: {} added to the wishlist!\n",product.getName());
				return true;
			}
		}
		LOG.info("\n\nFAILURE: Unable to add to wishlist.\n\n");
		return false;
	}
	
	@Override
	public boolean removeFromWishlist(WishlistProduct wishlistProduct)
	{
		WishlistProduct tempWishlistProduct=wishlistProductRepository.findByWishlistAndProduct(wishlistProduct.getWishlist(), wishlistProduct.getProduct());
		if(tempWishlistProduct!=null)
		{
			wishlistProductRepository.delete(tempWishlistProduct);
			LOG.info("SUCCESS: Removed object from wishlist");
			return true;
		}
		LOG.info("Failure: Unable to find the specific product in wishlist");
		return false;
	}

	@Override
	public List<WishlistProduct> fetchAllProductsInWishlist(Wishlist wishlist)
	{
		List<WishlistProduct> productsInWishlist=wishlistProductRepository.findByWishlist(wishlist);
		return productsInWishlist;
	}

	@Override
	public WishlistProduct findOne(Long wishlistProductId) {
		return wishlistProductRepository.findOne(wishlistProductId);
	}
}
