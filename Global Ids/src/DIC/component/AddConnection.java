package DIC.component;

import DIC.component.formcomponent.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.event.*;

import static java.lang.String.format;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 8/12/13
 * Time: 5:48 PM
 */
public class AddConnection extends JDialog implements ActionListener {
    private JPanel fields;
    private JPanel topPanel;
    private JPanel buttons;
    private TextField connectionName, system, instanceName, port, userName;
    private JComboBox dbTypeComboBox;
    private PasswordField password;
    private JButton ok, cancel;
    private JLabel messageDisplay;

    public JLabel getMessageDisplay() {
        return messageDisplay;
    }

    public JButton getCancel() {
        return cancel;
    }

    public JButton getOk() {
        return ok;
    }

    public JPanel getFields() {
        return fields;
    }

    public JPanel getTopPanel() {
        return topPanel;
    }

    public JPanel getButtons() {
        return buttons;
    }

    public TextField getConnectionName() {
        return connectionName;
    }

    public TextField getSystem() {
        return system;
    }

    public TextField getInstanceName() {
        return instanceName;
    }

    public TextField getPort() {
        return port;
    }

    public TextField getUserName() {
        return userName;
    }

    public PasswordField getPassword() {
        return password;
    }

    public JComboBox getDbTypeComboBox() {
        return dbTypeComboBox;
    }

    public void init() {
        connectionName.setText("");
        instanceName.setText("");
        userName.setText("");
        password.setText("");
    }

    public AddConnection(JFrame frame) {
        super(frame);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        createAddConnection();
        setResizable(false);
        setVisible(true);
        setModal(true);
    }

    private void createAddConnection() {
        topPanel = new JPanel();
        fields = new JPanel();
        fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
        fields.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        connectionName = new TextField("Connection Name", 34, true);
        fields.add(connectionName);
        fields.add(Box.createVerticalStrut(20));
        system = new TextField("System", "192.168.0.1", 34, true);
        fields.add(system);
        fields.add(Box.createVerticalStrut(20));
        instanceName = new TextField("Instance Name", 34, true);
        fields.add(instanceName);
        fields.add(Box.createVerticalStrut(20));
        dbTypeComboBox = new JComboBox(new String[]{"Derby", "Oracle"});
        dbTypeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (dbTypeComboBox.getSelectedIndex() == 0)
                    port.setText("1530");
                else if (dbTypeComboBox.getSelectedIndex() == 1)
                    port.setText("1521");
            }
        });
        fields.add(new ComboBoxField("Database type", dbTypeComboBox));
        fields.add(Box.createVerticalStrut(20));
        port = new TextField("Port", "1530", 34, true);
        fields.add(port);
        fields.add(Box.createVerticalStrut(20));
        userName = new TextField("User Name", 34, true);
        fields.add(userName);
        fields.add(Box.createVerticalStrut(20));
        password = new PasswordField("Password", 34, true);
        fields.add(password);
        fields.add(Box.createVerticalStrut(20));
        TitledBorder title = BorderFactory.createTitledBorder("Connection Information");
        title.setTitleJustification(TitledBorder.LEFT);
        topPanel.setLayout(new BorderLayout());
        topPanel.add(fields);
        topPanel.setBorder(title);

        buttons = new JPanel();
        String format = "<html>Fields marked <font color='red'>*</font> are compulsory</html>";
        messageDisplay = new JLabel(format);
        ok = new JButton("    Ok    ");
        ok.setBorder(BorderFactory.createEtchedBorder());
        cancel = new JButton(" Cancel ");
        cancel.setBorder(BorderFactory.createEtchedBorder());
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(messageDisplay);
        buttons.add(Box.createHorizontalGlue());
        buttons.add(ok);
        buttons.add(Box.createHorizontalStrut(20));
        buttons.add(cancel);
        cancel.addActionListener(this);
        buttons.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 19));

        setLayout(new BorderLayout());
        add(BorderLayout.NORTH, topPanel);
        add(BorderLayout.SOUTH, buttons);
        setSize(500, 400);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
//        new AddConnection();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancel) {
            dispose();
        }
    }

    public void close() {
        dispose();
    }

    public boolean validateFields() {
        if (connectionName.getText().isEmpty())
            return false;
        if (system.getText().isEmpty() || !validateSystem(system.getText()))
            return false;
        if (instanceName.getText().isEmpty())
            return false;
        if (port.getText().isEmpty() || !validatePort(port.getText()))
            return false;
        if (userName.getText().isEmpty())
            return false;
        return true;
    }

    boolean validateSystem(String systemIP) {
        /*String IPADDRESS_PATTERN = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

        Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
        Matcher matcher = pattern.matcher(systemIP);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }*/
        return true;
    }

    boolean validatePort(String portNumber) {
        int x;
        try {
            x = Integer.parseInt(portNumber);
        } catch (NumberFormatException e) {
            return false;
        }
        if (x < 65536)
            return true;
        else
            return false;
    }

    public void setMessageDisplay(String message) {
        this.messageDisplay.setText(message);
    }

    public void setErrorDisplay() {
        String format = format("<html><font color='red'>%s</font></html>", "Wrong or NULL inputs entered");
        setMessageDisplay(format);
    }

}
