package ee.vahutordid.vahutordid.service.impl;

import java.util.Set;

import ee.vahutordid.vahutordid.domain.User;
import ee.vahutordid.vahutordid.domain.security.PasswordResetToken;
import ee.vahutordid.vahutordid.domain.security.Role;
import ee.vahutordid.vahutordid.domain.security.UserRole;
import ee.vahutordid.vahutordid.repository.PasswordResetTokenRepository;
import ee.vahutordid.vahutordid.repository.RoleRepository;
import ee.vahutordid.vahutordid.repository.UserRepository;
import ee.vahutordid.vahutordid.service.RoleService;
import ee.vahutordid.vahutordid.service.ShoppingCartService;
import ee.vahutordid.vahutordid.service.UserRoleService;
import ee.vahutordid.vahutordid.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserRoleService userRoleService;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Override
	public User createUser(User user, Set<UserRole> userRoles) {
		User localUser = userRepository.findByUsername(user.getUsername());

		if (localUser != null) {
			LOG.info("\n\n\nFAILURE: User {} already exists. Nothing will be done.\n\n", user.getUsername());
		} 
		else 
			{
			localUser = userRepository.findByEmail(user.getEmail());
			if (localUser != null) 
				{
					LOG.info("\n\n\nFAILURE: User with email:{} already exists. Nothing will be done.\n\n", user.getEmail());
				}
			
			else
				{
					for (UserRole ur : userRoles) 
					{
						roleService.createRole(ur.getRole());
					}
			
					user.getUserRoles().addAll(userRoles);
					LOG.info("\n\n\nSUCCESS: User {} created. Database succesfully updated.\n\n", user.getUsername());
										
								
					localUser = userRepository.save(user);
					
					for (UserRole ur : user.getUserRoles()) 
					{
						userRoleService.createUserRole(ur);
						
					}
					
					
			
				}
			}

		return localUser;
	}
	
	@Override
	public User addRoleToExistingUser(User user,String roleName)
	{
		//Searching Database for User user
		User existingUser = userService.findByUsername(user.getUsername());
		
		if (existingUser != null) 
		{
			
			
			//Checking if it exists in user object's Set of UserRoles
			//and if it isn't part of it create it and add it to user object.
			if(!userRoleService.hasThisRole(roleName,existingUser))
			{
				//Finding the role
				Role role = new Role();
				role=roleRepository.findByName(roleName);
				
				//Creating  a UserRole object
				UserRole roleToAdd = new UserRole();
				roleToAdd.setUser(existingUser);
				roleToAdd.setRole(role);
				
				userRoleService.createUserRole(roleToAdd);
				existingUser.getUserRoles().add(roleToAdd);
				userService.save(existingUser);
			}
		}
		
		//Returning the final user or null in case the user search yielded no results
		return existingUser;
	}
	
	//Non Functional
	@Override
	public void updateUserThroughUsername(User user)
	{
		User localUser=new User();
		localUser=userRepository.findByUsername(user.getUsername());
		if(localUser!=null && !localUser.equals(user))
		{
			user.setEmail(localUser.getEmail());
			user.setUsername(localUser.getUsername());
			localUser = userRepository.save(user);
			
			user.getUserRoles().removeAll(localUser.getUserRoles());
			user.setUserRoles(user.getUserRoles());
			
			userRepository.update(user.getFirstName(),user.getLastName(),user.getPhone(),user.isEnabled(),user.getUserRoles(),user.getUsername());
			LOG.info("\n\nSUCCESS: User {} succesfully modified.\n\n", user.getUsername());
		}
	}
	
	//Non Functional
	@Override
	public void updateUserThroughEmail(String email, User user)
	{
		
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public void createPasswordResetTokenForUser(final User user, final String token) {
		final PasswordResetToken myToken = new PasswordResetToken(token, user);
		passwordResetTokenRepository.save(myToken);
	}
	
	@Override
	public User findByEmail (String email) {
		return userRepository.findByEmail(email);
	}
	
	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User findOne(Long id) {
		// TODO Auto-generated method stub
		return userRepository.findOne(id);
	}
	
	@Override
	public PasswordResetToken getPasswordResetToken(final String token) {
		return passwordResetTokenRepository.findByToken(token);
	}

}
