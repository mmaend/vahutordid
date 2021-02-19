package ee.vahutordid.vahutordid.controller;

import java.security.Principal;
import java.util.List;

import ee.vahutordid.vahutordid.domain.Product;
import ee.vahutordid.vahutordid.domain.User;
import ee.vahutordid.vahutordid.domain.Wishlist;
import ee.vahutordid.vahutordid.domain.security.UserRole;
import ee.vahutordid.vahutordid.service.ProductService;
import ee.vahutordid.vahutordid.service.UserRoleService;
import ee.vahutordid.vahutordid.service.UserService;
import ee.vahutordid.vahutordid.service.WishlistProductService;
import ee.vahutordid.vahutordid.utility.WishlistUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private WishlistProductService wishlistProductService;
	
	@Autowired
	private ProductService productService;

	@RequestMapping("/")
	public String wishlist(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		UserRole userRole = userRoleService.findByUserAndRole(user, "ROLE_CLIENT");
		Wishlist wishlist = userRole.getWishlist();
		List<Product> productList = WishlistUtility.getProductList(wishlist);
		model.addAttribute("wishlistProductList", wishlist.getWishlistProductList());
		model.addAttribute("productList", productList);
		model.addAttribute("wishlist", wishlist);
		
		for (Product p : productList) {
			if (p.getInStockNumber() > 0 && p.isActive()) {
				model.addAttribute("productsAvailable", true);
				break;
			}
		}
		
		return "wishlist";
	}

	@RequestMapping("/addItem")
	public String addItem(
			@ModelAttribute("product") Product product,
			Model model, Principal principal, RedirectAttributes redirectAttributes
			) {
		
		User user = userService.findByUsername(principal.getName());
		UserRole userRole = userRoleService.findByUserAndRole(user, "ROLE_CLIENT");
		
		product = productService.findOne(product.getId());
		
		boolean addProductSuccess;
		
		if (wishlistProductService.addToWishlist(userRole.getWishlist(), product)) {
			addProductSuccess = true;
			redirectAttributes.addAttribute("addProductSuccess", addProductSuccess);
		}else {
			addProductSuccess = false;
			redirectAttributes.addAttribute("addProductSuccess", addProductSuccess);
		}
		
		return "redirect:/products/productDetail2?id="+product.getId();
	}
	
	
	@RequestMapping("/removeItem")
	public String removeItem(@RequestParam("wishlistProductId") Long wishlistProductId) {
		wishlistProductService.removeFromWishlist(wishlistProductService.findOne(wishlistProductId));
		return "redirect:/wishlist/";
	}
}
