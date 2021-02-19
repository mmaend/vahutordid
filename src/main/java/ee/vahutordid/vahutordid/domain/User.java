package ee.vahutordid.vahutordid.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ee.vahutordid.vahutordid.domain.security.Authority;
import ee.vahutordid.vahutordid.domain.security.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usr")
public class User implements UserDetails{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(nullable = false, updatable = false)
	private Long id;
	@Column(unique = true ,nullable = false, updatable = true)
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	
	@Column(unique = true ,nullable = false, updatable = true)
	private String email;
	private String phone;
	
	
	private boolean enabled=true;
	
	@OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<UserRole> userRoles = new HashSet<>();
	
	
	
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Set<UserRole> getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorites = new HashSet<>();
		userRoles.forEach(ur -> authorites.add(new Authority(ur.getRole().getName())));
		
		return authorites;
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}
	public void updateUser(User user) {
		// TODO Auto-generated method stub
		this.email = user.email;
		this.enabled = user.enabled;
		this.username = user.username;
		this.firstName = user.firstName;
		this.phone = user.phone;
		this.lastName = user.lastName;
	}
	
	
	
}
