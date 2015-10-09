package DIC;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;

import DIC.component.formcomponent.*;
import DIC.login.LoginValidator;
import com.jidesoft.swing.JideButton;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 8/7/13
 * Time: 11:19 AM
 */

public class LogIn implements ActionListener, KeyListener {

    private JFrame logInFrame;
    private JideButton login;
    private JideButton cancel;
    private JPanel panel;
    private JLabel errorMsg;
    private PasswordField passField;
    private TextField textField1, textField2;

    LogIn() {
        logInFrame = new JFrame("Login");
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        logInFrame.setSize(360, 335);
        createLogIn();
        logInFrame.setResizable(false);
        logInFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        logInFrame.setVisible(true);
    }

    private void createLogIn() {
        JPanel loginPanel = new JPanel();
        JPanel pic = new JPanel();
        pic.add(BorderLayout.CENTER, new JLabel(new ImageIcon(getClass().getResource("resource\\image.png"))));
        ImageIcon icon = new ImageIcon(getClass().getResource("resource\\icon.png"));
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.X_AXIS));
        loginPanel.add(Box.createHorizontalGlue());
        login = new JideButton("  Login  ");
        login.setBorder(BorderFactory.createEtchedBorder());
        login.addActionListener(this);
        cancel = new JideButton(" Cancel ");
        cancel.setBorder(BorderFactory.createEtchedBorder());
        cancel.addActionListener(this);
        errorMsg = new JLabel();
        loginPanel.add(errorMsg);
        loginPanel.add(Box.createHorizontalStrut(10));
        loginPanel.add(login);
        loginPanel.add(Box.createHorizontalStrut(10));
        loginPanel.add(cancel);
        panel.add(pic);
        panel.add(Box.createVerticalStrut(5));
        textField1 = new TextField("User Name: ", "Arnab", 25, true);
        textField1.getTextField().addKeyListener(this);
        panel.add(textField1);
        panel.add(Box.createVerticalStrut(10));
        passField = new PasswordField("Password: ", "Arnab", 25, true);
        passField.getPasswordField().addKeyListener(this);
        panel.add(passField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JSeparator());
        panel.add(Box.createVerticalStrut(10));
        textField2 = new TextField("Server: ", "Arnab", 25, true);
        textField2.getTextField().addKeyListener(this);
        panel.add(textField2);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JSeparator());
        panel.add(Box.createVerticalStrut(10));
        panel.add(loginPanel);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 20, 15));
        logInFrame.getContentPane().add(BorderLayout.CENTER, panel);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        logInFrame.setIconImage(icon.getImage());
        logInFrame.setLocation(dim.width / 2 - logInFrame.getSize().width / 2, dim.height / 2 - logInFrame.getSize().height / 2);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            authenticate();
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void createSecondFrame() {
        new ApplicationWindows(textField1.getText());
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new LogIn();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == login) {
            authenticate();
        }
        if (e.getSource() == cancel) {
            System.exit(0);
        }
    }

    private void authenticate() {
        boolean result = LoginValidator.validate(textField1.getText(), passField.getText());
        if (result == true) {
            logInFrame.setVisible(false);
            logInFrame.dispose();
            createSecondFrame();
        } else {
            errorMsg.setText(String.format("<html>%s<font color='red'>%s</font></html>", errorMsg.getText(), "Invalid Username or password"));
        }
    }
}
