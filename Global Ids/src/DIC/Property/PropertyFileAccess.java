package DIC.Property;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 8/9/13
 * Time: 1:57 PM
 */
public class PropertyFileAccess {
    Properties properties;
    Properties p2;

    public PropertyFileAccess() {
        this.properties = new Properties();
        this.p2 = new Properties();
    }

    public void setProperty() {
        properties.setProperty("hello", "World");
        try {
            properties.store(new FileOutputStream("config.properties"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getProperty() {
        try {
            p2.load(new FileInputStream("config.properties"));
            System.out.println(p2.getProperty("hello"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        PropertyFileAccess f = new PropertyFileAccess();
        f.setProperty();
        f.getProperty();
    }
}
