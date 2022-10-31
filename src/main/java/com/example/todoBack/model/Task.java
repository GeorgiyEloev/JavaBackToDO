package com.example.todoBack.model;

import com.example.todoBack.entity.TaskEntity;
import lombok.Data;

@Data
public class Task {
  public Task() {
    super();
  }

  private Long id;
  private String name;
  private String text;
  private Boolean isCheck;

  public static Task toModel(TaskEntity entity) {
    Task model = new Task();
    model.setId(entity.getId());
    model.setName(entity.getName());
    model.setText(entity.getText());
    model.setIsCheck(entity.getIsCheck());
    return model;
  }
}
