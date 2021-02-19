package ee.vahutordid.vahutordid.service;

import ee.vahutordid.vahutordid.domain.Category;

import java.util.List;



public interface CategoryService {
	
	Category save(Category category);

	List<Category> findAll();
	
	Category findOne(Long id);
	
	void removeOne(Long id);

	Category createCategory(Category category);

	
}
