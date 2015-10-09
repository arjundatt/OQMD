package DIC.component.rightview;

import com.jidesoft.swing.JideLabel;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 8/21/13
 * Time: 4:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class InstanceInfo extends JPanel {
    String connectionName, system, instanceName, databaseType, port;

    public InstanceInfo(String connectionName, String instanceName, String system, String databaseType, String port) {
        setBorder(BorderFactory.createEmptyBorder(50,200,50,200));
        setLayout(new GridLayout(5,2,500,10));
        this.connectionName = connectionName;
        this.instanceName = instanceName;
        this.system = system;
        this.databaseType = databaseType;
        this.port = port;
        add(new JideLabel("Connection Name"));
        add(new JideLabel(connectionName));
        add(new JideLabel("System"));
        add(new JideLabel(system));
        add(new JideLabel("Instance Name"));
        add(new JideLabel(instanceName));
        add(new JideLabel("Database Type"));
        add(new JideLabel(databaseType));
        add(new JideLabel("Port"));
        add(new JideLabel(port));
    }
}
