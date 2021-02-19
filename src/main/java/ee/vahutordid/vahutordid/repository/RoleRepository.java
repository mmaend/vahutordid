package ee.vahutordid.vahutordid.repository;

import ee.vahutordid.vahutordid.domain.security.Role;
import org.springframework.data.repository.CrudRepository;



public interface RoleRepository extends CrudRepository<Role, Long> {
	Role findByName(String name);
	
}
