package DIC.Test;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 4/12/15
 * Time: 10:31 AM
 */
public class SampleSwingProgram {
    public static void main(String[] args) {
        SampleSwingProgram gui = new SampleSwingProgram();
        gui.go();
    }

    public void go() {
        JFrame frame = new JFrame("My first java program"); //this is the frame
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

        JLabel nameLabel = new JLabel("Name: ");
        JTextField nameTextField = new JTextField(10);
        topPanel.add(nameLabel);
        topPanel.add(nameTextField);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

        JLabel classLabel = new JLabel("Class: ");
        JTextField classTextField = new JTextField(10);
        bottomPanel.add(classLabel);
        bottomPanel.add(classTextField);

        frame.add(topPanel);
        frame.add(bottomPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 100); //setting the size of the frame
        frame.setVisible(true); //make the frame visible
    }
}
