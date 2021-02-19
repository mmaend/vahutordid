package ee.vahutordid.vahutordid.repository;


import ee.vahutordid.vahutordid.domain.ShoppingCart;
import ee.vahutordid.vahutordid.domain.security.UserRole;
import org.springframework.data.repository.CrudRepository;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long> {
	
	ShoppingCart findByUserRole(UserRole userRole);

}
