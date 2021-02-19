package ee.vahutordid.vahutordid.repository;


import ee.vahutordid.vahutordid.domain.Address;
import ee.vahutordid.vahutordid.domain.security.UserRole;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
	
	Address findByReceiverNameAndStreet1AndCityAndZipcodeAndUserRole(String receiverName, String street1, String city, String zipcode, UserRole userRole);

	Address findByIdAndUserRole(Long id, UserRole userRole);

}
