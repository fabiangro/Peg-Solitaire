package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


public class Board implements Serializable {
    private int[][] board;
    private final static int SIZE = 7;
    private final int CENTER_X = 3;
    private final int CENTER_Y = 3;
    private final Cell CENTER = new Cell(CENTER_X, CENTER_Y);
    private int non_empty;

    private ArrayList<Cell> borders;

    public Board(String variant) {
        if (variant.equals("en")) {
            initEnglish();
        } else if (variant.equals("eu")) {
            initEuropean();
        } else {
            throw new IllegalArgumentException("Board variant can be only eu or en");
        }
    }


    public int getNonempty() {
        return non_empty;
    }

    public int getSize() {
        return SIZE;
    }

    public ArrayList<Cell> getBorders() {
        return borders;
    }
    public int get(int x, int y) {
        if (0 <= x && x <= SIZE && 0 <= y && y <= SIZE) {
            return board[x][y];
        }
        return -1;
    }

    public void set(int x, int y, int val) {
        Cell c = new Cell(x, y);
        if (0 <= x && x <= SIZE && 0 <= y && y <= SIZE && !borders.contains(c)) {
            board[x][y] = val;
        }
    }

    public int[][] get_board() {
        return board;
    }

    public void set(Cell c, int val) {
        set(c.x, c.y, val);
    }

    public int get(Cell c) {
        return get(c.x, c.y);
    }

    private void initBoard() {
        board = new int[SIZE][SIZE];
        non_empty = SIZE * SIZE;

        for (int[] row : board) {
            Arrays.fill(row, 1);
        }
        board[CENTER.x][CENTER.y] = 0;
        non_empty--;
    }

    public static boolean inRange(int ... values) {
        for (int i : values) {
            if (i < 0 || i >= SIZE) {
                return false;
            }
        }
        return true;
    }

    private void initEnglish() {
        initBoard();
        borders = new ArrayList<>();

        int[] cells = {0, 1, 5, 6};

        for (int i : cells) {
            for (int j : cells) {
                borders.add(new Cell(i, j));
                board[i][j] = -1;
                non_empty--;
            }
        }
    }

    private void initEuropean() {
        initBoard();
        borders = new ArrayList<>();
        
        int[] cells = {0, 1, 5, 6};

        for (int i : cells) {
            for (int j : cells) {
                if ( !(i == 1 && (j == 1 || j == 5))
                  && !(i == 5 && (j == 1 || j == 5))) {
                    borders.add(new Cell(i, j));
                    board[i][j] = -1;
                    non_empty--;
                }
                
            }
        }
    }

}
