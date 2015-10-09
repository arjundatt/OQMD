package DIC.Test.ChatClient;

public class ZigbeeClient extends Client implements Runnable{
    public ZigbeeClient() {
        this.type = ClientType.zigbee;
        this.clientName = "Zigbee Chat Client";
    }

    @Override
    public void sendData(String data, int destinationPort) {
        Packet packet = new Packet(destinationPort, this.socket.getLocalPort(), data, type);
        System.out.println("Sent " + packet.getZigbeeOutputStream());
        printWriter.println(packet.getZigbeeOutputStream());
        printWriter.flush();
    }

    @Override
    public String getOutputStream(String data) {
//        PortDetails selectedPort = (PortDetails) otherClientsComboBox.getSelectedItem();
//        return new Packet(selectedPort.getPort(), socket.getLocalPort(), data, ClientType.zigbee).getOutputStream();
        return null;
    }

    public static void main(String[] args) {
        ZigbeeClient zigbeeClient = new ZigbeeClient();
        zigbeeClient.go();
    }

    @Override
    public void run() {
        go();
    }
}
