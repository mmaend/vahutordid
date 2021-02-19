package ee.vahutordid.vahutordid.utility;

import java.util.ArrayList;
import java.util.List;


import ee.vahutordid.vahutordid.domain.AbstractSale;
import ee.vahutordid.vahutordid.domain.ClientOrder;
import org.springframework.stereotype.Component;

@Component
public class AbstractSaleUtility {
	
	public static List<ClientOrder> castToClientList(List<AbstractSale> abstractSaleList){
		if (abstractSaleList != null) {
			List<ClientOrder> clientOrderList = new ArrayList<>();
			for (AbstractSale as : abstractSaleList) {
				clientOrderList.add((ClientOrder) as);
			}
			return clientOrderList;
		}
		return null;
	}

}
