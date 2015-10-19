import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Arnab Saha on 10/14/15.
 */
public class Stopwatch {
    public static void main(final String args[]) {
        JFrame jFrame = new JFrame();
        final JTextField jTextField = new JTextField();
        jTextField.setSize(200, 100);
        jTextField.setFont(new Font("Serif", Font.BOLD, 18));
        jFrame.add(jTextField);
        jFrame.setAlwaysOnTop(true);
        jFrame.setSize(150, 100);
        jFrame.setVisible(true);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int[] timeInMinutes = {30, 1, 30, 1, 30, 1, 35, 10, 30, 1, 35};
            String[] displayValues = {"Issue Essay", "Time Off", "Argument Essay", "Time Off", "Verbal", "Time Off", "Quants", "Time Off", "Verbal", "Time Off", "Quants"};
            int min = timeInMinutes[0] - 1;
            int sec = 60;
            int currentPosition = 0;

            public void run() {
                String currentTimeLeft;
                if (--sec < 0) {
                    sec = 59;
                    min--;
                }
                if (min < 0 && timeInMinutes.length > currentPosition + 1) {
                    makeSound();
                    currentPosition++;
                    min = timeInMinutes[currentPosition] - 1;
                    sec = 59;
                }
                currentTimeLeft = min + ":" + (sec <= 9 ? "0" + sec : sec) + "\n" + displayValues[currentPosition];
                jTextField.setText(currentTimeLeft);

            }
        }, 0, 1000);
    }

    private static void makeSound() {
        byte[] buf = new byte[1];
        AudioFormat af = new AudioFormat((float) 44100, 8, 1, true, false);
        SourceDataLine sdl = null;
        try {
            sdl = AudioSystem.getSourceDataLine(af);
            sdl.open(af);
            sdl.start();
            for (int i = 0; i < 1000 * (float) 44100 / 1000; i++) {
                double angle = i / ((float) 44100 / 440) * 2.0 * Math.PI;
                buf[0] = (byte) (Math.sin(angle) * 100);
                sdl.write(buf, 0, 1);
            }
            sdl.drain();
            sdl.stop();
            sdl.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }
}
