package ru.tihomirov.todolist.models;

public class Task {

    private int id;
    private  int columnId;
    private int order;

    private String name;
    private String description;

    private boolean isDone;

    public Task(int id, int columnId, int order, String name, String description, boolean isDone) {
        this.id = id;
        this.columnId = columnId;
        this.order = order;
        this.name = name;
        this.description = description;
        this.isDone = isDone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getColumnId() {
        return columnId;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
