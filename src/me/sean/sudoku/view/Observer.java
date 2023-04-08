package me.sean.sudoku.view;

public interface Observer<Subject, Data> {
    void update(Subject model, Data message);
}
