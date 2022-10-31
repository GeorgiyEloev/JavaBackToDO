package com.example.todoBack.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class TaskEntity extends BaseEntity {
  public TaskEntity() {}

  private String name;
  private String text;
  private Boolean isCheck;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;
}
