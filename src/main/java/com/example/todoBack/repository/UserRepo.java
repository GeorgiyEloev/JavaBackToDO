package com.example.todoBack.repository;

import com.example.todoBack.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<UserEntity, Long> {
  UserEntity findByEmail(String email);
}
