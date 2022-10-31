package com.example.todoBack.model;

import lombok.Data;

@Data
public class Token {
  public Token() {}

  private String accessToken;
  private String accessTokenExpires;
}
