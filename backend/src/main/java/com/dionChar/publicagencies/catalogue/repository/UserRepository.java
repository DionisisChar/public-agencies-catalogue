package com.dionChar.publicagencies.catalogue.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dionChar.publicagencies.catalogue.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

	Optional<User> findByEmail(String email);
}
