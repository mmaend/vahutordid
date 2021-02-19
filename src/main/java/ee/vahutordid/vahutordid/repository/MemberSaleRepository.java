package ee.vahutordid.vahutordid.repository;

import ee.vahutordid.vahutordid.domain.MemberSale;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;


public interface MemberSaleRepository extends CrudRepository<MemberSale, Long> {
	
	@Query("SELECT m FROM MemberSale m WHERE m.submittedDate BETWEEN :from AND :to")
    List<MemberSale> findSalesFromTo(@Param("from") Timestamp from, @Param("to") Timestamp to);

}
