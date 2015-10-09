package DIC.Test.ChatClient;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientDetails {
    private Socket socket;
    private PrintWriter printWriter;
    private ClientType type;

    public ClientDetails(Socket socket, PrintWriter printWriter) {
        this.socket = socket;
        this.printWriter = printWriter;
    }

    public Socket getSocket() {
        return socket;
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    public void setType(ClientType type) {
        this.type = type;
    }

    public ClientType getType() {
        return type;
    }

    public double getErrorProbability() {
        if (type == ClientType.bluetooth)
            return 0.05;
        else if (type == ClientType.ethernet)
            return 0.05;
        else if (type == ClientType.rfid)
            return 0.01;
        else          //zigbee
            return 0.04;
    }


}
