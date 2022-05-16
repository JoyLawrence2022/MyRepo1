package com.sportyshoes.controllers;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


import com.sportyshoes.entities.Request;
import com.sportyshoes.entities.Response;
import com.sportyshoes.exceptions.DisabledUserException;
import com.sportyshoes.exceptions.InvalidUserCredentialsException;
import com.sportyshoes.repositories.UserRepo;
import com.sportyshoes.services.OrderService;
import com.sportyshoes.services.UserAuthService;
import com.sportyshoes.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(value = "http://localhost:4200")
public class JwtRestApi {
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private UserAuthService userAuthService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserRepo userRepository;
	@PostMapping("/signin")
	public ResponseEntity<Response> generateJwtToken(@RequestBody Request request){
		Authentication authentication = null;
		try {
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getUserPwd()));
		} catch(DisabledException e) {
			throw new DisabledUserException("User inactive");
		} catch(BadCredentialsException e) {
			throw new InvalidUserCredentialsException("Invalid credentials");
		}
		//below is Spring security User class
		User user = (User) authentication.getPrincipal();
		Set<String> roles = user.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet());
		String token = jwtUtil.generateToken(authentication);

		Response response = new Response();
		response.setToken(token);
		response.setRoles(roles.stream().collect(Collectors.toList()));
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody Request request) {
		userAuthService.saveUser(request);
		return new ResponseEntity<String>("User successfully registered", HttpStatus.OK);
	}

	//Fetching user based on user_id
	@GetMapping("/{id}")
	//@GetMapping("/id")
	public ResponseEntity<String> findUserById(@PathVariable(value = "id") long id) {
		Optional<com.sportyshoes.entities.User> user = userRepository.findById((int) id);

		if(user.isPresent()) {
			return ResponseEntity.ok().body(user.get().getUserName());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	//Fetching user based on user_name

	@GetMapping("/name/{userName}")
	public ResponseEntity<String> findUserByUserName(@PathVariable(value = "userName") String userName) {
		Optional<com.sportyshoes.entities.User> user = userRepository.findByUserName(userName);

		if(user.isPresent()) {
			return ResponseEntity.ok().body(user.get().getUserName());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

/*
	@GetMapping("/users")
	public ResponseEntity<List<com.sportyshoes.entities.User>> getUsers(){
		List<com.sportyshoes.entities.User> users = userAuthService.getUsers();
		return ResponseEntity.ok(users);
	}

*/

	@GetMapping("/users")
	public ResponseEntity<List<String>> getUsers(){
		List<String> list = userAuthService.getUsers();
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/orders")
	public ResponseEntity<List<com.sportyshoes.entities.Order>> getOrders(){
		List<com.sportyshoes.entities.Order> orders = orderService.getOrders();
		return ResponseEntity.ok(orders);
	}
	
	
}
