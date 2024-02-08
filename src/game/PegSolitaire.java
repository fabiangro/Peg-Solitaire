package game;

import java.io.Serializable;
import java.util.ArrayList;

public class PegSolitaire implements Serializable {
    private Board board;
    private boolean active;
    private int moves;
    private String variant;
    public final static int CENTER_X = 3;
    public final static int CENTER_Y = 3;
    private Cell marked = null;
    private int pegs;
    private final Cell[] DIRECTIONS = {new Cell(0, 2), new Cell(0, -2), new Cell(2, 0), new Cell(-2, 0)};

    public boolean isActive() {
        return active;
    }

    public String getVariant() {
        if (variant.equals("en")) {
            return "english";
        } else {
            return "european";
        }
    }

    public ArrayList<Cell> getBorders() {
        return board.getBorders();
    }

    public PegSolitaire(String variant) {
        this.variant = variant;
        board = new Board(variant);
        pegs = board.getNonempty();
        active = true;
        moves = 0;
    }

    public int[][] getBoard() {
        return board.get_board();
    }

    public int getPegs() {
        return pegs;
    }

    public ArrayList<Cell> getPossibleMoves(int x, int y) {
        int cell = board.get(x, y);
        ArrayList<Cell> moves = new ArrayList<>();

        if (cell < 1) {
            return moves;
        }

        for (Cell c : DIRECTIONS) {
            int new_x = x + c.x;
            int new_y = y + c.y;
            int middle_x = x + c.x / 2;
            int middle_y = y + c.y / 2;

            Cell middle = new Cell(middle_x, middle_y);
            Cell end = new Cell(new_x, new_y);
            if (Board.inRange(new_x, new_y, middle_x, middle_y)) {
                if (board.get(end) == 0 && board.get(middle) == 1) {
                    moves.add(end);
                }
            }
        }
        return moves;
    }

    public void move(Cell end) {
        if (active) {
            if (board.get(end) == 0 && marked != null) {
                ArrayList<Cell> possible_moves = getPossibleMoves(marked.x, marked.y);

                if (possible_moves.contains(end)) {
                    Cell middle = new Cell((marked.x + end.x) / 2, (marked.y + end.y) / 2);
                    board.set(marked, 0);
                    board.set(middle, 0);
                    board.set(end, 1);

                    marked = null;
                    pegs--;
                    moves++;
                    isFinished();
                }
            } else if (marked != null && marked.equals(end)) {
                marked = null;
            } else if (board.get(end) == 1) {
                marked = end;
            }
        }
    }

    public Cell getMarked() {
        return marked;
    }


    public boolean isWon() {
        return pegs == 1 && board.get(CENTER_X, CENTER_Y) == 1;
    }

    public boolean isFinished() {
        int size = board.getSize();
        for (int x=0; x<size; x++) {
            for (int y=0; y<size; y++) {
                if (! getPossibleMoves(x, y).isEmpty()) {
                    return false;
                }
            }
        }
        active = false;
        return true;
    }

}
