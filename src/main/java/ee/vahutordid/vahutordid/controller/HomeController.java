package ee.vahutordid.vahutordid.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;


import ee.vahutordid.vahutordid.domain.Product;
import ee.vahutordid.vahutordid.domain.User;
import ee.vahutordid.vahutordid.domain.Wishlist;
import ee.vahutordid.vahutordid.domain.security.PasswordResetToken;
import ee.vahutordid.vahutordid.domain.security.Role;
import ee.vahutordid.vahutordid.domain.security.UserRole;
import ee.vahutordid.vahutordid.service.CategoryService;
import ee.vahutordid.vahutordid.service.ProductService;
import ee.vahutordid.vahutordid.service.UserRoleService;
import ee.vahutordid.vahutordid.service.UserService;
import ee.vahutordid.vahutordid.service.impl.UserSecurityService;
import ee.vahutordid.vahutordid.utility.SecurityUtility;
import ee.vahutordid.vahutordid.utility.WishlistUtility;
import ee.vahutordid.vahutordid.utility.MailConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private MailConstructor mailConstructor;
	
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserSecurityService userSecurityService;

	@Autowired
	private UserRoleService userRoleService;

	@RequestMapping("/")
	public String index(Principal principal, Model model){
		if (principal != null) {
			User user = userService.findByUsername(principal.getName());
			UserRole userRole = userRoleService.findByUserAndRole(user, "ROLE_CLIENT");
			Wishlist wishlist = userRole.getWishlist();
			List<Product> productList = WishlistUtility.getProductList(wishlist);
			for (Product p : productList) {
				if (p.getInStockNumber() > 0 && p.isActive()) {
					model.addAttribute("productsAvailable", true);
					break;
				}
			}
		}
		return "index";
	}
	
	@RequestMapping("/hours")
	public String hours() {
		return "hours";
	}


	@RequestMapping("/about")
	public String about() {
		return "about";
	}
	
	@RequestMapping("/faq")
	public String faq() {
		return "faq";
	}
	
	@RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("classActiveLogin", true);
		return "myAccount";
	}
	
	@RequestMapping("/forgetPassword")
	public String forgetPassword(
			HttpServletRequest request,
			@ModelAttribute("email") String email,
			Model model
			) {

		model.addAttribute("classActiveForgetPassword", true);
		
		User user = userService.findByEmail(email);
		
		if (user == null) {
			model.addAttribute("emailNotExist", true);
			return "myAccount";
		}
		
		String password = SecurityUtility.randomPassword();
		
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);
		
		userService.save(user);
		
		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);
		
		String appUrl = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		
		SimpleMailMessage newEmail = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);
		
		mailSender.send(newEmail);
		
		model.addAttribute("forgetPasswordEmailSent", "true");
		
		return "myAccount";
	}
	
	@RequestMapping("/newUser")
	public String newUser(Locale locale, @RequestParam("token") String token, Model model) {
		PasswordResetToken passToken = userService.getPasswordResetToken(token);

		if (passToken == null) {
			String message = "Invalid Token.";
			model.addAttribute("message", message);
			return "redirect:/badRequest";
		}

		User user = passToken.getUser();
		String username = user.getUsername();

		UserDetails userDetails = userSecurityService.loadUserByUsername(username);

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		model.addAttribute("user", user);

		model.addAttribute("classActiveEdit", true);
		
		return "myProfile";
	}
	
	@RequestMapping(value="/newUser", method = RequestMethod.POST)
	public String newUserPost(
			HttpServletRequest request,
			@ModelAttribute("email") String userEmail,
			@ModelAttribute("username") String username,
			Model model
			) throws Exception{
		
		model.addAttribute("classActiveNewAccount", true);
		model.addAttribute("email", userEmail);
		model.addAttribute("username", username);
		
		
		User client = userService.findByUsername(username);
		if (client != null) {
			if (!userRoleService.hasThisRole("ROLE_CLIENT", client)) {
				userService.addRoleToExistingUser(client, "ROLE_CLIENT");
				model.addAttribute("addedRoleToExistingUser", true); //no other changes to existing user takes place
			}else {
				model.addAttribute("clientAlreadyExistsFailure", true);
			}
			return "myAccount";
		}else if (userService.findByEmail(userEmail) != null) {
			model.addAttribute("emailExists", true);
			return "myAccount";
		}
		
		User user = new User();
		
		String password = SecurityUtility.randomPassword();
		
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setUsername(username);
		user.setPassword(encryptedPassword);
		user.setEmail(userEmail);
		
		Role role=new Role();
		role.setRoleId(2);
		role.setName("ROLE_CLIENT");
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(user, role));
		user = userService.createUser(user, userRoles);
		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);

		String appUrl = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		
		SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);
		
		mailSender.send(email);
		model.addAttribute("emailSent", "true");

		return "myAccount";
	}
	
}