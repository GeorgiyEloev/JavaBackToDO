package com.example.todoBack.controller;

import com.example.todoBack.dto.TaskDto;
import com.example.todoBack.entity.TaskEntity;
import com.example.todoBack.exception.NotFoundException;
import com.example.todoBack.model.Message;
import com.example.todoBack.model.Task;
import com.example.todoBack.service.JwtTokenProvider;
import com.example.todoBack.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

  private final TaskService taskService;
  private final JwtTokenProvider tokenProvider;

  @Autowired
  public TaskController(TaskService taskService, JwtTokenProvider tokenProvider) {
    this.taskService = taskService;
    this.tokenProvider = tokenProvider;
  }

  @PostMapping()
  public ResponseEntity create(@RequestBody TaskEntity task, HttpServletRequest req) {
    try {
      String email = tokenProvider.emailForToken(req);
      Task taskSave = taskService.create(email,task);
      return ResponseEntity.ok(taskSave);
    } catch (AuthenticationException e ){
      Message mes = new Message();
      mes.setMessage("Authentication");
      return ResponseEntity.status(401).body(mes);
    } catch (Exception e) {
      Message mes = new Message();
      mes.setMessage("Произошла ошибка");
      return ResponseEntity.badRequest().body(mes);
    }
  }

  @GetMapping()
  public ResponseEntity getAllTask(HttpServletRequest req){
    try {
      String email = tokenProvider.emailForToken(req);
      List<Task> tasks = taskService.getAllTasks(email);
      return ResponseEntity.ok(tasks);
    } catch (AuthenticationException e ){
      Message mes = new Message();
      mes.setMessage("Authentication");
      return ResponseEntity.status(401).body(mes);
    } catch (Exception e) {
      Message mes = new Message();
      mes.setMessage("Произошла ошибка");
      return ResponseEntity.badRequest().body(mes);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity getTask(HttpServletRequest req, @PathVariable String id){
    try {
      String email = tokenProvider.emailForToken(req);
      Long idL = Long.parseLong(id, 10);
      Task task = taskService.getTask(email, idL );
      return ResponseEntity.ok(task);
    } catch (AuthenticationException e ){
      Message mes = new Message();
      mes.setMessage("Authentication");
      return ResponseEntity.status(401).body(mes);
    } catch (Exception e) {
      Message mes = new Message();
      mes.setMessage("Произошла ошибка");
      return ResponseEntity.badRequest().body(mes);
    }
  }

  @DeleteMapping("/one/{id}")
  public ResponseEntity deleteOne(HttpServletRequest req, @PathVariable String id)  {
    try {
      String email = tokenProvider.emailForToken(req);
      Long idL = Long.parseLong(id, 10);
      Message mes = taskService.deleteOne(email, idL);
      return ResponseEntity.ok(mes);
    } catch (AuthenticationException e ){
      Message mes = new Message();
      mes.setMessage("Authentication");
      return ResponseEntity.status(401).body(mes);
    }catch (NotFoundException e) {
      Message mes = new Message();
      mes.setMessage("NotFound");
      return ResponseEntity.status(404).body(mes);
    } catch (Exception e) {
      Message mes = new Message();
      mes.setMessage("Произошла ошибка");
      return ResponseEntity.badRequest().body(mes);
    }
  }

  @DeleteMapping("/all")
  public ResponseEntity deleteOne(HttpServletRequest req)  {
    try {
      String email = tokenProvider.emailForToken(req);
      Message mes = taskService.deleteAll(email);
      return ResponseEntity.ok(mes);
    } catch (AuthenticationException e ){
      Message mes = new Message();
      mes.setMessage("Authentication");
      return ResponseEntity.status(401).body(mes);
    }catch (NotFoundException e) {
      Message mes = new Message();
      mes.setMessage("NotFound");
      return ResponseEntity.status(404).body(mes);
    } catch (Exception e) {
      Message mes = new Message();
      mes.setMessage("Произошла ошибка");
      return ResponseEntity.badRequest().body(mes);
    }
  }

  @PatchMapping()
  public ResponseEntity update(HttpServletRequest req, @RequestBody TaskDto taskDto){
    try {
      String email = tokenProvider.emailForToken(req);
      Task task = taskService.update(email, taskDto);
      return ResponseEntity.ok(task);
    } catch (AuthenticationException e ) {
      Message mes = new Message();
      mes.setMessage("Authentication");
      return ResponseEntity.status(401).body(mes);
    } catch (NotFoundException e) {
      Message mes = new Message();
      mes.setMessage("NotFound");
      return ResponseEntity.status(404).body(mes);
    }catch (Exception e) {
      Message mes = new Message();
      mes.setMessage("Произошла ошибка");
      return ResponseEntity.badRequest().body(mes);
    }

  }

}
