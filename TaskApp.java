package com.codility.tasks.hibernate.solution;

import org.springframework.data.jpa.repository.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import java.util.logging.Logger;
import java.util.NoSuchElementException;
import java.util.Optional;

@Entity
class Task {

  @Id
  @GeneratedValue(
    strategy = GenerationType.AUTO)
  private Long id;
  @Column(name = "description", 
          length = 200)
  @NotNull
  private String description;
  private Long priority;

  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  public Long getPriority() {
    return priority;
  }
  public void setPriority(Long priority) {
    this.priority = priority;
  }
}

@RestController
@RequestMapping("tasks")
class TaskController {
  private static Logger log = Logger.getLogger("Solution");
  private TaskService taskService;

  TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @PutMapping("/{id}")
  public ResponseEntity updateTask(
    @Valid @RequestBody Task task,
    @PathVariable Long id) {
      return ResponseEntity.ok().body(taskService.updateTask(id, task));
  }
}

@Service
class TaskService {
  private TaskRepository taskRepository;
  TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  public Task updateTask(Long id, Task task) {
    Optional<Task> foundTask = taskRepository.findById(id);
    return taskRepository.save(mapTask(task, foundTask.get()));
  }

  private Task mapTask(Task task, Task entity) {
    entity.setDescription(task.getDescription());
    if (task.getPriority() != null) {
      entity.setPriority(task.getPriority());
    }
    return entity;
  }
}

interface TaskRepository extends JpaRepository<Task, Long> {

}

class TaskErrorMessage {
  private Integer status;
  private String message;
  public Integer getStatus() {
    return status;
  }
  public void setStatus(Integer status) {
    this.status = status;
  }
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }

  TaskErrorMessage(Integer status, String message) {
    this.status = status;
    this.message = message;
  }
}

@RestControllerAdvice
class TaskRestExceptionHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler(NoSuchElementException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  protected ResponseEntity<Object> taskNotFound(NoSuchElementException ex) {
    return new ResponseEntity(
                  new TaskErrorMessage(404, "Cannot find task with given id"), 
                  new HttpHeaders(), 
                  HttpStatus.NOT_FOUND);
  }
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
    final MethodArgumentNotValidException ex,
    final HttpHeaders headers,
    final HttpStatus status,
    final WebRequest request) {
      return new ResponseEntity(
                  new TaskErrorMessage(400, "Task description is required"), 
                  new HttpHeaders(), 
                  HttpStatus.BAD_REQUEST);
    }
}
