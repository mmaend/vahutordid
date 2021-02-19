package ee.vahutordid.vahutordid.service;

import ee.vahutordid.vahutordid.domain.AbstractSale;
import ee.vahutordid.vahutordid.domain.MemberSale;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;



public interface MemberSaleService {
	
	AbstractSale createMemberSale(MemberSale memberSale);
	
	List<MemberSale> fetchSalesByPeriod(Timestamp fromTimestamp, Timestamp toTimestamp);

}
