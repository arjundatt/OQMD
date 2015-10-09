package DIC.Test.ChatClient;

public class EthernetClient extends Client implements Runnable {
    public EthernetClient() {
        this.type = ClientType.ethernet;
        this.clientName = "Ethernet Chat Client";
    }


    @Override
    public void sendData(String data, int destinationPort) {
        Packet packet = new Packet(destinationPort, this.socket.getLocalPort(), data, type);
        System.out.println("Sent " + packet.getEthernetOutputStream());
        printWriter.println(packet.getEthernetOutputStream());
        printWriter.flush();
    }

    @Override
    public String getOutputStream(String data) {
//        PortDetails selectedPort = (PortDetails) otherClientsComboBox.getSelectedItem();
//        return new Packet(selectedPort.getPort(), socket.getLocalPort(), data, ClientType.ethernet).getOutputStream();
        return null;
    }

    public static void main(String[] args) {
        EthernetClient ethernetClient = new EthernetClient();
        ethernetClient.go();
    }

    @Override
    public void run() {
        go();
    }
}
