package com.example.todoBack.service;

import com.example.todoBack.dto.UserDto;
import com.example.todoBack.entity.UserEntity;
import com.example.todoBack.model.User;
import com.example.todoBack.repository.UserRepo;
import com.example.todoBack.exception.UserAlreadyExistException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

  private final UserRepo userRepo;
  private final JwtTokenProvider tokenService;

  @Autowired
  public UserService(UserRepo userRepo, JwtTokenProvider tokenService) {
    this.userRepo = userRepo;
    this.tokenService = tokenService;
  }

  public User registration(UserEntity user) throws UserAlreadyExistException {
    if (userRepo.findByEmail(user.getEmail()) != null){
      throw new UserAlreadyExistException("Пользователь с таким именем существует");
    }
    String hashed = BCrypt.hashpw(user.getPassword(),BCrypt.gensalt());
    user.setPassword(hashed);
    UserEntity entity = userRepo.save(user);
    String token = tokenService.createToken(user.getId(), user.getEmail());
    return User.toModel(entity, token);
  }

  public User login(UserDto userDto) throws UserAlreadyExistException {
    UserEntity user = userRepo.findByEmail(userDto.getEmail());
    if (user == null || !BCrypt.checkpw(userDto.getPassword(), user.getPassword())) {
      throw new UserAlreadyExistException("Error");
    }
    String token = tokenService.createToken(user.getId(), user.getEmail());
    return User.toModel(user, token);
  }

  public UserEntity findByEmail(String email) {
    UserEntity result = userRepo.findByEmail(email);
    return result;
  }

}
