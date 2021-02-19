package ee.vahutordid.vahutordid.service.impl;

import java.util.ArrayList;
import java.util.List;


import ee.vahutordid.vahutordid.domain.Address;
import ee.vahutordid.vahutordid.domain.CreditCard;
import ee.vahutordid.vahutordid.domain.User;
import ee.vahutordid.vahutordid.domain.security.Role;
import ee.vahutordid.vahutordid.domain.security.UserRole;
import ee.vahutordid.vahutordid.repository.CreditCardRepository;
import ee.vahutordid.vahutordid.repository.RoleRepository;
import ee.vahutordid.vahutordid.repository.UserRoleRepository;
import ee.vahutordid.vahutordid.service.AccountService;
import ee.vahutordid.vahutordid.service.CreditCardService;
import ee.vahutordid.vahutordid.service.ShoppingCartService;
import ee.vahutordid.vahutordid.service.UserRoleService;
import ee.vahutordid.vahutordid.service.WishlistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl implements UserRoleService{
	
	private static final Logger LOG = LoggerFactory.getLogger(UserRoleService.class);
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private AccountService accountService;

	@Autowired
	private CreditCardService creditCardService;

	@Autowired
	private CreditCardRepository creditCardRepository;

	@Autowired
	private WishlistService wishlistService;
	
	@Override
	public boolean createUserRole(UserRole userRole)
	{
		
		UserRole ur=userRoleRepository.findByRoleAndUser(userRole.getRole(), userRole.getUser());
		if(ur==null && userRole.getUser()!=null && userRole.getRole()!=null)
		{	
			userRole=userRoleRepository.save(userRole);
			shoppingCartService.createShoppingCart(userRole);
			
			if(userRole.getRole().getRoleId()==2L)
			{
				wishlistService.createWishlist(userRole);
			}else if(userRole.getRole().getRoleId()==1L)
			{
				accountService.createAccount(userRole, 0.00);
			}
			else if(userRole.getRole().getRoleId()==0L)
			{
				accountService.createAccount(userRole, 100000.00);
			}
			LOG.info("\n\n\nSUCCESS: Added UserRole {} to user {} \n\n",userRole.getRole().getName(), userRole.getUser().getUsername());
			return true;
		}
		else
		{
			LOG.info("\n\n\nFAILURE: User {} already has this role.\n\n", userRole.getUser().getUsername());
			return false;
		}
			
		
	}
	
	
	@Override
    public boolean hasThisRole(String roleName, User user)
    {
        Role role=roleRepository.findByName(roleName);
        if(role==null)
        {
        	LOG.info("\n\n\nNo Such Role Exists\n\n");
            return false;
        }
        UserRole userRole=userRoleRepository.findByRoleAndUser(role,user);
        if(userRole!=null)
        {
            return true;
        }
        return false;
        
        
        
    }
    
    @Override
    public List<User> fetchUsersOfRole(String roleName)
    {
        Role role=roleRepository.findByName(roleName);
        ArrayList<User> userList=new ArrayList<User>();
        ArrayList<UserRole> userRoleList=new ArrayList<UserRole>();
        if(role==null)
        {
        	LOG.info("\n\n\nNo Such Role Exists\n\n");
            return userList;
            
        }
        userRoleList=userRoleRepository.findByRole(role);
        if(userRoleList!=null)
        {
            System.out.println();
            for(UserRole usr: userRoleList)
            {
                userList.add(usr.getUser());
                System.out.println(usr.getUser().toString());
            }
            System.out.println();
        }
        
        return userList;
    }


	@Override
	public UserRole findByUserAndRole(User user, String roleType) {
		return userRoleRepository.findByRoleAndUser(roleRepository.findByName(roleType), user);
	}
	
	@Override
	public void updateBillingAddress(Address billingAddress, CreditCard creditCard, UserRole userRole) {
		creditCard.setUserRole(userRole);
		creditCard.setBillingAddress(billingAddress);
		creditCard.setDefaultCreditCard(false);
		billingAddress.setCreditCard(creditCard);
		userRole.getCreditCardList().add(creditCard);
		userRoleRepository.save(userRole);
	}


	@Override
	public void updateShippingAddress(Address shippingAddress, UserRole userRole) {
		shippingAddress.setUserRole(userRole);
		shippingAddress.setUserShippingDefault(false);
		userRole.getUserShippingAddressList().add(shippingAddress);
		userRoleRepository.save(userRole);
	}


}
