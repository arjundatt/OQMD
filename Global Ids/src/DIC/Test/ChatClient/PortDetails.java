package DIC.Test.ChatClient;
public class PortDetails {
    private String name;
    private int port;

    public PortDetails(String name, int port) {
        this.name = name;
        this.port = port;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getPort() {
        return port;
    }
}
