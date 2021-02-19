package ee.vahutordid.vahutordid.service;


import ee.vahutordid.vahutordid.domain.security.UserRole;

public interface WishlistService {
	
	boolean createWishlist(UserRole userRole);

}
