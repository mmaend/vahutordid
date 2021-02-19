package ee.vahutordid.vahutordid.service;

import ee.vahutordid.vahutordid.domain.ClientOrder;
import ee.vahutordid.vahutordid.domain.security.UserRole;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;



public interface ClientOrderService {

	ClientOrder createClientOrder(ClientOrder clientOrder);
	
	List<ClientOrder> fetchOrdersByPeriod(Timestamp fromTimestamp, Timestamp toTimestamp);
	
	ClientOrder updateOrderStatusToShipped(ClientOrder clientOrder);
	
	ClientOrder updateOrderStatusToDelivered(ClientOrder clientOrder);

	ClientOrder updateShippingMethod(ClientOrder clientOrder, String method);

	ClientOrder updateShippingDate(ClientOrder clientOrder, Date date);

	List<ClientOrder> findAllUndistributedEarnings();

	void distributeEarningsToAllMembers(Long clientOrderId);

	ClientOrder findOne(Long l);

	List<ClientOrder> findByUserRole(UserRole userRole);

}
