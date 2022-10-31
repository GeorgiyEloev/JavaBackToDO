package com.example.todoBack.repository;

import com.example.todoBack.entity.TaskEntity;
import com.example.todoBack.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepo extends CrudRepository<TaskEntity, Long> {
  List<TaskEntity> findAllByUser(UserEntity user);
  void deleteAllByUser(UserEntity user);
}
