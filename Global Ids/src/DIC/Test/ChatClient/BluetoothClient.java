package DIC.Test.ChatClient;

public class BluetoothClient extends Client implements Runnable {
    public BluetoothClient() {
        this.type = ClientType.bluetooth;
        this.clientName = "Bluetooth Chat Client";
    }

    @Override
    public void sendData(String data, int destinationPort) {
        Packet packet = new Packet(destinationPort, this.socket.getLocalPort(), data, type);
        System.out.println("Sent " + packet.getBluetoothOutputStream());
        printWriter.println(packet.getBluetoothOutputStream());
        printWriter.flush();
    }

    @Override
    public String getOutputStream(String data) {
//        PortDetails selectedPort = (PortDetails) otherClientsComboBox.getSelectedItem();
//        return new Packet(selectedPort.getPort(), socket.getLocalPort(), data, ClientType.bluetooth).getOutputStream();
        return data + ",01";
    }

    public static void main(String[] args) {
        BluetoothClient bluetoothClient = new BluetoothClient();
        bluetoothClient.go();
    }

    @Override
    public void run() {
        go();
    }
}
