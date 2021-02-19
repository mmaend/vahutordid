package ee.vahutordid.vahutordid.service.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import ee.vahutordid.vahutordid.domain.ClientOrder;
import ee.vahutordid.vahutordid.domain.security.UserRole;
import ee.vahutordid.vahutordid.repository.ClientOrderRepository;
import ee.vahutordid.vahutordid.repository.RoleRepository;
import ee.vahutordid.vahutordid.repository.UserRoleRepository;
import ee.vahutordid.vahutordid.service.AccountService;
import ee.vahutordid.vahutordid.service.ClientOrderService;
import ee.vahutordid.vahutordid.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientOrderServiceImpl implements ClientOrderService{
	
	private static final Logger LOG = LoggerFactory.getLogger(ClientOrderService.class);
	
	@Autowired
	private ClientOrderRepository clientOrderRepository;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private AccountService accountService;
	
	@Override
	public ClientOrder createClientOrder(ClientOrder clientOrder)
	{
		
		
		clientOrder.setSubmittedDate(Timestamp.valueOf(LocalDateTime.now()));
		clientOrder.setOrderStatus("Processing");
		
		clientOrderRepository.save(clientOrder);
		return(clientOrder);
		
		
		
	}
	
	@Override
	public ClientOrder updateOrderStatusToShipped(ClientOrder clientOrder)
	{
		clientOrder=clientOrderRepository.findOne(clientOrder.getId());
		if(clientOrder!=null)
		{
			clientOrder.setOrderStatus("Shipped");
			clientOrderRepository.save(clientOrder);
			LOG.info("SUCCESS: Order Status changed to Shipped.");
		}
		else
		{
			LOG.info("FAILURE: Unable to find Client Order");
		}
		return clientOrder;
	}
	
	@Override
	public ClientOrder updateOrderStatusToDelivered(ClientOrder clientOrder)
	{
		clientOrder=clientOrderRepository.findOne(clientOrder.getId());
		if(clientOrder!=null)
		{
			clientOrder.setOrderStatus("Delivered");
			clientOrderRepository.save(clientOrder);
			LOG.info("SUCCESS: Order Status changed to Delivered.");
		}
		else
		{
			LOG.info("FAILURE: Unable to find Client Order");
		}
		return clientOrder;
	}
	
	
	@Override
	public List<ClientOrder> fetchOrdersByPeriod(Timestamp fromTimestamp, Timestamp toTimestamp)
	{
		List<ClientOrder> clientOrders=new ArrayList<ClientOrder>();
		
		clientOrders=clientOrderRepository.findOrdersFromTo(fromTimestamp, toTimestamp);
		return clientOrders;
	}

	@Override
	public ClientOrder updateShippingMethod(ClientOrder clientOrder, String method) {
		clientOrder=clientOrderRepository.findOne(clientOrder.getId());
		if(clientOrder!=null)
		{
			clientOrder.setShippingMethod(method);
			clientOrderRepository.save(clientOrder);
			LOG.info("SUCCESS: Shipping method changed to " + method  + ".");
		}
		else
		{
			LOG.info("FAILURE: Unable to find Client Order");
		}
		return clientOrder;
	}

	@Override
	public ClientOrder updateShippingDate(ClientOrder clientOrder, Date date) {
		clientOrder=clientOrderRepository.findOne(clientOrder.getId());
		if(clientOrder!=null)
		{
			clientOrder.setShippingDate(date);
			clientOrderRepository.save(clientOrder);
			LOG.info("SUCCESS: Shipping date changed to " + date  + ".");
		}
		else
		{
			LOG.info("FAILURE: Unable to find Client Order");
		}
		return clientOrder;
	}

	@Override
	public List<ClientOrder> findAllUndistributedEarnings()
	{
		return clientOrderRepository.findByDistributedFalse();	
	}
	
	@Override
	public void distributeEarningsToAllMembers(Long clientOrderId) {

		ArrayList<UserRole> members=userRoleRepository.findByRole(roleRepository.findByName("ROLE_MEMBER"));
		int divisor=members.size();
		ClientOrder clientOrder=clientOrderRepository.findOne(clientOrderId);
		if (divisor!=0)
		{
			BigDecimal amount=clientOrder.getTotal().divide(BigDecimal.valueOf(divisor*2), 2,  BigDecimal.ROUND_HALF_UP);
			for(UserRole usr:members)
			{
				transactionService.twoWayTransaction(amount, accountService.findAdminAccount(), usr.getAccount(), clientOrder);
			}
			
			clientOrder.setDistributed(true);
			clientOrderRepository.save(clientOrder);
		}
	}

	@Override
	public ClientOrder findOne(Long id) {
		return clientOrderRepository.findOne(id);
	}

	@Override
	public List<ClientOrder> findByUserRole(UserRole userRole) {
		return clientOrderRepository.findByUserRole(userRole);
	}
	
}
