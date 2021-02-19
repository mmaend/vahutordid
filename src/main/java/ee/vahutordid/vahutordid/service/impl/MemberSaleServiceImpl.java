package ee.vahutordid.vahutordid.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import ee.vahutordid.vahutordid.domain.AbstractSale;
import ee.vahutordid.vahutordid.domain.MemberSale;
import ee.vahutordid.vahutordid.repository.MemberSaleRepository;
import ee.vahutordid.vahutordid.service.MemberSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberSaleServiceImpl implements MemberSaleService{
	
	@Autowired
	private MemberSaleRepository memberSaleRepository;
	
	@Override
	public AbstractSale createMemberSale(MemberSale memberSale)
	{
		
		memberSale.setSubmittedDate(Timestamp.valueOf(LocalDateTime.now()));
		memberSale.setSaleStatus("Pending");
		
		memberSaleRepository.save(memberSale);
		return(memberSale);
		
		
		
	}
	
	@Override
	public List<MemberSale> fetchSalesByPeriod(Timestamp fromTimestamp, Timestamp toTimestamp)
	{
		List<MemberSale> memberSales=new ArrayList<MemberSale>();
		
		memberSales=memberSaleRepository.findSalesFromTo(fromTimestamp, toTimestamp);
		return memberSales;
	}

}
