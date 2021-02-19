package ee.vahutordid.vahutordid.service.impl;

import java.util.List;


import ee.vahutordid.vahutordid.domain.CreditCard;
import ee.vahutordid.vahutordid.domain.security.UserRole;
import ee.vahutordid.vahutordid.repository.CreditCardRepository;
import ee.vahutordid.vahutordid.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditCardServiceImpl implements CreditCardService {
	
	@Autowired
	private CreditCardRepository creditCardRepository;

	@Override
	public CreditCard createCreditCard(CreditCard creditCard) {
		
		CreditCard tempCard=creditCardRepository.findByCardNumberAndUserRole(creditCard.getCardNumber(),creditCard.getUserRole());
		if(tempCard==null)
		{
			
			tempCard=new CreditCard();
			tempCard.setUserRole(creditCard.getUserRole());
			tempCard.setBillingAddress(creditCard.getBillingAddress());
			tempCard.setCardNumber(creditCard.getCardNumber());
			tempCard.setCvc(creditCard.getCvc());
			tempCard.setExpiryMonth(creditCard.getExpiryMonth());
			tempCard.setExpiryYear(creditCard.getExpiryYear());
			tempCard.setHolderName(creditCard.getHolderName());
			tempCard.setType(creditCard.getType());
			tempCard=creditCardRepository.save(creditCard);
		}else {
			deepCopyCreditCard(creditCard, tempCard);
			tempCard=creditCardRepository.save(tempCard);
		}
		
		return tempCard;
	}

	@Override
	public CreditCard findById(Long creditCardId) {
		return creditCardRepository.findOne(creditCardId);
	}
	
	@Override
	public void setDefaultCreditCard(Long defaultCreditCardId, UserRole userRole) {
		
		List<CreditCard> creditCardList = userRole.getCreditCardList();
		
		for (CreditCard cc : creditCardList) {
			if (cc.isDefaultCreditCard()) {
				cc.setDefaultCreditCard(false);
				creditCardRepository.save(cc);
			}
			if (cc.getId() == defaultCreditCardId) {
				cc.setDefaultCreditCard(true);
				creditCardRepository.save(cc);
			}
		}	
	}

	@Override
	public void removeFromUserRole(Long creditCardId, UserRole userRole) {
		CreditCard creditCard = creditCardRepository.findOne(creditCardId);
		creditCard.setUserRole(null);
		creditCardRepository.save(creditCard);
	}

	@Override
	public CreditCard deepCopyCreditCard(CreditCard cc, CreditCard currentCreditCard) {
		currentCreditCard.setCardNumber(cc.getCardNumber());
		currentCreditCard.setCvc(cc.getCvc());
		currentCreditCard.setExpiryMonth(cc.getExpiryMonth());
		currentCreditCard.setExpiryYear(cc.getExpiryYear());
		currentCreditCard.setHolderName(cc.getHolderName());
		currentCreditCard.setType(cc.getType());
		currentCreditCard.setBillingAddress(cc.getBillingAddress());
		currentCreditCard.setDefaultCreditCard(cc.isDefaultCreditCard());
		return currentCreditCard;
	}

	@Override
	public void save(CreditCard creditCard) {
		creditCardRepository.save(creditCard);
	}

}
