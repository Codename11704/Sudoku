package me.sean.sudoku.model;

public class Cell {
    private Integer value;
    private boolean changeable;
    public Cell(Integer value, boolean changeable) {
        this.value = value;
        this.changeable = changeable;
    }

    public Integer getValue() {
        return this.value;
    }

    public void setValue(Integer value) {
        if(this.changeable) {
            this.value = value;
        }
    }

    @Override
    public String toString() {
        if(value == null) return "";
        return value.toString();
    }
}
