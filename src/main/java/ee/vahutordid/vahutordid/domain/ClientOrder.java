package ee.vahutordid.vahutordid.domain;

import javax.persistence.Entity;



@Entity
public class ClientOrder extends AbstractSale {
	
	private String orderStatus;
	boolean distributed=false;
	
	public ClientOrder() {};

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public boolean isDistributed() {
		return distributed;
	}

	public void setDistributed(boolean distributed) {
		this.distributed = distributed;
	}
	
}
