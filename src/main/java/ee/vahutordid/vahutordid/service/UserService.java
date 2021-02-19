package ee.vahutordid.vahutordid.service;

import ee.vahutordid.vahutordid.domain.User;
import ee.vahutordid.vahutordid.domain.security.PasswordResetToken;
import ee.vahutordid.vahutordid.domain.security.UserRole;

import java.util.List;
import java.util.Set;




public interface UserService {
	User createUser(User user, Set<UserRole> userRoles) throws Exception;
	
	User save(User user);
	
	User addRoleToExistingUser(User user,String rolename);
	
	//Non-Functional
	void updateUserThroughUsername(User user);
	//Non-Functional
	void updateUserThroughEmail(String email, User user);
	

	void createPasswordResetTokenForUser(final User user, final String token);
	
	User findByUsername(String username);
	
	User findByEmail (String email);

	User findOne(Long id);

	PasswordResetToken getPasswordResetToken(final String token);
}
