package ee.vahutordid.vahutordid.service;

import ee.vahutordid.vahutordid.domain.Category;
import ee.vahutordid.vahutordid.domain.Product;

import java.util.ArrayList;
import java.util.List;



public interface ProductService {

	List<Product> findAll();
	
	Product findOne(Long id);
	
	ArrayList<Product> searchActive(String name);
	
	void removeOne(Long id);

	Product createProduct(Product product, String type);
	
	boolean restock(Long productId, int qty);
	
	void toggleActive(Product product);

	void deactivate(Long id);
	
	void activate(Long id);

	List<Product> findByCategory(Category category);
	
	List<Product> findAllActive();

	List<Product> findByCategoryAndActiveTrue(Category category);
}
