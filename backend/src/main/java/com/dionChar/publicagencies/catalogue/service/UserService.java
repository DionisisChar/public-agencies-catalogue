package com.dionChar.publicagencies.catalogue.service;

import java.util.Optional;

import com.dionChar.publicagencies.catalogue.model.User;

public interface UserService {

	Optional<User> findByEmail(String email);
}
