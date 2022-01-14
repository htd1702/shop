package com.shop.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shop.common.entities.Brand;
import com.shop.common.entities.Category;
import com.shop.common.entities.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateProduct() {
		Brand brand = entityManager.find(Brand.class, 1);
		Category category = entityManager.find(Category.class, 8);

		Product product = new Product();
		product.setName("Acer laptop new 2022");
		product.setAlias("acer_laptop_new_2022");
		product.setShortDescription("A good laptop in Brand Acer version");
		product.setFullDescription("This is a very good laptop full description as Brand Acer");

		product.setBrand(brand);
		product.setCategory(category);

		product.setPrice(678);
		product.setCost(600);
		product.setEnabled(true);
		product.setInStock(true);

		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());

		Product saveProduct = repo.save(product);
		assertThat(saveProduct.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllProducts() {
		Iterable<Product> iterableProducts = repo.findAll();
		iterableProducts.forEach(System.out::println);
	}

	@Test
	public void testGetProduct() {

		Product product = repo.findById(2).get();
		System.out.println(product);

		assertThat(product).isNotNull();

	}
	
	@Test
	public void testUpdateProduct() {
		Integer id = 1;
		Product product = repo.findById(id).get();
		product.setPrice(499);
		
		repo.save(product);
		Product updatedProduct = entityManager.find(Product.class, id);
		assertThat(updatedProduct.getPrice()).isEqualTo(499);
	}

	@Test
	public void testDeleteProduct() {
		Integer id = 3;
		repo.deleteById(id);
		Optional<Product> result = repo.findById(id);
		assertThat(!result.isPresent());
	}
	
	@Test
	public void testSaveProductWithImages() {
		Integer productId = 1;
		Product product= repo.findById(productId).get();
		
		product.setMainImage("main image.jpg");
		product.addExtraImage("extra image 1.png");
		product.addExtraImage("extra_image_2.png");
		product.addExtraImage("extra-image3.png");
		
		Product saveProduct = repo.save(product);
		
		assertThat(saveProduct.getImages().size()).isEqualTo(3);
	}
	
	@Test
	public void testSaveProductWithDetails() {
		Integer productId=2;
		Product product = repo.findById(productId).get();
		
		product.addDetail("Devide Memory","128gb");
		product.addDetail("CPU Model","Meditek");
		product.addDetail("OS","Android");
		
		Product savedProduct = repo.save(product);
		assertThat(savedProduct.getDetails()).isNotEmpty();
	}
}
