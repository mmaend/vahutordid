package ee.vahutordid.vahutordid.repository;

import ee.vahutordid.vahutordid.domain.User;
import ee.vahutordid.vahutordid.domain.security.Role;
import ee.vahutordid.vahutordid.domain.security.UserRole;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;



public interface UserRoleRepository extends CrudRepository<UserRole, Long> {

	UserRole findByRoleAndUser(Role role, User user);

    ArrayList<UserRole> findByRole(Role role);
    
    UserRole findFirstByRole(Role role);

}
