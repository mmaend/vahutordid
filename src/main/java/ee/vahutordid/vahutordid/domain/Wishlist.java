package ee.vahutordid.vahutordid.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ee.vahutordid.vahutordid.domain.security.UserRole;

import javax.persistence.*;
import java.util.List;


@Entity
public class Wishlist {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@OneToMany(mappedBy="wishlist", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JsonIgnore
	private List<WishlistProduct> wishlistProductList;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="user_role_id")
	private UserRole userRole;
	
	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<WishlistProduct> getWishlistProductList() {
		return wishlistProductList;
	}

	public void setWishlistProductList(List<WishlistProduct> wishlistProductList) {
		this.wishlistProductList = wishlistProductList;
	}
	
}
