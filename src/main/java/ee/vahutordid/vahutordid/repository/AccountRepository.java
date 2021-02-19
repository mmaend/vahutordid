package ee.vahutordid.vahutordid.repository;


import ee.vahutordid.vahutordid.domain.Account;
import ee.vahutordid.vahutordid.domain.security.UserRole;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {

	Account findByUserRole(UserRole userRole);
	
	Account findOne(Long id);
	
}
