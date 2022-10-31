package com.example.todoBack.service;

import com.example.todoBack.dto.TaskDto;
import com.example.todoBack.entity.TaskEntity;
import com.example.todoBack.entity.UserEntity;
import com.example.todoBack.exception.NotFoundException;
import com.example.todoBack.model.Message;
import com.example.todoBack.model.Task;
import com.example.todoBack.repository.TaskRepo;
import com.example.todoBack.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.security.sasl.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TaskService {

  private final TaskRepo taskRepo;
  private final UserRepo userRepo;

  @Autowired
  public TaskService(TaskRepo taskRepo, UserRepo userRepo) {
    this.taskRepo = taskRepo;
    this.userRepo = userRepo;
  }

  public Task create(String email, TaskEntity task) {
    UserEntity user = userRepo.findByEmail(email);
    task.setUser(user);
    task.setIsCheck(false);
    TaskEntity entity = taskRepo.save(task);
    return Task.toModel(entity);
  }

  public List<Task> getAllTasks(String email) {
    UserEntity user = userRepo.findByEmail(email);
    List<TaskEntity> tasks = taskRepo.findAllByUser(user);
    List<Task> taskList = new ArrayList<>();
    for (TaskEntity task : tasks) {
      taskList.add(Task.toModel(task));
    }
    taskList.sort((task1, task2) -> (int) (task1.getId() - task2.getId()));
    return taskList;
  }

  public Task getTask(String email, Long id) throws NotFoundException, AuthenticationException {
    TaskEntity task = checkUser(email,id);
    return Task.toModel(task);
  }


  public Task update(String email,TaskDto taskDto) throws AuthenticationException, NotFoundException {
    TaskEntity task = checkUser(email,taskDto.getId());

    task.setName(taskDto.getName()== null ?  task.getName() : taskDto.getName());
    task.setText(taskDto.getText()== null ?  task.getText() : taskDto.getText());
    task.setIsCheck(taskDto.getIsCheck() == null ?  task.getIsCheck() : taskDto.getIsCheck());
    return Task.toModel(taskRepo.save(task));
  }

  public Message deleteOne(String email, Long id) throws AuthenticationException, NotFoundException {
    checkUser(email, id);
    taskRepo.deleteById(id);
    Message mes = new Message();
    mes.setMessage("Task delete");
    return mes;
  }

  public Message deleteAll(String email) throws AuthenticationException, NotFoundException {
    UserEntity user = userRepo.findByEmail(email);
    List<TaskEntity> tasks = taskRepo.findAllByUser(user);
    int count = 0;
    for (TaskEntity task : tasks) {
      count++;
      taskRepo.deleteById(task.getId());
    }
    Message mes = new Message();
    mes.setMessage("Task " + count +" delete");
    return mes;
  }

  private TaskEntity checkUser(String email, Long id) throws NotFoundException, AuthenticationException {
    UserEntity user = userRepo.findByEmail(email);
    Optional<TaskEntity> optional = taskRepo.findById(id);

    if (optional.isEmpty()) {
      throw new NotFoundException("Error");
    }

    TaskEntity task = optional.get();

    if (!Objects.equals(task.getUser().getId(), user.getId())) {
      throw new AuthenticationException("Error");
    }
    return task;
  }

}
