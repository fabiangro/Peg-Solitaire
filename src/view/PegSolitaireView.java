package view;

import game.Board;
import game.Cell;
import game.PegSolitaire;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class PegSolitaireView extends JFrame implements ActionListener {
    private String variant;
    private JMenuBar menuBar;
    private JMenu gameMenu, movesMenu, settingsMenu, helpMenu;

    private JMenuItem startItem, exitItem;

    private JRadioButtonMenuItem englishItem, europeanItem;
    private JMenuItem pegColor, boardColor;
    private JMenuItem markItem, upItem, downItem, leftItem, rightItem;
    private JMenuItem aboutGameItem, aboutAppItem;
    private final int BOARD_SIZE = 7;

    private JLabel stateLabel;
    private PegSolitaire game;
    private JPanel mainPanel;
    private final BoardPanel boardPanel;
    private JPanel panelPanel;

    private void handleKeyMove(int keyEvent) {
        if (game != null && game.isActive()) {
            Cell keyMarked = boardPanel.getKeyMarked();

            if (keyMarked == null) {
                boardPanel.setKeyMarked(new Cell(PegSolitaire.CENTER_X, PegSolitaire.CENTER_Y));
            } else {
                int x = keyMarked.x;
                int y = keyMarked.y;

                switch (keyEvent) {
                    case KeyEvent.VK_UP:
                        x--;
                        break;
                    case KeyEvent.VK_DOWN:
                        x++;
                        break;
                    case KeyEvent.VK_RIGHT:
                        y++;
                        break;
                    case KeyEvent.VK_LEFT:
                        y--;
                        break;
                    case KeyEvent.VK_ENTER:
                        if (boardPanel.getMarked() != null && boardPanel.getMarked().equals(keyMarked)) {
                            boardPanel.setKeyMarked(null);
                        }
                        game.move(keyMarked);
                        boardPanel.setMarked(game.getMarked());
                        break;
                }
                Cell new_keyMarked = new Cell(x, y);

                if (boardPanel.getKeyMarked() != null && !game.getBorders().contains(new_keyMarked) && Board.inRange(x, y)) {
                    boardPanel.setKeyMarked(new Cell(x, y));
                }
            }

            updateState();
            boardPanel.repaint();
        }
    }

    private void updateState() {
        String text;
        if (game == null) {
            text = "Choose board type from menu and start new game";
        } else if (game.isActive()) {
            text = "Game is running " + "(" + game.getVariant() + " board)" + " pegs= " + game.getPegs();
        } else {
            if (game.isWon()) {
                text = "You won!";
            } else {
                text = "You lost with " + game.getPegs() + " pegs";
            }
            englishItem.setEnabled(true);
            europeanItem.setEnabled(true);
        }
        stateLabel.setText(text);
    }

    private void saveGame() throws IOException {
        FileOutputStream fos = new FileOutputStream("solitaire.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(game);
        oos.flush();
        oos.close();
    }

    private boolean loadGame() throws IOException, ClassNotFoundException {
        try {
            FileInputStream fis = new FileInputStream("solitaire.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            game = (PegSolitaire) ois.readObject();
            ois.close();
            File temp_file = new File("solitaire.ser");
            temp_file.delete();
            return true;
        }
        catch (FileNotFoundException e) {
            return false;
        }
    }

    private void initMenu() {
        menuBar = new JMenuBar();

        gameMenu = new JMenu("Game");
        gameMenu.setMnemonic(KeyEvent.VK_G);
        movesMenu = new JMenu("Moves");
        settingsMenu = new JMenu("Settings");
        helpMenu = new JMenu("Help");

        startItem = new JMenuItem("New game", KeyEvent.VK_S);
        startItem.setEnabled(false);
        exitItem = new JMenuItem("Exit", KeyEvent.VK_Q);

        startItem.addActionListener(this);
        exitItem.addActionListener(this);

        gameMenu.add(startItem);
        gameMenu.add(new JSeparator());
        gameMenu.add(exitItem);

        markItem = new JMenuItem("choose peg");
        markItem.setAccelerator(KeyStroke.getKeyStroke("ENTER"));
        upItem = new JMenuItem("move up");
        upItem.setAccelerator(KeyStroke.getKeyStroke("UP"));
        downItem = new JMenuItem("move down");
        downItem.setAccelerator(KeyStroke.getKeyStroke("DOWN"));
        leftItem = new JMenuItem("move left");
        leftItem.setAccelerator(KeyStroke.getKeyStroke("LEFT"));
        rightItem = new JMenuItem("move right");
        rightItem.setAccelerator(KeyStroke.getKeyStroke("RIGHT"));

        markItem.addActionListener(this);
        upItem.addActionListener(this);
        downItem.addActionListener(this);
        leftItem.addActionListener(this);
        rightItem.addActionListener(this);

        movesMenu.add(markItem);
        movesMenu.add(upItem);
        movesMenu.add(downItem);
        movesMenu.add(rightItem);
        movesMenu.add(leftItem);

        ButtonGroup group = new ButtonGroup();
        englishItem = new JRadioButtonMenuItem("english");
        europeanItem = new JRadioButtonMenuItem("european");

        englishItem.addActionListener(this);
        europeanItem.addActionListener(this);

        group.add(englishItem);
        group.add(europeanItem);

        pegColor = new JMenuItem("peg color");
        boardColor = new JMenuItem("board color");

        pegColor.addActionListener(this);
        boardColor.addActionListener(this);

        settingsMenu.add(englishItem);
        settingsMenu.add(europeanItem);
        settingsMenu.add(pegColor);
        settingsMenu.add(boardColor);

        aboutGameItem = new JMenuItem("About game");
        aboutAppItem = new JMenuItem("About author");

        aboutGameItem.addActionListener(this);
        aboutAppItem.addActionListener(this);

        helpMenu.add(aboutGameItem);
        helpMenu.add(aboutAppItem);

        menuBar.add(gameMenu);
        menuBar.add(movesMenu);
        menuBar.add(settingsMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    public PegSolitaireView() {
        boardPanel = new BoardPanel();

        try {
            if (loadGame()) {
                boardPanel.board = game.getBoard();
            }
        }
        catch (ClassNotFoundException | IOException ignored) {}

        panelPanel.setLayout(new GridLayout(1, 1));
        panelPanel.add(boardPanel);

        setContentPane(mainPanel);
        setTitle("Peg Solitaire");
        updateState();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (game != null && game.isActive()) {
                    try {
                        saveGame();}
                    catch (IOException ignore) {}
                }
                dispose();
            }
        });

        setSize(600, 650);

        boardPanel.setFocusable(true);
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (game != null && game.isActive()) {
                    Component source = (Component) e.getSource();
                    int row = (int) ((double) e.getY() / source.getHeight() * BOARD_SIZE);
                    int col = (int) ((double) e.getX() / source.getWidth() * BOARD_SIZE);
                    Cell c = new Cell(row, col);

                    game.move(c);
                    boardPanel.setMarked(game.getMarked());
                    updateState();
                    repaint();
                }
            }
        });

        initMenu();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (source == startItem) {
            game = new PegSolitaire(variant);
            boardPanel.board = game.getBoard();

            updateState();
        }
        if (source == exitItem) {
            if (game != null && game.isActive()) {
                try {
                    saveGame();}
                catch (IOException ignore) {}
            }
            dispose();
        }
        if (source == englishItem) {
            variant = "en";
            startItem.setEnabled(true);
        }
        if (source == europeanItem) {
            variant = "eu";
            startItem.setEnabled(true);
        }
        if (source == pegColor) {
            Color color = JColorChooser.showDialog(null, "Choose peg color", boardPanel.getDefaultPegColor());
            boardPanel.setPegColor(color);
        }
        if (source == boardColor) {
            Color color = JColorChooser.showDialog(null, "Choose peg color", boardPanel.getDefaultBoardColor());
            boardPanel.setBoardColor(color);
        }
        if (source == markItem) {
            handleKeyMove(KeyEvent.VK_ENTER);
        }
        if (source == upItem) {
            handleKeyMove(KeyEvent.VK_UP);
        }
        if (source == downItem) {
            handleKeyMove(KeyEvent.VK_DOWN);
        }
        if (source == leftItem) {
            handleKeyMove(KeyEvent.VK_LEFT);
        }
        if (source == rightItem) {
            handleKeyMove(KeyEvent.VK_RIGHT);
        }
        if (source == aboutGameItem) {
            new AboutDialog(this, "About game", "rules:\nhttps://en.wikipedia.org/wiki/Peg_solitaire");
        }
        if (source == aboutAppItem) {
            new AboutDialog(this, "About app", "Author: Fabian Grodek\nVersion: 1.0\nRelease date: 24.12.2023");
        }
        repaint();

    }

    @Override
    public Dimension getPreferredSize() {
        return super.getPreferredSize();
    }
}
