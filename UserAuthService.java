package com.sportyshoes.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sportyshoes.entities.Request;
import com.sportyshoes.entities.User;
import com.sportyshoes.entities.UserRole;
import com.sportyshoes.repositories.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAuthService implements UserDetailsService{
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepo userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//if no User exists when below method is run, so we use orElseThrow
		User user = userRepository.findByUserName(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
		
		List<UserRole> userRoles = user.getUserRoles().stream().collect(Collectors.toList());
		
		List<GrantedAuthority> grantedAuthorities = userRoles.stream().map(r->{
			return new SimpleGrantedAuthority(r.getRole());	//r.getRole() returns the role name
		}).collect(Collectors.toList());	//roles provided by us are converted to granted authorities by Spring security
		
		return new org.springframework.security.core.userdetails.User(username, user.getUserPass(), grantedAuthorities);
	}

	
	//User registration and saving User
	public void saveUser(Request request) {
		if(userRepository.findByUserName(request.getUserName()).isPresent()) {
			throw new RuntimeException("User already exists");
		}
		User user = new User();
		user.setUserName(request.getUserName());
		//request.getUserPwd() fetches the raw password provided by Client
		user.setUserPass(passwordEncoder.encode(request.getUserPwd())); 
		//we are getting Roles as a String from Client, now we are saving the roles as Role object in db
		//r is a String
		user.setUserRoles(request.getRoles().stream().map(r->{  //mapping String roles into UserRole objects
			UserRole ur = new UserRole();
			ur.setRole(r);
			return ur;
		}).collect(Collectors.toSet()));//setUserRoles method accepts Set of roles, so converting to Set of roles
		userRepository.save(user);
	}
/*
	public List<User> getUsers(){
		return userRepository.findAll();
	}
*/
	public List<String> getUsers(){
	List<String> list1 = new ArrayList<String>();
	List<com.sportyshoes.entities.User> users = userRepository.findAll();
	for(int i=0;i<users.size();i++) {
	String s = users.get(i).getUserName();
	list1.add(s);
	}
	return list1;
	}
	
}
