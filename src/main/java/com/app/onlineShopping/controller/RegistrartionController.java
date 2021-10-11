package com.app.onlineShopping.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.onlineShopping.model.User;
import com.app.onlineShopping.repository.RegistrartionRepository;
import com.app.onlineShopping.service.RegistrationService;

@RestController
public class RegistrartionController {
	
	@Autowired
	private RegistrationService service;
	
	@Autowired
	private RegistrartionRepository repo;
	
	@GetMapping("/getUser/{userId}")
	@CrossOrigin(origins = "http://localhost:4200")
	public User getUser(@PathVariable(value = "userId") int id) throws Exception,Throwable {
		return service.fetchUserByUserId(id);
	}
	
	@PostMapping("/registeruser")
	@CrossOrigin(origins = "http://localhost:4200")
	public User registerUser(@RequestBody User user) throws Exception,Throwable {
		String tempEmailId = user.getEmailId();
		
		if(tempEmailId != null && !"".equals(tempEmailId)) {
			User userObj = service.fetchUserByEmailId(tempEmailId);
			if(userObj != null)
				throw new Exception("User With Email ID "+ tempEmailId + " Already Exists");
		}
		User userObj = null;
		userObj = service.saveUser(user);
		return userObj;
	}
	
	@PostMapping("/login")
	@CrossOrigin(origins = "http://localhost:4200")
	public User loginUser(@RequestBody User user) throws Exception ,Throwable{
		String tempEmailId = user.getEmailId(); 
		String tempPassword = user.getPassword();
		User userObj = null;
		if(tempEmailId != null && tempPassword != null) {
			userObj = service.fetchUserByEmailIdAndPassword(tempEmailId, tempPassword);
		}
		if(userObj == null)
			throw new Exception("Bad Credentials");
		return userObj;
	}
	
	@PutMapping("/updateUser/{userId}")
	@CrossOrigin(origins = "http://localhost:4200")
	private ResponseEntity<Object> updateOrder(@RequestBody User user,@PathVariable(value = "userId") int id)   
	{  
		Optional<User> userRepo = Optional.ofNullable(repo.findById(id));
		if(!userRepo.isPresent())
			return ResponseEntity.notFound().build();
		user.setId(id);
		repo.save(user);
		return ResponseEntity.noContent().build();
	}
}
