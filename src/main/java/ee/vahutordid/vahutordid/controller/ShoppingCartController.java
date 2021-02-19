package ee.vahutordid.vahutordid.controller;

import java.security.Principal;
import java.util.HashSet;

import javax.websocket.server.PathParam;


import ee.vahutordid.vahutordid.domain.CartItem;
import ee.vahutordid.vahutordid.domain.Product;
import ee.vahutordid.vahutordid.domain.ShoppingCart;
import ee.vahutordid.vahutordid.domain.User;
import ee.vahutordid.vahutordid.domain.security.UserRole;
import ee.vahutordid.vahutordid.service.CartItemService;
import ee.vahutordid.vahutordid.service.ProductService;
import ee.vahutordid.vahutordid.service.ShoppingCartService;
import ee.vahutordid.vahutordid.service.UserRoleService;
import ee.vahutordid.vahutordid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ShoppingCartService shoppingCartService;

	@RequestMapping("/cart")
	public String shoppingCart(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		UserRole userRole = userRoleService.findByUserAndRole(user, "ROLE_CLIENT");
		
		
		ShoppingCart shoppingCart = userRole.getShoppingCart();
		
		HashSet<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		
		for(CartItem ci:cartItemList)
		{
			ci.setCurrentPrice(ci.getProduct().getOurPrice());
		}
		
		shoppingCartService.setGrandTotal(shoppingCart);
		
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", shoppingCart);
		
		return "shoppingCart";
	}
	
	@RequestMapping("/cart2")
	public String shoppingCart(Model model, Principal principal, @PathParam("emptyCart") boolean emptyCart, @PathParam("notEnoughStock") boolean notEnoughStock) {
		User user = userService.findByUsername(principal.getName());
		UserRole userRole = userRoleService.findByUserAndRole(user, "ROLE_CLIENT");
		
		
		ShoppingCart shoppingCart = userRole.getShoppingCart();
		
		HashSet<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		
		for(CartItem ci:cartItemList)
		{
			ci.setCurrentPrice(ci.getProduct().getOurPrice());
		}
		
		shoppingCartService.setGrandTotal(shoppingCart);
		
		model.addAttribute("notEnoughStock",notEnoughStock);
		model.addAttribute("emptyCart", emptyCart);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", shoppingCart);
		
		return "shoppingCart";
	}

	@RequestMapping("/addItem")
	public String addItem(
			@ModelAttribute("product") Product product,
			@ModelAttribute("qty") String qty,
			Model model, Principal principal, RedirectAttributes redirectAttributes
			) {
		
		User user = userService.findByUsername(principal.getName());
		UserRole userRole = userRoleService.findByUserAndRole(user, "ROLE_CLIENT");
		
		product = productService.findOne(product.getId());
		
		
		
		if (Integer.parseInt(qty) > product.getInStockNumber()) {
			model.addAttribute("notEnoughStock", true);
			return "redirect:/products/productDetail?id="+product.getId();
		}
		
		boolean addProductSuccess;
		
		if (cartItemService.addToCart(userRole.getShoppingCart(), product, Integer.parseInt(qty))) {
			addProductSuccess = true;
			redirectAttributes.addAttribute("addProductSuccess", addProductSuccess);
		}else {
			addProductSuccess = false;
			redirectAttributes.addAttribute("addProductSuccess", addProductSuccess);
		}
		
		return "redirect:/products/productDetail2?id="+product.getId();
	}
	
	@RequestMapping("/updateCartItem")
	public String updateShoppingCart(
			@ModelAttribute("cartItemId") Long cartItemId,
			@ModelAttribute("qty") int qty
			) {
		CartItem cartItem = cartItemService.findById(cartItemId);
		if (qty <= cartItem.getProduct().getInStockNumber() && qty > 0)
				cartItemService.updateToCart(cartItem, qty);
		
		return "redirect:/shoppingCart/cart";
	}
	
	@RequestMapping("/removeItem")
	public String removeItem(@RequestParam("cartItemId") Long cartItemId) {
		cartItemService.removeCartItem(cartItemId);
		return "redirect:/shoppingCart/cart";
	}
}
