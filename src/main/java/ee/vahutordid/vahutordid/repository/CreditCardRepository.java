package ee.vahutordid.vahutordid.repository;


import ee.vahutordid.vahutordid.domain.CreditCard;
import ee.vahutordid.vahutordid.domain.security.UserRole;
import org.springframework.data.repository.CrudRepository;

public interface CreditCardRepository extends CrudRepository<CreditCard, Long> {
	
	CreditCard findByCardNumberAndUserRole(String creditCardNumber, UserRole userRole);

}
