package ee.vahutordid.vahutordid.repository;

import ee.vahutordid.vahutordid.domain.ClientOrder;
import ee.vahutordid.vahutordid.domain.security.UserRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;



public interface ClientOrderRepository extends CrudRepository<ClientOrder, Long> {
	
	ClientOrder findOne(Long id);

	@Query("SELECT c FROM ClientOrder c WHERE c.submittedDate BETWEEN :from AND :to")
    List<ClientOrder> findOrdersFromTo(@Param("from") Timestamp from, @Param("to") Timestamp to);
	
	List<ClientOrder> findByDistributedFalse();

	List<ClientOrder> findByUserRole(UserRole userRole);
	
	
}
