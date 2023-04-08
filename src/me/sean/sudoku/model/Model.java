package me.sean.sudoku.model;

import me.sean.sudoku.view.Observer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Model implements Config{

    private final Observer observer;
    private int size;
    private Cell[][] board;
    private Integer[] cursor = {0, 0};
    private GameState gameState = GameState.STANDARD;
    enum GameState {
        STANDARD("Fill in the grid!"),
        WIN("You Win!"),
        INVALID("Invalid!");
        String message;

        String getMessage() {
            return this.message;
        }
        GameState(String message) {
            this.message = message;
        }
    }


    public Model(String path, Observer observer) {
        this.size = 0;
        this.observer = observer;
        try(FileReader fr = new FileReader(path)) {
            BufferedReader br = new BufferedReader(fr);
            String size = br.readLine();
            try {
                this.size = Integer.parseInt(size);
            } catch(NumberFormatException nfe) {
                System.err.println("Invalid Input File Format");
                System.exit(-2);
            }
            this.board = new Cell[this.size*this.size][this.size*this.size];
            for(int i = 0; i < this.size*this.size; i++) {
                String line = br.readLine();
                for(int j = 0; j < this.size*this.size; j++) {
                    String s = line.substring(j, j+1);
                    try {
                        if(s.equals("-")) {
                            this.board[i][j] = new Cell(null, true);
                        } else {
                            this.board[i][j] = new Cell(Integer.parseInt(s), false);
                        }

                    } catch(NumberFormatException nfe) {
                        System.err.println("Invalid Input File Format");
                        System.exit(-3);
                    } catch(IndexOutOfBoundsException iob) {
                        System.err.println("Invalid Input File Format");
                        System.exit(-3);
                    }
                }
            }
        } catch(IOException ioe) {
            System.err.println("File Not Found");
            System.exit(-1);
        }
    }

    private Model(Model copy) {
        this.observer = copy.observer;
        this.size = copy.getSize();
        this.board = new Cell[this.size*this.size][this.size*this.size];
        for(int i = 0; i < this.size*this.size; i++) {
            for(int j = 0; j < this.size*this.size; j++) {
                Integer item = copy.board[i][j].getValue();
                if(item == null) {
                    this.board[i][j] = new Cell(null, true);
                } else {
                    this.board[i][j] = new Cell(item.intValue(), true);
                }

            }
        }

        System.arraycopy(copy.cursor, 0, this.cursor, 0, 2);
        this.increaseCursor();
    }


    public String get(int a, int b) {
        Cell item = this.board[a][b];
        if(item == null) return "";
        return item.toString();
    }

    public int getSize() {
        return this.size;
    }

    public boolean isCursor(int a, int b) {
        if(this.cursor[0] == a && this.cursor[1] == b) {
            return true;
        }
        return false;
    }

    public void setCursor(int a, int b) {
        this.cursor[0] = a;
        this.cursor[1] = b;
        this.updateObserver("Cursor Moved");
    }

    public void setCell(Integer a) {
        if(this.gameState == GameState.WIN) return;
        this.board[this.cursor[0]][this.cursor[1]].setValue(a);
        if(!isValid()) {
            this.gameState = GameState.INVALID;
            System.out.println("Invalid");
        } else {
            this.gameState = GameState.STANDARD;
            System.out.println("Good");
        }
        if(isWon()) {
            System.out.println("Win");
            this.gameState = GameState.WIN;
        }
        this.updateObserver("Updated Cell");
    }

    public String getGameState() {
        return this.gameState.getMessage();
    }

    @Override
    public List<Config> generateChildren() {
        List<Config> list = new ArrayList<>();
        Cell item = this.board[this.cursor[0]][this.cursor[1]];
        if(item.getValue() != null) {
            list.add(new Model(this));
        } else {
            for(int i = 1; i <= this.size*this.size; i++) {
                this.board[cursor[0]][cursor[1]].setValue(i);
                list.add(new Model(this));
            }
        }
    return list;
    }

    private void increaseCursor() {
        this.cursor[1]++;
        if(cursor[1] == this.size*this.size) {
            cursor[0]++;
            cursor[1] = 0;
        }
    }
    @Override
    public boolean isWon() {
        for(int i = 0; i < this.size*this.size; i++) {
            ArrayList<Integer> vList = new ArrayList<>();
            ArrayList<Integer> hList = new ArrayList<>();
            for(int j = 0; j < this.size*this.size; j++) {
                Cell vItem = this.board[j][i];
                Cell hItem = this.board[i][j];
                if(hItem.getValue() == null || vItem.getValue() == null) return false;
                if(vList.contains(vItem.getValue()) || hList.contains(hItem.getValue())) return false;
                vList.add(vItem.getValue());
                hList.add(hItem.getValue());
            }
        }
        return true;
    }

    @Override
    public boolean isValid() {
        for(int i = 0; i < this.size*this.size; i++) {
            ArrayList<Integer> vList = new ArrayList<>();
            ArrayList<Integer> hList = new ArrayList<>();
            for(int j = 0; j < this.size*this.size; j++) {
                Cell vItem = this.board[j][i];
                Cell hItem = this.board[i][j];
                if(hItem.getValue() != null) {
                    if(hList.contains(hItem.getValue())) return false;
                }
                if(vItem.getValue() != null) {
                    if(vList.contains(vItem.getValue())) {
                        return false;
                    }
                }
                vList.add(vItem.getValue());
                hList.add(hItem.getValue());
            }
        }
        return true;
    }


    public void updateObserver(String message) {
        observer.update(this, message);
    }

    @Override
    public String toString() {
        String object = "";
        for(Cell[] row : this.board) {
            for(Cell item : row) {
                String cell;
                if(item == null) {
                    cell = "[]";
                } else {
                    cell = "[" + item + "]";
                }
                object += cell;
            }
            object +="\n";
        }
        return object;
    }



}
