package de.bpmn.springrestdocs.domain;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.experimental.Accessors;
/**
 * Task
 */
@Data
@Accessors(chain = true)
public class Task {
    /**
     * Id
     */
    @NotNull
    @Min(0)
    private int id;
    /**
     * Name des Tasks
     */
    @Size(max = 255)
    private String name;
    /**
     * Bereich
     */
    @Size(max = 50)
    private String section;
    /**
     * Kategorie
     */
    @Size(max = 50)
    private String category;
    /**
     * Fälligkeitsdatum
     */
    private Date dueDate;
    /**
     * Postkorb
     */
    @NotNull
    @Min(0)
    private int boxId;
}