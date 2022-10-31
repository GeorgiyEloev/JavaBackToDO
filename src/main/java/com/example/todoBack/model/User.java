package com.example.todoBack.model;

import com.example.todoBack.entity.UserEntity;
import lombok.Data;

@Data
public class User {
  public User() {
  }

  private Long id;
  private String email;
  private String name;
  private Token token = new Token();

  public static User toModel(UserEntity entity, String accessToken) {
    User model = new User();
    model.setId(entity.getId());
    model.setName(entity.getName());
    model.setEmail(entity.getEmail());
    Token token = new Token();
    token.setAccessToken(accessToken);
    token.setAccessTokenExpires("1d");
    model.setToken(token);
    return model;
  }
}
