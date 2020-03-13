package de.bpmn.springrestdocs.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.bpmn.springrestdocs.domain.Task;
import de.bpmn.springrestdocs.service.TaskService;


/**
 * TestTaskController
 */
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@WebMvcTest(TaskController.class)
public class TaskControllerTest extends BaseControllerTest {

  @MockBean
  private TaskService TasksService;

    @Test
  void getTasks() throws Exception {
    when(this.TasksService.getTasks(any())).thenReturn(Collections.singletonList(new Task()));

    mockMvc.perform(get("/api/v1/tasks")
    .header("Authorization", "Bearer 12"))
      .andExpect(status().isOk());
  }

  @Test
  void getTask() throws Exception {
    mockMvc.perform(get("/api/v1/tasks/{id}", 1)
        .header("Authorization", "Bearer 12"))
    .andExpect(status().isOk());
  }

  @Test
  void insertTask() throws Exception {
    var task = new Task().setName("WorkItem Insert");
    mockMvc.perform(post("/api/v1/tasks")
    .header("Authorization", "Bearer 12")
    .contentType("application/json").content(objectMapper.writeValueAsString(task)))
        .andExpect(status().isOk());
  }
  @Test
  void updateTask() throws Exception {
    var task = new Task().setName("WorkItem update");
    mockMvc.perform(post("/api/v1/tasks/{id}",3)
        .contentType(
            "application/json")
    .header("Authorization", "Bearer 12")
    .content(objectMapper.writeValueAsString(task)))
        .andExpect(status().isOk());
  }

  @Test
  void deleteTask() throws Exception {
    mockMvc.perform(delete("/api/v1/tasks/{id}", 1)
        .header("Authorization", "Bearer 12"))
        .andExpect(status().isOk());
  }
}