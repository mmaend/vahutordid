package ee.vahutordid.vahutordid.service.impl;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;


import ee.vahutordid.vahutordid.domain.*;
import ee.vahutordid.vahutordid.repository.CartItemRepository;
import ee.vahutordid.vahutordid.service.AccountService;
import ee.vahutordid.vahutordid.service.CartItemService;
import ee.vahutordid.vahutordid.service.ClientOrderService;
import ee.vahutordid.vahutordid.service.ShoppingCartService;
import ee.vahutordid.vahutordid.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImpl implements CartItemService{
	
	private static final Logger LOG = LoggerFactory.getLogger(CartItemService.class);
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private ClientOrderService clientOrderService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private AccountService accountService;
	
	
	//ShoppingCart,Product CANNOT be null AND qty CANNOT be any value less than 1 (Controller has to check)
	@Override
	public boolean addToCart(ShoppingCart shoppingCart, Product product, int qty)
	{
		if (qty > 0) {
			CartItem cartItem=new CartItem();
			cartItem=cartItemRepository.findByShoppingCartAndProduct(shoppingCart, product);

			if(cartItem==null)
			{	
				cartItem=new CartItem();
				
				cartItem.setShoppingCart(shoppingCart);
				cartItem.setProduct(product);
				cartItem.setQty(qty);
				
				cartItemRepository.save(cartItem);
				
				return true;
			}
			else if (cartItem.getQty()+qty <= product.getInStockNumber())
			{
				cartItem.setQty(cartItem.getQty()+qty);
				cartItemRepository.save(cartItem);
				
				System.out.println("\n\nCartItem modified");
				return true;
			}
		}
		return false;
	}
	

	@Override
	public void updateToCart(CartItem cartItem, int qty) {
		cartItem.setQty(qty);
		cartItemRepository.save(cartItem);
	}
	
	@Override
	public HashSet<Product> commitSale(ShoppingCart shoppingCart, CreditCard creditCard, Address billingAddress, Address shippingAddress, String shippingMethod)
	{
		AbstractSale abstractSale=new ClientOrder();
		HashSet<Product> itemsUnavailable=new HashSet<Product>();
		HashSet<CartItem> itemsInCart=new HashSet<CartItem>();
		itemsInCart=cartItemRepository.findByShoppingCart(shoppingCart);
		
		//Checks if CartItem X in shopping Cart exists in coop's warehouse
		//and if not then it is removed from itemsInCart about to be passed for sale
		for(CartItem ci: itemsInCart)
		{
			if(cartItemRepository.checkAvailabilityAndUpdate(ci.getProduct(),ci.getQty())==0)
			{
				itemsUnavailable.add(ci.getProduct());
				itemsInCart.remove(ci);
			}
		}
				
		if(itemsInCart.isEmpty())
		{
			return itemsUnavailable;
		}
		else
		{
			BigDecimal total=calculateGrandTotal(itemsInCart);
			ClientOrder clientOrder=new ClientOrder();
			clientOrder.setUserRole(shoppingCart.getUserRole());
			clientOrder.setTotal(total);
			clientOrder.setBillingAddress(billingAddress);
			clientOrder.setShippingAddress(shippingAddress);
			clientOrder.setShippingMethod(shippingMethod);
			clientOrder.setCreditCard(creditCard);
			abstractSale=clientOrderService.createClientOrder(clientOrder);
			for(CartItem ci: itemsInCart)
			{
				ci.setShoppingCart(null);
				ci.setCurrentPrice(ci.getProduct().getOurPrice()); 
				ci.setAbstractSale(abstractSale);
				cartItemRepository.save(ci);
			}
			transactionService.oneWayTransaction(total, abstractSale);
			
			
			return itemsUnavailable;
		}
	}
	
	@Override
	public ClientOrder commitAndGetSale(ShoppingCart shoppingCart,CreditCard creditCard, Address billingAddress,Address shippingAddress,String shippingMethod)
	{
		AbstractSale abstractSale=new ClientOrder();
		HashSet<Product> itemsUnavailable=new HashSet<Product>();
		HashSet<CartItem> itemsInCart=new HashSet<CartItem>();
		itemsInCart=cartItemRepository.findByShoppingCart(shoppingCart);
		
		//Checks if CartItem X in shopping Cart exists in coop's warehouse
		//and if not then it is removed from itemsInCart about to be passed for sale
		for(CartItem ci: itemsInCart)
		{
			if(cartItemRepository.checkAvailabilityAndUpdate(ci.getProduct(),ci.getQty())==0)
			{
				itemsUnavailable.add(ci.getProduct());
				itemsInCart.remove(ci);
			}
		}
				
		if(itemsInCart.isEmpty())
		{
			return null;
		}
		else
		{
			BigDecimal total=calculateGrandTotal(itemsInCart);
			ClientOrder clientOrder=new ClientOrder();
			clientOrder.setUserRole(shoppingCart.getUserRole());
			clientOrder.setTotal(total);
			clientOrder.setBillingAddress(billingAddress);
			clientOrder.setShippingAddress(shippingAddress);
			clientOrder.setShippingMethod(shippingMethod);
			clientOrder.setCreditCard(creditCard);
			clientOrder=clientOrderService.createClientOrder(clientOrder);
			abstractSale=clientOrder;
			for(CartItem ci: itemsInCart)
			{
				ci.setShoppingCart(null);
				ci.setCurrentPrice(ci.getProduct().getOurPrice()); 
				ci.setAbstractSale(abstractSale);
				cartItemRepository.save(ci);
			}
			transactionService.oneWayTransaction(total, abstractSale);
			
			
			return clientOrder;
		}
	}
	
	@Override
	public boolean removeCartItem(Long id)
	{
		if(cartItemRepository.deleteByIdAndShoppingCartId(id,findById(id).getShoppingCart().getId())>0)
		{
			LOG.info("\n\nSUCCESS: Removed cartItem {} from shoppingCart\n",id);
			return true;
		}
		LOG.info("\n\nFAILURE: Removing cartItem {} failed miserably.\n",id);
		return false;
	}
	
	@Override
	public boolean emptyCart(Long shoppingCartId)
	{
		if(cartItemRepository.deleteByShoppingCartId(shoppingCartId)>0)
		{
			LOG.info("\n\nSUCCESS: Shopping Cart cleared.");
			return true;
		}
		LOG.info("\n\nFAILURE: Emptying the Shopping Cart {} failed miserably.");
		return false;
	}
	
	
	private BigDecimal calculateGrandTotal(HashSet<CartItem> itemsInCart)
	{
		BigDecimal grandTotal=BigDecimal.valueOf(0);
			
		for(CartItem ci: itemsInCart)
		{
			grandTotal=grandTotal.add(ci.getProduct().getOurPrice().multiply(BigDecimal.valueOf(ci.getQty())));
		}
		
		return grandTotal;
	}
	
	@Override
	public BigDecimal getGrandTotal(ShoppingCart shoppingCart)
	{
		BigDecimal grandTotal=calculateGrandTotal(cartItemRepository.findByShoppingCart(shoppingCart));
		return grandTotal;
	}
	

	@Override
	public List<CartItem> findByClientOrder(ClientOrder clientOrder) {
		return cartItemRepository.findByAbstractSale(clientOrder);
	}

	@Override
	public HashSet<CartItem> findByShoppingCart(ShoppingCart shoppingCart) {
		return cartItemRepository.findByShoppingCart(shoppingCart);
	}

	@Override
	public CartItem findById(Long cartItemId) {
		return cartItemRepository.findOne(cartItemId);
	}


	@Override
	public List<CartItem> findByAbstractSale(AbstractSale abstractSale) {
		return cartItemRepository.findByAbstractSale(abstractSale);
	}
	
}
