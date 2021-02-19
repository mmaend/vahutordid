package ee.vahutordid.vahutordid.service.impl;


import ee.vahutordid.vahutordid.domain.Wishlist;
import ee.vahutordid.vahutordid.domain.security.UserRole;
import ee.vahutordid.vahutordid.repository.WishlistRepository;
import ee.vahutordid.vahutordid.service.WishlistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishlistServiceImpl implements WishlistService {

	private static final Logger LOG = LoggerFactory.getLogger(WishlistService.class);
	
	@Autowired
	private WishlistRepository wishlistRepository;
	
	@Override
	public boolean createWishlist(UserRole userRole)
	{
		Wishlist wishlist=new Wishlist();
		wishlist.setUserRole(userRole);
		if(wishlistRepository.save(wishlist)!=null)
		{
			LOG.info("\n\nSUCCESS: Wishlist generated succesfully for user {}.\n",userRole.getUser().getUsername());
			return true;
		}
		LOG.info("\n\nFAILURE: Unable to generate corresponding wishlist.");
		return false;
	}

}
