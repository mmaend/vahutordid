package ee.vahutordid.vahutordid.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import javax.websocket.server.PathParam;

import ee.vahutordid.vahutordid.domain.Category;
import ee.vahutordid.vahutordid.domain.Product;
import ee.vahutordid.vahutordid.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ee.vahutordid.vahutordid.service.CategoryService;
import ee.vahutordid.vahutordid.service.ProductService;
import ee.vahutordid.vahutordid.service.UserService;

@Controller
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private UserService userService;
	
	
	@RequestMapping("/productShelf")
	public String productShelf(Model model, Principal principal) {
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		
		List<Product> productList = productService.findAllActive();
		List<Category> categoryList = categoryService.findAll();
		
		model.addAttribute("productList", productList);
		model.addAttribute("activeAll",true);
		model.addAttribute("categoryList", categoryList);
		
		return "productShelf";
	}
	
	@RequestMapping("/productDetail")
	public String productDetail(
			@PathParam("id") Long id, Model model, Principal principal
			) {
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		
		Product product = productService.findOne(id);
		
		model.addAttribute("product", product);
		
		List<Integer> qtyList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		
		model.addAttribute("qtyList", qtyList);
		model.addAttribute("qty", 1);
		
		return "productDetail";
	}
	
	@RequestMapping("/productDetail2")
	public String productDetail(
			@PathParam("id") Long id, @PathParam("addProductSuccess") boolean addProductSuccess, 
			Model model, Principal principal
			) {
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		
		Product product = productService.findOne(id);
		
		model.addAttribute("product", product);
		model.addAttribute("addProductSuccess", addProductSuccess);
		
		List<Integer> qtyList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		
		model.addAttribute("qtyList", qtyList);
		model.addAttribute("qty", 1);
		
		return "productDetail";
	}
	
	
	
	
	


}