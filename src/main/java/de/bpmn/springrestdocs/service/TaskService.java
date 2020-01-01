package de.bpmn.springrestdocs.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import de.bpmn.springrestdocs.domain.Task;
import de.bpmn.springrestdocs.domain.TaskFilter;

@Service
public class TaskService {

    public List<Task> getTasks(TaskFilter TaskFilter) {
        return Arrays.asList(new Task().setId(1).setName("Workitem 1"), new Task().setId(2).setName("Workitem 2"));
    }

    public Task getTask(int id) {
        return new Task().setId(1).setName("Workitem 1");
    }


    public Task insertTask(Task task) {
        return task.setId(3);
    }
    public Task updateTask(Task task) {
        return task;
    }
    public void deleteTask(int id) {

    }
}