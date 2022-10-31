package com.example.todoBack.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class UserEntity extends BaseEntity {
  public UserEntity() {}

  private String email;
  private String name;
  private String password;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private List<TaskEntity> tasks;
}
