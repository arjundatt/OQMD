package DIC.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 3/28/15
 * Time: 9:29 AM
 */
public class Pong implements KeyListener {
    // initialize globals - pos and vel encode vertical info for paddles
    final int FRAME_WIDTH = 600;
    final int FRAME_HEIGHT = 400;
    final int BALL_RADIUS = 20;
    final int PAD_WIDTH = 8;
    final int PAD_HEIGHT = 80;
    final int HALF_PAD_WIDTH = PAD_WIDTH / 2;
    final int HALF_PAD_HEIGHT = PAD_HEIGHT / 2;
    final boolean LEFT = false;
    final boolean RIGHT = true;
    double[] ball_pos = {FRAME_WIDTH / 2, FRAME_HEIGHT / 2};
    double[] vel = {1, 1};
    int score1 = 0;
    int score2 = 0;
    int paddle1_vel = 0;
    int paddle2_vel = 0;
    int[] paddle1_pos = {0, 0};
    int[] paddle2_pos = {0, 0};
    JFrame frame = new JFrame();

    // initialize ball_pos and ball_vel for new bal in middle of table
    // if direction is RIGHT, the ball's velocity is upper right, else upper left
    public void spawn_ball(boolean direction) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MyDraw drawPanel = new MyDraw();

        frame.getContentPane().add(drawPanel);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setVisible(true);

        vel[0] = (int) (Math.random() * 120 + 60) / 60;
        vel[1] = (int) (Math.random() * 120 + 120) / 60;
        ball_pos[0] = FRAME_WIDTH / 2;
        ball_pos[1] = FRAME_HEIGHT / 2;
        if (direction == LEFT)
            vel[0] = -vel[0];

        while (true) {
            drawPanel.repaint();
            try {
                Thread.sleep(50);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // define event handlers
    public void new_game() {
        paddle1_pos[0] = HALF_PAD_WIDTH;
        paddle1_pos[1] = FRAME_HEIGHT / 2;
        paddle2_pos[0] = FRAME_WIDTH - HALF_PAD_WIDTH;
        paddle2_pos[1] = FRAME_HEIGHT / 2;
        score1 = 0;
        score2 = 0;
        spawn_ball(LEFT);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent key) {
        System.out.println(key);
        /*if (key == simplegui.KEY_MAP["W"]:
        paddle1_vel -= 4
        elif key == simplegui.KEY_MAP["S"]:
        paddle1_vel += 4
        elif key == simplegui.KEY_MAP["down"]:
        paddle2_vel += 4
        elif key == simplegui.KEY_MAP["up"]:
        paddle2_vel -= 4*/
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    class MyDraw extends JPanel {
        public void paintComponent(Graphics g) {
            g.setColor(Color.white);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            // draw mid line and gutters
            g.drawLine(FRAME_WIDTH / 2, 0, FRAME_WIDTH / 2, FRAME_HEIGHT);
            g.drawLine(PAD_WIDTH, 0, PAD_WIDTH, FRAME_HEIGHT);
            g.drawLine(FRAME_WIDTH - PAD_WIDTH, 0, FRAME_WIDTH - PAD_WIDTH, FRAME_HEIGHT);

            // update ball
            ball_pos[0] += vel[0];
            ball_pos[1] += vel[1];

            // if the ball stucks on top or bottom of the canvas
            if (ball_pos[1] <= BALL_RADIUS || ball_pos[1] >= FRAME_HEIGHT - 1 - BALL_RADIUS)
                vel[1] = -vel[1];

            // draw ball
            g.setColor(Color.RED);
            g.drawOval((int) ball_pos[0], (int) ball_pos[1], BALL_RADIUS, BALL_RADIUS);

            // update paddle 's vertical position, keep paddle on the screen
            paddle1_pos[1] += paddle1_vel;
            if (paddle1_pos[1] <= HALF_PAD_HEIGHT || paddle1_pos[1] >= FRAME_HEIGHT - HALF_PAD_HEIGHT)
                paddle1_pos[1] -= paddle1_vel;

            paddle2_pos[1] += paddle2_vel;
            if (paddle2_pos[1] <= HALF_PAD_HEIGHT || paddle2_pos[1] >= FRAME_HEIGHT - HALF_PAD_HEIGHT)
                paddle2_pos[1] -= paddle2_vel;

            // draw paddles
            int x1 = paddle1_pos[0];
            int y1 = paddle1_pos[1] - HALF_PAD_HEIGHT;
            int x2 = paddle1_pos[0];
            int y2 = paddle1_pos[1] + HALF_PAD_HEIGHT;

            int x3 = paddle2_pos[0];
            int y3 = paddle2_pos[1] - HALF_PAD_HEIGHT;
            int x4 = paddle2_pos[0];
            int y4 = paddle2_pos[1] + HALF_PAD_HEIGHT;

            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(PAD_WIDTH));
            g2.setColor(Color.blue);
            g2.draw(new Line2D.Float(x1, y1, x2, y2));
            g2.setColor(Color.red);
            g2.draw(new Line2D.Float(x3, y3, x4, y4));

            // determine whether paddle and ball collide
            if ((ball_pos[0] <= BALL_RADIUS + PAD_WIDTH) && (Math.abs(ball_pos[1] - paddle1_pos[1]) <= HALF_PAD_HEIGHT))
                vel[0] = vel[0] * -1.1;
            else if ((ball_pos[0] >= FRAME_WIDTH - 1 - BALL_RADIUS - PAD_WIDTH) && (Math.abs(ball_pos[1] - paddle2_pos[1]) <= HALF_PAD_HEIGHT))
                vel[0] = vel[0] * -1.1;
            else if (ball_pos[0] <= BALL_RADIUS + PAD_WIDTH) {
                score2 += 1;
                spawn_ball(RIGHT);
            } else if (ball_pos[0] >= FRAME_WIDTH - 1 - BALL_RADIUS - PAD_WIDTH) {
                score1 += 1;
                spawn_ball(LEFT);
            }

            // draw scores
            ((Graphics2D) g).drawString(((Integer) score1).toString(), FRAME_WIDTH / 4, 50);
            ((Graphics2D) g).drawString(((Integer) score2).toString(), FRAME_WIDTH / 4 * 3, 50);

        }
    }

    public static void main(String[] args) {
        Pong pong = new Pong();
        pong.new_game();
    }
}
