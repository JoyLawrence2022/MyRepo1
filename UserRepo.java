package com.sportyshoes.repositories;

import java.util.Optional;

import com.sportyshoes.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer>{
	//findByUserId method is available in JpaRepository
	//findByUserName method is not available in JpaRepository
	Optional<User> findByUserName(String userName);  
}
