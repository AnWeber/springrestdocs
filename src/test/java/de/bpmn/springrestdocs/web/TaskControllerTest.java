package de.bpmn.springrestdocs.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.cli.CliDocumentation;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import capital.scalable.restdocs.AutoDocumentation;
import capital.scalable.restdocs.jackson.JacksonResultHandlers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import de.bpmn.springrestdocs.domain.Task;
import de.bpmn.springrestdocs.service.TaskService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import com.fasterxml.jackson.databind.ObjectMapper;



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

    mockMvc.perform(get("/api/v1/tasks"))
      .andExpect(status().isOk());
  }

  @Test
  void getTask() throws Exception {
    mockMvc.perform(get("/api/v1/tasks/{id}", 1)).andExpect(status().isOk());
  }

  @Test
  void insertTask() throws Exception {
    var task = new Task().setName("WorkItem Insert");
    mockMvc.perform(post("/api/v1/tasks")
    .contentType("application/json").content(objectMapper.writeValueAsString(task)))
        .andExpect(status().isOk());
  }
  @Test
  void updateTask() throws Exception {
    var task = new Task().setName("WorkItem update");
    mockMvc.perform(post("/api/v1/tasks/{id}", 3)
    .contentType("application/json")
    .content(objectMapper.writeValueAsString(task)))
        .andExpect(status().isOk());
  }

  @Test
  void deleteTask() throws Exception {
    mockMvc.perform(delete("/api/v1/tasks/{id}", 1))
        .andExpect(status().isOk());
  }
}