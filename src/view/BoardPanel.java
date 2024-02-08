package view;

import game.Cell;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {

    private static final int BOARD_SIZE = 7;
    public int[][] board = null;
    public Cell marked = null;
    private Cell keyMarked = null;
    private final Color defaultBoardColor = new Color(255, 255, 204);
    private final Color defaultPegColor = new Color(101, 51, 0);
    private Color boardColor = new Color(255, 255, 204);
    private Color pegColor = new Color(101, 51, 0);


    public Color getDefaultBoardColor() {
        return defaultBoardColor;
    }

    public Color getDefaultPegColor() {
        return defaultPegColor;
    }

    public void setPegColor(Color c) {
        pegColor = c;
    }

    public void setBoardColor(Color c) {
        boardColor = c;
    }

    public Cell getMarked() {
        return marked;
    }

    public Cell getKeyMarked() {
        return keyMarked;
    }


    public void setKeyMarked(Cell keyMarked) {
        this.keyMarked = keyMarked;
    }

    public BoardPanel() {
    }

    public void setMarked(Cell c) {
        marked = c;
    }
    public BoardPanel(int[][] board) {
        this.board = board;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (board != null) {
            Graphics2D g2d = (Graphics2D) g;
            for (int row = 0; row < BOARD_SIZE; row++) {
                for (int col = 0; col < BOARD_SIZE; col++) {
                    double cellWidth = (double) getWidth() / BOARD_SIZE;
                    double cellHeight = (double) getHeight() / BOARD_SIZE;

                    int x = (int) (col * cellWidth);
                    int y = (int) (row * cellHeight);

                    if (board[row][col] >= 0) {
                        if (keyMarked != null && row == keyMarked.x && col == keyMarked.y) {
                            g2d.setColor(new Color(255, 102, 102));
                        } else {
                            g2d.setColor(boardColor);
                        }
                        g2d.fillRect(x, y, (int) cellWidth, (int) cellHeight);
                        g2d.setColor(Color.GRAY);
                        g2d.drawRect(x, y, (int) cellWidth, (int) cellHeight);

                        if (board[row][col] == 1) {
                            g2d.setStroke(new BasicStroke(10));
                            g2d.setColor(Color.BLACK);

                            int height = (int) (cellHeight * 4/5);
                            int start_height = (int) (cellHeight/2) - height/2;

                            int width = (int) (cellWidth * 4/5);
                            int start_width = (int) (cellWidth/2) - width/2;

                            g2d.drawOval(x + start_width, y + start_height, width, height);
                            g2d.setStroke(new BasicStroke(1));

                            g2d.setColor(pegColor);
                            g2d.fillOval(x + start_width, y + start_height, width, height);


                            if (marked != null && marked.x == row && marked.y == col) {
                                GradientPaint gradient = new GradientPaint(
                                        x, y, new Color(255, 0, 0, 100),
                                        x + (int) cellWidth, y + (int) cellHeight, new Color(255, 182, 193, 100));
                                g2d.setPaint(gradient);

                                g2d.fillOval(x +start_width, y + start_height, width, height);
                                g2d.setPaint(null);
                            }
                        }
                    }
                }
            }
        }
    }
}

