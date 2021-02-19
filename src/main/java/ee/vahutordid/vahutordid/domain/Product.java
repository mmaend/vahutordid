package ee.vahutordid.vahutordid.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity
public class Product {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@Column(unique=true,nullable=false)
	private String name;
	private String ingredients;
	private Long inStockNumber = Long.valueOf(0);
	private BigDecimal weight;
	private BigDecimal ourPrice;
	private boolean active;
	private BigDecimal size;
	
	public Product() {}
	
	@Column(columnDefinition="text")
	private String description;
	
	@ManyToOne
	private Category category;
	
	@OneToMany(mappedBy="product", fetch=FetchType.LAZY)
	private Set<CartItem> cartItems;
	
	@OneToMany(mappedBy="product", fetch=FetchType.LAZY)
	private Set<MemberCartItem> memberCartItems;
	
	@OneToMany(mappedBy="product", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JsonIgnore
	private List<WishlistProduct> wishlistProductList;
	
	@Transient
	private MultipartFile productImage;

	public MultipartFile getProductImage() {
		return productImage;
	}

	public void setProductImage(MultipartFile productImage) {
		this.productImage = productImage;
	}

	public List<WishlistProduct> getWishlistProductList() {
		return wishlistProductList;
	}

	public void setWishlistProductList(List<WishlistProduct> wishlistProductList) {
		this.wishlistProductList = wishlistProductList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getInStockNumber() {
		return inStockNumber;
	}

	public void setInStockNumber(Long inStockNumber) {
		this.inStockNumber = inStockNumber;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getDescription() {
		return description;
	}

	public BigDecimal getOurPrice() {
		return ourPrice;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal getSize() {
		return size;
	}

	public void setSize(BigDecimal size) {
		this.size = size;
	}

	public Set<MemberCartItem> getMemberCartItems() {
		return memberCartItems;
	}

	public void setMemberCartItems(Set<MemberCartItem> memberCartItems) {
		this.memberCartItems = memberCartItems;
	}

	public void setOurPrice(BigDecimal ourPrice) {
		this.ourPrice = ourPrice;
	}

	public Set<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(Set<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
