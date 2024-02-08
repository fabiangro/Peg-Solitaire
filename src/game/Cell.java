package game;

import java.io.Serializable;

public class Cell implements Serializable {
    public int x;
    public int y;
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (! (o instanceof Cell c)) {
            return false;
        } else {
            return x == c.x && y == c.y;
        }
    }
}
