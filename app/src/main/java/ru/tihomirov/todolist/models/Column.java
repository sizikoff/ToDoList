package ru.tihomirov.todolist.models;

public class Column {

    private int id;
    private String name;
    private int order;

    public Column(int id, String name, int order) {
        this.id = id;
        this.name = name;
        this.order = order;
    }

    public Column(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
