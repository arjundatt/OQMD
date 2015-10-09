package DIC.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 3/28/15
 * Time: 9:23 AM
 */

import javax.swing.*;
import java.awt.*;

public class Animation {
    int x = 0, y = 0;

    public static void main(String[] args) {
        Animation gui = new Animation();
        gui.go();
    }

    public void go() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MyDraw drawPanel = new MyDraw();

        frame.getContentPane().add(drawPanel);
        frame.setSize(800, 500);
        frame.setVisible(true);
        animation(drawPanel);
    }

    public void animation(MyDraw drawPanel) {
        int addX = 1, addY = 1;
        while (true) {
            drawPanel.repaint();
            try {
                Thread.sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (x >= (drawPanel.getWidth() - 100) || x < 0)
                addX *= -1;
            if (y >= (drawPanel.getHeight() - 100) || y < 0)
                addY *= -1;
            x += addX;
            y += addY;
        }
    }

    class MyDraw extends JPanel {
        public void paintComponent(Graphics g) {
            g.setColor(Color.white);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());

            g.setColor(Color.orange);
            g.fillOval(x, y, 100, 100);
        }
    }
}