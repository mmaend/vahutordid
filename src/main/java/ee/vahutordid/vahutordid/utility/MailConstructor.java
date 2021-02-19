package ee.vahutordid.vahutordid.utility;

import java.util.Locale;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import ee.vahutordid.vahutordid.domain.ClientOrder;
import ee.vahutordid.vahutordid.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import ee.vahutordid.vahutordid.service.CartItemService;

@Component
public class MailConstructor {
	@Autowired
	private Environment env;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private CartItemService cartItemService;
	
	public SimpleMailMessage constructResetTokenEmail(
			String contextPath, Locale locale, String token, User user, String password
			) {
		
		String url = contextPath + "/newUser?token="+token;
		String message = "\nPlease click on this link to verify your email and edit your personal information. Your password is: \n"+password;
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(user.getEmail());
		email.setSubject("Vahutordid - New Client");
		email.setText(url+message);
		email.setFrom(env.getProperty("support.email"));
		return email;
	}
	
	public MimeMessagePreparator constructOrderConfirmationEmail (User user, ClientOrder clientOrder, Locale locale) {
		Context context = new Context();
		context.setVariable("order", clientOrder);
		context.setVariable("user", user);
		context.setVariable("cartItemList", cartItemService.findByAbstractSale(clientOrder));
		String text = templateEngine.process("orderConfirmationEmailTemplate", context);
		
		MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
				email.setTo(user.getEmail());
				email.setSubject("Order Confirmation - "+clientOrder.getId());
				email.setText(text, true);
				email.setFrom(new InternetAddress("mojtestsajt5@gmail.com"));
			}
		};
		
		return messagePreparator;
	}
}
