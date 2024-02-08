package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class AboutDialog extends JDialog implements ActionListener {
    public AboutDialog(JFrame parent, String title, String message) {
        super(parent, title, true);
        if (parent != null) {
            Dimension parentSize = parent.getSize();
            Point p = parent.getLocation();
            setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
        }
        JPanel messagePane = new JPanel();
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        messagePane.add(textArea);
        getContentPane().add(messagePane);
        JPanel buttonPane = new JPanel();
        JButton button = new JButton("OK");
        buttonPane.add(button);
        button.addActionListener(this);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        dispose();
    }

}
