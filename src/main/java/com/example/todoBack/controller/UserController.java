package com.example.todoBack.controller;

import com.example.todoBack.dto.UserDto;
import com.example.todoBack.entity.UserEntity;
import com.example.todoBack.exception.UserAlreadyExistException;
import com.example.todoBack.model.Message;
import com.example.todoBack.model.User;
import com.example.todoBack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity registration(@RequestBody UserEntity user) {
    try {
      User userSave = userService.registration(user);
      return ResponseEntity.ok(userSave);
    } catch (UserAlreadyExistException e) {
      Message mes = new Message();
      mes.setMessage("Email already exists");
      return ResponseEntity.status(401).body(mes);
    } catch (Exception e) {
      Message mes = new Message();
      mes.setMessage("Произошла ошибка");
      return ResponseEntity.badRequest().body(mes);
    }
  }

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody UserDto userDto) {
    try {
      User user = userService.login(userDto);
      return ResponseEntity.ok(user);
    } catch (UserAlreadyExistException e) {
      Message mes = new Message();
      mes.setMessage("Error");
      return ResponseEntity.status(401).body(mes);
    } catch (Exception e) {
      Message mes = new Message();
      mes.setMessage("Произошла ошибка");
      return ResponseEntity.badRequest().body(mes);
    }
  }
}
