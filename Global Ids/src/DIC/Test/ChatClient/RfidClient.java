package DIC.Test.ChatClient;

public class RfidClient extends Client implements Runnable{
    public RfidClient() {
        this.type = ClientType.rfid;
        this.clientName = "Rfid Chat Client";
    }

    @Override
    public void sendData(String data, int destinationPort) {
        Packet packet = new Packet(destinationPort, this.socket.getLocalPort(), data, type);
        System.out.println("Sent " + packet.getRfidOutputStream());
        printWriter.println(packet.getRfidOutputStream());
        printWriter.flush();
    }

    @Override
    public String getOutputStream(String data) {
//        PortDetails selectedPort = (PortDetails) otherClientsComboBox.getSelectedItem();
//        return new Packet(selectedPort.getPort(), socket.getLocalPort(), data, ClientType.rfid).getOutputStream();
        return null;
    }

    public static void main(String[] args) {
        RfidClient rfidClient = new RfidClient();
        rfidClient.go();
    }

    @Override
    public void run() {
       go();
    }
}