package de.bpmn.springrestdocs.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class TaskFilter {
    /**
     * Ids für die Suche
     */
    private List<Integer> ids;
    /**
     * Id
     */
    private Integer id;
    /**
     * Name des Tasks
     */
    private String name;
    /**
     * Bereich
     */
    private String section;
    /**
     * Kategorie
     */
    private String category;
    /**
     * Fälligkeitsdatum
     */
    private Date dueDate;
    /**
     * Postkorb
     */
    private int boxId;
}