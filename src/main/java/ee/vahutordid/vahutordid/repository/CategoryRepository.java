package ee.vahutordid.vahutordid.repository;


import ee.vahutordid.vahutordid.domain.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {

	Category findByType(String type);
	
	
	
}
