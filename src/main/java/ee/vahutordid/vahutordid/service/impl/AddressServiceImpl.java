package ee.vahutordid.vahutordid.service.impl;

import java.util.List;


import ee.vahutordid.vahutordid.domain.Address;
import ee.vahutordid.vahutordid.domain.security.UserRole;
import ee.vahutordid.vahutordid.repository.AddressRepository;
import ee.vahutordid.vahutordid.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService{
	
	private static final Logger LOG = LoggerFactory.getLogger(AddressService.class);
	
	@Autowired
	private AddressRepository addressRepository;
	
	public Address createAddress(Address address)
	{
		if (address.getUserRole()==null || address.getReceiverName()==null || address.getStreet1()==null || address.getCity()==null || address.getZipcode()==null)
		{
			LOG.info("\n\n\nFAILURE: Insufficient information passed. Unable to create a valid address.\n\n");
			return null;
		}
		else
		{
			Address tempAddress=addressRepository.findByIdAndUserRole(address.getId(),address.getUserRole());
			if(tempAddress==null) 
			{
				tempAddress = addressRepository.findByReceiverNameAndStreet1AndCityAndZipcodeAndUserRole(address.getReceiverName(), address.getStreet1(), address.getCity(), address.getZipcode(), address.getUserRole());
				if (tempAddress==null) {
					tempAddress = new Address();
					tempAddress.setReceiverName(address.getReceiverName());
					tempAddress.setStreet1(address.getStreet1());
					tempAddress.setStreet2(address.getStreet2());
					tempAddress.setCity(address.getCity());
					tempAddress.setState(address.getState());
					tempAddress.setCountry(address.getCountry());
					tempAddress.setZipcode(address.getZipcode());
					tempAddress.setUserRole(address.getUserRole());
					tempAddress=addressRepository.save(tempAddress);
					LOG.info("SUCCESS: Address Succesfully Added!!");
				}
			}else {
				deepCopyAddress(address, tempAddress);
				tempAddress=addressRepository.save(tempAddress);
				LOG.info("SUCCESS: Address Succesfully Modified!!");
			}
			return tempAddress;
		}
		
	}

	@Override
	public Address findById(Long shippingAddressId) {
		return addressRepository.findOne(shippingAddressId);
	}

	@Override
	public void setDefaultShippingAddress(Long defaultShippingAddressId, UserRole userRole) {
		List<Address> shippingAddressList = userRole.getUserShippingAddressList();
		
		for (Address sa : shippingAddressList) {
			if (sa.isUserShippingDefault()) {
				sa.setUserShippingDefault(false);
				addressRepository.save(sa);
			}
			if (sa.getId() == defaultShippingAddressId) {
				sa.setUserShippingDefault(true);
				addressRepository.save(sa);
			}
		}
		
	}

	@Override
	public void removeFromUserRole(Long shippingAddressId, UserRole userRole) {
		Address shippingAddress = addressRepository.findOne(shippingAddressId);
		shippingAddress.setUserRole(null);
		addressRepository.save(shippingAddress);
	}

	@Override
	public Address deepCopyAddress(Address ad, Address currentShippingAddress) {
		currentShippingAddress.setCity(ad.getCity());
		currentShippingAddress.setCountry(ad.getCountry());
		currentShippingAddress.setReceiverName(ad.getReceiverName());
		currentShippingAddress.setState(ad.getState());
		currentShippingAddress.setStreet1(ad.getStreet1());
		currentShippingAddress.setStreet2(ad.getStreet2());
		currentShippingAddress.setZipcode(ad.getZipcode());
		currentShippingAddress.setUserShippingDefault(ad.isUserShippingDefault());
		return currentShippingAddress;
	}

	@Override
	public void save(Address address) {
		addressRepository.save(address);
	}
	
	

}
