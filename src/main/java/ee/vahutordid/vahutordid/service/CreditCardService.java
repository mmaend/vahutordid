package ee.vahutordid.vahutordid.service;


import ee.vahutordid.vahutordid.domain.CreditCard;
import ee.vahutordid.vahutordid.domain.security.UserRole;

public interface CreditCardService {
	
	CreditCard createCreditCard(CreditCard creditCard);

	CreditCard findById(Long creditCardId);

	void setDefaultCreditCard(Long defaultCreditCardId, UserRole userRole);

	void removeFromUserRole(Long creditCardId, UserRole userRole);

	CreditCard deepCopyCreditCard(CreditCard cc, CreditCard currentCreditCard);

	void save(CreditCard creditCard);
	
	
	

}
