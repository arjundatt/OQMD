package DIC.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StudentsDetails implements ActionListener, KeyListener {

    JTextField nameField, rollField, ageField, secField, schoolField, classField;
    JButton nameRedButton, rollRedButton, ageRedButton, secRedButton, schoolRedButton, classRedButton, submitButton;
    JFrame frame;
    JLabel nameDialogLabel, rollDialogLabel, ageDialogLabel, secDialogLabel, schoolDialogLabel, classDialogLabel;
    JDialog nameDialog, rollDialog, secDialog, schoolDialog, classDialog, ageDialog;
    JTextArea detailArea;

    public static void main(String[] args) {
        StudentsDetails gui = new StudentsDetails();
        gui.createDetail();
    }

    void createDetail() {

        //creating a frame.
        frame = new JFrame("STUDENT DETAILS");

        JPanel mergePanel = new JPanel();
        JPanel panel = new JPanel(new GridLayout(7, 3));
        JPanel submitPanel = new JPanel();

        //creating 7 labels.

        JLabel nameLabel = new JLabel("<html><font color=red> * </font><font color=black>Name</font></html>");
        JLabel rollLabel = new JLabel("<html><font color = red> * </font><font color=black>Roll</font></html>");
        JLabel ageLabel = new JLabel("<html><font color=red> * </font><font color=black>Age</font></html>");
        JLabel secLabel = new JLabel("<html><font color=red> * </font><font color=black>Sec</font></html>");
        JLabel schoolLabel = new JLabel("<html><font color=red> * </font><font color=black>School</font></html>");
        JLabel classLabel = new JLabel("<html><font color=red> * </font><font color=black>Class</font></html>");
        JLabel detailLabel = new JLabel(" Details");

        //creating 7 text fields.
        nameField = new JTextField(10);
        nameField.addKeyListener(this);
        rollField = new JTextField(2);
        ageField = new JTextField(2);
        secField = new JTextField(1);
        schoolField = new JTextField(10);
        classField = new JTextField(2);
        detailArea = new JTextArea();

        //creating 6 red buttons.
        nameRedButton = createRedButton(nameRedButton);
        rollRedButton = createRedButton(rollRedButton);
        ageRedButton = createRedButton(ageRedButton);
        secRedButton = createRedButton(secRedButton);
        classRedButton = createRedButton(classRedButton);
        schoolRedButton = createRedButton(schoolRedButton);

        //creating 6 labels which will get printed in each of the dialog box.
        nameDialogLabel = new JLabel("   only characters are allowed not numbers!!");
        rollDialogLabel = new JLabel("  only numbers are allowed not characters!!");
        ageDialogLabel = new JLabel("   only numbers are allowed not characters!!");
        secDialogLabel = new JLabel("   only a character is allowed not numbers!!");
        schoolDialogLabel = new JLabel("     only characters are allowed not numbers!!");
        classDialogLabel = new JLabel("     only numbers are allowed not characters!!");

        //creating 6 dialogs which will pop out if the user givers a wrong input.
        nameDialog = createDialog(nameDialog, nameDialogLabel);
        rollDialog = createDialog(rollDialog, rollDialogLabel);
        ageDialog = createDialog(ageDialog, ageDialogLabel);
        secDialog = createDialog(secDialog, secDialogLabel);
        schoolDialog = createDialog(schoolDialog, schoolDialogLabel);
        classDialog = createDialog(classDialog, classDialogLabel);


        //1st input
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(nameRedButton);


        //2nd input.
        panel.add(rollLabel);
        panel.add(rollField);
        panel.add(rollRedButton);

        //3rd input.
        panel.add(ageLabel);
        panel.add(ageField);
        panel.add(ageRedButton);

        //4th input.
        panel.add(secLabel);
        panel.add(secField);
        panel.add(secRedButton);

        //5th input.
        panel.add(schoolLabel);
        panel.add(schoolField);
        panel.add(schoolRedButton);

        //6th input.
        panel.add(classLabel);
        panel.add(classField);
        panel.add(classRedButton);

        panel.add(detailLabel);
        panel.add(detailArea);

        submitButton = new JButton("SUBMIT");
        submitButton.setEnabled(true);
        submitButton.addActionListener(this);

        submitPanel.add(submitButton);
        mergePanel.add(panel);
        mergePanel.add(submitPanel);

        frame.add(mergePanel);
        frame.setSize(300, 300);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
    }

    JButton createRedButton(JButton button) {
        button = new JButton("!");
        button.setForeground(Color.red);
        button.addActionListener(this);
        button.setVisible(false);
        button.setPreferredSize(new Dimension(5, 5));
        return button;
    }

    JDialog createDialog(JDialog dialog, JLabel label) {
        dialog = new JDialog();
        dialog.setSize(300, 100);
        dialog.add(label);
        dialog.setModal(true);
        dialog.setVisible(false);
        dialog.setLocationRelativeTo(null);
        return dialog;
    }

    public void actionPerformed(ActionEvent event) {

        String name = nameField.getText();
        String roll = rollField.getText();
        String age = ageField.getText();
        String sec = secField.getText();
        String school = schoolField.getText();
        String class1 = classField.getText();
        String detail = detailArea.getText();

        /*if(name == "" || roll == "" || class1 == "" || school == "" || age == "" || sec == "" )
            submitButton.setEnabled(false);
        else{*/
        submitButton.setEnabled(true);
        //}

        if (event.getSource() == submitButton) {
            System.out.print("\f");
            //checking whether the name is correct or not.
            for (int i = 0; i < name.length(); i++) {
                char store = name.charAt(i);
                if ((int) store > 64 && (int) store < 91)
                    nameRedButton.setVisible(false);
                else if ((int) store > 96 && (int) store < 123)
                    nameRedButton.setVisible(false);
                else if ((int) store == 32)
                    nameRedButton.setVisible(false);
                else
                    nameRedButton.setVisible(true);
            }

            //checking whether the roll no. is correct or not.
            for (int i = 0; i < roll.length(); i++) {
                char store = roll.charAt(i);
                if ((int) store > 48 && (int) store < 57)
                    rollRedButton.setVisible(false);
                else
                    rollRedButton.setVisible(true);
            }

            //checking whether age is correct or not.
            for (int i = 0; i < age.length(); i++) {
                char store = age.charAt(i);
                if ((int) store > 48 && (int) store < 57)
                    ageRedButton.setVisible(false);
                else
                    ageRedButton.setVisible(true);
            }

            //checking whether the section is correct or not.
            for (int i = 0; i < sec.length(); i++) {
                char store = sec.charAt(i);
                if (i != 0)
                    secRedButton.setVisible(true);
                else if ((int) store > 64 && (int) store < 91)
                    secRedButton.setVisible(false);
                else if ((int) store > 96 && (int) store < 123)
                    secRedButton.setVisible(false);
                else
                    secRedButton.setVisible(true);
            }

            //checking whether school is correct or not.
            for (int i = 0; i < school.length(); i++) {
                char store = school.charAt(i);
                if ((int) store > 64 && (int) store < 91)
                    schoolRedButton.setVisible(false);
                else if ((int) store > 96 && (int) store < 123)
                    schoolRedButton.setVisible(false);
                else if ((int) store == 32)
                    schoolRedButton.setVisible(false);
                else
                    schoolRedButton.setVisible(true);
            }

            //checking whether class is correct or not.
            for (int i = 0; i < class1.length(); i++) {
                char store = class1.charAt(i);
                if ((int) store > 48 && (int) store < 57)
                    classRedButton.setVisible(false);
                else
                    classRedButton.setVisible(true);
            }

            System.out.println("Name = " + name);
            System.out.println("Roll = " + roll);
            System.out.println("Class = " + class1);
            System.out.println("Section = " + sec);
            System.out.println("Age = " + age);
            System.out.println("School = " + school);
            System.out.println("DetailS = " + detail);

        } else if (event.getSource() == nameRedButton) {
            nameDialog.setVisible(true);
            nameDialog.setLocationRelativeTo(null);
        } else if (event.getSource() == rollRedButton) {
            rollDialog.setVisible(true);
            rollDialog.setLocationRelativeTo(null);
        } else if (event.getSource() == classRedButton) {
            classDialog.setVisible(true);
            classDialog.setLocationRelativeTo(null);
        } else if (event.getSource() == schoolRedButton) {
            schoolDialog.setVisible(true);
            schoolDialog.setLocationRelativeTo(null);
        } else if (event.getSource() == ageRedButton) {
            ageDialog.setVisible(true);
            ageDialog.setLocationRelativeTo(null);
        } else if (event.getSource() == secRedButton) {
            secDialog.setVisible(true);
            secDialog.setLocationRelativeTo(null);
        } else
            System.exit(0);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("key typed = " + e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}