package ee.vahutordid.vahutordid.repository;

import ee.vahutordid.vahutordid.domain.Category;
import ee.vahutordid.vahutordid.domain.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;



public interface ProductRepository extends CrudRepository<Product, Long> {
	Product findByName(String name);
	
	ArrayList<Product> findByNameContaining(String name);

	List<Product> findByCategory(Category category);

	List<Product> findByActiveTrue();

	List<Product> findByCategoryAndActiveTrue(Category category);

	ArrayList<Product> findByNameContainingAndActiveTrue(String name);
	
}
