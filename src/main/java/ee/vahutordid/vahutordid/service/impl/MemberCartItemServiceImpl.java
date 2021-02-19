package ee.vahutordid.vahutordid.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


import ee.vahutordid.vahutordid.domain.*;
import ee.vahutordid.vahutordid.repository.MemberCartItemRepository;
import ee.vahutordid.vahutordid.service.AccountService;
import ee.vahutordid.vahutordid.service.ClientOrderService;
import ee.vahutordid.vahutordid.service.MemberCartItemService;
import ee.vahutordid.vahutordid.service.MemberSaleService;
import ee.vahutordid.vahutordid.service.ProductService;
import ee.vahutordid.vahutordid.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberCartItemServiceImpl implements MemberCartItemService{
	
	private static final Logger LOG = LoggerFactory.getLogger(ClientOrderService.class);
	
	@Autowired
	private MemberCartItemRepository memberCartItemRepository;
	
	@Autowired 
	private MemberSaleService memberSaleService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private AccountService accountService;
	
	
	@Override
	public MemberCartItem findById(Long memberCartItemId) {
		
		return memberCartItemRepository.findOne(memberCartItemId);
	}

	@Override
	public boolean putUpForSale(Product product, int qty, ShoppingCart shoppingCart) {

		MemberCartItem memberCartItem;
		memberCartItem=memberCartItemRepository.findByShoppingCartAndProduct(shoppingCart, product);
		if(qty>0)
		{
			if(memberCartItem==null)
			{	
				product=productService.findOne(product.getId());
				memberCartItem=new MemberCartItem();
				memberCartItem.setShoppingCart(shoppingCart);
				memberCartItem.setProduct(product);
				memberCartItem.setQty(qty);

				
				memberCartItemRepository.save(memberCartItem);
				
				LOG.info("\n\nMemberCartItem created.\n");
				
				return true;
			}
			else if (memberCartItem !=null)
			{
				memberCartItem.setQty(memberCartItem.getQty()+qty);
				memberCartItem=memberCartItemRepository.save(memberCartItem);
				
				
				LOG.info("\n\nMemberCartItem modified.\n");
				return true;
			}
		}
		
		return false;
	
	}

	@Override
	public boolean fullPurchaseFromMember(MemberCartItem memberCartItem) {
		
		MemberSale memberSale=new MemberSale();
		memberSale.setUserRole(memberCartItem.getShoppingCart().getUserRole());
		memberSale.setSubmittedDate(new Date());
		BigDecimal total=memberCartItem.getCurrentPurchasePrice().multiply(BigDecimal.valueOf(memberCartItem.getQty()));
		memberSale.setTotal(total);
		AbstractSale abstractSale=memberSaleService.createMemberSale(memberSale);
		
		if(memberCartItemRepository.fullPurchase(memberCartItem.getProduct(),memberCartItem.getShoppingCart(),abstractSale)>0)
		{
			productService.restock(memberCartItem.getProduct().getId(), memberCartItem.getQty());
			
			transactionService.twoWayTransaction(total, accountService.findAdminAccount(), memberCartItem.getShoppingCart().getUserRole().getAccount(), abstractSale);
			LOG.info("\n\n\nSUCCESS: Purchase successful\n\n");
			return true;
		}
		
		return false;
	}

	@Override
	public boolean partialPurchaseFromMember(MemberCartItem memberCartItem, int qty) {

		if(memberCartItemRepository.partialPurchase(memberCartItem.getProduct(), qty, memberCartItem.getShoppingCart())>0)
		{
			MemberSale memberSale=new MemberSale();
			memberSale.setUserRole(memberCartItem.getShoppingCart().getUserRole());
			memberSale.setSubmittedDate(new Date());
			memberSale.setShippingMethod("Standard Mail");
			BigDecimal total=memberCartItem.getCurrentPurchasePrice().multiply(BigDecimal.valueOf(qty));
			memberSale.setTotal(total);
			AbstractSale abstractSale=memberSaleService.createMemberSale(memberSale);
			
			MemberCartItem soldCartItem=new MemberCartItem();
			soldCartItem.setAbstractSale(abstractSale);
			soldCartItem.setCurrentPurchasePrice(memberCartItem.getCurrentPurchasePrice());
			soldCartItem.setProduct(memberCartItem.getProduct());
			soldCartItem.setQty(qty);
			soldCartItem.setVisible(false);
			soldCartItem=memberCartItemRepository.save(soldCartItem);
			
			if(soldCartItem!=null)
			{
				transactionService.twoWayTransaction(total, accountService.findAdminAccount(), memberCartItem.getShoppingCart().getUserRole().getAccount(), abstractSale);
				productService.restock(soldCartItem.getProduct().getId(), soldCartItem.getQty());
				LOG.info("\n\n\nSUCCESS: Purchase of {} units of {} successful\n\n",qty,soldCartItem.getProduct());
				return true;
			}
			
			
		}
		return false;
	}

	@Override
	public List<MemberCartItem> findAllAvailableItems(Long productId) {
		
		return memberCartItemRepository.findByProductIdAndIsVisibleTrue(productId);
	}

	@Override
	public boolean toggleVisible(MemberCartItem memberCartItem) 
	{
		memberCartItem.setVisible(!memberCartItem.isVisible());

		
		try 
		{
			memberCartItemRepository.save(memberCartItem);
			LOG.info("\n\n\nSUCCESS: Visibility switch concluded\n\n");
			return true;
		}
		catch(Exception e)
		{
			LOG.info("\n\n\nFAILURE: Failed to switch Visibility\n\n");
			return false;
		}
		
	}
	
	@Override
    public void deactivate(Long id) {
        MemberCartItem memberCartItem = memberCartItemRepository.findOne(id);
        memberCartItem.setVisible(false);
        memberCartItemRepository.save(memberCartItem);
    }

    @Override
    public void activate(Long id) {
    	MemberCartItem memberCartItem = memberCartItemRepository.findOne(id);
        memberCartItem.setVisible(true);
        memberCartItemRepository.save(memberCartItem);
    }

	@Override
	public List<MemberCartItem> findAll() {
		return (List<MemberCartItem>) memberCartItemRepository.findAll();
	}
	
    
    @Override
	public boolean removeMemberCartItem(Long id,Long shoppingCartId)
	{
		if(memberCartItemRepository.deleteByIdAndShoppingCartId(id,shoppingCartId)>0)
		{
			LOG.info("\n\nSUCCESS: Removed memberCartItem {} from shoppingCart\n",id);
			return true;
		}
		LOG.info("\n\nFAILURE: Removing memberCartItem {} failed miserably.",id);
		return false;
	}
	
	@Override
	public boolean emptyMemberCart(Long shoppingCartId)
	{
		if(memberCartItemRepository.deleteByShoppingCartId(shoppingCartId)>0)
		{
			LOG.info("\n\nSUCCESS: MemberShoppingCart cleared.");
			return true;
		}
		LOG.info("\n\nFAILURE: Emptying the MemberShoppingCart {} failed miserably.");
		return false;
	}

	@Override
	public HashSet<MemberCartItem> findByShoppingCart(ShoppingCart shoppingCart) {
		return memberCartItemRepository.findByShoppingCart(shoppingCart);
	}
	
	

}
