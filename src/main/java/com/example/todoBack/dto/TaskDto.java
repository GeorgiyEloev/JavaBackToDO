package com.example.todoBack.dto;

import lombok.Data;

@Data
public class TaskDto {
  private String name;
  private String text;
  private Long id;
  private Boolean isCheck;
}
