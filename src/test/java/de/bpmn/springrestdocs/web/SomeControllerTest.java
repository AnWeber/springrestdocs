package de.bpmn.springrestdocs.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * TestTaskController
 */
@ExtendWith({ SpringExtension.class, RestDocumentationExtension.class })
@WebMvcTest(SomeController.class)
public class SomeControllerTest extends BaseControllerTest {

  @Test
  void some() throws Exception {
    mockMvc.perform(get("/some").header("someHeader", "error")).andExpect(status().isOk());
  }
}