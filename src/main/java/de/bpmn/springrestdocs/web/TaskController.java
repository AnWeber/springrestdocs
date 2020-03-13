package de.bpmn.springrestdocs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.bpmn.springrestdocs.domain.Task;
import de.bpmn.springrestdocs.domain.TaskFilter;
import de.bpmn.springrestdocs.service.TaskService;

@RestController
@RequestMapping("api/v1/tasks")
public class TaskController {
    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    /**
     * ermittle die Tasks anhand Filterkriterien
     * @param authorization Anmeldetoken
     * @param filter Filtereinstellungen für die Suche
     * @return Liste an Tasksn
     */
    @GetMapping()
    public List<Task> getTasks(
        @RequestHeader("Authorization") String authorization,
        TaskFilter filter) {
        return this.taskService.getTasks(filter);
    }

    /**
     * ermittle die Tasks anhand Filterkriterien
     * @param authorization Anmeldetoken
     * @param id Id des Tasks
     * @return der angefragte Task
     */
    @GetMapping("{id}")
    public Task getTask(
        @RequestHeader("Authorization") String authorization,
        @PathVariable("id") int id) {
        return this.taskService.getTask(id);
    }
    /**
     * einfügen eines Tasks
     * @param authorization Anmeldetoken
     * @param task der Task
     * @return der angefragte Task
     */
    @PostMapping
    public Task insertTask(
        @RequestHeader("Authorization") String authorization,
        @RequestBody Task task) {
        return this.taskService.insertTask(task);
    }

    /**
     * aktualisiere en Task
     * @param authorization Anmeldetoken
     * @param id Id des Tasks
     * @param task der Task
     * @return der angefragte Task
     */
    @PostMapping("{id}")
    public Task updateTask(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("id") int id,
            @RequestBody Task task) {
        return this.taskService.updateTask(task.setId(id));
    }

    /**
    * einen Task löschen
    * @param authorization Anmeldetoken
    * @param id Id des Tasks
    * @return der angefragte Task
    */
   @DeleteMapping("{id}")
   public void deleteTask(
        @RequestHeader("Authorization") String authorization,
        @PathVariable("id") int id) {
       this.taskService.deleteTask(id);
   }
}