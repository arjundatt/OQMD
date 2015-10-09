package DIC.component.rightview;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 8/14/13
 * Time: 1:06 PM
 *
 */
public class DefaultTableView extends JPanel implements Runnable {
    private JLabel label;

    public JLabel getLabel() {
        return label;
    }

    public DefaultTableView() {
        setLayout(new BorderLayout());
    }

    public Component refresh(Component table) {
        removeAll();
        add(BorderLayout.CENTER, table);
        return this;
    }

    public Component refresh(InstanceInfo view) {
        removeAll();
        add(BorderLayout.NORTH, view);
        return this;
    }

    public Component emptyDisplay() {
        removeAll();
        JLabel temp = new JLabel("Click on the table to display");
        temp.setVerticalAlignment(SwingConstants.CENTER);
        temp.setHorizontalAlignment(SwingConstants.CENTER);
        add(BorderLayout.CENTER, temp);
        return this;
    }

    public Component showProgress(boolean isIndeterminate) {
        removeAll();
        label = new JLabel("");
        Box box = Box.createVerticalBox();
        box.setAlignmentX(Component.CENTER_ALIGNMENT);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(isIndeterminate);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(Box.createVerticalGlue());
        box.add(progressBar);
        box.add(label);
        box.add(Box.createVerticalGlue());
        box.setBorder(BorderFactory.createEmptyBorder(0, 200, 0, 200));
        add(box);
        return this;
    }

    @Override
    public void run() {
        showProgress(true);
    }
}
