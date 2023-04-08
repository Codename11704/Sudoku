package me.sean.sudoku.model;

import java.util.List;

public interface Config {

    List<Config> generateChildren();

    boolean isWon();

    boolean isValid();

}
